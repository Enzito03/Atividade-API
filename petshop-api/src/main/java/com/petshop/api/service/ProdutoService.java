package com.petshop.api.service;

import com.petshop.api.dto.request.ProdutoRequest;
import com.petshop.api.entity.Categoria;
import com.petshop.api.entity.Produto;
import com.petshop.api.exception.RecursoNaoEncontradoException;
import com.petshop.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    public Page<Produto> listar(String nome, Long categoriaId, Pageable pageable) {
        return produtoRepository.findByFiltros(nome, categoriaId, pageable);
    }

    public List<Produto> listarPorCategoria(Long categoriaId) {
        categoriaService.buscarPorId(categoriaId);
        return produtoRepository.findByCategoriaId(categoriaId);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    @Transactional
    public Produto criar(ProdutoRequest request) {
        Categoria categoria = categoriaService.buscarPorId(request.getCategoriaId());
        Produto produto = Produto.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .estoque(request.getEstoque())
                .urlImagem(request.getUrlImagem())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .categoria(categoria)
                .build();
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarPorId(id);
        Categoria categoria = categoriaService.buscarPorId(request.getCategoriaId());
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPreco(request.getPreco());
        produto.setEstoque(request.getEstoque());
        produto.setUrlImagem(request.getUrlImagem());
        produto.setAtivo(request.getAtivo() != null ? request.getAtivo() : produto.getAtivo());
        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarComEstoqueBaixo(int quantidade) {
        return produtoRepository.findByEstoqueLessThan(quantidade);
    }
}
