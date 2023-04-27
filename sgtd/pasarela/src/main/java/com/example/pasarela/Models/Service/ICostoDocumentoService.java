package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.CostoDocumento;

public interface ICostoDocumentoService {
    
    public List<CostoDocumento> lista_costo_documento_legalizaciones(Long id_nacionalidad);

	public List<CostoDocumento> lista_costo_documento_supletorio(Long id_nacionalidad);

	public List<CostoDocumento> lista_costo_documento_titulo(Long id_nacionalidad);
	
    public List<CostoDocumento> findAll();

	public void save(CostoDocumento costoDocumento);

	public CostoDocumento findOne(Long id);

	public void delete(Long id);
}
