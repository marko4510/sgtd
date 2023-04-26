package com.example.pasarela.Models.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
    
    @Query("select u from Usuario u where u.usuario = ?1 and u.contrasena = ?2")
    public Usuario getUsuarioContraseña(String correo, String password);

    @Query("select u from Usuario u left join u.persona p where p.id_persona = ?1 and u.estado != 'T' ")
    public Usuario getUsuarioPersona(Long id_persona);
}
