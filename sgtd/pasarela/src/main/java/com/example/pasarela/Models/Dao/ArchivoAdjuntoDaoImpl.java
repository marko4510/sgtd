package com.example.pasarela.Models.Dao;


import java.util.List;


import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;

@Repository("IArchivoAdjuntoDao")
public class ArchivoAdjuntoDaoImpl implements IArchivoAdjuntoDao{
    
    @PersistenceContext
    private javax.persistence.EntityManager em;

    @Transactional
  
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto){

        em.persist(archivoAdjunto);
        return archivoAdjunto;
    }

    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto){
        String sql = " SELECT arc "
        + " FROM ArchivoAdjunto arc "
        + " WHERE arc.id_archivo_adjunto =:id_archivo_adjunto"
        + " AND arc.idEstado = 'A' ";
        Query q = em.createQuery(sql);
        q.setParameter("id_archivo_adjunto", id_archivo_adjunto);
        try {
        return (ArchivoAdjunto) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }


    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto){

        em.merge(archivoAdjunto);
    }

    @Override
    public List<ArchivoAdjunto> listarArchivoAdjuntoJPQL() {
        String sql = "SELECT adj "
        + " FROM ArchivoAdjunto adj "
        + " WHERE adj.estado = 'A' ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjuntoPorSolicitud(Long id_solicitud_legalizacion) {
        String sql = "SELECT gaa  "
        + " FROM SolicitudLegalizacion sol LEFT JOIN  sol.archivoAdjunto gaa"
        + " WHERE sol.id_solicitud_legalizacion =:id_solicitud_legalizacion "
        + " AND gaa.estado = 'A' ";
        Query q = em.createQuery(sql);
        q.setParameter("id_solicitud_legalizacion", id_solicitud_legalizacion);
        try {
        return (ArchivoAdjunto) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjuntoPorTramite(Long id_tramite) {
      
         String sql = "SELECT gaa  "
        + " FROM Tramite tr LEFT JOIN  tr.archivoAdjunto gaa"
        + " WHERE tr.id_tramite =:id_tramite "
        + " AND gaa.estado = 'A' ";
        /*String sql = "select gaa from pasarela_tramite tr, pasarela_archivo_adjunto ar WHERE tr.id_archivo_adjunto=ar.id_archivo_adjunto and tr.estado='A' and tr.id_tramite=:id_tramite;";*/
        Query q = em.createQuery(sql);
        q.setParameter("id_tramite", id_tramite);
        try {
        return (ArchivoAdjunto) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }
}
