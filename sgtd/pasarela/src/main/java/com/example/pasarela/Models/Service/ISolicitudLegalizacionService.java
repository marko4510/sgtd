package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.SolicitudLegalizacion;

public interface ISolicitudLegalizacionService {
    
    public List<SolicitudLegalizacion> lista_solicitudes_usuario(Long id_usuario);
	
    public List<SolicitudLegalizacion> findAll();

	public void save(SolicitudLegalizacion solicitudLegalizacion);

	public SolicitudLegalizacion findOne(Long id);

	public void delete(Long id);
}
