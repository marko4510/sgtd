package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Revalidacion;
import com.example.pasarela.Models.Entity.Titulo;


public interface IRevalidacionService {
    
     public List<Revalidacion> findAll();


	
	public void save(Revalidacion revalidacion);

	public Revalidacion findOne(Long id);

	public void delete(Long id);

	public Revalidacion getRevalidacionPorNroTitulo(String nro_revalidacio);

	public List<Revalidacion> titulosRevalidacion();
}
