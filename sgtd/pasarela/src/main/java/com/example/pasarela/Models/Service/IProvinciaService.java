package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Provincia;

public interface IProvinciaService {
    
    public List<Provincia> findAll();

	public List<Provincia> provinPorIdDeparta(Long id_departamento);

	public void save(Provincia provincia);

	public Provincia findOne(Long id);

	public void delete(Long id);
}
