package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.ITipoDocumentoDao;
import com.example.pasarela.Models.Entity.TipoDocumento;
import com.example.pasarela.Models.Service.ITipoDocumentoService;

@Service
public class TipoDocumentoServiceImpl implements ITipoDocumentoService{
    
    @Autowired
    private ITipoDocumentoDao tipoDocumentoDao;
    
    @Override
    public List<TipoDocumento> findAll() {
        // TODO Auto-generated method stub
        return (List<TipoDocumento>) tipoDocumentoDao.findAll();
    }

    @Override
    public void save(TipoDocumento tipoDocumento) {
        // TODO Auto-generated method stub
        tipoDocumentoDao.save(tipoDocumento);
    }

    @Override
    public TipoDocumento findOne(Long id) {
        // TODO Auto-generated method stub
        return tipoDocumentoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        tipoDocumentoDao.deleteById(id);
    }
}
