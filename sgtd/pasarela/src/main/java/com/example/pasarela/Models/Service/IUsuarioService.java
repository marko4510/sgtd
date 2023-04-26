package com.example.pasarela.Models.Service;

import java.util.List;

import com.example.pasarela.Models.Entity.Usuario;

public interface IUsuarioService {
    
    public List<Usuario> findAll();
    
    public void save(Usuario usuario);

	public Usuario findOne(Long id);

	public void delete(Long id);

    public Usuario getUsuarioContraseña(String correo, String password);
	
    public Usuario getUsuarioPersona(Long id_persona);
}
