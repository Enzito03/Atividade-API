package com.petshop.api.service;

import com.petshop.api.dto.request.PedidoRequest;
import com.petshop.api.entity.*;
import com.petshop.api.exception.RecursoNaoEncontradoException;
import com.petshop.api.exception.RegraNegocioException;
import com.petshop.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public Page<Pedido> listar(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    public Page<Pedido> listarPorStatus(StatusPedido status, Pageable pageable) {
        return pedidoRepository.findByStatus(status, pageable);
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        clienteService.buscarPorId(clienteId);
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido", id));
    }

    @Transactional
    public Pedido criar(PedidoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId());

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .status(StatusPedido.AGUARDANDO_PAGAMENTO)
                .valorFrete(request.getValorFrete() != null ? request.getValorFrete() : BigDecimal.ZERO)
                .enderecoEntrega(request.getEnderecoEntrega())
                .observacao(request.getObservacao())
                .itens(new ArrayList<>())
                .build();

        List<ItemPedido> itens = new ArrayList<>();

        for (var itemReq : request.getItens()) {
            Produto produto = produtoService.buscarPorId(itemReq.getProdutoId());

            if (!produto.getAtivo()) {
                throw new RegraNegocioException("Produto indisponível: " + produto.getNome());
            }
            if (produto.getEstoque() < itemReq.getQuantidade()) {
                throw new RegraNegocioException(
                    "Estoque insuficiente para o produto: " + produto.getNome() +
                    ". Disponível: " + produto.getEstoque()
                );
            }

            ItemPedido item = ItemPedido.builder()
                    .pedido(pedido)
                    .produto(produto)
                    .quantidade(itemReq.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .build();
            item.calcularSubtotal();

            itens.add(item);

            // Baixar estoque
            produto.setEstoque(produto.getEstoque() - itemReq.getQuantidade());
            produtoService.salvar(produto);
        }

        pedido.setItens(itens);
        pedido.calcularTotal();

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("Não é possível alterar o status de um pedido cancelado");
        }
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new RegraNegocioException("Não é possível alterar o status de um pedido já entregue");
        }

        // Se for cancelar, devolver estoque
        if (novoStatus == StatusPedido.CANCELADO) {
            devolverEstoque(pedido);
        }

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelar(Long id) {
        atualizarStatus(id, StatusPedido.CANCELADO);
    }

    private void devolverEstoque(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoService.salvar(produto);
        }
    }
}
