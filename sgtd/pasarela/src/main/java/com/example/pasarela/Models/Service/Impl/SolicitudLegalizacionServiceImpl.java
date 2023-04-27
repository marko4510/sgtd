package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Dao.ISolicitudLegalizacionDao;
import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Service.ISolicitudLegalizacionService;

@Service
@Transactional
public class SolicitudLegalizacionServiceImpl implements ISolicitudLegalizacionService{
    
    @Autowired
    private ISolicitudLegalizacionDao solicitudLegDao;

    @Override
    public List<SolicitudLegalizacion> findAll() {
        return (List<SolicitudLegalizacion>) solicitudLegDao.findAll();
    }

    @Override
    public void save(SolicitudLegalizacion solicitudLegalizacion) {
        solicitudLegDao.save(solicitudLegalizacion);
    }

    @Override
    public SolicitudLegalizacion findOne(Long id) {
        return solicitudLegDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        solicitudLegDao.deleteById(id);
    }

    @Override
    public List<SolicitudLegalizacion> lista_solicitudes_usuario(Long id_usuario) {
        return (List<SolicitudLegalizacion>) solicitudLegDao.lista_solicitudes_usuario(id_usuario);
    }

}
