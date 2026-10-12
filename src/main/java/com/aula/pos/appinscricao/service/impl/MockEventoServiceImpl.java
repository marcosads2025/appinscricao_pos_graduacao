package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.service.EventoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service("mockEventoService")
public class MockEventoServiceImpl implements EventoService {

    private final Map<UUID, EventoResponse> eventos = new ConcurrentHashMap<>();

    public MockEventoServiceImpl() {
        // Initialize with sample data
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        LocalDateTime future1 = LocalDateTime.now().plusDays(30);
        LocalDateTime future2 = LocalDateTime.now().plusDays(15);
        LocalDateTime future3 = LocalDateTime.now().plusDays(60);

        eventos.put(id1, new EventoResponse(
                id1,
                "Conferência de Tecnologia 2026",
                future1,
                "Centro de Convenções",
                "São Paulo",
                "SP",
                500,
                ChronoUnit.DAYS.between(LocalDateTime.now(), future1)
        ));

        eventos.put(id2, new EventoResponse(
                id2,
                "Workshop de Desenvolvimento Web",
                future2,
                "Hotel Plaza",
                "Rio de Janeiro",
                "RJ",
                100,
                ChronoUnit.DAYS.between(LocalDateTime.now(), future2)
        ));

        eventos.put(id3, new EventoResponse(
                id3,
                "Festival de Inovação",
                future3,
                "Arena Multiuso",
                "Belo Horizonte",
                "MG",
                1000,
                ChronoUnit.DAYS.between(LocalDateTime.now(), future3)
        ));
    }

    @Override
    public Page<EventoResponse> findAll(String nome, String cidade, String estado, Pageable pageable) {
        List<EventoResponse> filtered = new ArrayList<>(eventos.values());

        if (nome != null && !nome.isBlank()) {
            filtered = filtered.stream()
                    .filter(e -> e.nome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }
        if (cidade != null && !cidade.isBlank()) {
            filtered = filtered.stream()
                    .filter(e -> e.cidade().toLowerCase().contains(cidade.toLowerCase()))
                    .toList();
        }
        if (estado != null && !estado.isBlank()) {
            filtered = filtered.stream()
                    .filter(e -> e.estado().equalsIgnoreCase(estado))
                    .toList();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<EventoResponse> pageContent = filtered.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    public EventoResponse findById(UUID id) {
        EventoResponse evento = eventos.get(id);
        if (evento == null) {
            throw new ResourceNotFoundException(id);
        }
        return evento;
    }

    @Override
    public EventoResponse save(EventoRequest request) {
        UUID id = UUID.randomUUID();
        long diasParaEvento = ChronoUnit.DAYS.between(LocalDateTime.now(), request.dataHoraEvento());
        
        EventoResponse response = new EventoResponse(
                id,
                request.nome(),
                request.dataHoraEvento(),
                request.local(),
                request.cidade(),
                request.estado(),
                request.qtde(),
                diasParaEvento
        );
        
        eventos.put(id, response);
        return response;
    }

    @Override
    public EventoResponse update(UUID id, EventoRequest request) {
        EventoResponse existing = findById(id);
        long diasParaEvento = ChronoUnit.DAYS.between(LocalDateTime.now(), request.dataHoraEvento());
        
        EventoResponse updated = new EventoResponse(
                id,
                request.nome(),
                request.dataHoraEvento(),
                request.local(),
                request.cidade(),
                request.estado(),
                request.qtde(),
                diasParaEvento
        );
        
        eventos.put(id, updated);
        return updated;
    }

    @Override
    public void deleteById(UUID id) {
        EventoResponse evento = findById(id);
        if (evento.dataHoraEvento().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível excluir um evento já realizado");
        }
        eventos.remove(id);
    }
}
