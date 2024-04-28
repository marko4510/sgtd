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
    public List<Titulo> titulosAcademicos() {
        return (List<Titulo>) tituloDao.titulosAcademicos();
    }

    @Override
    public List<Titulo> titulosSinFirmarRector() {
        return (List<Titulo>) tituloDao.titulosSinFirmarRector();
    }

    @Override
    public List<Titulo> titulosSinFirmarVicerrector() {
        return (List<Titulo>) tituloDao.titulosSinFirmarVicerrector();
    }

    @Override
    public List<Titulo> titulosSinFirmarSecretario() {
        return (List<Titulo>) tituloDao.titulosSinFirmarSecretario();
    }

    @Override
    public List<Titulo> titulosBachiller() {
        return (List<Titulo>) tituloDao.titulosBachiller();
    }

    @Override
    public List<Titulo> titulosProvision() {
        return (List<Titulo>) tituloDao.titulosProvision();
    }

    @Override
    public List<Titulo> titulosDiplomado() {
       return (List<Titulo>) tituloDao.titulosDiplomado();
    }

    @Override
    public List<Titulo> titulosEspecialidad() {
         return (List<Titulo>) tituloDao.titulosEspecialidad();
    }

    @Override
    public List<Titulo> titulosMaestria() {
         return (List<Titulo>) tituloDao.titulosMaestria();
    }

    @Override
    public List<Titulo> titulosDoctorado() {
       return (List<Titulo>) tituloDao.titulosDoctorado();
    }

    @Override
    public List<Titulo> titulosProvisionRevalidacion() {
        return (List<Titulo>) tituloDao.titulosProvisionRevalidacion();
    }

    @Override
    public Titulo getTituloPorNroTitulo(String nro_titulo) {
      return tituloDao.getTituloPorNroTitulo(nro_titulo);
    }
    
}
