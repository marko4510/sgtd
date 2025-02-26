package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Revalidacion;



public interface IRevalidacionDao extends CrudRepository<Revalidacion, Long>{
    
    @Query(value = "SELECT * FROM pasarela_revalidacion pr WHERE pr.nro_revalidacio = ?1 AND pr.estado = 'A'", nativeQuery = true)
    public Revalidacion getRevalidacionPorNroTitulo(String nro_revalidacio);

    @Query(value = "SELECT * FROM public.pasarela_revalidacion WHERE estado='A'", nativeQuery = true)
    public List<Revalidacion> titulosRevalidacion();
}
