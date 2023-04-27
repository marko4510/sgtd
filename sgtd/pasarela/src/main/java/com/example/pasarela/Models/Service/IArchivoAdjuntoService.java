package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;

public interface IArchivoAdjuntoService {
    
    public List<ArchivoAdjunto> listarArchivoAdjunto();
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto);

    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public ArchivoAdjunto buscarArchivoAdjuntoPorSolicitud(Long id_solicitud_legalizacion);

    public ArchivoAdjunto buscarArchivoAdjuntoPorTramite(Long id_tramite);
}
