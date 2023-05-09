package com.example.pasarela.Controller.CertificadoControllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public String generarCertificadoSupletorio(@RequestParam("id_persona") Long id_persona,
    @RequestParam("tipoSerie") String tipoSerie,
    @RequestParam("nroTitulo") String nroTitulo,
    Model model)throws FileNotFoundException, IOException, ParseException {

    	   // Capturar Fecha de Registro de SolicitudLegalizacion
           Date fechaActual = new Date();
           SimpleDateFormat formato = new SimpleDateFormat("d ' de ' ' ' MMMM ' del ' ' ' yyyy", new Locale("es", "ES"));
           String fechaTexto = formato.format(fechaActual);

    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
        model.addAttribute("fechaTitulo", fechaTexto);
        model.addAttribute("tipoSerie", tipoSerie);
        model.addAttribute("nroTitulo", nroTitulo);

        return "certificado/plantillaSupleAcademico";
    }

    @PostMapping("/generarTituloProvisionNacional")
    public String generarTituloProvisionNacional(@RequestParam("id_persona") Long id_persona, Model model) {
    	
    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
    
        return "certificado/plantillaAcademico";
    }

    @PostMapping("/generarTitulo")
    public String generarTitulo(@RequestParam("id_persona") Long id_persona,
    @RequestParam("tipoSerie") String tipoSerie,
    @RequestParam("nroTitulo") String nroTitulo,
    Model model) throws FileNotFoundException, IOException, ParseException{
    	
         // Capturar Fecha de Registro de SolicitudLegalizacion
         Date fechaActual = new Date();
         SimpleDateFormat formato = new SimpleDateFormat("d ' de ' ' ' MMMM ' del ' ' ' yyyy", new Locale("es", "ES"));
         String fechaTexto = formato.format(fechaActual);

    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
        model.addAttribute("fechaTitulo", fechaTexto);
        model.addAttribute("tipoSerie", tipoSerie);
        model.addAttribute("nroTitulo", nroTitulo);
        return "certificado/plantillaTituloAcademico";
    }

    @PostMapping("/generarTituloBachiller")
    public String generarTituloBachiller(@RequestParam("id_persona") Long id_persona,
    @RequestParam("tipoSerie") String tipoSerie,
    @RequestParam("nroTitulo") String nroTitulo,
    Model model) throws FileNotFoundException, IOException, ParseException{
    	
         // Capturar Fecha de Registro de SolicitudLegalizacion
         Date fechaActual = new Date();
         SimpleDateFormat formato = new SimpleDateFormat("d ' de ' ' ' MMMM ' del ' ' ' yyyy", new Locale("es", "ES"));
         String fechaTexto = formato.format(fechaActual);

    	Persona persona = personaService.findOne(id_persona);
    	model.addAttribute("persona", persona);
        model.addAttribute("fechaTitulo", fechaTexto);
        model.addAttribute("tipoSerie", tipoSerie);
        model.addAttribute("nroTitulo", nroTitulo);
        return "certificado/plantillaBachiller";
    }
}
