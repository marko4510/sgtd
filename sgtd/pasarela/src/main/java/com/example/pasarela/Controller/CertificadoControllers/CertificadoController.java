package com.example.pasarela.Controller.CertificadoControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Service.IPersonaService;



@Controller
public class CertificadoController {
    
    @Autowired
    private IPersonaService personaService;

    @GetMapping("/inicioGenerarCertificado")
    public String inicioGenerarCertificado( Model model) {
    	model.addAttribute("personas", personaService.findAll());
    
        return "certificado/generarTitulosDiplomaCertificado";
    }

    @PostMapping("/generarCertificadoSupletorio")
    public String generarCertificadoSupletorio(@RequestParam("id_persona") Long id_persona, Model model) {
    	
    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
    
        return "certificado/prueba";
    }

    @PostMapping("/generarTituloProvisionNacional")
    public String generarTituloProvisionNacional(@RequestParam("id_persona") Long id_persona, Model model) {
    	
    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
    
        return "certificado/plantillaAcademico";
    }

    @PostMapping("/generarTitulo")
    public String generarTitulo(@RequestParam("id_persona") Long id_persona, Model model) {
    	
    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
    
        return "certificado/titulo";
    }
}
