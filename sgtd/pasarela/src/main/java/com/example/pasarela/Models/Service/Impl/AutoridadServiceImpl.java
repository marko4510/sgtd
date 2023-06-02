package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IAutoridadDao;
import com.example.pasarela.Models.Entity.Autoridad;
import com.example.pasarela.Models.Service.IAutoridadService;

@Service
public class AutoridadServiceImpl implements IAutoridadService{

    @Autowired
    private IAutoridadDao autoridadDao;

    @Override
    public List<Autoridad> findAll() {
        return (List<Autoridad>) autoridadDao.findAll();
    }

    @Override
    public void save(Autoridad autoridad) {
        autoridadDao.save(autoridad);
    }

    @Override
    public Autoridad findOne(Long id) {
        return autoridadDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        autoridadDao.deleteById(id);
    }

    @Override
    public Autoridad autoridadPorIdPersona(Long id_persona) {
        return autoridadDao.autoridadPorIdPersona(id_persona);
    }
    
}
