package com.petshop.api.controller;

import com.petshop.api.dto.request.AtualizarStatusRequest;
import com.petshop.api.dto.request.PedidoRequest;
import com.petshop.api.dto.response.ApiResponse;
import com.petshop.api.entity.Pedido;
import com.petshop.api.entity.StatusPedido;
import com.petshop.api.service.PedidoService;
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
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos do e-commerce")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todos os pedidos com paginação")
    public ResponseEntity<ApiResponse<Page<Pedido>>> listar(
            @RequestParam(required = false) StatusPedido status,
            @PageableDefault(size = 10, sort = "dataPedido") Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(ApiResponse.ok(pedidoService.listarPorStatus(status, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.ok(pedidoService.listar(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<ApiResponse<Pedido>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(pedidoService.buscarPorId(id)));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos de um cliente")
    public ResponseEntity<ApiResponse<List<Pedido>>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ApiResponse.ok(pedidoService.listarPorCliente(clienteId)));
    }

    @PostMapping
    @Operation(summary = "Criar novo pedido")
    public ResponseEntity<ApiResponse<Pedido>> criar(@Valid @RequestBody PedidoRequest request) {
        Pedido criado = pedidoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pedido criado com sucesso", criado));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido")
    public ResponseEntity<ApiResponse<Pedido>> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Status atualizado com sucesso",
                pedidoService.atualizarStatus(id, request.getStatus())));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<ApiResponse<Void>> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.ok(ApiResponse.ok("Pedido cancelado com sucesso", null));
    }
}
