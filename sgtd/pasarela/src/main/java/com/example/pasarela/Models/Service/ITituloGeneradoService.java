package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.TituloGenerado;

public interface ITituloGeneradoService {
    public TituloGenerado registrarTituloGenerado(TituloGenerado tituloGenerado);

    public TituloGenerado buscarTituloGenerado(Long id_titulo_generado);


    public TituloGenerado buscarTituloGeneradoPorTitulo(Long id_titulo);

    public void modificarTituloGenerado(TituloGenerado tituloGenerado);

    public List<TituloGenerado> listarTituloGenerado();
}
