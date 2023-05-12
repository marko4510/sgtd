package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Tramite;

public interface ITramiteDao extends CrudRepository<Tramite, Long> {
    
    @Query(value = "select * from pasarela_tramite as tra where tra.id_tipo_documento=?1", nativeQuery = true)
    public List<Tramite> tramitePorTipoDocumento(Long id_tipo_documento);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_documento=?1 ", nativeQuery = true)
    public List<Tramite> tramitePorDocumento(Long id_documento);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_documento=?1 AND tra.id_tipo_documento=?2", nativeQuery = true)
    public List<Tramite> tramitePorDocumentoTipoDocumento(Long id_documento, Long id_tipo_documento);

    //LISTAS DE CARPETAS POR TIPO DOCUMENTO

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON tr.id_tipo_documento = td.id_tipo_documento WHERE td.nombre_tipo_documento= 'LEGALIZACIÓN'", nativeQuery = true)
    public List<Tramite> listaCarpetaLegalizacion();

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON tr.id_tipo_documento = td.id_tipo_documento WHERE td.nombre_tipo_documento= 'SUPLETORIO'", nativeQuery = true)
    public List<Tramite> listaCarpetaSupletorio();

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON tr.id_tipo_documento = td.id_tipo_documento WHERE td.nombre_tipo_documento= 'TÍTULO / DIPLOMA'", nativeQuery = true)
    public List<Tramite> listaCarpetaTitulos();

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON tr.id_tipo_documento = td.id_tipo_documento WHERE td.nombre_tipo_documento= 'PROVISIÓN NACIONAL'", nativeQuery = true)
    public List<Tramite> listaCarpetaProvision();

    //LISTAS PARA REPORTES POR GESTION

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad INNER JOIN pasarela_persona as per ON per.id_persona = tr.id_persona INNER JOIN pasarela_grado_academico as gr ON per.id_grado_academico = gr.id_grado_academico INNER JOIN pasarela_carrera as car ON car.id_carrera = gr.id_carrera WHERE uni.id_unidad = ?1 AND car.id_carrera = ?2 AND tr.gestion = ?3 ", nativeQuery = true)
    public List<Tramite> reporteCarpetaPorUnidadCarreraGestion(Long id_unidad, Long id_carrera, String gestion);

    
    
}
