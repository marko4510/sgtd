package com.example.pasarela.Models.Dao;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Unidad;

public interface IUnidadDao extends CrudRepository<Unidad, Long> {
    
}
