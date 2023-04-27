package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Tramite;

public interface ITramiteService {
    public List<Tramite> findAll();

	public void save(Tramite tramite);

	public Tramite findOne(Long id);

	public void delete(Long id);
}
