package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.CostoDocumento;

public interface ICostoDocumentoDao extends CrudRepository<CostoDocumento, Long>{

    @Query(value = "SELECT * FROM pasarela_costo_documento as csd WHERE csd.id_nacionalidad = ?1 AND csd.id_tipo_documento = 1", nativeQuery= true)
    public List<CostoDocumento> lista_costo_documento_legalizaciones(Long id_nacionalidad);

    @Query(value = "SELECT * FROM pasarela_costo_documento as csd WHERE csd.id_nacionalidad = ?1 AND csd.id_tipo_documento = 2", nativeQuery= true)
    public List<CostoDocumento> lista_costo_documento_supletorio(Long id_nacionalidad);

    @Query(value = "SELECT * FROM pasarela_costo_documento as csd WHERE csd.id_nacionalidad = ?1 AND csd.id_tipo_documento = 3", nativeQuery= true)
    public List<CostoDocumento> lista_costo_documento_titulo(Long id_nacionalidad);

    @Query(value = "SELECT * FROM pasarela_costo_documento as csd WHERE csd.id_nacionalidad = ?1 AND csd.id_tipo_documento = 4", nativeQuery= true)
    public List<CostoDocumento> lista_costo_documento_titulo_provision(Long id_nacionalidad);
}
