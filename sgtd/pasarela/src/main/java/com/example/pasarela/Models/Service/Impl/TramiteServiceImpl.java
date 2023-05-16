package com.example.pasarela.Models.Service.Impl;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Dao.ITramiteDao;
import com.example.pasarela.Models.Entity.Tramite;
import com.example.pasarela.Models.Service.ITramiteService;



@Service
@Transactional
public class TramiteServiceImpl implements ITramiteService{
    @Autowired
    private ITramiteDao tramiteDao;

    @Override
    public List<Tramite> findAll() {
        return (List<Tramite>) tramiteDao.findAll();
    }

    @Override
    public void save(Tramite Tramite) {
        tramiteDao.save(Tramite);
    }

    @Override
    public Tramite findOne(Long id) {
        return tramiteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tramiteDao.deleteById(id);
    }

    @Override
    public List<Tramite> tramitePorTipoDocumento(Long id_tipo_documento) {
        return (List<Tramite>) tramiteDao.tramitePorTipoDocumento(id_tipo_documento);
    }

    @Override
    public List<Tramite> tramitePorDocumento(Long id_documento) {
        return (List<Tramite>) tramiteDao.tramitePorDocumento(id_documento);
    }

    @Override
    public List<Tramite> tramitePorDocumentoTipoDocumento(Long id_documento, Long id_tipo_documento) {
        return (List<Tramite>) tramiteDao.tramitePorDocumentoTipoDocumento(id_documento,id_tipo_documento );
    }

    @Override
    public List<Tramite> listaCarpetaLegalizacion() {
        return (List<Tramite>) tramiteDao.listaCarpetaLegalizacion();
    }

    @Override
    public List<Tramite> listaCarpetaSupletorio() {
        return (List<Tramite>) tramiteDao.listaCarpetaSupletorio();
    }

    @Override
    public List<Tramite> listaCarpetaTitulos() {
        return (List<Tramite>) tramiteDao.listaCarpetaTitulos();
    }

    @Override
    public List<Tramite> listaCarpetaProvision() {
        return (List<Tramite>) tramiteDao.listaCarpetaProvision();
    }

    @Override
    public List<Tramite> reporteCarpetaPorUnidadCarreraGestion(Long id_unidad, Long id_carrera, String gestion) {
        return (List<Tramite>) tramiteDao.reporteCarpetaPorUnidadCarreraGestion(id_unidad, id_carrera, gestion);
    }

    @Override
    public List<Tramite> reporteCarpetaPorUnidadCarreraGestionSexo(Long id_unidad, Long id_carrera, String gestion,
            String sexo) {
      return (List<Tramite>) tramiteDao.reporteCarpetaPorUnidadCarreraGestionSexo(id_unidad, id_carrera, gestion, sexo);
    }

    @Override
    public List<Tramite> reporteTituladosPorFechas(Date fechaInicio, Date fechaFin) {
        return (List<Tramite>) tramiteDao.reporteTituladosPorFechas(fechaInicio, fechaFin);
    }

    @Override
    public List<Tramite> reporteTituladosPorUnidadGestion(Long id_unidad, String gestion) {
        return (List<Tramite>) tramiteDao.reporteTituladosPorUnidadGestion(id_unidad, gestion);
    }

    @Override
    public List<Tramite> tramitePorUnidadTipoDocumentoGestion(Long id_unidad, Long id_tipo_documento, String gestion) {
        return (List<Tramite>) tramiteDao.tramitePorUnidadTipoDocumentoGestion(id_unidad, id_tipo_documento, gestion);
    }

    @Override
    public List<Tramite> tramitePorUnidadTipoDocumentoDocumentoGestion(Long id_unidad, Long id_tipo_documento,
            Long id_documento, String gestion) {
                return (List<Tramite>) tramiteDao.tramitePorUnidadTipoDocumentoDocumentoGestion(id_unidad, id_tipo_documento, id_documento, gestion);
    }

    @Override
    public List<Tramite> reporteTituladosDoctorado(String gestion) {
        return (List<Tramite>) tramiteDao.reporteTituladosDoctorado(gestion);
    }

    @Override
    public List<Tramite> reporteTituladosMaestria(String gestion) {
        return (List<Tramite>) tramiteDao.reporteTituladosMaestria(gestion);
    }

    @Override
    public List<Tramite> reporteTituladosPosgradoPorFechas(Date fechaInicio, Date fechaFin) {
        return (List<Tramite>) tramiteDao.reporteTituladosPosgradoPorFechas(fechaInicio, fechaFin);
    }

 

}
