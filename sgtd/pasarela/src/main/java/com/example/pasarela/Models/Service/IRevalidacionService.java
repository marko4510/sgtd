package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Revalidacion;


public interface IRevalidacionService {
    
     public List<Revalidacion> findAll();


	
	public void save(Revalidacion revalidacion);

	public Revalidacion findOne(Long id);

	public void delete(Long id);
}
