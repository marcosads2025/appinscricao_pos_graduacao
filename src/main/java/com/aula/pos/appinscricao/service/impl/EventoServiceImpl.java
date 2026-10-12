package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.ConflitoRecursoException;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.mapper.EventoMapper;
import com.aula.pos.appinscricao.model.Evento;
import com.aula.pos.appinscricao.repository.EventoRepository;
import com.aula.pos.appinscricao.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper mapper;

    @Override
    public Page<EventoResponse> findAll(String nome, String cidade, String estado, Pageable pageable) {
        Page<Evento> page = eventoRepository.findByFilters(nome, cidade, estado, pageable);
        return page.map(mapper::toResponse);
    }

    @Override
    public EventoResponse findById(UUID id) {
        var evento = findByIdOrThrow(id);
        return mapper.toResponse(evento);
    }

    @Override
    @Transactional
    public EventoResponse save(EventoRequest request) {
        checkDuplicate(request.nome(), request.dataHoraEvento(), request.cidade(), null);
        var evento = mapper.toEntity(request);
        return mapper.toResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional
    public EventoResponse update(UUID id, EventoRequest request) {
        var evento = findByIdOrThrow(id);
        checkDuplicate(request.nome(), request.dataHoraEvento(), request.cidade(), id);
        mapper.updateEntity(request, evento);
        return mapper.toResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        var evento = findByIdOrThrow(id);
        if (evento.getDataHoraEvento().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível excluir um evento já realizado");
        }
        eventoRepository.deleteById(id);
    }

    private Evento findByIdOrThrow(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private void checkDuplicate(String nome, LocalDateTime dataHoraEvento, String cidade, UUID excludeId) {
        var existing = excludeId == null
                ? eventoRepository.findByNomeAndDataHoraEventoAndCidadeIgnoreCase(nome, dataHoraEvento, cidade)
                : eventoRepository.findByNomeAndDataHoraEventoAndCidadeIgnoreCaseAndIdNot(nome, dataHoraEvento, cidade, excludeId);

        if (existing.isPresent()) {
            throw new ConflitoRecursoException("Já existe um evento com o mesmo nome, data e cidade");
        }
    }
}
