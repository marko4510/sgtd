package com.example.pasarela.Models.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasarela.Models.Dao.IFirmaDao;
import com.example.pasarela.Models.Entity.Firma;
import com.example.pasarela.Models.Service.IFirmaService;

@Service
public class FirmaServiceImpl implements IFirmaService{

    @Autowired
    private IFirmaDao firmaDao;


    @Override
    public List<Firma> findAll() {
        return (List<Firma>) firmaDao.findAll();
    }

    @Override
    public void save(Firma firma) {
        firmaDao.save(firma);
    }

    @Override
    public Firma findOne(Long id) {
        return firmaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        firmaDao.deleteById(id);
    }

    @Override
    public List<Firma> Firmas(Long id_titulo) {
        return (List<Firma>) firmaDao.Firmas(id_titulo);
    }

    
}
