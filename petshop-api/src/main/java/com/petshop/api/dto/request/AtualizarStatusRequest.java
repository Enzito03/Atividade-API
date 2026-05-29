package com.petshop.api.dto.request;

import com.petshop.api.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarStatusRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusPedido status;
}
