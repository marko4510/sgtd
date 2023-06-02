package com.example.pasarela.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Firma;

public interface IFirmaDao extends CrudRepository<Firma, Long>{
    
}
