package com.example.pasarela.Models.Service;

import java.util.List;


import com.example.pasarela.Models.Entity.RevalidacionGenerado;

public interface IRevalidacionGeneradoService {
    
     public List<RevalidacionGenerado> findAll();

	public void save(RevalidacionGenerado revalidacionGenerado);

	public RevalidacionGenerado findOne(Long id);

	public void delete(Long id);

	public RevalidacionGenerado buscarRevalidacionGeneradoPorRevalidacion(Long id_revalidacion);
}
