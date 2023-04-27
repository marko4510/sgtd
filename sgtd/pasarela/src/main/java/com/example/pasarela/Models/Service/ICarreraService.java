package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Carrera;

public interface ICarreraService {
    public List<Carrera> findAll();

	public void save(Carrera carrera);

	public Carrera findOne(Long id);

	public void delete(Long id);
}
