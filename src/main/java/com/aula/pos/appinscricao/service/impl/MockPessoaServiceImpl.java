package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.dto.PessoaRequest;
import com.aula.pos.appinscricao.dto.PessoaResponse;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.service.PessoaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service("mockPessoaService")
public class MockPessoaServiceImpl implements PessoaService {

    private final Map<UUID, PessoaResponse> pessoas = new ConcurrentHashMap<>();

    public MockPessoaServiceImpl() {
        // Initialize with sample data
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        pessoas.put(id1, new PessoaResponse(
                id1,
                "João Silva",
                "123.456.789-00",
                "joao.silva@email.com",
                "(11) 98765-4321"
        ));

        pessoas.put(id2, new PessoaResponse(
                id2,
                "Maria Santos",
                "987.654.321-11",
                "maria.santos@email.com",
                "(21) 91234-5678"
        ));

        pessoas.put(id3, new PessoaResponse(
                id3,
                "Pedro Oliveira",
                "456.789.123-22",
                "pedro.oliveira@email.com",
                "(31) 99876-5432"
        ));
    }

    @Override
    public Page<PessoaResponse> findAll(String nome, Pageable pageable) {
        List<PessoaResponse> filtered = new ArrayList<>(pessoas.values());

        if (nome != null && !nome.isBlank()) {
            filtered = filtered.stream()
                    .filter(p -> p.nome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<PessoaResponse> pageContent = filtered.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    public PessoaResponse findById(UUID id) {
        PessoaResponse pessoa = pessoas.get(id);
        if (pessoa == null) {
            throw new ResourceNotFoundException(id);
        }
        return pessoa;
    }

    @Override
    public PessoaResponse save(PessoaRequest request) {
        UUID id = UUID.randomUUID();
        
        PessoaResponse response = new PessoaResponse(
                id,
                request.nome(),
                request.cpf(),
                request.email(),
                request.telefone()
        );
        
        pessoas.put(id, response);
        return response;
    }

    @Override
    public PessoaResponse update(UUID id, PessoaRequest request) {
        findById(id); // Verify exists
        
        PessoaResponse updated = new PessoaResponse(
                id,
                request.nome(),
                request.cpf(),
                request.email(),
                request.telefone()
        );
        
        pessoas.put(id, updated);
        return updated;
    }

    @Override
    public void deleById(UUID id) {
        findById(id); // Verify exists
        pessoas.remove(id);
    }
}
