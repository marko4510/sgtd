package com.example.pasarela.Controller.prueba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Entity.Nacionalidad;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.PrgMtrCertificadoDto;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Entity.Recibo;
import com.example.pasarela.Models.Entity.Revalidacion;
import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IReciboService;
import com.example.pasarela.Models.Service.IRevalidacionGeneradoService;
import com.example.pasarela.Models.Service.IRevalidacionService;
import com.example.pasarela.Models.Service.ISolicitudLegalizacionService;
import com.example.pasarela.Models.Service.ITituloGeneradoService;
import com.example.pasarela.Models.Service.ITituloService;
import com.example.pasarela.Models.Service.IUsuarioService;
import com.example.pasarela.Models.Utils.Archive;
import com.example.pasarela.Models.Utils.numeroAtexto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.TextStyle;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.EncodeHintType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


@Controller
public class PdfController {

  @Autowired
  private IPersonaService personaService;

  @Autowired
  private ITituloService tituloService;

  @Autowired
  private ITituloGeneradoService tituloGeneradoService;

  @Autowired
  private IReciboService reciboService;

  @Autowired
  private ISolicitudLegalizacionService solicitudLegalizacionService;

  @Autowired
  private IUsuarioService usuarioService;

    @Autowired
    private IRevalidacionService revalidacionService;

