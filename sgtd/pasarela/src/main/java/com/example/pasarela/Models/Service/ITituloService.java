package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Titulo;

public interface ITituloService {


    public List<Titulo> findAll();


	public void save(Titulo titulo);

	public Titulo findOne(Long id);

	public void delete(Long id);
}