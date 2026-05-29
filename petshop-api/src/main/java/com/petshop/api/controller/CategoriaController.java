package com.petshop.api.controller;

import com.petshop.api.dto.request.CategoriaRequest;
import com.petshop.api.dto.response.ApiResponse;
import com.petshop.api.entity.Categoria;
import com.petshop.api.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gerenciamento de categorias de produtos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    public ResponseEntity<ApiResponse<List<Categoria>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(categoriaService.listarTodas()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    public ResponseEntity<ApiResponse<Categoria>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(categoriaService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Criar nova categoria")
    public ResponseEntity<ApiResponse<Categoria>> criar(@Valid @RequestBody CategoriaRequest request) {
        Categoria criada = categoriaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Categoria criada com sucesso", criada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    public ResponseEntity<ApiResponse<Categoria>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Categoria atualizada com sucesso", categoriaService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Categoria excluída com sucesso", null));
    }
}
