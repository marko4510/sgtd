package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IGradoAcademicoDao;
import com.example.pasarela.Models.Entity.GradoAcademico;
import com.example.pasarela.Models.Service.IGradoAcademicoService;


@Service
public class GradoAcademicoImpl implements IGradoAcademicoService{

    @Autowired
    private IGradoAcademicoDao gradoAcademicoDao;


    @Override
    public List<GradoAcademico> findAll() {
        return (List<GradoAcademico>) gradoAcademicoDao.findAll();
    }

    @Override
    public void save(GradoAcademico gradoAcademico) {
        gradoAcademicoDao.save(gradoAcademico);
    }

    @Override
    public GradoAcademico findOne(Long id) {
        return gradoAcademicoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        gradoAcademicoDao.deleteById(id);
    }

    @Override
    public List<GradoAcademico> gradoPorIdCarrera(Long id_carrera) {
        // TODO Auto-generated method stub
        return (List<GradoAcademico>) gradoAcademicoDao.gradoPorIdCarrera(id_carrera);
    }
    
}
