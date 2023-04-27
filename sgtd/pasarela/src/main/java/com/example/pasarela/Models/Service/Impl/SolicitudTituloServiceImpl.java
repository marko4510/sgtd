package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.ISolicitudTituloDao;
import com.example.pasarela.Models.Entity.SolicitudTitulo;
import com.example.pasarela.Models.Service.ISolicitudTituloService;

@Service
public class SolicitudTituloServiceImpl implements ISolicitudTituloService{
   
    @Autowired
    private ISolicitudTituloDao solicitudTituloDao;

    @Override
    public List<SolicitudTitulo> lista_solicitudes_titulo_usuario(Long id_usuario) {
        return (List<SolicitudTitulo>) solicitudTituloDao.lista_solicitudes_titulo_usuario(id_usuario);
    }

    @Override
    public List<SolicitudTitulo> findAll() {
        return (List<SolicitudTitulo>) solicitudTituloDao.findAll();
    }

    @Override
    public void save(SolicitudTitulo solicitudTitulo) {
        solicitudTituloDao.save(solicitudTitulo);
    }

    @Override
    public SolicitudTitulo findOne(Long id) {
        return solicitudTituloDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        solicitudTituloDao.deleteById(id);
    }
     
}
