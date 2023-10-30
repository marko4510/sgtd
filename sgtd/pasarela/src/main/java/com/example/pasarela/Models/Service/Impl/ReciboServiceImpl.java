package com.example.pasarela.Models.Service.Impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IReciboDao;

import com.example.pasarela.Models.Entity.Recibo;
import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Service.IReciboService;


@Service
public class ReciboServiceImpl implements IReciboService{
    
    @Autowired
    private IReciboDao reciboDao;

    @Override
    public List<Recibo> findAll() {
          return (List<Recibo>) reciboDao.findAll();
    }

    @Override
    public void save(Recibo recibo) {
        reciboDao.save(recibo);
    }

    @Override
    public Recibo findOne(Long id) {
        return reciboDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        reciboDao.deleteById(id);
    }

    @Override
    public List<Recibo> lista_recibo_usuario(Long id_usuario) {
     return (List<Recibo>) reciboDao.lista_recibo_usuario(id_usuario);
    }
}
