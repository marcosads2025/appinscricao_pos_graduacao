package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.ErrorResponse;
import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.ApiException;
import com.aula.pos.appinscricao.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos")
@Tag(name = "Eventos", description = "API para gerenciamento de eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os eventos", description = "Retorna uma página de eventos com filtros opcionais")
    public Page<EventoResponse> findAll(
            @Parameter(description = "Filtro por nome do evento") @RequestParam(required = false) String nome,
            @Parameter(description = "Filtro por cidade") @RequestParam(required = false) String cidade,
            @Parameter(description = "Filtro por estado (UF)") @RequestParam(required = false) String estado,
            @PageableDefault(size = 10, sort = "dataHoraEvento", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return eventoService.findAll(nome, cidade, estado, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Retorna um evento específico pelo seu ID")
    public ResponseEntity<?> findById(@Parameter(description = "ID do evento") @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(eventoService.findById(id));
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ErrorResponse(LocalDateTime.now(), e.getStatus(), e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Criar novo evento", description = "Cria um novo evento no sistema")
    public ResponseEntity<EventoResponse> save(@Valid @RequestBody EventoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.save(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Atualiza os dados de um evento existente")
    public ResponseEntity<?> update(
            @Parameter(description = "ID do evento") @PathVariable UUID id,
            @Valid @RequestBody EventoRequest request
    ) {
        try {
            return ResponseEntity.ok(eventoService.update(id, request));
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ErrorResponse(LocalDateTime.now(), e.getStatus(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir evento", description = "Exclui um evento do sistema (apenas eventos futuros)")
    public ResponseEntity<?> deleteById(@Parameter(description = "ID do evento") @PathVariable UUID id) {
        try {
            eventoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ErrorResponse(LocalDateTime.now(), e.getStatus(), e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}
