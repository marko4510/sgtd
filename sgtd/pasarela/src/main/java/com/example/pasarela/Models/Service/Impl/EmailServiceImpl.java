package com.example.pasarela.Models.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IEmailService;
import com.example.pasarela.Models.Service.IPersonaService;

import javax.mail.MessagingException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;


@Service
@Transactional
public class EmailServiceImpl implements IEmailService{
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private IPersonaService personaService;


    @Override
    public void enviarMensajeSimple(String destinatario, String asunto, String texto){
        
        
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(destinatario);
            //setFrom es desde donde estoy enviando 
            helper.setSubject(asunto);
            helper.setText(texto);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        
    }

    @Override
    public void enviarMensajeRegistro(Usuario usuario, String contrasena) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        final Context ctx = new Context();

        Persona persona = personaService.findOne(usuario.getPersona().getId_persona());

        ctx.setVariable("persona", persona.getNombre());
        ctx.setVariable("username", usuario.getUsuario_nom());
        ctx.setVariable("contrasena", contrasena);
        final String htmlContent = templateEngine.process("email/email-registro.html", ctx);

        // Enviar el correo electrónico
        String destinatario = usuario.getPersona().getCorreo();
        

        try {
            helper.setTo(destinatario);
            helper.setSubject("Registro exitoso al Sistema de Titulos y Diplomas UAP");
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
