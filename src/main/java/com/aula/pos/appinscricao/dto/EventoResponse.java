package com.aula.pos.appinscricao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta de evento")
public record EventoResponse(
        @Schema(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do evento", example = "Conferência de Tecnologia 2026")
        String nome,

        @Schema(description = "Data e hora do evento", example = "2026-10-29T14:00:00")
        LocalDateTime dataHoraEvento,

        @Schema(description = "Local do evento", example = "Centro de Convenções")
        String local,

        @Schema(description = "Cidade do evento", example = "São Paulo")
        String cidade,

        @Schema(description = "Estado (UF) do evento", example = "SP")
        String estado,

        @Schema(description = "Quantidade de vagas disponíveis", example = "500")
        Integer qtde,

        @Schema(description = "Dias restantes até o evento", example = "30")
        Long diasParaEvento
) {
}
