package com.example.pasarela.Models.Service.Impl;
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

}
