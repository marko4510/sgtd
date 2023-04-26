package com.example.pasarela.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.Model;

@Controller
public class LoginController {

    // Funcion de visualizacion de iniciar sesiòn administrador
	@RequestMapping(value = "/LoginR", method = RequestMethod.GET)
	public String LoginR(Model model) {

		return "login";
	}
    
}
