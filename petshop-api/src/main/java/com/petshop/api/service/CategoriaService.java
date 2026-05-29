package com.petshop.api.service;

import com.petshop.api.dto.request.CategoriaRequest;
import com.petshop.api.entity.Categoria;
import com.petshop.api.exception.RecursoNaoEncontradoException;
import com.petshop.api.exception.RegraNegocioException;
import com.petshop.api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }

    @Transactional
    public Categoria criar(CategoriaRequest request) {
        if (categoriaRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new RegraNegocioException("Já existe uma categoria com o nome: " + request.getNome());
        }

        Categoria categoria = Categoria.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .build();

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarPorId(id);

        categoriaRepository.findByNomeIgnoreCase(request.getNome())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new RegraNegocioException("Já existe uma categoria com o nome: " + request.getNome()); });

        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoria = buscarPorId(id);

        if (categoria.getProdutos() != null && !categoria.getProdutos().isEmpty()) {
            throw new RegraNegocioException("Não é possível excluir a categoria pois existem produtos vinculados a ela");
        }

        categoriaRepository.delete(categoria);
    }
}
