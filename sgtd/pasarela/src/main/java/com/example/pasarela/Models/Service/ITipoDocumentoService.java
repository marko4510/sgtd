package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.TipoDocumento;

public interface ITipoDocumentoService {
    
    public List<TipoDocumento> findAll();

	public void save(TipoDocumento tipoDocumento);

	public TipoDocumento findOne(Long id);

	public void delete(Long id);
}
