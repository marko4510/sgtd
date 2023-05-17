package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Documento;

public interface IDocumentoService {
    
    public List<Documento> findAll();

	public List<Documento> documentoPorIdTipodoc(Long id_tipo_documento);

	public void save(Documento tipoDocumento);

	public Documento findOne(Long id);

	public void delete(Long id);
}
