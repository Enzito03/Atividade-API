package com.petshop.api.controller;

import com.petshop.api.dto.request.ClienteRequest;
import com.petshop.api.dto.response.ApiResponse;
import com.petshop.api.entity.Cliente;
import com.petshop.api.service.ClienteService;
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

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes com paginação e filtro por nome")
    public ResponseEntity<ApiResponse<Page<Cliente>>> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.listar(nome, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ApiResponse<Cliente>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo cliente")
    public ResponseEntity<ApiResponse<Cliente>> criar(@Valid @RequestBody ClienteRequest request) {
        Cliente criado = clienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cliente cadastrado com sucesso", criado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do cliente")
    public ResponseEntity<ApiResponse<Cliente>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cliente atualizado com sucesso", clienteService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar cliente (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Cliente desativado com sucesso", null));
    }
}
