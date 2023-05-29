package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Titulo;

public interface ITituloDao extends CrudRepository<Titulo, Long>{

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A'", nativeQuery = true)
    public List<Titulo> titulosSinFirmar();
}
