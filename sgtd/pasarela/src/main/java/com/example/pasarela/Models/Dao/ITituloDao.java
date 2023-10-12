package com.example.pasarela.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.pasarela.Models.Entity.Titulo;

public interface ITituloDao extends CrudRepository<Titulo, Long>{

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A'", nativeQuery = true)
    public List<Titulo> titulosSinFirmar();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND firma_rector IS NULL", nativeQuery = true)
    public List<Titulo> titulosSinFirmarRector();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Academico' AND firma_vicerrector IS NULL", nativeQuery = true)
    public List<Titulo> titulosSinFirmarVicerrector();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND firma_secretario IS NULL", nativeQuery = true)
    public List<Titulo> titulosSinFirmarSecretario();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Academico'", nativeQuery = true)
    public List<Titulo> titulosAcademicos();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Bachiller'", nativeQuery = true)
    public List<Titulo> titulosBachiller();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Provision'", nativeQuery = true)
    public List<Titulo> titulosProvision();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Diplomado'", nativeQuery = true)
    public List<Titulo> titulosDiplomado();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Especialidad'", nativeQuery = true)
    public List<Titulo> titulosEspecialidad();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Maestria'", nativeQuery = true)
    public List<Titulo> titulosMaestria();

    @Query(value = "SELECT * FROM public.pasarela_titulo WHERE estado='A' AND tipo_titulo = 'Doctorado'", nativeQuery = true)
    public List<Titulo> titulosDoctorado();

    //@Query(value = "SELECT * FROM public.pasarela_titulo WHERE (tipo_titulo = 'Bachiller' OR tipo_titulo = 'Academico') AND estado = 'A'", nativeQuery = true)
//public List<Titulo> titulosBachillerAcademicoSinFirmar();

  
}
