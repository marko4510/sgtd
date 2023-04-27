package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IProvinciaDao;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Service.IProvinciaService;


@Service
public class ProvinciaServiceImpl implements IProvinciaService{
    @Autowired
    private IProvinciaDao provinciaDao;

    @Override
    public List<Provincia> findAll() {
        // TODO Auto-generated method stub
        return (List<Provincia>) provinciaDao.findAll();
    }

    @Override
    public void save(Provincia provincia) {
        // TODO Auto-generated method stub
        provinciaDao.save(provincia);
    }

    @Override
    public Provincia findOne(Long id) {
        // TODO Auto-generated method stub
        return provinciaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        provinciaDao.deleteById(id);
    }

    @Override
    public List<Provincia> provinPorIdDeparta(Long id_departamento) {
        // TODO Auto-generated method stub
        return (List<Provincia>) provinciaDao.provinPorIdDeparta(id_departamento);
    }
}
