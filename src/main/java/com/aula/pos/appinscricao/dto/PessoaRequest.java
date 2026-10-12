package com.aula.pos.appinscricao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação/atualização de pessoa")
public record PessoaRequest(
        @Schema(description = "Nome completo da pessoa", example = "João Silva", required = true)
        @NotBlank(message = "{messages.erro.pessoa.notblank}")
        @Size(min = 3, max = 100, message = "Nome deve possuir entre 3 a 100 caracteres")
        String nome,

        @Schema(description = "CPF da pessoa", example = "123.456.789-00", required = true)
        @NotBlank(message = "Cpf é Obrigatório")
        @Size(max = 15, message = "Cpf deve possuir no máximo 15 caracteres")
        String cpf,

        @Schema(description = "E-mail da pessoa", example = "joao.silva@email.com", required = true)
        @NotBlank(message = "Email é Obrigatório")
        @Size(max = 100, message = "E-mail deve possuir no máximo 100 caracteres")
        @Email(message = "E-mail Inválido")
        String email,

        @Schema(description = "Telefone da pessoa", example = "(11) 98765-4321", required = true)
        @NotBlank(message = "Telefone é Obrigatório")
        @Size(max = 15, message = "Telefone deve possuir no máximo 15 caracteres")
        String telefone
) {
}
