package com.example.pasarela.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InicioController {
    

    //Funcion de visualizaciòn de la pagina principal
    @RequestMapping(value = "/Inicio", method = RequestMethod.GET) // Pagina principal
	public String Inicio2(Model model) {  
		return "index";
		
	}


    
}