    @Autowired
    private IRevalidacionGeneradoService revalidacionGeneradoService;

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
  public String generarTituloPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona,
      @RequestParam("gestion") String gestion, @RequestParam("nroTitulo") String nroTitulo, Model model)
      throws FileNotFoundException, IOException, ParseException, DocumentException {

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
    String codigo = archive.getMD5(titulo.getId_titulo() + "");

    Path rootPathPlantilla = Paths.get("plantillas/");
    Path rootAbsolutPathPlantilla = rootPathPlantilla.toAbsolutePath();
    String plantilla = rootAbsolutPathPlantilla + "/plantilla_titulo_academico.pdf";
    // Crear el contexto con los datos necesarios para la vista
    Context context = new Context();
    // Agregar los datos que necesites en tu vista
    context.setVariable("departamento", cadenaDepartamentoC);
    context.setVariable("provincia", cadenaProvinciaC);
    context.setVariable("persona", persona);
    context.setVariable("dia", dia);
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
    String nombreArchivo = codigo + ".pdf";

    // Generar la ruta completa del archivo
    String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;

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

    // Registrar titulo Generado

    tituloGenerado.setNombre_archivo(nombreArchivo);
    tituloGenerado.setRuta_archivo(rutaCompleta);
    tituloGenerado.setEstado("A");
    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

    // Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setPersona(persona);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setTipo_titulo("Academico");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);

    return "redirect:listarTitulos";
  }

  @PostMapping("/generarTituloBachillerPdf")
  public String generarTituloBachillerPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona,
      @RequestParam("gestion") String gestion, Model model)
      throws FileNotFoundException, IOException, ParseException, DocumentException {

    Persona persona = personaService.findOne(id_persona);
    String codigo = archive.getMD5(id_persona + "");
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
    String nroTitulo = (listTitulo.size() + 1) + "";
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
    String rutaDirectorioTitulos = rootPathTitulos + "/";
    try {
      if (!Files.exists(rootPathTitulos)) {
        Files.createDirectories(rootPathTitulos);
        System.out.println("Directorio creado: " + rutaDirectorioTitulos);
      } else {
        System.out.println("El directorio ya existe: " + rutaDirectorioTitulos);
      }
    } catch (IOException e) {
      System.err.println("Error al crear el directorio: " + e.getMessage());
    }
    // Directorio donde se guardará el archivo PDF con Plantilla
    Path rootPathTitulosP = Paths.get("archivos/titulos/bachiller");
    Path rootAbsolutPathTitulosP = rootPathTitulosP.toAbsolutePath();
    String rutaDirectorioTitulosP = rootPathTitulosP + "/";
    try {
      if (!Files.exists(rootPathTitulosP)) {
        Files.createDirectories(rootPathTitulosP);
        System.out.println("Directorio creado: " + rutaDirectorioTitulosP);
      } else {
        System.out.println("El directorio ya existe: " + rutaDirectorioTitulosP);
      }
    } catch (IOException e) {
      System.err.println("Error al crear el directorio: " + e.getMessage());
    }

    // Directorio de la Plantilla
    Path rootPathPlantillaPath = Paths.get("plantillas/");
    Path rootAbsolutPathPlantillasPath = rootPathPlantillaPath.toAbsolutePath();
    TituloGenerado tituloGenerado = new TituloGenerado();

    // Nombre del archivo PDF
    String nombreArchivo = codigo + ".pdf";

    // Generar la ruta completa del archivo
    String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;
    // Ruta completa de la Plantilla
    String rutaCompletaP = rootAbsolutPathPlantillasPath + "/esc.jpg";

    String rutaCompletaSalida = rootAbsolutPathTitulosP + "/" + nombreArchivo;

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
    archive.plantillaB(rutaCompleta, rutaCompletaSalida, rutaCompletaP, codigo);
    // Registrar titulo Generado

    tituloGenerado.setNombre_archivo(nombreArchivo);
    tituloGenerado.setRuta_archivo(rutaCompletaSalida);
    tituloGenerado.setEstado("A");

    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

    // Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setPersona(persona);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setTipo_titulo("Bachiller");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);

    return "redirect:listarTitulos";
  }

  @PostMapping("/generarTituloProvisionPdf")
  public String generarTituloProvisionPdf(@Validated Titulo titulo, @RequestParam("id_persona") Long id_persona,
      @RequestParam("gestion") String gestion, @RequestParam("nroTitulo") String nroTitulo, Model model)
      throws FileNotFoundException, IOException, ParseException, DocumentException {

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
    String codigo = archive.getMD5(id_persona + "");

    Date fechaNacimiento = persona.getFecha_nacimiento();
    LocalDate localDateFa2 = convertirDateALocalDate(fechaNacimiento);
    int diaN = localDateFa2.getDayOfMonth();
    String mesN = localDateFa2.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    int anioN = localDateFa2.getYear();
    // Crear el contexto con los datos necesarios para la vista
    Context context = new Context();
    // Agregar los datos que necesites en tu vista
    context.setVariable("departamento", cadenaDepartamentoC);
    context.setVariable("provincia", cadenaProvinciaC);
    context.setVariable("persona", persona);
    context.setVariable("dia", diaC);
    context.setVariable("diaN", diaN);
    context.setVariable("mesN", mesN);
    context.setVariable("anioN", anioN);
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
    String nombreArchivo = codigo + ".pdf";

    // Generar la ruta completa del archivo
    String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;

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

    // Registrar titulo Generado

    tituloGenerado.setNombre_archivo(nombreArchivo);
    tituloGenerado.setRuta_archivo(rutaCompleta);
    tituloGenerado.setEstado("A");
    TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

    // Registrar titulo

    titulo.setTituloGenerado(tituloGenerado2);
    titulo.setPersona(persona);
    titulo.setNro_titulo(nroTitulo);
    titulo.setEstado("A");
    titulo.setTipo_titulo("Provision");
    titulo.setFecha_generacion(localDateFA);
    tituloService.save(titulo);

    return "redirect:listarTitulos";
  }

  @PostMapping("/generarTituloPlantillaPdf")
  public String generarTituloPlantillaPdf(@Validated Titulo titulo,
      @RequestParam(value = "usarPlantilla", required = false) boolean usarPlantilla,
      @RequestParam("id_persona") Long id_persona, @RequestParam("gestion") String gestion,
      @RequestParam("nroTitulo") String nroTitulo, Model model)
      throws FileNotFoundException, IOException, ParseException, DocumentException, WriterException {
    List<Titulo> listTitulo = tituloService.findAll();
    Date fechaActual = new Date();
    LocalDate localDateFA = convertirDateALocalDate(fechaActual);
    String fechaComoString = localDateFA.toString();
    int dia = localDateFA.getDayOfMonth();
    String mes = localDateFA.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    Persona persona = personaService.findOne(id_persona);
    String cadenaDepartamento = persona.getProvincia().getDepartamento().getNombre();
    String cadenaProvincia = persona.getProvincia().getNombre_provincia();
    String cadenaDepartamentoC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaDepartamento);
    String cadenaProvinciaC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(cadenaProvincia);
    String cadenaMesC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(mes);
    String codigo = archive.getMD5((listTitulo.size() + 1) + "");

    Path rootPathPlantilla = Paths.get("plantillas/");
    Path rootAbsolutPathPlantilla = rootPathPlantilla.toAbsolutePath();
    String plantilla = rootAbsolutPathPlantilla + "/plantilla_titulo_academico.pdf";
    // Crear el contexto con los datos necesarios para la vista
    Context context = new Context();
    // Agregar los datos que necesites en tu vista
    context.setVariable("departamento", cadenaDepartamentoC);
    context.setVariable("provincia", cadenaProvinciaC);
    context.setVariable("persona", persona);
    context.setVariable("dia", dia);
    context.setVariable("mes", cadenaMesC);
    context.setVariable("gestion", gestion);
    context.setVariable("nroTitulo", nroTitulo);
    context.setVariable("codigo", codigo);
    context.setVariable("plantilla", plantilla);
    String nombreCompleto = persona.getNombre() + " " + persona.getAp_paterno() + " " + persona.getAp_materno();
    int cantidadCaracteres = nombreCompleto.length();
    System.out.println("+++++++++++++++++++++++++++++" + cantidadCaracteres);
    String esMayor = null;
    if (cantidadCaracteres > 32) {
      esMayor = "a";
    } else {
      esMayor = null;
    }
    System.out.println("+++++++++++++++++++++++" + esMayor);
    context.setVariable("esMayor", esMayor);

    // Renderizar la vista HTML utilizando Thymeleaf
    String htmlContent = templateEngine.process("certificado/tituloAcademico-pdf", context);
    String htmlContent2 = templateEngine.process("certificado/plantilla/ptituloAcademico-pdf", context);
    if (usarPlantilla) {

      // Directorio donde se guardará el archivo PDF en el disco local C
      Path rootPathTitulos = Paths.get("archivos/titulos/");
      Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();
      String rutaDirectorioTitulos = rootAbsolutPathTitulos + "/";
      try {
        if (!Files.exists(rootPathTitulos)) {
          Files.createDirectories(rootPathTitulos);
          System.out.println("Directorio creado: " + rutaDirectorioTitulos);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulos);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      // Directorio donde se guardará el archivo PDF con Plantilla
      Path rootPathTitulosP = Paths.get("archivos/titulos/titulosP");
      Path rootAbsolutPathTitulosP = rootPathTitulosP.toAbsolutePath();
      String rutaDirectorioTitulosP = rootAbsolutPathTitulosP + "/";
      try {
        if (!Files.exists(rootPathTitulosP)) {
          Files.createDirectories(rootPathTitulosP);
          System.out.println("Directorio creado: " + rutaDirectorioTitulosP);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulosP);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      // Directorio de la Plantilla
      Path rootPathPlantillaPath = Paths.get("plantillas/");
      Path rootAbsolutPathPlantillasPath = rootPathPlantillaPath.toAbsolutePath();
      TituloGenerado tituloGenerado = new TituloGenerado();

      // Nombre del archivo PDF
      String nombreArchivo = codigo + ".pdf";

      // Generar la ruta completa del archivo
      String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;
      // Ruta completa de la Plantilla
      String rutaCompletaP = rootAbsolutPathPlantillasPath + "/tpc.jpg";

      String rutaCompletaSalida = rootAbsolutPathTitulosP + "/" + nombreArchivo;

      try {
        // Generar el contenido del código QR
        String qrContent = "Persona: " + persona.getNombre() + " " + persona.getAp_paterno() + " "
            + persona.getAp_materno() + "\n" +
            "Numero de titulo: " + nroTitulo + "\n" +
            "Codigo de titulo: " + codigo + "\n" +
            "Fecha de Generacion titulo: " + fechaComoString;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 0, 0);

        // Crear la imagen BufferedImage del código QR
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }

        // GENERAR QR DEL RECTOR
        String qrContentRector = "Firmado por: MSc. Franz Navia Miranda";
        QRCodeWriter qrCodeWriterRector = new QRCodeWriter();
        BitMatrix bitMatrixRector = qrCodeWriterRector.encode(qrContentRector, BarcodeFormat.QR_CODE, 0, 0);

        int widthRector = bitMatrixRector.getWidth();
        int heightRector = bitMatrixRector.getHeight();
        BufferedImage qrImageRector = new BufferedImage(widthRector, heightRector, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthRector; x++) {
          for (int y = 0; y < heightRector; y++) {
            qrImageRector.setRGB(x, y, bitMatrixRector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL VICERRECTOR
        String qrContentVicerrector = "Firmado por: MSc. Oscar Felipe Melgar Saucedo";
        QRCodeWriter qrCodeWriterVicerrector = new QRCodeWriter();
        BitMatrix bitMatrixVicerrector = qrCodeWriterVicerrector.encode(qrContentVicerrector, BarcodeFormat.QR_CODE,
            0, 0);

        int widthVicerrector = bitMatrixVicerrector.getWidth();
        int heightVicerrector = bitMatrixVicerrector.getHeight();
        BufferedImage qrImageVicerrector = new BufferedImage(widthVicerrector, heightVicerrector,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthVicerrector; x++) {
          for (int y = 0; y < heightVicerrector; y++) {
            qrImageVicerrector.setRGB(x, y, bitMatrixVicerrector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL SECRETARIO
        String qrContentSecretario = "Firmado por: MSc. Ariz Humerez Alvez";
        QRCodeWriter qrCodeWriterSecretario = new QRCodeWriter();
        BitMatrix bitMatrixSecretario = qrCodeWriterSecretario.encode(qrContentSecretario, BarcodeFormat.QR_CODE, 0,
            0);

        int widthSecretario = bitMatrixSecretario.getWidth();
        int heightSecretario = bitMatrixSecretario.getHeight();
        BufferedImage qrImageSecretario = new BufferedImage(widthSecretario, heightSecretario,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthSecretario; x++) {
          for (int y = 0; y < heightSecretario; y++) {
            qrImageSecretario.setRGB(x, y, bitMatrixSecretario.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // Crear el contenido HTML y convertirlo a PDF utilizando Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent2);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Crear un nuevo documento PDF
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfOutputStream.toByteArray()));

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, qrImage);

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageRector = LosslessFactory.createFromImage(pdfDocument, qrImageRector);

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageVicerrector = LosslessFactory.createFromImage(pdfDocument, qrImageVicerrector);
        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageSecretario = LosslessFactory.createFromImage(pdfDocument, qrImageSecretario);

        // Obtener la página donde deseas agregar la imagen
        PDPage page = pdfDocument.getPage(0); // Puedes ajustar el número de página

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 15; // Ajusta esta coordenada x según tus necesidades
          float y = 830; // Ajusta esta coordenada y según tus necesidades
          float widthj = 90; // Ajusta el ancho de la imagen
          float heightj = 90; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImage, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 201; // Ajusta esta coordenada x según tus necesidades
          float y = 109; // Ajusta esta coordenada y según tus necesidades
          float widthj = 50; // Ajusta el ancho de la imagen
          float heightj = 50; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageRector, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 20; // Ajusta esta coordenada x según tus necesidades
          float y = 77; // Ajusta esta coordenada y según tus necesidades
          float widthj = 50; // Ajusta el ancho de la imagen
          float heightj = 50; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageVicerrector, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 550; // Ajusta esta coordenada x según tus necesidades
          float y = 77;
          float widthj = 50; // Ajusta el ancho de la imagen
          float heightj = 50; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageSecretario, x, y, widthj, heightj);
        }

        // Guardar el PDF con la imagen del código QR agregada
        pdfDocument.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
        pdfDocument.close();
        // Ruta al archivo de fuente personalizada
        String fontFilePath = "sgtd/pasarela/src/main/resources/static/fonts/Kuenstler Script Bold.ttf";
        try {
          // Cargar el documento PDF existente
          PDDocument pdfDocument2 = PDDocument.load(new File(rutaCompleta)); // Asegúrate de que "rutaCompleta" apunte
                                                                             // al archivo existente

          // Obtener la página donde deseas agregar el texto
          PDPage page2 = pdfDocument2.getPage(0); // Puedes ajustar el número de página

          // Crear un objeto de contenido para escribir texto en la página
          PDPageContentStream contentStream = new PDPageContentStream(pdfDocument2, page2,
              PDPageContentStream.AppendMode.APPEND, true, true);

          // Cargar la fuente personalizada
          PDType0Font customFont = PDType0Font.load(pdfDocument2, new File(fontFilePath));

          float fontSize = 32; // Ajusta el tamaño según tus necesidades
          float fontSize2 = 20;
          float fontFirma = 17;
          // Configurar el texto y calcular su ancho
          String texto = persona.getGradoAcademico().getNombre();
          contentStream.setFont(customFont, fontSize);
          float textWidth = customFont.getStringWidth(texto) * fontSize / 1000f;

          // Obtener el ancho de la página en puntos
          float pageWidth = page2.getMediaBox().getWidth();

          // Calcular la posición X para centrar el texto
          float xTexto = (pageWidth - textWidth) / 2;

          // Configurar la posición Y del texto
          float yTexto = 288; // Ajusta esta coordenada y según tus necesidades

          // Agregar el texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto, yTexto);
          contentStream.showText(texto);
          contentStream.endText();

          // Configurar el segundo texto y calcular su ancho
          String texto2 = persona.getGradoAcademico().getCarrera().getNombre_carrera(); // Reemplaza con tu segundo
                                                                                        // texto
          float textWidth2 = customFont.getStringWidth(texto2) * fontSize / 1000f;

          // Configurar la posición Y del segundo texto (un poco más arriba)
          float yTexto2 = 420; // Ajusta esta coordenada y según tus necesidades

          // Agregar el segundo texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset((page2.getMediaBox().getWidth() - textWidth2) / 2, yTexto2);
          contentStream.showText(texto2);
          contentStream.endText();

          contentStream.setFont(customFont, fontSize2);
          String diaConvertido = String.valueOf(dia);
          String texto3 = diaConvertido;
          float xTexto3 = 250;
          float yTexto3 = 170;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto3, yTexto3);
          contentStream.showText(texto3);
          contentStream.endText();

          String mesConvertido = String.valueOf(cadenaMesC);
          String texto4 = mesConvertido;
          float xTexto4 = 326;
          float yTexto4 = 170;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto4, yTexto4);
          contentStream.showText(texto4);
          contentStream.endText();

          String gestionC = String.valueOf(gestion);
          String texto5 = "Dos mil " + gestionC;
          float xTexto5 = 450;
          float yTexto5 = 170;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto5, yTexto5);
          contentStream.showText(texto5);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto7 = "MSc. Franz Navia Miranda";
          float xTexto7 = 245;
          float yTexto7 = 90;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto7, yTexto7);
          contentStream.showText(texto7);
          contentStream.endText();

          String texto9 = "Rector Magnífico";
          float xTexto9 = 285;
          float yTexto9 = 72;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto9, yTexto9);
          contentStream.showText(texto9);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto10 = "MSc. Oscar Felipe Melgar Saucedo";
          float xTexto10 = 50;
          float yTexto10 = 60;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto10, yTexto10);
          contentStream.showText(texto10);
          contentStream.endText();

          String texto11 = "Vicerrector";
          float xTexto11 = 120;
          float yTexto11 = 42;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto11, yTexto11);
          contentStream.showText(texto11);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto12 = "MSc. Ariz Humerez Alvez";
          float xTexto12 = 410;
          float yTexto12 = 60;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto12, yTexto12);
          contentStream.showText(texto12);
          contentStream.endText();

          String texto13 = "Secretario General";
          float xTexto13 = 445;
          float yTexto13 = 42;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto13, yTexto13);
          contentStream.showText(texto13);
          contentStream.endText();

          // Cerrar el contenido y guardar el documento PDF
          contentStream.close();
          pdfDocument2.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
          pdfDocument2.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

      } catch (Exception e) {
        e.printStackTrace(); // Maneja las excepciones según tus necesidades
      }

      archive.plantilla(rutaCompleta, rutaCompletaSalida, rutaCompletaP, codigo);
      // Registrar titulo Generado

      tituloGenerado.setNombre_archivo(nombreArchivo);
      tituloGenerado.setRuta_archivo(rutaCompletaSalida);
      tituloGenerado.setEstado("A");

      TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

      // Registrar titulo

      titulo.setTituloGenerado(tituloGenerado2);
      titulo.setPersona(persona);
      titulo.setNro_titulo(nroTitulo);
      titulo.setEstado("A");
      titulo.setTipo_titulo("Academico");
      titulo.setFecha_generacion(localDateFA);
      tituloService.save(titulo);

      return "redirect:listarTitulos";
    } else {
      // Directorio donde se guardará el archivo PDF
      Path rootPathTitulos = Paths.get("archivos/titulos/");
      Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();
      String rutaDirectorioTitulos = rootPathTitulos + "/";
      try {
        if (!Files.exists(rootPathTitulos)) {
          Files.createDirectories(rootPathTitulos);
          System.out.println("Directorio creado: " + rutaDirectorioTitulos);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulos);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      TituloGenerado tituloGenerado = new TituloGenerado();

      // Nombre del archivo PDF
      String nombreArchivo = codigo + ".pdf";

      // Generar la ruta completa del archivo
      String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;

      try {
        // Generar el contenido del código QR
        String qrContent = "Persona: " + persona.getNombre() + " " + persona.getAp_paterno() + " "
            + persona.getAp_materno() + "\n" +
            "Numero de titulo: " + nroTitulo + "\n" +
            "Codigo de titulo: " + codigo + "\n" +
            "Fecha de Generacion titulo: " + fechaComoString;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

        // Crear la imagen BufferedImage del código QR
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }

        // GENERAR QR DEL RECTOR
        String qrContentRector = "Firmado por: MSc. Franz Navia Miranda";
        QRCodeWriter qrCodeWriterRector = new QRCodeWriter();
        BitMatrix bitMatrixRector = qrCodeWriterRector.encode(qrContentRector, BarcodeFormat.QR_CODE, 200, 200);

        int widthRector = bitMatrixRector.getWidth();
        int heightRector = bitMatrixRector.getHeight();
        BufferedImage qrImageRector = new BufferedImage(widthRector, heightRector, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthRector; x++) {
          for (int y = 0; y < heightRector; y++) {
            qrImageRector.setRGB(x, y, bitMatrixRector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL VICERRECTOR
        String qrContentVicerrector = "Firmado por: MSc. Oscar Felipe Melgar Saucedo";
        QRCodeWriter qrCodeWriterVicerrector = new QRCodeWriter();
        BitMatrix bitMatrixVicerrector = qrCodeWriterVicerrector.encode(qrContentVicerrector, BarcodeFormat.QR_CODE,
            200, 200);

        int widthVicerrector = bitMatrixVicerrector.getWidth();
        int heightVicerrector = bitMatrixVicerrector.getHeight();
        BufferedImage qrImageVicerrector = new BufferedImage(widthVicerrector, heightVicerrector,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthVicerrector; x++) {
          for (int y = 0; y < heightVicerrector; y++) {
            qrImageVicerrector.setRGB(x, y, bitMatrixVicerrector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL SECRETARIO
        String qrContentSecretario = "Firmado por: MSc. Ariz Humerez Alvez";
        QRCodeWriter qrCodeWriterSecretario = new QRCodeWriter();
        BitMatrix bitMatrixSecretario = qrCodeWriterSecretario.encode(qrContentSecretario, BarcodeFormat.QR_CODE, 200,
            200);

        int widthSecretario = bitMatrixSecretario.getWidth();
        int heightSecretario = bitMatrixSecretario.getHeight();
        BufferedImage qrImageSecretario = new BufferedImage(widthSecretario, heightSecretario,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthSecretario; x++) {
          for (int y = 0; y < heightSecretario; y++) {
            qrImageSecretario.setRGB(x, y, bitMatrixSecretario.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // Crear el contenido HTML y convertirlo a PDF utilizando Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Crear un nuevo documento PDF
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfOutputStream.toByteArray()));

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, qrImage);

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageRector = LosslessFactory.createFromImage(pdfDocument, qrImageRector);

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageVicerrector = LosslessFactory.createFromImage(pdfDocument, qrImageVicerrector);
        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageSecretario = LosslessFactory.createFromImage(pdfDocument, qrImageSecretario);

        // Obtener la página donde deseas agregar la imagen
        PDPage page = pdfDocument.getPage(0); // Puedes ajustar el número de página

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 5; // Ajusta esta coordenada x según tus necesidades
          float y = 840; // Ajusta esta coordenada y según tus necesidades
          float widthj = 90; // Ajusta el ancho de la imagen
          float heightj = 90; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImage, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 210; // Ajusta esta coordenada x según tus necesidades
          float y = 100; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageRector, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 20; // Ajusta esta coordenada x según tus necesidades
          float y = 70; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageVicerrector, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 550; // Ajusta esta coordenada x según tus necesidades
          float y = 70;
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageSecretario, x, y, widthj, heightj);
        }

        // Guardar el PDF con la imagen del código QR agregada
        pdfDocument.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
        pdfDocument.close();

        // Ruta al archivo de fuente personalizada
        String fontFilePath = "sgtd/pasarela/src/main/resources/static/fonts/Kuenstler Script Bold.ttf";
        try {
          // Cargar el documento PDF existente
          PDDocument pdfDocument2 = PDDocument.load(new File(rutaCompleta)); // Asegúrate de que "rutaCompleta" apunte
                                                                             // al archivo existente

          // Obtener la página donde deseas agregar el texto
          PDPage page2 = pdfDocument2.getPage(0); // Puedes ajustar el número de página

          // Crear un objeto de contenido para escribir texto en la página
          PDPageContentStream contentStream = new PDPageContentStream(pdfDocument2, page2,
              PDPageContentStream.AppendMode.APPEND, true, true);

          // Cargar la fuente personalizada
          PDType0Font customFont = PDType0Font.load(pdfDocument2, new File(fontFilePath));

          float fontSize = 32; // Ajusta el tamaño según tus necesidades
          float fontSize2 = 20;
          float fontFirma = 17;
          // Configurar el texto y calcular su ancho
          String texto = persona.getGradoAcademico().getNombre();
          contentStream.setFont(customFont, fontSize);
          float textWidth = customFont.getStringWidth(texto) * fontSize / 1000f;

          // Obtener el ancho de la página en puntos
          float pageWidth = page2.getMediaBox().getWidth();

          // Calcular la posición X para centrar el texto
          float xTexto = (pageWidth - textWidth) / 2;

          // Configurar la posición Y del texto
          float yTexto = 298; // Ajusta esta coordenada y según tus necesidades

          // Agregar el texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto, yTexto);
          contentStream.showText(texto);
          contentStream.endText();

          // Configurar el segundo texto y calcular su ancho
          String texto2 = persona.getGradoAcademico().getCarrera().getNombre_carrera(); // Reemplaza con tu segundo
                                                                                        // texto
          float textWidth2 = customFont.getStringWidth(texto2) * fontSize / 1000f;

          // Configurar la posición Y del segundo texto (un poco más arriba)
          float yTexto2 = 430; // Ajusta esta coordenada y según tus necesidades

          // Agregar el segundo texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset((page2.getMediaBox().getWidth() - textWidth2) / 2, yTexto2);
          contentStream.showText(texto2);
          contentStream.endText();

          contentStream.setFont(customFont, fontSize2);
          String diaConvertido = String.valueOf(dia);
          String texto3 = diaConvertido;
          float xTexto3 = 250;
          float yTexto3 = 185;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto3, yTexto3);
          contentStream.showText(texto3);
          contentStream.endText();

          String mesConvertido = String.valueOf(cadenaMesC);
          String texto4 = mesConvertido;
          float xTexto4 = 326;
          float yTexto4 = 185;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto4, yTexto4);
          contentStream.showText(texto4);
          contentStream.endText();

          String gestionC = String.valueOf(gestion);
          String texto5 = "Dos mil " + gestionC;
          float xTexto5 = 450;
          float yTexto5 = 185;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto5, yTexto5);
          contentStream.showText(texto5);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto7 = "MSc. Franz Navia Miranda";
          float xTexto7 = 245;
          float yTexto7 = 90;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto7, yTexto7);
          contentStream.showText(texto7);
          contentStream.endText();

          String texto9 = "Rector Magnífico";
          float xTexto9 = 285;
          float yTexto9 = 72;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto9, yTexto9);
          contentStream.showText(texto9);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto10 = "MSc. Oscar Felipe Melgar Saucedo";
          float xTexto10 = 50;
          float yTexto10 = 60;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto10, yTexto10);
          contentStream.showText(texto10);
          contentStream.endText();

          String texto11 = "Vicerrector";
          float xTexto11 = 120;
          float yTexto11 = 42;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto11, yTexto11);
          contentStream.showText(texto11);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto12 = "MSc. Ariz Humerez Alvez";
          float xTexto12 = 410;
          float yTexto12 = 60;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto12, yTexto12);
          contentStream.showText(texto12);
          contentStream.endText();

          String texto13 = "Secretario General";
          float xTexto13 = 445;
          float yTexto13 = 42;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto13, yTexto13);
          contentStream.showText(texto13);
          contentStream.endText();

          // Cerrar el contenido y guardar el documento PDF
          contentStream.close();
          pdfDocument2.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
          pdfDocument2.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

      } catch (Exception e) {
        e.printStackTrace(); // Maneja las excepciones según tus necesidades
      }

      // Registrar titulo Generado

      tituloGenerado.setNombre_archivo(nombreArchivo);
      tituloGenerado.setRuta_archivo(rutaCompleta);
      tituloGenerado.setEstado("A");
      TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

      // Registrar titulo

      titulo.setTituloGenerado(tituloGenerado2);
      titulo.setPersona(persona);
      titulo.setNro_titulo(nroTitulo);
      titulo.setEstado("A");
      titulo.setTipo_titulo("Academico");
      titulo.setFecha_generacion(localDateFA);
      tituloService.save(titulo);

      return "redirect:listarTitulos";
    }

  }

  @PostMapping("/generarTituloProvisionPlantillaPdf")
  public String generarTituloProvisionPlantillaPdf(@Validated Titulo titulo,
      @RequestParam(value = "usarPlantilla", required = false) boolean usarPlantilla,
      @RequestParam("id_persona") Long id_persona, @RequestParam("gestion") String gestion,
      @RequestParam("nroTitulo") String nroTitulo, Model model)
      throws FileNotFoundException, IOException, ParseException, DocumentException {
    List<Titulo> listTitulo = tituloService.findAll();
    Date fechaActual = new Date();

    LocalDate localDateFA = convertirDateALocalDate(fechaActual);
    String fechaComoString = localDateFA.toString();
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
    String codigo = archive.getMD5((listTitulo.size() + 1) + "");
    Date fechaNacimiento = persona.getFecha_nacimiento();
    LocalDate localDateFa2 = convertirDateALocalDate(fechaNacimiento);
    int diaN = localDateFa2.getDayOfMonth();
    String mesN = localDateFa2.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    int anioN = localDateFa2.getYear();

    Path rootPathPlantilla = Paths.get("plantillas/");
    Path rootAbsolutPathPlantilla = rootPathPlantilla.toAbsolutePath();
    String plantilla = rootAbsolutPathPlantilla + "/provisionPDF.pdf";
    // Crear el contexto con los datos necesarios para la vista
    Context context = new Context();
    // Agregar los datos que necesites en tu vista
    context.setVariable("departamento", cadenaDepartamentoC);
    context.setVariable("provincia", cadenaProvinciaC);
    context.setVariable("persona", persona);
    context.setVariable("dia", diaC);
    context.setVariable("diaN", diaN);
    context.setVariable("mesN", mesN);
    context.setVariable("anioN", anioN);
    context.setVariable("mes", cadenaMesC);
    context.setVariable("gestion", gestion);
    context.setVariable("nroTitulo", nroTitulo);
    context.setVariable("codigo", codigo);
    // Renderizar la vista HTML utilizando Thymeleaf
    String htmlContent = templateEngine.process("certificado/provisionPrueba-pdf", context);
    String htmlContent2 = templateEngine.process("certificado/plantilla/pprovisionPrueba-pdf", context);
    if (usarPlantilla) {

      // Directorio donde se guardará el archivo PDF en el disco local C
      Path rootPathTitulos = Paths.get("archivos/titulos/provision");
      Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();

      String rutaDirectorioTitulos = rootPathTitulos + "/";
      try {
        if (!Files.exists(rootPathTitulos)) {
          Files.createDirectories(rootPathTitulos);
          System.out.println("Directorio creado: " + rutaDirectorioTitulos);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulos);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      // Directorio donde se guardará el archivo PDF con Plantilla
      Path rootPathTitulosP = Paths.get("archivos/titulos/provisionP");
      Path rootAbsolutPathTitulosP = rootPathTitulosP.toAbsolutePath();
      String rutaDirectorioTitulosP = rootPathTitulosP + "/";
      try {
        if (!Files.exists(rootPathTitulosP)) {
          Files.createDirectories(rootPathTitulosP);
          System.out.println("Directorio creado: " + rutaDirectorioTitulosP);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulosP);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      // Directorio de la Plantilla
      Path rootPathPlantillaPath = Paths.get("plantillas/");
      Path rootAbsolutPathPlantillasPath = rootPathPlantillaPath.toAbsolutePath();
      TituloGenerado tituloGenerado = new TituloGenerado();
      // Nombre del archivo PDF
      String nombreArchivo = codigo + ".pdf";

      // Generar la ruta completa del archivo
      String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;
      // Ruta completa de la Plantilla
      String rutaCompletaP = rootAbsolutPathPlantillasPath + "/provision.jpg";

      String rutaCompletaSalida = rootAbsolutPathTitulosP + "/" + nombreArchivo;
      // Generar el documento PDF utilizando Flying Saucer
      try {
        // Generar el contenido del código QR
        String qrContent = "Persona: " + persona.getNombre() + " " + persona.getAp_paterno() + " "
            + persona.getAp_materno() + "\n" +
            "Numero de titulo: " + nroTitulo + "\n" +
            "Codigo de titulo: " + codigo + "\n" +
            "Fecha de Generacion titulo: " + fechaComoString;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 0, 0);

        // Crear la imagen BufferedImage del código QR
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }

        // GENERAR QR DEL RECTOR
        String qrContentRector = "Firmado por: MSc. Franz Navia Miranda";
        QRCodeWriter qrCodeWriterRector = new QRCodeWriter();
        BitMatrix bitMatrixRector = qrCodeWriterRector.encode(qrContentRector, BarcodeFormat.QR_CODE, 0, 0);

        int widthRector = bitMatrixRector.getWidth();
        int heightRector = bitMatrixRector.getHeight();
        BufferedImage qrImageRector = new BufferedImage(widthRector, heightRector, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthRector; x++) {
          for (int y = 0; y < heightRector; y++) {
            qrImageRector.setRGB(x, y, bitMatrixRector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL SECRETARIO
        String qrContentSecretario = "Firmado por: MSc. Ariz Humerez Alvez";
        QRCodeWriter qrCodeWriterSecretario = new QRCodeWriter();
        BitMatrix bitMatrixSecretario = qrCodeWriterSecretario.encode(qrContentSecretario, BarcodeFormat.QR_CODE, 0,
            0);

        int widthSecretario = bitMatrixSecretario.getWidth();
        int heightSecretario = bitMatrixSecretario.getHeight();
        BufferedImage qrImageSecretario = new BufferedImage(widthSecretario, heightSecretario,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthSecretario; x++) {
          for (int y = 0; y < heightSecretario; y++) {
            qrImageSecretario.setRGB(x, y, bitMatrixSecretario.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // Crear el contenido HTML y convertirlo a PDF utilizando Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent2);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Crear un nuevo documento PDF
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfOutputStream.toByteArray()));

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, qrImage);
        // Convertir la imagen BufferedImage a PDImageXObject

        PDImageXObject pdImageRector = LosslessFactory.createFromImage(pdfDocument, qrImageRector);
        PDImageXObject pdImageSecretario = LosslessFactory.createFromImage(pdfDocument, qrImageSecretario);
        // Obtener la página donde deseas agregar la imagen

        PDPage page = pdfDocument.getPage(0); // Puedes ajustar el número de página

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 10; // Ajusta esta coordenada x según tus necesidades
          float y = 855; // Ajusta esta coordenada y según tus necesidades
          float widthj = 70; // Ajusta el ancho de la imagen
          float heightj = 70; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImage, x, y, widthj, heightj);
        }

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 46; // Ajusta esta coordenada x según tus necesidades
          float y = 75; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageRector, x, y, widthj, heightj);
        }

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 525; // Ajusta esta coordenada x según tus necesidades
          float y = 75; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageSecretario, x, y, widthj, heightj);
        }

        // Guardar el PDF con la imagen del código QR agregada
        pdfDocument.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
        pdfDocument.close();

        // Ruta al archivo de fuente personalizada
        String fontFilePath = "sgtd/pasarela/src/main/resources/static/fonts/Kuenstler Script Bold.ttf";

        try {
          // Cargar el documento PDF existente
          PDDocument pdfDocument2 = PDDocument.load(new File(rutaCompleta)); // Asegúrate de que "rutaCompleta" apunte
                                                                             // al archivo existente
          String primerTexto = "primerTexto";
          String segundoTexto = "segundoTexto";
          String tercerTexto = "tercerTexto";
          float fontSize2 = 32;
          float fontSize = 20;
          float fontFirma = 18;

          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Enfermería")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Enfermería";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Enfermería";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Medicina")) {

            primerTexto = "Médico Cirujano";
            segundoTexto = "Médico Cirujano";
            tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Odontología")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Cirujano Odontólogo";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Cirujano Odontólogo";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Medicina Veterinaria y Zootecnia")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Médico Veterinario Zootecnista";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Médico Veterinario Zootecnista";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Biología")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Biología";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Biología";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Ambiental")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Ambiental";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Ambiental";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Agroforestal")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Agroforestal";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Agroforestal";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }

          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Industrial")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Industrial";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Industrial";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Civil")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Civil";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Civil";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Informática")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Informático";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Informático";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería de Sistemas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero de Sistemas";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera de Sistemas";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Comunicación Social")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Ciencias de la Comunicación Social";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Ciencias de la Comunicación Social";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Trabajo Social")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Trabajo Social";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Trabajo Social";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ciencias Jurídicas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Abogado";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Abogado";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ciencias Jurídicas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Abogado";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Abogado";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera()
              .equals("Ciencias Políticas y Gestión Pública")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Ciencias Políticas y Gestión Pública";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Ciencias Políticas y Gestión Pública";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Administración de Empresas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Administración de Empresas";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Administración de Empresas";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Contaduría Pública")) {
            if (persona.getGradoAcademico().getNombre().equals("Técnico Universitario Superior")) {

              primerTexto = "Técnico en Contabilidad";
              segundoTexto = "Técnico Universitario Superior";
              tercerTexto = "Contabilidad";

            } else {
              if (persona.getSexo().equals("Masculino")) {
                primerTexto = "Licenciado en Contaduría Pública";
                segundoTexto = "Licenciado";
                tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
              } else {
                primerTexto = "Licenciada en Contaduría Pública";
                segundoTexto = "Licenciada";
                tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
              }
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Comercial")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Comercial";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Comercial";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }

          // Obtener la página donde deseas agregar el texto
          PDPage page2 = pdfDocument2.getPage(0); // Puedes ajustar el número de página

          // Crear un objeto de contenido para escribir texto en la página
          PDPageContentStream contentStream = new PDPageContentStream(pdfDocument2, page2,
              PDPageContentStream.AppendMode.APPEND, true, true);

          // Cargar la fuente personalizada
          PDType0Font customFont = PDType0Font.load(pdfDocument2, new File(fontFilePath));

          // Configurar el texto y calcular su ancho
          String texto = segundoTexto;
          contentStream.setFont(customFont, fontSize2);
          float textWidth = customFont.getStringWidth(texto) * fontSize2 / 1000f;

          // Obtener el ancho de la página en puntos
          float pageWidth = page2.getMediaBox().getWidth();

          // Calcular la posición X para centrar el texto
          float xTexto = (pageWidth - textWidth) / 2;

          // Configurar la posición Y del texto
          float yTexto = 365; // Ajusta esta coordenada y según tus necesidades

          // Agregar el texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto, yTexto);
          contentStream.showText(texto);
          contentStream.endText();

          if (persona.getGradoAcademico().getCarrera().getNombre_carrera()
              .equals("Ciencias Políticas y Gestión Pública")) {
            // Configurar el segundo texto y calcular su ancho
            String texto2 = tercerTexto; // Reemplaza con tu segundo
                                         // texto
            float textWidth2 = customFont.getStringWidth(texto2) * fontSize2 / 1000f;
            float xTexto2 = 180;
            // Configurar la posición Y del segundo texto (un poco más arriba)
            float yTexto2 = 330; // Ajusta esta coordenada y según tus necesidades

            // Agregar el segundo texto al documento centrado
            contentStream.beginText();
            contentStream.newLineAtOffset(xTexto2, yTexto2);
            contentStream.showText(texto2);
            contentStream.endText();
          } else {
            // Configurar el segundo texto y calcular su ancho
            String texto2 = tercerTexto; // Reemplaza con tu segundo
                                         // texto
            float textWidth2 = customFont.getStringWidth(texto2) * fontSize2 / 1000f;
            float xTexto2 = (pageWidth - textWidth2) / 2;
            // Configurar la posición Y del segundo texto (un poco más arriba)
            float yTexto2 = 330; // Ajusta esta coordenada y según tus necesidades

            // Agregar el segundo texto al documento centrado
            contentStream.beginText();
            contentStream.newLineAtOffset(xTexto2, yTexto2);
            contentStream.showText(texto2);
            contentStream.endText();
          }

          // Configurar el segundo texto y calcular su ancho
          String texto3 = primerTexto; // Reemplaza con tu segundo
                                       // texto
          float textWidth3 = customFont.getStringWidth(texto3) * fontSize2 / 1000f;

          // Configurar la posición Y del segundo texto (un poco más arriba)
          float yTexto3 = 530; // Ajusta esta coordenada y según tus necesidades

          // Agregar el segundo texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset((page2.getMediaBox().getWidth() - textWidth3) / 2, yTexto3);
          contentStream.showText(texto3);
          contentStream.endText();

          contentStream.setFont(customFont, fontSize);
          String diaConvertido = String.valueOf(diaNum);
          String texto4 = diaConvertido;
          float xTexto4 = 505;
          float yTexto4 = 200;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto4, yTexto4);
          contentStream.showText(texto4);
          contentStream.endText();

          String texto5 = cadenaMesC;
          float xTexto5 = 185;
          float yTexto5 = 176;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto5, yTexto5);
          contentStream.showText(texto5);
          contentStream.endText();

          String texto6 = "Dos mil " + gestion;
          float xTexto6 = 380;
          float yTexto6 = 176;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto6, yTexto6);
          contentStream.showText(texto6);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto7 = "MSc. Franz Navia Miranda";
          float xTexto7 = 85;
          float yTexto7 = 80;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto7, yTexto7);
          contentStream.showText(texto7);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto7, yTexto7);
          contentStream.showText(texto7);
          contentStream.endText();

          String texto8 = "MSc. Ariz Humerez Alvez";
          float xTexto8 = 367;
          float yTexto8 = 80;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto8, yTexto8);
          contentStream.showText(texto8);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto8, yTexto8);
          contentStream.showText(texto8);
          contentStream.endText();

          String texto9 = "Rector Magnífico";
          float xTexto9 = 115;
          float yTexto9 = 62;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto9, yTexto9);
          contentStream.showText(texto9);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto9, yTexto9);
          contentStream.showText(texto9);
          contentStream.endText();

          String texto10 = "Secretario General";
          float xTexto10 = 405;
          float yTexto10 = 62;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto10, yTexto10);
          contentStream.showText(texto10);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto10, yTexto10);
          contentStream.showText(texto10);
          contentStream.endText();

          // Cerrar el contenido y guardar el documento PDF
          contentStream.close();
          pdfDocument2.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
          pdfDocument2.close();

        } catch (IOException e) {
          e.printStackTrace();
        }

      } catch (Exception e) {
        e.printStackTrace(); // Maneja las excepciones según tus necesidades
      }
      archive.plantilla(rutaCompleta, rutaCompletaSalida, rutaCompletaP, codigo);
      // Registrar titulo Generado

      tituloGenerado.setNombre_archivo(nombreArchivo);
      tituloGenerado.setRuta_archivo(rutaCompletaSalida);
      tituloGenerado.setEstado("A");

      TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

      // Registrar titulo

      titulo.setTituloGenerado(tituloGenerado2);
      titulo.setPersona(persona);
      titulo.setNro_titulo(nroTitulo);
      titulo.setEstado("A");
      titulo.setTipo_titulo("Provision");
      titulo.setFecha_generacion(localDateFA);
      tituloService.save(titulo);

      return "redirect:listarTitulos";
    } else {
      // Directorio donde se guardará el archivo PDF
      Path rootPathTitulos = Paths.get("archivos/titulos/provision");
      Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();
      String rutaDirectorioTitulos = rootPathTitulos + "/";
      try {
        if (!Files.exists(rootPathTitulos)) {
          Files.createDirectories(rootPathTitulos);
          System.out.println("Directorio creado: " + rutaDirectorioTitulos);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulos);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      TituloGenerado tituloGenerado = new TituloGenerado();

      // Nombre del archivo PDF
      String nombreArchivo = codigo + ".pdf";

      // Generar la ruta completa del archivo
      String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;

      try {
        // Generar el contenido del código QR
        String qrContent = "Persona: " + persona.getNombre() + " " + persona.getAp_paterno() + " "
            + persona.getAp_materno() + "\n" +
            "Numero de titulo: " + nroTitulo + "\n" +
            "Codigo de titulo: " + codigo + "\n" +
            "Fecha de Generacion titulo: " + fechaComoString;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

        // Crear la imagen BufferedImage del código QR
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }

        // GENERAR QR DEL RECTOR
        String qrContentRector = "Firmado por: MSc. Franz Navia Miranda";
        QRCodeWriter qrCodeWriterRector = new QRCodeWriter();
        BitMatrix bitMatrixRector = qrCodeWriterRector.encode(qrContentRector, BarcodeFormat.QR_CODE, 200, 200);

        int widthRector = bitMatrixRector.getWidth();
        int heightRector = bitMatrixRector.getHeight();
        BufferedImage qrImageRector = new BufferedImage(widthRector, heightRector, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthRector; x++) {
          for (int y = 0; y < heightRector; y++) {
            qrImageRector.setRGB(x, y, bitMatrixRector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL SECRETARIO
        String qrContentSecretario = "Firmado por: MSc. Ariz Humerez Alvez";
        QRCodeWriter qrCodeWriterSecretario = new QRCodeWriter();
        BitMatrix bitMatrixSecretario = qrCodeWriterSecretario.encode(qrContentSecretario, BarcodeFormat.QR_CODE, 200,
            200);

        int widthSecretario = bitMatrixSecretario.getWidth();
        int heightSecretario = bitMatrixSecretario.getHeight();
        BufferedImage qrImageSecretario = new BufferedImage(widthSecretario, heightSecretario,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthSecretario; x++) {
          for (int y = 0; y < heightSecretario; y++) {
            qrImageSecretario.setRGB(x, y, bitMatrixSecretario.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // Crear el contenido HTML y convertirlo a PDF utilizando Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Crear un nuevo documento PDF
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfOutputStream.toByteArray()));

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, qrImage);
        // Convertir la imagen BufferedImage a PDImageXObject

        PDImageXObject pdImageRector = LosslessFactory.createFromImage(pdfDocument, qrImageRector);
        PDImageXObject pdImageSecretario = LosslessFactory.createFromImage(pdfDocument, qrImageSecretario);
        // Obtener la página donde deseas agregar la imagen

        PDPage page = pdfDocument.getPage(0); // Puedes ajustar el número de página

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 10; // Ajusta esta coordenada x según tus necesidades
          float y = 855; // Ajusta esta coordenada y según tus necesidades
          float widthj = 70; // Ajusta el ancho de la imagen
          float heightj = 70; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImage, x, y, widthj, heightj);
        }

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 46; // Ajusta esta coordenada x según tus necesidades
          float y = 75; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageRector, x, y, widthj, heightj);
        }

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 525; // Ajusta esta coordenada x según tus necesidades
          float y = 75; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageSecretario, x, y, widthj, heightj);
        }

        // Guardar el PDF con la imagen del código QR agregada
        pdfDocument.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
        pdfDocument.close();

        // Ruta al archivo de fuente personalizada
        String fontFilePath = "sgtd/pasarela/src/main/resources/static/fonts/Kuenstler Script Bold.ttf";

        try {
          // Cargar el documento PDF existente
          PDDocument pdfDocument2 = PDDocument.load(new File(rutaCompleta)); // Asegúrate de que "rutaCompleta" apunte
                                                                             // al archivo existente
          String primerTexto = "primerTexto";
          String segundoTexto = "segundoTexto";
          String tercerTexto = "tercerTexto";
          float fontSize2 = 32;
          float fontSize = 20;
          float fontFirma = 18;

          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Enfermería")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Enfermería";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Enfermería";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Medicina")) {

            primerTexto = "Médico Cirujano";
            segundoTexto = "Médico Cirujano";
            tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Odontología")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Cirujano Odontólogo";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Cirujano Odontólogo";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Medicina Veterinaria y Zootecnia")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Médico Veterinario Zootecnista";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Médico Veterinario Zootecnista";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Biología")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Biología";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Biología";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Ambiental")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Ambiental";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Ambiental";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Agroforestal")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Agroforestal";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Agroforestal";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }

          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Industrial")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Industrial";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Industrial";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Civil")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Civil";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Civil";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Informática")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Informático";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Informático";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería de Sistemas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero de Sistemas";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera de Sistemas";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Comunicación Social")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Ciencias de la Comunicación Social";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Ciencias de la Comunicación Social";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Trabajo Social")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Trabajo Social";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Trabajo Social";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ciencias Jurídicas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Abogado";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Abogado";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera()
              .equals("Ciencias Políticas y Gestión Pública")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Ciencias Políticas y Gestión Pública";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Ciencias Políticas y Gestión Pública";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Administración de Empresas")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Licenciado en Administración de Empresas";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Licenciada en Administración de Empresas";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Contaduría Pública")) {
            if (persona.getGradoAcademico().getNombre().equals("Técnico Universitario Superior")) {

              primerTexto = "Técnico en Contabilidad";
              segundoTexto = "Técnico Universitario Superior";
              tercerTexto = "Contabilidad";

            } else {
              if (persona.getSexo().equals("Masculino")) {
                primerTexto = "Licenciado en Contaduría Pública";
                segundoTexto = "Licenciado";
                tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
              } else {
                primerTexto = "Licenciada en Contaduría Pública";
                segundoTexto = "Licenciada";
                tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
              }
            }

          }
          if (persona.getGradoAcademico().getCarrera().getNombre_carrera().equals("Ingeniería Comercial")) {
            if (persona.getSexo().equals("Masculino")) {
              primerTexto = "Ingeniero Comercial";
              segundoTexto = "Licenciado";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            } else {
              primerTexto = "Ingeniera Comercial";
              segundoTexto = "Licenciada";
              tercerTexto = persona.getGradoAcademico().getCarrera().getNombre_carrera();
            }

          }

          // Obtener la página donde deseas agregar el texto
          PDPage page2 = pdfDocument2.getPage(0); // Puedes ajustar el número de página

          // Crear un objeto de contenido para escribir texto en la página
          PDPageContentStream contentStream = new PDPageContentStream(pdfDocument2, page2,
              PDPageContentStream.AppendMode.APPEND, true, true);

          // Cargar la fuente personalizada
          PDType0Font customFont = PDType0Font.load(pdfDocument2, new File(fontFilePath));

          // Configurar el texto y calcular su ancho
          String texto = segundoTexto;
          contentStream.setFont(customFont, fontSize2);
          float textWidth = customFont.getStringWidth(texto) * fontSize2 / 1000f;

          // Obtener el ancho de la página en puntos
          float pageWidth = page2.getMediaBox().getWidth();

          // Calcular la posición X para centrar el texto
          float xTexto = (pageWidth - textWidth) / 2;

          // Configurar la posición Y del texto
          float yTexto = 375; // Ajusta esta coordenada y según tus necesidades

          // Agregar el texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto, yTexto);
          contentStream.showText(texto);
          contentStream.endText();

          if (persona.getGradoAcademico().getCarrera().getNombre_carrera()
              .equals("Ciencias Políticas y Gestión Pública")) {
            // Configurar el segundo texto y calcular su ancho
            String texto2 = tercerTexto; // Reemplaza con tu segundo
                                         // texto
            float textWidth2 = customFont.getStringWidth(texto2) * fontSize2 / 1000f;
            float xTexto2 = 180;
            // Configurar la posición Y del segundo texto (un poco más arriba)
            float yTexto2 = 340; // Ajusta esta coordenada y según tus necesidades

            // Agregar el segundo texto al documento centrado
            contentStream.beginText();
            contentStream.newLineAtOffset(xTexto2, yTexto2);
            contentStream.showText(texto2);
            contentStream.endText();
          } else {
            // Configurar el segundo texto y calcular su ancho
            String texto2 = tercerTexto; // Reemplaza con tu segundo
                                         // texto
            float textWidth2 = customFont.getStringWidth(texto2) * fontSize2 / 1000f;
            float xTexto2 = (pageWidth - textWidth2) / 2;
            // Configurar la posición Y del segundo texto (un poco más arriba)
            float yTexto2 = 340; // Ajusta esta coordenada y según tus necesidades

            // Agregar el segundo texto al documento centrado
            contentStream.beginText();
            contentStream.newLineAtOffset(xTexto2, yTexto2);
            contentStream.showText(texto2);
            contentStream.endText();
          }

          // Configurar el segundo texto y calcular su ancho
          String texto3 = primerTexto; // Reemplaza con tu segundo
                                       // texto
          float textWidth3 = customFont.getStringWidth(texto3) * fontSize2 / 1000f;

          // Configurar la posición Y del segundo texto (un poco más arriba)
          float yTexto3 = 540; // Ajusta esta coordenada y según tus necesidades

          // Agregar el segundo texto al documento centrado
          contentStream.beginText();
          contentStream.newLineAtOffset((page2.getMediaBox().getWidth() - textWidth3) / 2, yTexto3);
          contentStream.showText(texto3);
          contentStream.endText();

          contentStream.setFont(customFont, fontSize);
          String diaConvertido = String.valueOf(diaNum);
          String texto4 = diaConvertido;
          float xTexto4 = 500;
          float yTexto4 = 216;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto4, yTexto4);
          contentStream.showText(texto4);
          contentStream.endText();

          String texto5 = cadenaMesC;
          float xTexto5 = 185;
          float yTexto5 = 191;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto5, yTexto5);
          contentStream.showText(texto5);
          contentStream.endText();

          String texto6 = "Dos mil " + gestion;
          float xTexto6 = 380;
          float yTexto6 = 191;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto6, yTexto6);
          contentStream.showText(texto6);
          contentStream.endText();

          // parrafos de firmas
          contentStream.setFont(customFont, fontFirma);
          String texto7 = "MSc. Franz Navia Miranda";
          float xTexto7 = 85;
          float yTexto7 = 80;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto7, yTexto7);
          contentStream.showText(texto7);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto7, yTexto7);
          contentStream.showText(texto7);
          contentStream.endText();

          String texto8 = "MSc. Ariz Humerez Alvez";
          float xTexto8 = 367;
          float yTexto8 = 80;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto8, yTexto8);
          contentStream.showText(texto8);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto8, yTexto8);
          contentStream.showText(texto8);
          contentStream.endText();

          String texto9 = "Rector Magnífico";
          float xTexto9 = 115;
          float yTexto9 = 62;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto9, yTexto9);
          contentStream.showText(texto9);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto9, yTexto9);
          contentStream.showText(texto9);
          contentStream.endText();

          String texto10 = "Secretario General";
          float xTexto10 = 405;
          float yTexto10 = 62;
          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto10, yTexto10);
          contentStream.showText(texto10);
          contentStream.endText();

          contentStream.beginText();
          contentStream.newLineAtOffset(xTexto10, yTexto10);
          contentStream.showText(texto10);
          contentStream.endText();

          // Cerrar el contenido y guardar el documento PDF
          contentStream.close();
          pdfDocument2.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
          pdfDocument2.close();

        } catch (IOException e) {
          e.printStackTrace();
        }

      } catch (Exception e) {
        e.printStackTrace(); // Maneja las excepciones según tus necesidades
      }

      // Registrar titulo Generado

      tituloGenerado.setNombre_archivo(nombreArchivo);
      tituloGenerado.setRuta_archivo(rutaCompleta);
      tituloGenerado.setEstado("A");
      TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

      // Registrar titulo

      titulo.setTituloGenerado(tituloGenerado2);
      titulo.setPersona(persona);
      titulo.setNro_titulo(nroTitulo);
      titulo.setEstado("A");
      titulo.setTipo_titulo("Provision");
      titulo.setFecha_generacion(localDateFA);
      tituloService.save(titulo);

      return "redirect:listarTitulos";
    }

  }

  public static String generarNumeroEnFormato() {
    Random random = new Random();

    // Generar tres segmentos de 4 dígitos cada uno
    int segmento1 = random.nextInt(10000);
    int segmento2 = random.nextInt(10000);
    int segmento3 = random.nextInt(10000);

    // Formatear los segmentos como una cadena en el formato deseado
    String numeroGenerado = String.format("%04d-%04d-%04d", segmento1, segmento2, segmento3);

    return numeroGenerado;
  }

  @RequestMapping(value = "/ReciboF", method = RequestMethod.POST)
  public String LegalizacionPersonaF(@RequestParam("id_solicitud") Long id_solicitud,
      @RequestParam("opcionPago") String opcionPago,
      Model model, HttpServletRequest request, RedirectAttributes attr)
      throws FileNotFoundException, IOException, ParseException {
    SolicitudLegalizacion solicitud = solicitudLegalizacionService.findOne(id_solicitud);
    Usuario usuario = usuarioService.findOne(solicitud.getUsuario().getId_usuario());
    Long id_usuario = usuario.getId_usuario();
    Date fechaActual = new Date();
    // Ruta donde se guardarán los recibos
    Path rootPath = Paths.get("recibos/legalizacion/");
    Path rootAbsolutePath = rootPath.toAbsolutePath();
    String directoryPath = rootAbsolutePath.toString();
    String cpt = generarNumeroEnFormato();
    List<Recibo> listRecibos = reciboService.findAll();
    String nro_recibo = (listRecibos.size() + 1) + "";
    Recibo recibo = new Recibo();

    recibo.setNro_recibo(nro_recibo);
    recibo.setEstado_recibo("No Pagado");
    recibo.setEstado("A");
    recibo.setTipo_pago_recibo(opcionPago);
    recibo.setFecha_recibo(fechaActual);
    recibo.setMonto_recibo(solicitud.getCostoDocumento().getCosto());
    recibo.setRazon_recibo(solicitud.getUsuario().getPersona().getNombre() + " "
        + solicitud.getUsuario().getPersona().getAp_paterno() + " "
        + solicitud.getUsuario().getPersona().getAp_materno());
    recibo.setNit_recibo(solicitud.getUsuario().getPersona().getCi());

    // Añade el nombre del archivo a la ruta
    String pdfFileName = rootAbsolutePath + File.separator + "recibo_" + recibo.getNro_recibo() + ".pdf";

    solicitud.setEstado("Completado");
    solicitudLegalizacionService.save(solicitud);

    recibo.setArchivo_recibo(pdfFileName);
    recibo.setUsuario(usuario);
    recibo.setCpt_recibo(cpt);
    reciboService.save(recibo);

    // Definir el formato deseado
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);

    // Formatear la fecha
    String fechaFormateada = sdf.format(recibo.getFecha_recibo());

    // Crear el contexto con los datos necesarios para la vista
    if (opcionPago.equals("cpt")) {
      Context context = new Context();
      // Agregar los datos que necesites en tu vista
      context.setVariable("nroRecibo", recibo.getNro_recibo());
      context.setVariable("cpt", recibo.getCpt_recibo());
      context.setVariable("nombre", recibo.getRazon_recibo());
      context.setVariable("nit", recibo.getNit_recibo());
      context.setVariable("fecha", fechaFormateada);
      context.setVariable("solicitud", solicitud);

      // Renderizar la vista HTML utilizando Thymeleaf
      String htmlContent = templateEngine.process("recibo/modelo_recibo", context);
      // Generar el documento PDF utilizando Flying Saucer
      try (OutputStream outputStream = new FileOutputStream(pdfFileName)) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);

        // Establecer tamaño de página Legal
        renderer.layout();
        renderer.createPDF(outputStream);
      } catch (Exception e) {
        // Manejar la excepción según sea necesario
      }
    }
    if (opcionPago.equals("qr")) {
      System.out.println("Pago con QR");
      Context context = new Context();
      // Agregar los datos que necesites en tu vista
      context.setVariable("nroRecibo", recibo.getNro_recibo());
      context.setVariable("nombre", recibo.getRazon_recibo());
      context.setVariable("nit", recibo.getNit_recibo());
      context.setVariable("fecha", fechaFormateada);
      context.setVariable("solicitud", solicitud);

      // Renderizar la vista HTML utilizando Thymeleaf
      String htmlContent = templateEngine.process("recibo/modelo_recibo_qr", context);
      try {
        // Generar el contenido del código QR
        String qrContent = "Numero de Recibo: " + recibo.getNro_recibo() + "\n" +
            "Monto: " + recibo.getMonto_recibo() + "\n" +
            "Fecha de Generacion: " + fechaFormateada;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 50, 50);

        // Crear la imagen BufferedImage del código QR
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        // Crear el contenido HTML y convertirlo a PDF utilizando Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Crear un nuevo documento PDF
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfOutputStream.toByteArray()));

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, qrImage);
        // Obtener la página donde deseas agregar la imagen
        PDPage page = pdfDocument.getPage(0); // Puedes ajustar el número de página

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 475; // Ajusta esta coordenada x según tus necesidades
          float y = 718; // Ajusta esta coordenada y según tus necesidades
          float widthj = 60; // Ajusta el ancho de la imagen
          float heightj = 60; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImage, x, y, widthj, heightj);
        }
        // Guardar el PDF con la imagen del código QR agregada
        pdfDocument.save(pdfFileName); // Reemplaza con la ruta y el nombre adecuados
        pdfDocument.close();

      } catch (Exception e) {
        e.printStackTrace(); // Maneja las excepciones según tus necesidades
      }
    }

    return "redirect:/Historial/" + id_usuario;
  }

  
 

  @RequestMapping(value = "/generarDiplomado", method = RequestMethod.POST)
  public String generarDiplomado(@Validated Titulo titulo, @RequestParam(value = "correlativo") String correlativo,
  @RequestParam(value = "nroTitulo", required = false) String nroTitulo,
      @RequestParam(value = "usarPlantilla", required = false) boolean usarPlantilla, Model model,
      HttpServletRequest request,
      RedirectAttributes redirectAttrs)
      throws FileNotFoundException, IOException, ParseException, DocumentException, WriterException {

    List<Titulo> listTitulo = tituloService.findAll();
    Date fechaActual = new Date();
    LocalDate localDateFA = convertirDateALocalDate(fechaActual);
    String fechaComoString = localDateFA.toString();
    int dia = localDateFA.getDayOfMonth();
    String mes = localDateFA.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());

    String cadenaMesC = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(mes);
    String codigo = archive.getMD5((listTitulo.size() + 1) + "");

    Map<String, Object> requests = new HashMap<String, Object>();
    requests.put("correlativo", correlativo);
    String url = "http://virtual.uap.edu.bo:6061/v1/api/certificado";
    String key = "ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SnpkV0lpT2lKVVNWUlZURUZEU1U5T0lpd2libUZ0WlNJNklsVk9TVlpGVWxOSlJFRkVYMEZOUVZwUFRrbERRVjlRUVU1RVQxOVFUMU5IVWtGRVR5SXNJbWxoZENJNk1qQXlNMzAuZThZeU42YmVyalhMbXFneENzMEl1ZWdiZlRSbWJrUTVOSG95bEVrUV91OA==";
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("api-key", key);

    HttpEntity<HashMap> req = new HttpEntity(requests, headers);

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

    if (resp.getStatusCode().equals(HttpStatus.OK)) {

      PrgMtrCertificadoDto data = new ObjectMapper().convertValue(resp.getBody(), PrgMtrCertificadoDto.class);
      numeroAtexto a = new numeroAtexto();
      String nombre = data.getNombrePersona();
      String apellidoP = data.getApellidoPaterno();
      String apellidoM = data.getApellidoMaterno();
      String programa = data.getNombrePrograma();
      String plan = data.getPlan();
      String version = data.getVersion();
      Integer horas = data.getHorasAcademicas();
      String numeroTextoHoras = a.convertirNumeroATexto(horas);
      String numeroTextoHorasConver = convertirMayusculasAMinusculasConPrimeraMayusPorPalabra(numeroTextoHoras);


      Context context = new Context();
      // Agregar los datos que necesites en tu vista
      context.setVariable("nombre", nombre);
      context.setVariable("apellidoP", apellidoP);
      context.setVariable("apellidoM", apellidoM);
      context.setVariable("programa", programa);
      context.setVariable("plan", plan);
      context.setVariable("version", version);
      context.setVariable("horas", numeroTextoHorasConver);
      
      String htmlContent = templateEngine.process("certificado/tituloDiplomado-pdf", context);

      // Directorio donde se guardará el archivo PDF
      Path rootPathTitulos = Paths.get("archivos/diplomado/");
      Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();
      String rutaDirectorioTitulos = rootPathTitulos + "/";
      try {
        if (!Files.exists(rootPathTitulos)) {
          Files.createDirectories(rootPathTitulos);
          System.out.println("Directorio creado: " + rutaDirectorioTitulos);
        } else {
          System.out.println("El directorio ya existe: " + rutaDirectorioTitulos);
        }
      } catch (IOException e) {
        System.err.println("Error al crear el directorio: " + e.getMessage());
      }

      TituloGenerado tituloGenerado = new TituloGenerado();

      // Nombre del archivo PDF
      String nombreArchivo = codigo + ".pdf";

      // Generar la ruta completa del archivo
      String rutaCompleta = rootAbsolutPathTitulos + "/" + nombreArchivo;

       try {
        // Generar el contenido del código QR
        String qrContent = "Persona: " + nombre + " " + apellidoP + " "
            + apellidoM + "\n" +
            "Numero de titulo: " + nroTitulo + "\n" +
            "Codigo de titulo: " + codigo + "\n" +
            "Fecha de Generacion titulo: " + fechaComoString;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

        // Crear la imagen BufferedImage del código QR
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }

        // GENERAR QR DEL RECTOR
        String qrContentRector = "Firmado por: MSc. Franz Navia Miranda";
        QRCodeWriter qrCodeWriterRector = new QRCodeWriter();
        BitMatrix bitMatrixRector = qrCodeWriterRector.encode(qrContentRector, BarcodeFormat.QR_CODE, 200, 200);

        int widthRector = bitMatrixRector.getWidth();
        int heightRector = bitMatrixRector.getHeight();
        BufferedImage qrImageRector = new BufferedImage(widthRector, heightRector, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthRector; x++) {
          for (int y = 0; y < heightRector; y++) {
            qrImageRector.setRGB(x, y, bitMatrixRector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

        // GENERAR QR DEL VICERRECTOR
        String qrContentVicerrector = "Firmado por: MSc. Oscar Felipe Melgar Saucedo";
        QRCodeWriter qrCodeWriterVicerrector = new QRCodeWriter();
        BitMatrix bitMatrixVicerrector = qrCodeWriterVicerrector.encode(qrContentVicerrector, BarcodeFormat.QR_CODE,
            200, 200);

        int widthVicerrector = bitMatrixVicerrector.getWidth();
        int heightVicerrector = bitMatrixVicerrector.getHeight();
        BufferedImage qrImageVicerrector = new BufferedImage(widthVicerrector, heightVicerrector,
            BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < widthVicerrector; x++) {
          for (int y = 0; y < heightVicerrector; y++) {
            qrImageVicerrector.setRGB(x, y, bitMatrixVicerrector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
          }
        }
        //

       

        // Crear el contenido HTML y convertirlo a PDF utilizando Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Crear un nuevo documento PDF
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfOutputStream.toByteArray()));

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, qrImage);

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageRector = LosslessFactory.createFromImage(pdfDocument, qrImageRector);

        // Convertir la imagen BufferedImage a PDImageXObject
        PDImageXObject pdImageVicerrector = LosslessFactory.createFromImage(pdfDocument, qrImageVicerrector);
        // Convertir la imagen BufferedImage a PDImageXObject

        // Obtener la página donde deseas agregar la imagen
        PDPage page = pdfDocument.getPage(0); // Puedes ajustar el número de página

        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 5; // Ajusta esta coordenada x según tus necesidades
          float y = 840; // Ajusta esta coordenada y según tus necesidades
          float widthj = 90; // Ajusta el ancho de la imagen
          float heightj = 90; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImage, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 210; // Ajusta esta coordenada x según tus necesidades
          float y = 100; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageRector, x, y, widthj, heightj);
        }
        // Agregar la imagen del código QR al contenido del PDF
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float x = 20; // Ajusta esta coordenada x según tus necesidades
          float y = 70; // Ajusta esta coordenada y según tus necesidades
          float widthj = 40; // Ajusta el ancho de la imagen
          float heightj = 40; // Ajusta la altura de la imagen

          contentStream.drawImage(pdImageVicerrector, x, y, widthj, heightj);
        }
       

        // Guardar el PDF con la imagen del código QR agregada
        pdfDocument.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
        pdfDocument.close();

      

      } catch (Exception e) {
        e.printStackTrace(); // Maneja las excepciones según tus necesidades
      }
      Persona personaExiste = personaService.getPersonaByNombres(nombre, apellidoP, apellidoM);

      if (personaExiste == null) {
      Persona personaNueva = new Persona();
      personaNueva.setNombre(nombre);
      personaNueva.setAp_paterno(apellidoP);
      personaNueva.setAp_materno(apellidoM);
      personaNueva.setEstado("P");
      personaNueva.setCi("0-0-0-0-0-0");
      personaService.save(personaNueva); 
      titulo.setPersona(personaNueva); 
      }else{
      titulo.setPersona(personaExiste);
      }

      // Registrar titulo Generado

      tituloGenerado.setNombre_archivo(nombreArchivo);
      tituloGenerado.setRuta_archivo(rutaCompleta);
      tituloGenerado.setEstado("A");
      TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);

      // Registrar titulo
      List<Titulo> titulosDiplomado = tituloService.titulosDiplomado();
   
      titulo.setNro_titulo((titulosDiplomado.size()+1)+"");
      titulo.setTituloGenerado(tituloGenerado2);
      titulo.setNro_titulo(nroTitulo);
      
      titulo.setEstado("A");
      titulo.setTipo_titulo("Diplomado");
      titulo.setFecha_generacion(localDateFA);
      tituloService.save(titulo);



      return "redirect:/listarTitulosPosgrado";

    }
    return "redirect:/LoginR";
  }


  @PostMapping("/generarRevalidacion")
    public void generarRevalidacion(HttpServletResponse response) throws IOException {
          Date fechaActual = new Date();
    // Ruta donde se guardarán las revalidaciones
    Path rootPath = Paths.get("archivos/revalidaciones/");
    Path rootAbsolutePath = rootPath.toAbsolutePath();
    String directoryPath = rootAbsolutePath.toString();
    String cpt = generarNumeroEnFormato();
    List<Revalidacion> listRevalidacion = revalidacionService.findAll();
    String nro_revalidacion = (listRevalidacion.size() + 1) + "";
    Revalidacion revalidacion = new Revalidacion();
    
        // Crear un contexto Thymeleaf
        Context context = new Context();

        // Obtener la URL de la imagen desde la carpeta static
        Resource imageResource = new ClassPathResource("/static/assets/images/uap_negro.png");
        String imageUrl = "file:" + imageResource.getFile().getAbsolutePath(); // URL local

        context.setVariable("imageUrl", imageUrl);
         // Añade el nombre del archivo a la ruta
    String pdfFileName = rootAbsolutePath + File.separator + "recibo_" + revalidacion.getNro_revalidacio() + ".pdf";

        // Procesar el template Thymeleaf
        String processedHtml = templateEngine.process("revalidacion/plantillaRevalidacion", context);

        // Convertir HTML a PDF usando Flying Saucer
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(outputStream, true);
            renderer.finishPDF();

            // Configurar la respuesta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=generated.pdf");

            // Escribir el PDF en la respuesta
            response.getOutputStream().write(outputStream.toByteArray());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

}
