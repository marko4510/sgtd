package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Documento;

public interface IDocumentoDao extends CrudRepository<Documento, Long>{
    @Query(value= "SELECT * FROM pasarela_tipo_doc_documento as tdocD INNER JOIN pasarela_tipo_documento as tdoc ON tdocD.id_tipo_documento = tdoc.id_tipo_documento INNER JOIN pasarela_documento as doc ON tdocD.id_documento = doc.id_documento WHERE tdocD.id_tipo_documento = ?1", nativeQuery = true)
    public List<Documento> documentoPorIdTipodoc(Long id_tipo_documento);
}
