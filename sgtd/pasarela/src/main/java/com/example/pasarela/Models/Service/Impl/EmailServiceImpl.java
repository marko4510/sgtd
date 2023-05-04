package com.example.pasarela.Models.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;

/* 
import javax.servlet. * ;
import javax.servlet.http. *;
import javax.mail. * ;
import javax.mail.internet. * ;
import javax.activation.  * ;*/

@Service
@Transactional
public class EmailServiceImpl {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendListEmail(String emailTo){
        MimeMessage message = javaMailSender.createMimeMessage();
        
    }
}
