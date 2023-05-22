package com.example.pasarela.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Titulo;

public interface ITituloDao extends CrudRepository<Titulo, Long>{
    
}
