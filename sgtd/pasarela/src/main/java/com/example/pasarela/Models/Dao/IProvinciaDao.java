package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Provincia;

public interface IProvinciaDao extends CrudRepository<Provincia, Long>{
    @Query(value = "select * from pasarela_provincia as p where p.id_departamento=?1", nativeQuery = true)
    public List<Provincia> provinPorIdDeparta(Long id_departamento);
}
