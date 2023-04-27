package com.example.pasarela.Models.Service.Impl;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Dao.ITramiteDao;
import com.example.pasarela.Models.Entity.Tramite;
import com.example.pasarela.Models.Service.ITramiteService;



@Service
@Transactional
public class TramiteServiceImpl implements ITramiteService{
    @Autowired
    private ITramiteDao tramiteDao;

    @Override
    public List<Tramite> findAll() {
        return (List<Tramite>) tramiteDao.findAll();
    }

    @Override
    public void save(Tramite Tramite) {
        tramiteDao.save(Tramite);
    }

    @Override
    public Tramite findOne(Long id) {
        return tramiteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tramiteDao.deleteById(id);
    }

}
