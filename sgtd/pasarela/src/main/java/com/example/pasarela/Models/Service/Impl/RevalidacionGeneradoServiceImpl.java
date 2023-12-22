package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IRevalidacionGeneradoDao;
import com.example.pasarela.Models.Entity.Revalidacion;
import com.example.pasarela.Models.Entity.RevalidacionGenerado;
import com.example.pasarela.Models.Service.IRevalidacionGeneradoService;

@Service
@Transactional
public class RevalidacionGeneradoServiceImpl implements IRevalidacionGeneradoService{

    @Autowired
    private IRevalidacionGeneradoDao revalidacionGeneradoDao;

    @Override
    public List<RevalidacionGenerado> findAll() {
         return (List<RevalidacionGenerado>) revalidacionGeneradoDao.findAll();
    }

    @Override
    public void save(RevalidacionGenerado revalidacionGenerado) {
        revalidacionGeneradoDao.save(revalidacionGenerado);
    }

    @Override
    public RevalidacionGenerado findOne(Long id) {
        return revalidacionGeneradoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        revalidacionGeneradoDao.deleteById(id);
    }

    @Override
    public RevalidacionGenerado buscarRevalidacionGeneradoPorRevalidacion(Long id_revalidacion) {
        return revalidacionGeneradoDao.buscarRevalidacionGeneradoPorRevalidacion(id_revalidacion);
    }


   
    
}
