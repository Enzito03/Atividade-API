package com.petshop.api.controller;

import com.petshop.api.dto.request.ProdutoRequest;
import com.petshop.api.dto.response.ApiResponse;
import com.petshop.api.entity.Produto;
import com.petshop.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Gerenciamento do catálogo de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Listar produtos com filtros e paginação")
    public ResponseEntity<ApiResponse<Page<Produto>>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(produtoService.listar(nome, categoriaId, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ApiResponse<Produto>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(produtoService.buscarPorId(id)));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar produtos por categoria")
    public ResponseEntity<ApiResponse<List<Produto>>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(ApiResponse.ok(produtoService.listarPorCategoria(categoriaId)));
    }

    @GetMapping("/estoque-baixo")
    @Operation(summary = "Listar produtos com estoque abaixo do limite")
    public ResponseEntity<ApiResponse<List<Produto>>> listarEstoqueBaixo(
            @RequestParam(defaultValue = "5") int quantidade) {
        return ResponseEntity.ok(ApiResponse.ok(produtoService.listarComEstoqueBaixo(quantidade)));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo produto")
    public ResponseEntity<ApiResponse<Produto>> criar(@Valid @RequestBody ProdutoRequest request) {
        Produto criado = produtoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Produto cadastrado com sucesso", criado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ApiResponse<Produto>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Produto atualizado com sucesso", produtoService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar produto (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Produto desativado com sucesso", null));
    }
}
