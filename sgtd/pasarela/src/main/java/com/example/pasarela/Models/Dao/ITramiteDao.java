package com.example.pasarela.Models.Dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Tramite;

public interface ITramiteDao extends CrudRepository<Tramite, Long> {
    


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

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad INNER JOIN pasarela_persona as per ON per.id_persona = tr.id_persona  INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento WHERE uni.id_unidad = ?1  AND tr.gestion = ?2 AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA' AND doc.nombre_documento = 'LICENCIATURA'", nativeQuery = true)
    public List<Tramite> reporteTituladosPorUnidadGestion(Long id_unidad, String gestion);

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad INNER JOIN pasarela_persona as per ON per.id_persona = tr.id_persona INNER JOIN pasarela_grado_academico as gr ON per.id_grado_academico = gr.id_grado_academico INNER JOIN pasarela_carrera as car ON car.id_carrera = gr.id_carrera INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento WHERE uni.id_unidad = ?1 AND car.id_carrera = ?2 AND tr.gestion = ?3 AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA' AND doc.nombre_documento = 'LICENCIATURA'", nativeQuery = true)
    public List<Tramite> reporteCarpetaPorUnidadCarreraGestion(Long id_unidad, Long id_carrera, String gestion);

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad INNER JOIN pasarela_persona as per ON per.id_persona = tr.id_persona INNER JOIN pasarela_grado_academico as gr ON per.id_grado_academico = gr.id_grado_academico INNER JOIN pasarela_carrera as car ON car.id_carrera = gr.id_carrera INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento WHERE uni.id_unidad = ?1 AND car.id_carrera = ?2 AND tr.gestion = ?3 AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA' AND per.sexo = ?4 AND doc.nombre_documento = 'LICENCIATURA'", nativeQuery = true)
    public List<Tramite> reporteCarpetaPorUnidadCarreraGestionSexo(Long id_unidad, Long id_carrera, String gestion, String sexo);

    //LISTA DE REPORTES POR FECHA
    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento WHERE tr.fecha_titulacion BETWEEN :fechaInicial AND :fechaFinal AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA' AND doc.nombre_documento = 'LICENCIATURA' ", nativeQuery = true)
    public List<Tramite> reporteTituladosPorFechas(Date fechaInicial, Date fechaFinal);

    //LISTA DE REPORTES POR TIPO DOCUMENTOS
    @Query(value = "SELECT * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad WHERE uni.id_unidad = ?1 AND td.id_tipo_documento = ?2 AND tr.gestion = ?3", nativeQuery = true)
    public List<Tramite> tramitePorUnidadTipoDocumentoGestion(Long id_unidad,Long id_tipo_documento,String gestion);

    @Query(value = "SELECT * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad WHERE uni.id_unidad = ?1 AND td.id_tipo_documento = ?2 AND doc.id_documento = ?3 AND tr.gestion = ?4 ", nativeQuery = true)
    public List<Tramite> tramitePorUnidadTipoDocumentoDocumentoGestion(Long id_unidad,Long id_tipo_documento,Long id_documento,String gestion);

    @Query(value = "SELECT * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento INNER JOIN pasarela_unidad as uni ON tr.id_unidad = uni.id_unidad WHERE uni.id_unidad = ?1 AND td.id_tipo_documento = ?2 AND doc.id_documento = ?3 AND tr.fecha_titulacion BETWEEN ?4 AND ?5", nativeQuery = true)
    public List<Tramite> tramitePorUnidadTipoDocumentoDocumentoFechas(Long id_unidad,Long id_tipo_documento,Long id_documento,Date fecha1, Date fecha2);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_tipo_documento=?1", nativeQuery = true)
    public List<Tramite> tramitePorTipoDocumento(Long id_tipo_documento);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_documento=?1 ", nativeQuery = true)
    public List<Tramite> tramitePorDocumento(Long id_documento);

    @Query(value = "select * from pasarela_tramite as tra where tra.id_documento=?1 AND tra.id_tipo_documento=?2", nativeQuery = true)
    public List<Tramite> tramitePorDocumentoTipoDocumento(Long id_documento, Long id_tipo_documento);

    //LISTA DE REPORTES TITULADOS EN POSGRADO
    @Query(value = "SELECT * FROM pasarela_tramite as tr INNER JOIN pasarela_persona as per ON tr.id_persona=per.id_persona INNER JOIN pasarela_documento as doc ON tr.id_documento=doc.id_documento INNER JOIN pasarela_tipo_documento as td ON tr.id_tipo_documento=td.id_tipo_documento WHERE doc.nombre_documento='DOCTORADO' AND tr.gestion= ?1 AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA'", nativeQuery = true)
    public List<Tramite> reporteTituladosDoctorado(String gestion);

    @Query(value = "SELECT * FROM pasarela_tramite as tr INNER JOIN pasarela_persona as per ON tr.id_persona=per.id_persona INNER JOIN pasarela_documento as doc ON tr.id_documento=doc.id_documento INNER JOIN pasarela_tipo_documento as td ON tr.id_tipo_documento=td.id_tipo_documento WHERE doc.nombre_documento='MAESTRIA' AND tr.gestion= ?1 AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA'", nativeQuery = true)
    public List<Tramite> reporteTituladosMaestria(String gestion);

    @Query(value = "SELECT   * FROM pasarela_tramite as tr INNER JOIN pasarela_tipo_documento as td ON td.id_tipo_documento = tr.id_tipo_documento INNER JOIN pasarela_documento as doc ON doc.id_documento = tr.id_documento WHERE tr.fecha_titulacion BETWEEN :fechaInicio AND :fechaFin AND td.nombre_tipo_documento = 'TÍTULO / DIPLOMA' AND (doc.nombre_documento = 'DOCTORADO' OR doc.nombre_documento = 'MAESTRIA')", nativeQuery = true)
    public List<Tramite> reporteTituladosPosgradoPorFechas(Date fechaInicio, Date fechaFin);
}
