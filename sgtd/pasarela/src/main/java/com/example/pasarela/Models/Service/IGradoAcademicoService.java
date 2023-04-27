package com.example.pasarela.Models.Service;
import java.util.List;

import com.example.pasarela.Models.Entity.GradoAcademico;

public interface IGradoAcademicoService {
    public List<GradoAcademico> findAll();

    public List<GradoAcademico> gradoPorIdCarrera(Long id_carrera);

    public void save(GradoAcademico gradoAcademico);

	public GradoAcademico findOne(Long id);

	public void delete(Long id);
}
