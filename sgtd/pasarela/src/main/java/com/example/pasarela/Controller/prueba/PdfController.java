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
import com.example.pasarela.Models.Utils.Archive;
import com.itextpdf.text.DocumentException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    
    Archive archive = new Archive();

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
    public String generarTituloPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona, @RequestParam("gestion") String gestion,@RequestParam("nroTitulo") String nroTitulo, Model model) throws FileNotFoundException, IOException, ParseException, DocumentException {
        
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
      String codigo = archive.getMD5(id_persona+"");
   
      Path rootPathPlantilla = Paths.get("plantillas/");
      Path rootAbsolutPathPlantilla = rootPathPlantilla.toAbsolutePath();
      String plantilla = rootAbsolutPathPlantilla+"/plantilla_titulo_academico.pdf";
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
     context.setVariable("codigo", codigo);
     context.setVariable("plantilla", plantilla);
     // Renderizar la vista HTML utilizando Thymeleaf
     String htmlContent = templateEngine.process("certificado/certificadoPrueba-pdf", context);

     // Directorio donde se guardará el archivo PDF 
     Path rootPathTitulos = Paths.get("archivos/titulos/");
		Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();
   

     TituloGenerado tituloGenerado = new TituloGenerado();
   
     // Nombre del archivo PDF
     String nombreArchivo = codigo+".pdf";

     // Generar la ruta completa del archivo
     String rutaCompleta =rootAbsolutPathTitulos + "/" + nombreArchivo;


    

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
    tituloGenerado.setRuta_archivo(rutaCompleta);
    tituloGenerado.setEstado("A");
    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);


    //Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setPersona(persona);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);



    return "redirect:listarTitulos";
    }

    @PostMapping("/generarTituloBachillerPdf")
    public String generarTituloBachillerPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona, @RequestParam("gestion") String gestion, Model model) throws FileNotFoundException, IOException, ParseException, DocumentException {
        
      Persona persona = personaService.findOne(id_persona);
      String codigo = archive.getMD5(id_persona+"");
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
      List<Titulo> listTitulo = tituloService.findAll();
      String nroTitulo = (listTitulo.size() + 1)+"";
     // Crear el contexto con los datos necesarios para la vista
     Context context = new Context();
     // Agregar los datos que necesites en tu vista
     context.setVariable("persona", persona);
     context.setVariable("fechaTitulo", fechaTexto);
     context.setVariable("edad", edad);
     context.setVariable("departamento", cadenaDepartamentoC);
     context.setVariable("provincia", cadenaProvinciaC);
     context.setVariable("dia", diaS);
     context.setVariable("mes", mes);
     context.setVariable("anio", anio);
     context.setVariable("codigo", codigo);

     // Renderizar la vista HTML utilizando Thymeleaf
     String htmlContent = templateEngine.process("certificado/bachillerPrueba-pdf", context);

     // Directorio donde se guardará el archivo PDF en el disco local C
     Path rootPathTitulos = Paths.get("archivos/titulos/");
		Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();

     // Directorio donde se guardará el archivo PDF con Plantilla 
     Path rootPathTitulosP = Paths.get("archivos/titulos/bachiller");
     Path rootAbsolutPathTitulosP = rootPathTitulosP.toAbsolutePath();
      // Directorio de la Plantilla 
      Path rootPathPlantillaPath = Paths.get("plantillas/");
      Path rootAbsolutPathPlantillasPath = rootPathPlantillaPath.toAbsolutePath();
     TituloGenerado tituloGenerado = new TituloGenerado();
   
     // Nombre del archivo PDF
     String nombreArchivo = codigo+".pdf";

     // Generar la ruta completa del archivo
     String rutaCompleta =rootAbsolutPathTitulos + "/" + nombreArchivo;
     //Ruta completa de la Plantilla
     String rutaCompletaP = rootAbsolutPathPlantillasPath + "/esc.jpg";

      String rutaCompletaSalida = rootAbsolutPathTitulosP +"/"+nombreArchivo;
    

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
    archive.plantilla(rutaCompleta, rutaCompletaSalida, rutaCompletaP, codigo);
    //Registrar titulo Generado 
    
    tituloGenerado.setNombre_archivo(nombreArchivo);
    tituloGenerado.setRuta_archivo(rutaCompletaSalida);
    tituloGenerado.setEstado("A");
    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);


    //Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setPersona(persona);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);



    return "redirect:listarTitulos";
    }


    @PostMapping("/generarTituloProvisionPdf")
    public String generarTituloProvisionPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona, @RequestParam("gestion") String gestion,@RequestParam("nroTitulo") String nroTitulo, Model model) throws FileNotFoundException, IOException, ParseException, DocumentException {
        
      Date fechaActual = new Date();
      
      LocalDate localDateFA = convertirDateALocalDate(fechaActual);
      int diaNum = localDateFA.getDayOfMonth();
      String dia = convertirNumTexto(diaNum);
      String diaC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(dia);
      String mes = localDateFA.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
      Persona persona = personaService.findOne(id_persona);
      String cadenaDepartamento = persona.getProvincia().getDepartamento().getNombre();
      String cadenaProvincia = persona.getProvincia().getNombre_provincia();
      String cadenaDepartamentoC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaDepartamento);
      String cadenaProvinciaC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaProvincia);
      String cadenaMesC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(mes);
      String codigo = archive.getMD5(id_persona+"");
   
      Date fechaNacimiento = persona.getFecha_nacimiento();
      LocalDate localDateFa2 = convertirDateALocalDate(fechaNacimiento);
      int diaN = localDateFa2.getDayOfMonth();
      String mesN = localDateFa2.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
      int anioN = localDateFa2.getYear();
     // Crear el contexto con los datos necesarios para la vista
     Context context = new Context();
     // Agregar los datos que necesites en tu vista
     context.setVariable("departamento", cadenaDepartamentoC);
     context.setVariable("provincia",cadenaProvinciaC);
     context.setVariable("persona",persona);
     context.setVariable("dia",diaC);
     context.setVariable("diaN",diaN);
     context.setVariable("mesN",mesN);
     context.setVariable("anioN",anioN);
     context.setVariable("mes", cadenaMesC);
     context.setVariable("gestion", gestion);
     context.setVariable("nroTitulo", nroTitulo);
     context.setVariable("codigo", codigo);

     // Renderizar la vista HTML utilizando Thymeleaf
     String htmlContent = templateEngine.process("certificado/provisionPrueba-pdf", context);

     // Directorio donde se guardará el archivo PDF 
     Path rootPathTitulos = Paths.get("archivos/titulos/provision");
		Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();
   

     TituloGenerado tituloGenerado = new TituloGenerado();
   
     // Nombre del archivo PDF
     String nombreArchivo = codigo+".pdf";

     // Generar la ruta completa del archivo
     String rutaCompleta =rootAbsolutPathTitulos + "/" + nombreArchivo;


    

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
    tituloGenerado.setRuta_archivo(rutaCompleta);
    tituloGenerado.setEstado("A");
    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);


    //Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setPersona(persona);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);



    return "redirect:listarTitulos";
    }

  
    
}