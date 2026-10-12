package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.ErrorResponse;
import com.aula.pos.appinscricao.dto.PessoaRequest;
import com.aula.pos.appinscricao.dto.PessoaResponse;
import com.aula.pos.appinscricao.exception.ApiException;
import com.aula.pos.appinscricao.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pessoas")
@Tag(name = "Pessoas", description = "API para gerenciamento de pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as pessoas", description = "Retorna uma página de pessoas comFiltro opcional por nome")
    public Page<PessoaResponse> findAll(
            @Parameter(description = "Filtro por nome da pessoa") @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return pessoaService.findAll(nome, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID", description = "Retorna uma pessoa específica pelo seu ID")
    public ResponseEntity<?> findById(@Parameter(description = "ID da pessoa") @PathVariable UUID id){
        try {
            return ResponseEntity.ok(pessoaService.findById(id));
        }catch (ApiException e){
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ErrorResponse(LocalDateTime.now(), e.getStatus(), e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Criar nova pessoa", description = "Cria uma nova pessoa no sistema")
    public ResponseEntity<PessoaResponse> save(@Valid @RequestBody PessoaRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.save(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa", description = "Atualiza os dados de uma pessoa existente")
    public ResponseEntity<?> update(
            @Parameter(description = "ID da pessoa") @PathVariable UUID id,
            @Valid @RequestBody PessoaRequest request
    ){
        try{
            return ResponseEntity.ok(pessoaService.update(id, request));
        }catch(ApiException e){
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ErrorResponse(LocalDateTime.now(), e.getStatus(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pessoa", description = "Exclui uma pessoa do sistema")
    public ResponseEntity<?> deleteById(@Parameter(description = "ID da pessoa") @PathVariable UUID id){
        try{
            pessoaService.deleById(id);
            return ResponseEntity.noContent().build();
        }catch (ApiException e){
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ErrorResponse(LocalDateTime.now(), e.getStatus(), e.getMessage()));
        }
    }
}
