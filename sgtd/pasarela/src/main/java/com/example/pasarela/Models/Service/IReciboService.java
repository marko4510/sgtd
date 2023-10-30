package com.example.pasarela.Models.Service;

import java.util.List;


import com.example.pasarela.Models.Entity.Recibo;

public interface IReciboService {
    

     public List<Recibo> findAll();

	public void save(Recibo recibo);

	public Recibo findOne(Long id);

	public void delete(Long id);

	public List<Recibo> lista_recibo_usuario(Long id_usuario);
}
