package com.example.pasarela.Models.Service.Impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.ICarreraDao;
import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Service.ICarreraService;

@Service
public class CarreraServiceImpl implements ICarreraService{
    @Autowired
    private ICarreraDao carreraDao;

    @Override
    public List<Carrera> findAll() {
        return (List<Carrera>) carreraDao.findAll();
    }

    @Override
    public void save(Carrera carrera) {
        carreraDao.save(carrera);
    }

    @Override
    public Carrera findOne(Long id) {
        return carreraDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        carreraDao.deleteById(id);
    }
}
