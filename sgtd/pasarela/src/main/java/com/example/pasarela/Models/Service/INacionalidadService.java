package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Nacionalidad;

public interface INacionalidadService {
    
    public List<Nacionalidad> findAll();

	public void save(Nacionalidad nacionalidad);

	public Nacionalidad findOne(Long id);

	public void delete(Long id);
}
