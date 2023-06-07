package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.SolicitudTitulo;

public interface ISolicitudTituloDao extends CrudRepository<SolicitudTitulo, Long>{
    
    @Query(value = "SELECT * FROM pasarela_solicitud_titulo as psl inner join pasarela_usuario as usu ON usu.id_usuario = psl.id_usuario Where usu.id_usuario = ?1", nativeQuery= true)
    public List<SolicitudTitulo> lista_solicitudes_titulo_usuario(Long id_usuario);

    @Query(value = "SELECT * FROM pasarela_solicitud_titulo as psl inner join pasarela_usuario as usu ON usu.id_usuario = psl.id_usuario Where usu.id_usuario = ?1", nativeQuery= true)
    public SolicitudTitulo SolicitudPorUsuario(Long id_usuario);
    
}
