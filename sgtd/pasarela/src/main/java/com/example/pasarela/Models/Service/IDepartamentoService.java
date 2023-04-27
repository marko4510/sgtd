package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Departamento;

public interface IDepartamentoService {
    public List<Departamento> findAll();

	public Departamento departamentoPorNombre(String nombre);

	public List<Departamento> departamentosPorIdPais(Long id_nacionalidad);

	public void save(Departamento departamento);

	public Departamento findOne(Long id);

	public void delete(Long id);
}
