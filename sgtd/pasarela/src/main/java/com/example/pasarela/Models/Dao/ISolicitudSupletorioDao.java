package com.example.pasarela.Models.Dao;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.SolicitudSupletorio;




public interface ISolicitudSupletorioDao extends CrudRepository<SolicitudSupletorio, Long> {
    @Query(value = "SELECT * FROM pasarela_solicitud_supletorio as psl inner join pasarela_usuario as usu ON usu.id_usuario = psl.id_usuario Where usu.id_usuario = ?1", nativeQuery= true)
    public List<SolicitudSupletorio> lista_solicitudes_supletorio_usuario(Long id_usuario);
}
