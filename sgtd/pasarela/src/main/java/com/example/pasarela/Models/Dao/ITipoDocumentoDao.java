package com.example.pasarela.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.TipoDocumento;

public interface ITipoDocumentoDao extends CrudRepository<TipoDocumento, Long>{
    
}
