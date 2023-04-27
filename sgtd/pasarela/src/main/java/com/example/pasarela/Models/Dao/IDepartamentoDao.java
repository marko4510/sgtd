package com.example.pasarela.Models.Dao;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Departamento;
public interface IDepartamentoDao extends CrudRepository<Departamento, Long>{
    @Query("SELECT e FROM Departamento e WHERE e.nombre = ?1")
    public Departamento departamentoPorNombre(String nombre);

    @Query(value = "select * from pasarela_departamento as d where d.id_nacionalidad=?1", nativeQuery = true)
    public List<Departamento> departamentosPorIdPais(Long id_nacionalidad);
}
