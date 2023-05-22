package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.pasarela.Models.Dao.ITituloGeneradoDao;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Service.ITituloGeneradoService;

@Service
@Transactional
public class TituloGeneradoServiceImpl implements ITituloGeneradoService{
    @Autowired
    private ITituloGeneradoDao tituloGeneradoDao;

    @Override
    public TituloGenerado registrarTituloGenerado(TituloGenerado tituloGenerado) {
        return tituloGeneradoDao.registrarTituloGenerado(tituloGenerado);
    }

    @Override
    public TituloGenerado buscarTituloGenerado(Long id_titulo_generado) {
        return tituloGeneradoDao.buscarTituloGenerado(id_titulo_generado);
    }

    @Override
    public TituloGenerado buscarTituloGeneradoPorTitulo(Long id_titulo) {
        return tituloGeneradoDao.buscarTituloGeneradoPorTitulo(id_titulo);
    }

    @Override
    public void modificarTituloGenerado(TituloGenerado tituloGenerado) {
        tituloGeneradoDao.modificarTituloGenerado(tituloGenerado);
    }

    @Override
    public List<TituloGenerado> listarTituloGenerado() {
        return tituloGeneradoDao.listarTituloGeneradoJPQL();
    }
    
}
