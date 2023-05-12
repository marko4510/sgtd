package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Tramite;

public interface ITramiteService {

	public List<Tramite> tramitePorDocumentoTipoDocumento(Long id_documento, Long id_tipo_documento);

	public List<Tramite> tramitePorDocumento(Long id_documento);
	
	public List<Tramite> tramitePorTipoDocumento(Long id_tipo_documento);

    public List<Tramite> findAll();

	public void save(Tramite tramite);

	public Tramite findOne(Long id);

	public void delete(Long id);

	//LISTAS DE CARPETAS POR TIPO DOCUMENTO
	public List<Tramite> listaCarpetaLegalizacion();
	public List<Tramite> listaCarpetaSupletorio();
	public List<Tramite> listaCarpetaTitulos();
	public List<Tramite> listaCarpetaProvision();

	//LISTAS PARA REPORTES POR GESTION
	public List<Tramite> reporteCarpetaPorUnidadCarreraGestion(Long id_unidad, Long id_carrera, String gestion);
}
