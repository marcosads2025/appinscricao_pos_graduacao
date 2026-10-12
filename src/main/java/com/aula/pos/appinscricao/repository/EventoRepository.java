package com.aula.pos.appinscricao.repository;

import com.aula.pos.appinscricao.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {

    @Query("SELECT e FROM Evento e WHERE " +
           "LOWER(e.nome) = LOWER(:nome) AND " +
           "e.dataHoraEvento = :dataHoraEvento AND " +
           "LOWER(e.cidade) = LOWER(:cidade)")
    Optional<Evento> findByNomeAndDataHoraEventoAndCidadeIgnoreCase(
            @Param("nome") String nome,
            @Param("dataHoraEvento") LocalDateTime dataHoraEvento,
            @Param("cidade") String cidade
    );

    @Query("SELECT e FROM Evento e WHERE " +
           "LOWER(e.nome) = LOWER(:nome) AND " +
           "e.dataHoraEvento = :dataHoraEvento AND " +
           "LOWER(e.cidade) = LOWER(:cidade) AND " +
           "e.id <> :id")
    Optional<Evento> findByNomeAndDataHoraEventoAndCidadeIgnoreCaseAndIdNot(
            @Param("nome") String nome,
            @Param("dataHoraEvento") LocalDateTime dataHoraEvento,
            @Param("cidade") String cidade,
            @Param("id") UUID id
    );

    @Query("SELECT e FROM Evento e WHERE " +
           "(:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:cidade IS NULL OR LOWER(e.cidade) LIKE LOWER(CONCAT('%', :cidade, '%'))) AND " +
           "(:estado IS NULL OR LOWER(e.estado) = LOWER(:estado))")
    Page<Evento> findByFilters(
            @Param("nome") String nome,
            @Param("cidade") String cidade,
            @Param("estado") String estado,
            Pageable pageable
    );
}
