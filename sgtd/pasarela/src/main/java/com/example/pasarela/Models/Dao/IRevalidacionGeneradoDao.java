package com.example.pasarela.Models.Dao;

import java.util.List;


import com.example.pasarela.Models.Entity.RevalidacionGenerado;


public interface IRevalidacionGeneradoDao {

    public RevalidacionGenerado registrarRevalidacionGenerado(RevalidacionGenerado revalidacionGenerado);

    public RevalidacionGenerado buscarRevalidacionGenerado(Long id_revalidacion_generado);


    public RevalidacionGenerado buscarRevalidacionGeneradoPorRevalidacion(Long id_revalidacion);

    public void modificarRevalidacionGenerado(RevalidacionGenerado revalidacionGenerado);

    public List<RevalidacionGenerado> listarRevalidacionGeneradoJPQL();
}
