package com.aula.pos.appinscricao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "DTO para criação/atualização de evento")
public record EventoRequest(
        @Schema(description = "Nome do evento", example = "Conferência de Tecnologia 2026", required = true)
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve possuir no máximo 150 caracteres")
        String nome,

        @Schema(description = "Data e hora do evento (formato ISO 8601)", example = "2026-10-29T14:00:00", required = true)
        @NotNull(message = "Data e hora do evento são obrigatórias")
        @Future(message = "Data e hora do evento deve ser futura")
        LocalDateTime dataHoraEvento,

        @Schema(description = "Local do evento", example = "Centro de Convenções", required = true)
        @NotBlank(message = "Local é obrigatório")
        @Size(max = 200, message = "Local deve possuir no máximo 200 caracteres")
        String local,

        @Schema(description = "Cidade do evento", example = "São Paulo", required = true)
        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100, message = "Cidade deve possuir no máximo 100 caracteres")
        String cidade,

        @Schema(description = "Estado (UF) do evento", example = "SP", required = true)
        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve possuir exatamente 2 caracteres")
        String estado,

        @Schema(description = "Quantidade de vagas disponíveis", example = "500", required = true)
        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade mínima é 1")
        Integer qtde
) {
}
