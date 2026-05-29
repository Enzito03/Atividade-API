package com.petshop.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotEmpty(message = "O pedido deve ter ao menos um item")
    @Valid
    private List<ItemPedidoRequest> itens;

    private BigDecimal valorFrete;

    private String enderecoEntrega;

    private String observacao;
}
