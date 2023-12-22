package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.example.pasarela.Models.Entity.RevalidacionGenerado;



public interface IRevalidacionGeneradoDao extends CrudRepository<RevalidacionGenerado, Long>{

   @Query(value = "SELECT * FROM pasarela_revalidacion_generado as psl inner join pasarela_revalidacion as reva \r\n" + //
       "ON reva.id_revalidacion_generado = psl.id_revalidacion_generado  Where reva.id_revalidacion_generado = 1", nativeQuery = true)
  public RevalidacionGenerado buscarRevalidacionGeneradoPorRevalidacion(Long id_revalidacion);
}
