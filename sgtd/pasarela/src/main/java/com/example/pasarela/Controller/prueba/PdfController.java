package com.example.pasarela.Controller.prueba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ITituloGeneradoService;
import com.example.pasarela.Models.Service.ITituloService;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class PdfController {


    @Autowired
    private IPersonaService personaService;

    @Autowired
    private ITituloService tituloService;

    @Autowired
    private ITituloGeneradoService tituloGeneradoService;
    
    private final TemplateEngine templateEngine;
    
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
    
        String[] centenas = { "", "ciento", "doscientos", "trescientos", "cuatrocientos",
            "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos" };
    
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
    public PdfController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping("/generar-pdf")
    public String generarPDF() throws Exception {
        // Crear el contexto con los datos necesarios para la vista
        Context context = new Context();
        // Agregar los datos que necesites en tu vista
        context.setVariable("nombre", "John Doe");
        // Renderizar la vista HTML utilizando Thymeleaf
        String htmlContent = templateEngine.process("certificado/certificadoPrueba-pdf", context);

        // Directorio donde se guardará el archivo PDF en el disco local C
        String directorioSalida = "C:/SGTD/generados/titulos";

        // Nombre del archivo PDF
        String nombreArchivo = "certificadoPrueba.pdf";

        // Generar la ruta completa del archivo
        String rutaCompleta = directorioSalida + "/" + nombreArchivo;

        // Generar el documento PDF utilizando Flying Saucer
        try (OutputStream outputStream = new FileOutputStream(rutaCompleta)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
        }
        return "redirect:LoginR";
    }

    @PostMapping("/generarTituloPdf")
    public String generarTituloPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona, @RequestParam("gestion") String gestion,@RequestParam("nroTitulo") String nroTitulo, Model model) throws FileNotFoundException, IOException, ParseException {
        
      Date fechaActual = new Date();
      LocalDate localDateFA = convertirDateALocalDate(fechaActual);
      int dia = localDateFA.getDayOfMonth();
      String mes = localDateFA.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
      Persona persona = personaService.findOne(id_persona);
      String cadenaDepartamento = persona.getProvincia().getDepartamento().getNombre();
      String cadenaProvincia = persona.getProvincia().getNombre_provincia();
      String cadenaDepartamentoC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaDepartamento);
      String cadenaProvinciaC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaProvincia);
      String cadenaMesC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(mes);

    //
    //Codigo para generar PDF
    //

     // Crear el contexto con los datos necesarios para la vista
     Context context = new Context();
     // Agregar los datos que necesites en tu vista
     context.setVariable("departamento", cadenaDepartamentoC);
     context.setVariable("provincia",cadenaProvinciaC);
     context.setVariable("persona",persona);
     context.setVariable("dia",dia);
     context.setVariable("mes", cadenaMesC);
     context.setVariable("gestion", gestion);
     context.setVariable("nroTitulo", nroTitulo);
     // Renderizar la vista HTML utilizando Thymeleaf
     String htmlContent = templateEngine.process("certificado/certificadoPrueba-pdf", context);

     // Directorio donde se guardará el archivo PDF en el disco local C
     String directorioSalida = "C:/SGTD/generados/titulos";
     TituloGenerado tituloGenerado = new TituloGenerado();
   
     // Nombre del archivo PDF
     String nombreArchivo = nroTitulo+"-Titulo-"+persona.getCi()+".pdf";

     // Generar la ruta completa del archivo
     String rutaCompleta =directorioSalida + "/" + nombreArchivo;

     // Crear los directorios si no existen
    File directorio = new File(directorioSalida);
    if (!directorio.exists()) {
        directorio.mkdirs();
    }

      // Generar el documento PDF utilizando Flying Saucer
      try (OutputStream outputStream = new FileOutputStream(rutaCompleta)) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        
        // Establecer tamaño de página Legal
        renderer.layout();
        renderer.createPDF(outputStream);
    } catch (Exception e) {
        // Manejar la excepción según sea necesario
    }

    //Registrar titulo Generado 
    
    tituloGenerado.setNombre_archivo(nombreArchivo);
    tituloGenerado.setRuta_archivo("SGTD/generados/titulos/"+nombreArchivo);
    tituloGenerado.setEstado("A");
    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);


    //Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);



    return "redirect:listarTitulos";
    }
  
    
}