package com.example.pasarela.Models.Service.Impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IDepartamentoDao;
import com.example.pasarela.Models.Dao.IUsuarioDao;
import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Service.IDepartamentoService;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService{
    @Autowired
    private IDepartamentoDao departamentoDao;
    
    @Override
    public List<Departamento> findAll() {
        // TODO Auto-generated method stub
        return (List<Departamento>) departamentoDao.findAll();
    }

    @Override
    public void save(Departamento departamento) {
        // TODO Auto-generated method stub
        departamentoDao.save(departamento);
    }

    @Override
    public Departamento findOne(Long id) {
        // TODO Auto-generated method stub
        return departamentoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        departamentoDao.deleteById(id);
    }

    @Override
    public Departamento departamentoPorNombre(String nombre) {
        return departamentoDao.departamentoPorNombre(nombre);
    }

    @Override
    public List<Departamento> departamentosPorIdPais(Long id_nacionalidad) {
        // TODO Auto-generated method stub
        return (List<Departamento>) departamentoDao.departamentosPorIdPais(id_nacionalidad);
    }
}
