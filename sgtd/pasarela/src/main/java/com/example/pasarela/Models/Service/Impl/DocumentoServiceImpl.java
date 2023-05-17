package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IDocumentoDao;
import com.example.pasarela.Models.Entity.Documento;
import com.example.pasarela.Models.Service.IDocumentoService;

@Service
public class DocumentoServiceImpl implements IDocumentoService{
    
    @Autowired
    private IDocumentoDao documentoDao;
    
    @Override
    public List<Documento> findAll() {
        // TODO Auto-generated method stub
        return (List<Documento>) documentoDao.findAll();
    }

    @Override
    public void save(Documento documento) {
        // TODO Auto-generated method stub
        documentoDao.save(documento);
    }

    @Override
    public Documento findOne(Long id) {
        // TODO Auto-generated method stub
        return documentoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        documentoDao.deleteById(id);
    }

    @Override
    public List<Documento> documentoPorIdTipodoc(Long id_tipo_documento) {
        // TODO Auto-generated method stub
        return (List<Documento>) documentoDao.documentoPorIdTipodoc(id_tipo_documento);
    }
}
