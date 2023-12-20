package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IRevalidacionGeneradoDao;
import com.example.pasarela.Models.Entity.RevalidacionGenerado;
import com.example.pasarela.Models.Service.IRevalidacionGeneradoService;

@Service
@Transactional
public class RevalidacionGeneradoServiceImpl implements IRevalidacionGeneradoService{

    @Autowired(required = false)
    private IRevalidacionGeneradoDao revalidacionGeneradoDao;


    @Override
    public RevalidacionGenerado registrarRevalidacionGenerado(RevalidacionGenerado revalidacionGenerado) {
        return revalidacionGeneradoDao.registrarRevalidacionGenerado(revalidacionGenerado);
    }

    @Override
    public RevalidacionGenerado buscarRevalidacionGenerado(Long id_revalidacion_generado) {
        return revalidacionGeneradoDao.buscarRevalidacionGenerado(id_revalidacion_generado);
    }

    @Override
    public RevalidacionGenerado buscarRevalidacionGeneradoPorRevalidacion(Long id_revalidacion) {
        return revalidacionGeneradoDao.buscarRevalidacionGeneradoPorRevalidacion(id_revalidacion);
    }

    @Override
    public void modificarRevalidacionGenerado(RevalidacionGenerado revalidacionGenerado) {
        revalidacionGeneradoDao.modificarRevalidacionGenerado(revalidacionGenerado);
    }

    @Override
    public List<RevalidacionGenerado> listarRevalidacionGeneradoJPQL() {
        return revalidacionGeneradoDao.listarRevalidacionGeneradoJPQL();
    }
    
}
