package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.ITituloDao;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Service.ITituloService;

@Service
public class TituloServiceImpl implements ITituloService{
    @Autowired
    private ITituloDao tituloDao;
    
    @Override
    public List<Titulo> findAll() {
        return (List<Titulo>) tituloDao.findAll();
    }

    @Override
    public void save(Titulo titulo) {
        tituloDao.save(titulo);
    }

    @Override
    public Titulo findOne(Long id) {
        return tituloDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tituloDao.deleteById(id);
    }

    @Override
    public List<Titulo> titulosSinFirmar() {
        return (List<Titulo>) tituloDao.titulosSinFirmar();
    }

    @Override
    public List<Titulo> titulosAcademicosSinFirmar() {
        return (List<Titulo>) tituloDao.titulosAcademicosSinFirmar();
    }
    
}
