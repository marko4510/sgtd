package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Titulo;

public interface ITituloService {

	public List<Titulo> titulosSinFirmar();
	public List<Titulo> titulosAcademicos();
	public List<Titulo> titulosBachiller();
	public List<Titulo> titulosProvision();
	public List<Titulo> titulosProvisionRevalidacion();
	public List<Titulo> titulosDiplomado();
	public List<Titulo> titulosEspecialidad();
	public List<Titulo> titulosMaestria();
	public List<Titulo> titulosDoctorado();

    public List<Titulo> findAll();

	public List<Titulo> titulosSinFirmarRector();
	public List<Titulo> titulosSinFirmarVicerrector();
	public List<Titulo> titulosSinFirmarSecretario();
	
	public void save(Titulo titulo);

	public Titulo findOne(Long id);

	public void delete(Long id);

	public Titulo getTituloPorNroTitulo(String nro_titulo);

	public Titulo getTituloPorNroTituloProvision(String nro_titulo);
}
