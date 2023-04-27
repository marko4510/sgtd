package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.GradoAcademico;

public interface IGradoAcademicoDao extends CrudRepository<GradoAcademico, Long>{
    @Query("select g from GradoAcademico g left join g.carrera c where c.id_carrera=?1")
    public List<GradoAcademico> gradoPorIdCarrera(Long id_carrera);
}
