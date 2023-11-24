package com.example.pasarela.Models.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long>{
    
    @Query("Select p from Persona p where p.ci = ?1")
    public Persona getPersonaCI(String ci);

    @Query("SELECT p FROM Persona p WHERE p.nombre = ?1 AND p.ap_paterno = ?2 AND p.ap_materno = ?3")
    public Persona getPersonaByNombres(String nombre, String apellidoPaterno, String ap_materno);

    Optional<Persona> findByCorreo(String correo);
}
