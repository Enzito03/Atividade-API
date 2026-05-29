package com.petshop.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErroValidacaoResponse {

    private boolean sucesso;
    private String mensagem;
    private Map<String, String> erros;
    private LocalDateTime timestamp;
}
