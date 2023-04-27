package com.example.pasarela.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Documento;

public interface IDocumentoDao extends CrudRepository<Documento, Long>{
    
}
