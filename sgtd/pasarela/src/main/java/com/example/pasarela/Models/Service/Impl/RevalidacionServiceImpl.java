package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IRevalidacionDao;
import com.example.pasarela.Models.Entity.Revalidacion;

import com.example.pasarela.Models.Service.IRevalidacionService;

@Service
public class RevalidacionServiceImpl implements IRevalidacionService{

    @Autowired
    private IRevalidacionDao revalidacionDao;

    @Override
    public List<Revalidacion> findAll() {
         return (List<Revalidacion>) revalidacionDao.findAll();
    }

    @Override
    public void save(Revalidacion revalidacion) {
        revalidacionDao.save(revalidacion);
    }

    @Override
    public Revalidacion findOne(Long id) {
        return revalidacionDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        revalidacionDao.deleteById(id);
    }

    @Override
    public Revalidacion getRevalidacionPorNroTitulo(String nro_revalidacio) {
        return revalidacionDao.getRevalidacionPorNroTitulo(nro_revalidacio);
    }
    
}
