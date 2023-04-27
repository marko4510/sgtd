package com.example.pasarela.Models.Dao;

import java.util.List;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;

public interface IArchivoAdjuntoDao {
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto);

    public ArchivoAdjunto buscarArchivoAdjuntoPorSolicitud(Long id_solicitud_legalizacion);

    public ArchivoAdjunto buscarArchivoAdjuntoPorTramite(Long id_tramite);

    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public List<ArchivoAdjunto> listarArchivoAdjuntoJPQL();
}
