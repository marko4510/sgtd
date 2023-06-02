package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Autoridad;

public interface IAutoridadDao extends CrudRepository<Autoridad, Long>{
    
    @Query(value = "select * from pasarela_autoridad as d where d.id_persona=?1", nativeQuery = true)
    public Autoridad autoridadPorIdPersona(Long id_persona);
}
