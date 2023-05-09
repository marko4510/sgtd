package com.example.pasarela.Controller.CertificadoControllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;
import org.springframework.ui.Model;


import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Service.IPersonaService;

@Controller
public class CertificadoController {

    @Autowired
    private IPersonaService personaService;

    @GetMapping("/inicioGenerarCertificado")
    public String inicioGenerarCertificado(Model model) {
        model.addAttribute("personas", personaService.findAll());

        return "certificado/generarTitulosDiplomaCertificado";
    }
    public int calcularEdad(LocalDate fechaNacimiento) {
        LocalDate fechaActual = LocalDate.now();
        return Period.between(fechaNacimiento, fechaActual).getYears();
    }

    public LocalDate convertirDateALocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String convertirMayusculasAMinusculasConPrimeraMayus(String cadena) {
        return cadena.substring(0, 1).toUpperCase() + cadena.substring(1).toLowerCase();
    }

    public String convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(String cadena) {
        String[] palabras = cadena.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }
    public static String convertirNumTexto(int num) {
        if (num == 0) {
          return "cero";
        }
    
        String[] unidades = {
          "", "un", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez",
          "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho",
          "diecinueve"
        };
    
        String[] decenas = {
          "", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta",
          "noventa"
        };
    
        String[] centenas = {"", "ciento", "doscientos", "trescientos", "cuatrocientos",
          "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};
    
        String texto = "";
    
        if (num < 0) {
          texto += "menos ";
          num *= -1;
        }
    
        if ((num / 1000000) > 0) {
          texto += convertirNumTexto(num / 1000000) + " millones ";
          num %= 1000000;
        }
    
        if ((num / 1000) > 0) {
          if ((num / 1000) == 1) {
            texto += "mil ";
          } else {
            texto += convertirNumTexto(num / 1000) + " mil ";
          }
          num %= 1000;
        }
    
        if ((num / 100) > 0) {
          texto += centenas[num / 100] + " ";
          num %= 100;
        }
    
        if (num > 0) {
          if (num < 20) {
            texto += unidades[num] + " ";
          } else {
            texto += decenas[num / 10] + " ";
            num %= 10;
    
            if (num > 0) {
              texto += unidades[num] + " ";
            }
          }
        }
    
        return texto.trim();
      }
    
    @PostMapping("/generarCertificadoSupletorio")
    public String generarCertificadoSupletorio(@RequestParam("id_persona") Long id_persona,
            @RequestParam("tipoSerie") String tipoSerie,
            @RequestParam("nroTitulo") String nroTitulo,
            Model model) throws FileNotFoundException, IOException, ParseException {

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
            Model model) throws FileNotFoundException, IOException, ParseException {

        // Capturar Fecha de Registro de SolicitudLegalizacion
        Date fechaActual = new Date();
        LocalDate localDateFA = convertirDateALocalDate(fechaActual);
        int dia = localDateFA.getDayOfMonth();
        String mes = localDateFA.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anio = localDateFA.getYear();
        String anioS = convertirNumTexto(anio);
        Persona persona = personaService.findOne(id_persona);
        model.addAttribute("persona", persona);
       
        model.addAttribute("tipoSerie", tipoSerie);
        model.addAttribute("nroTitulo", nroTitulo);
        model.addAttribute("dia", dia);
        model.addAttribute("mes", mes);
        model.addAttribute("anioS", anioS);
        return "certificado/plantillaTituloAcademico";
    }

   
    @PostMapping("/generarTituloBachiller")
    public String generarTituloBachiller(@RequestParam("id_persona") Long id_persona,
            @RequestParam("tipoSerie") String tipoSerie,
            @RequestParam("nroTitulo") String nroTitulo,
            Model model) throws FileNotFoundException, IOException, ParseException {
        Persona persona = personaService.findOne(id_persona);
        // Capturar Fecha Actual
        Date fechaActual = new Date();
        LocalDate localDateFA = convertirDateALocalDate(fechaActual);
        int dia = localDateFA.getDayOfMonth();
        String diaS = convertirNumTexto(dia);
        String mes = localDateFA.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anio = localDateFA.getYear();
        
        SimpleDateFormat formato = new SimpleDateFormat("dd ' de ' ' ' MMMM ' del ' ' ' yyyy", new Locale("es", "ES"));
        String fechaTexto = formato.format(fechaActual);
        // Calcular Edad
        Date fechaEdad = persona.getFecha_nacimiento();
      
        LocalDate localDate = convertirDateALocalDate(fechaEdad);
        
        int edad = calcularEdad(localDate);

        String cadenaDepartamento = persona.getProvincia().getDepartamento().getNombre();
        String cadenaProvincia = persona.getProvincia().getNombre_provincia();
        String cadenaDepartamentoC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaDepartamento);
        String cadenaProvinciaC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaProvincia);
        
        model.addAttribute("persona", persona);
        model.addAttribute("fechaTitulo", fechaTexto);
        model.addAttribute("tipoSerie", tipoSerie);
        model.addAttribute("nroTitulo", nroTitulo);
        model.addAttribute("edad", edad);
        model.addAttribute("departamento", cadenaDepartamentoC);
        model.addAttribute("provincia", cadenaProvinciaC);
        model.addAttribute("dia", diaS);
        model.addAttribute("mes", mes);
        model.addAttribute("anio", anio);
        return "certificado/plantillaBachiller";
    }
}
