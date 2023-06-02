package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Autoridad;

public interface IAutoridadService {
    public List<Autoridad> findAll();

	public Autoridad autoridadPorIdPersona(Long id_persona);

	public void save(Autoridad autoridad);

	public Autoridad findOne(Long id);

	public void delete(Long id);
}
