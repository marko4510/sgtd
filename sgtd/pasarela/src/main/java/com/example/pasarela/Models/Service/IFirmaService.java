package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Firma;

public interface IFirmaService {
    public List<Firma> findAll();

	public void save(Firma firma);

	public Firma findOne(Long id);

	public void delete(Long id);
}
