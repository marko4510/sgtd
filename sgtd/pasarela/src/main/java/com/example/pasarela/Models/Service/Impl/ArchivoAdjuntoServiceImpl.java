package com.example.pasarela.Models.Service.Impl;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Dao.IArchivoAdjuntoDao;
import com.example.pasarela.Models.Entity.ArchivoAdjunto;
import com.example.pasarela.Models.Service.IArchivoAdjuntoService;

@Service
@Transactional
public class ArchivoAdjuntoServiceImpl implements IArchivoAdjuntoService{
    @Autowired
    private IArchivoAdjuntoDao archivoAdjuntoDao;

    @Override
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        return archivoAdjuntoDao.registrarArchivoAdjunto(archivoAdjunto);
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto) {
        return archivoAdjuntoDao.buscarArchivoAdjunto(id_archivo_adjunto);
    }

    @Override
    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        archivoAdjuntoDao.modificarArchivoAdjunto(archivoAdjunto);
    }

    @Override
    public List<ArchivoAdjunto> listarArchivoAdjunto() {
        return archivoAdjuntoDao.listarArchivoAdjuntoJPQL();
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjuntoPorSolicitud(Long id_solicitud_legalizacion) {
        return archivoAdjuntoDao.buscarArchivoAdjuntoPorSolicitud(id_solicitud_legalizacion);
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjuntoPorTramite(Long id_tramite) {
       return archivoAdjuntoDao.buscarArchivoAdjuntoPorTramite(id_tramite);
    }
 
}
