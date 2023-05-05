package com.example.pasarela.Models.Service;

import com.example.pasarela.Models.Entity.Usuario;

public interface IEmailService {

    public void enviarMensajeSimple(String destinatario, String asunto, String texto);
    public void enviarMensajeRegistro(Usuario usuario, String password);
    //public void enviarMensajerestablecimiento(Usuario usuario, String password);
}
