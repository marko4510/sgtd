package com.example.pasarela.Models.Dao;
import java.util.List;


import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Entity.TituloGenerado;

@Repository("ITituloGeneradoDao")
public class TituloGeneradoDaoImpl implements ITituloGeneradoDao{

    @PersistenceContext
    private javax.persistence.EntityManager em;

    @Transactional


    @Override
    public TituloGenerado registrarTituloGenerado(TituloGenerado tituloGenerado) {
        em.persist(tituloGenerado);
        return tituloGenerado
        ;
    }

    @Override
    public TituloGenerado buscarTituloGenerado(Long id_titulo_generado) {
        String sql = " SELECT arc "
        + " FROM TituloGenerado arc "
        + " WHERE arc.id_titulo_generado =:id_titulo_generado"
        + " AND arc.idEstado = 'A' ";
        Query q = em.createQuery(sql);
        q.setParameter("id_titulo_generado", id_titulo_generado);
        try {
        return (TituloGenerado) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }

    @Override
    public TituloGenerado buscarTituloGeneradoPorTitulo(Long id_titulo) {
        String sql = "SELECT gaa  "
        + " FROM Titulo tr LEFT JOIN  tr.tituloGenerado gaa"
        + " WHERE tr.id_titulo =:id_titulo "
        + " AND gaa.estado = 'A' ";
        /*String sql = "select gaa from pasarela_tramite tr, pasarela_archivo_adjunto ar WHERE tr.id_archivo_adjunto=ar.id_archivo_adjunto and tr.estado='A' and tr.id_tramite=:id_tramite;";*/
        Query q = em.createQuery(sql);
        q.setParameter("id_titulo", id_titulo);
        try {
        return (TituloGenerado) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }

    @Override
    public void modificarTituloGenerado(TituloGenerado tituloGenerado) {
        em.merge(tituloGenerado);
    }

    @Override
    public List<TituloGenerado> listarTituloGeneradoJPQL() {
        String sql = "SELECT adj "
        + " FROM TituloGenerado adj "
        + " WHERE adj.estado = 'A' ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
}
