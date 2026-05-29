package com.petshop.api.service;

import com.petshop.api.dto.request.ClienteRequest;
import com.petshop.api.entity.Cliente;
import com.petshop.api.exception.RecursoNaoEncontradoException;
import com.petshop.api.exception.RegraNegocioException;
import com.petshop.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Page<Cliente> listar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return clienteRepository.findByNomeContaining(nome, pageable);
        }
        return clienteRepository.findByAtivoTrue(pageable);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
    }

    @Transactional
    public Cliente criar(ClienteRequest request) {
        if (clienteRepository.existsByCpf(request.getCpf())) {
            throw new RegraNegocioException("CPF já cadastrado: " + request.getCpf());
        }
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado: " + request.getEmail());
        }

        Cliente cliente = Cliente.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .dataNascimento(request.getDataNascimento())
                .logradouro(request.getLogradouro())
                .numero(request.getNumero())
                .complemento(request.getComplemento())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .build();

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);

        // Verifica CPF de outro cliente
        clienteRepository.findByCpf(request.getCpf())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new RegraNegocioException("CPF já cadastrado: " + request.getCpf()); });

        // Verifica email de outro cliente
        clienteRepository.findByEmail(request.getEmail())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new RegraNegocioException("E-mail já cadastrado: " + request.getEmail()); });

        cliente.setNome(request.getNome());
        cliente.setCpf(request.getCpf());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());
        cliente.setDataNascimento(request.getDataNascimento());
        cliente.setLogradouro(request.getLogradouro());
        cliente.setNumero(request.getNumero());
        cliente.setComplemento(request.getComplemento());
        cliente.setBairro(request.getBairro());
        cliente.setCidade(request.getCidade());
        cliente.setEstado(request.getEstado());
        cliente.setCep(request.getCep());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
}
