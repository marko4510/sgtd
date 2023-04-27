package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.INacionalidadDao;
import com.example.pasarela.Models.Entity.Nacionalidad;
import com.example.pasarela.Models.Service.INacionalidadService;

@Service
public class NacionalidadServiceImpl implements INacionalidadService{
    
    @Autowired
    private INacionalidadDao nacionalidadDao; 


    @Override
    public List<Nacionalidad> findAll() {
        // TODO Auto-generated method stub
        return (List<Nacionalidad>) nacionalidadDao.findAll();
    }

    @Override
    public void save(Nacionalidad Nacionalidad) {
        // TODO Auto-generated method stub
        nacionalidadDao.save(Nacionalidad);
    }

    @Override
    public Nacionalidad findOne(Long id) {
        // TODO Auto-generated method stub
        return nacionalidadDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        nacionalidadDao.deleteById(id);
    }
}
