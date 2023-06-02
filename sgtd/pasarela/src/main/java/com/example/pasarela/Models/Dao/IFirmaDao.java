package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Firma;

public interface IFirmaDao extends CrudRepository<Firma, Long>{
    @Query(value = "select * from pasarela_firma as fir where fir.id_titulo = ?1", nativeQuery = true)
    public List<Firma> Firmas(Long id_titulo);
}
