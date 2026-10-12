package com.aula.pos.appinscricao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO de resposta de pessoa")
public record PessoaResponse(
        @Schema(description = "ID único da pessoa", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome completo da pessoa", example = "João Silva")
        String nome,

        @Schema(description = "CPF da pessoa", example = "123.456.789-00")
        String cpf,

        @Schema(description = "E-mail da pessoa", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Telefone da pessoa", example = "(11) 98765-4321")
        String telefone
) {
}
