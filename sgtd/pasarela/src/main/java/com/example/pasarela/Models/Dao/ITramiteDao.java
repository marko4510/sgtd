package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Tramite;

public interface ITramiteDao extends CrudRepository<Tramite, Long> {
    
    @Query(value = "select * from pasarela_tramite as tra where tra.id_tipo_documento=?1", nativeQuery = true)
    public List<Tramite> tramitePorTipoDocumento(Long id_tipo_documento);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_documento=?1 ", nativeQuery = true)
    public List<Tramite> tramitePorDocumento(Long id_documento);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_documento=?1 AND tra.id_tipo_documento=?2", nativeQuery = true)
    public List<Tramite> tramitePorDocumentoTipoDocumento(Long id_documento, Long id_tipo_documento);

    
    
}
