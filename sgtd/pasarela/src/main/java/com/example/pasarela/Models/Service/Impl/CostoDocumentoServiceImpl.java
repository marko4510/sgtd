package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.ICostoDocumentoDao;
import com.example.pasarela.Models.Entity.CostoDocumento;
import com.example.pasarela.Models.Service.ICostoDocumentoService;

@Service
public class CostoDocumentoServiceImpl implements ICostoDocumentoService{
    
    @Autowired
    private ICostoDocumentoDao costoDao;
  
    @Override
    public List<CostoDocumento> lista_costo_documento_legalizaciones(Long id_nacionalidad) {
      return (List<CostoDocumento>) costoDao.lista_costo_documento_legalizaciones(id_nacionalidad);
    }
  
    @Override
    public List<CostoDocumento> lista_costo_documento_supletorio(Long id_nacionalidad) {
      return (List<CostoDocumento>) costoDao.lista_costo_documento_supletorio(id_nacionalidad);
    }
  
    @Override
    public List<CostoDocumento> findAll() {
      // TODO Auto-generated method stub
      return (List<CostoDocumento>) costoDao.findAll();
    }
  
    @Override
    public void save(CostoDocumento costoDocumento) {
      // TODO Auto-generated method stub
      costoDao.save(costoDocumento);
    }
  
    @Override
    public CostoDocumento findOne(Long id) {
      // TODO Auto-generated method stub
      return costoDao.findById(id).orElse(null);
    }
  
    @Override
    public void delete(Long id) {
      // TODO Auto-generated method stub
      costoDao.deleteById(id);
    }
  
    @Override
    public List<CostoDocumento> lista_costo_documento_titulo(Long id_nacionalidad) {
      return (List<CostoDocumento>) costoDao.lista_costo_documento_titulo(id_nacionalidad);
    }
  
}
