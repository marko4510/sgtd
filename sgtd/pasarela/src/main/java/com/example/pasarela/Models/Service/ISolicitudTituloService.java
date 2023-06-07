package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.SolicitudTitulo;

public interface ISolicitudTituloService {
    
    public List<SolicitudTitulo> lista_solicitudes_titulo_usuario(Long id_usuario);

    public List<SolicitudTitulo> findAll();

	public void save(SolicitudTitulo solicitudTitulo);

	public SolicitudTitulo findOne(Long id);

	public void delete(Long id);

	public SolicitudTitulo SolicitudPorUsuario(Long id_usuario);
}
