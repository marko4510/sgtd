package com.example.pasarela.Models.Service.Impl;


import com.example.pasarela.Models.Dao.ISolicitudSupletorioDao;
import com.example.pasarela.Models.Entity.SolicitudSupletorio;
import com.example.pasarela.Models.Service.ISolicitudSupletorioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SolicitudSupletorioImpl implements ISolicitudSupletorioService{
    @Autowired
    private ISolicitudSupletorioDao solicitudSupletorioDao;


    @Override
    public List<SolicitudSupletorio> lista_solicitudes_supletorio_usuario(Long id_usuario) {
        return (List<SolicitudSupletorio>) solicitudSupletorioDao.lista_solicitudes_supletorio_usuario(id_usuario);
    }

    @Override
    public List<SolicitudSupletorio> findAll() {
        return (List<SolicitudSupletorio>) solicitudSupletorioDao.findAll();
    }

    @Override
    public void save(SolicitudSupletorio solicitudSupletorio) {
        solicitudSupletorioDao.save(solicitudSupletorio);
    }

    @Override
    public SolicitudSupletorio findOne(Long id) {
        return solicitudSupletorioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        solicitudSupletorioDao.deleteById(id);
    }
}
