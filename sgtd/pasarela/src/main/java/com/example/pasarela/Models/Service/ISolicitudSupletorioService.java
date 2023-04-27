package com.example.pasarela.Models.Service;
import java.util.List;

import com.example.pasarela.Models.Entity.SolicitudSupletorio;

public interface ISolicitudSupletorioService {
    public List<SolicitudSupletorio> lista_solicitudes_supletorio_usuario(Long id_usuario);

    public List<SolicitudSupletorio> findAll();

	public void save(SolicitudSupletorio solicitudSupletorio);

	public SolicitudSupletorio findOne(Long id);

	public void delete(Long id);
}
