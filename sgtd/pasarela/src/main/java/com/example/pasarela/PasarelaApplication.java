package com.example.pasarela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.example.pasarela.Models.Service.Impl.EmailServiceImpl;


@SpringBootApplication
public class PasarelaApplication {

	@Autowired
	private EmailServiceImpl senderService;
	public static void main(String[] args) {
		SpringApplication.run(PasarelaApplication.class, args);
	}
	
	

}
