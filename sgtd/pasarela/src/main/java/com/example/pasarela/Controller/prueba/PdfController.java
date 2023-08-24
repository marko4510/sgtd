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

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.TextStyle;

import java.util.Date;

import java.util.List;
import java.util.Locale;
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

    // Renderizar la vista HTML utilizando Thymeleaf
    String htmlContent = templateEngine.process("certificado/tituloAcademico-pdf", context);
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
      String rutaCompletaP = rootAbsolutPathPlantillasPath + "/tp.jpg";

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
    String qrContent = "Persona: " + persona.getNombre()+" "+persona.getAp_paterno()+" "+persona.getAp_materno() + "\n" +
                        "Numero de titulo: " + nroTitulo + "\n" +
                        "Codigo de titulo: "+ codigo + "\n" +
                         "Fecha de Generacion titulo: "+ fechaComoString;
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

    //GENERAR QR DEL RECTOR
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

     //GENERAR QR DEL VICERRECTOR
     String qrContentVicerrector = "Firmado por: MSc. Oscar Felipe Melgar Saucedo";
    QRCodeWriter qrCodeWriterVicerrector = new QRCodeWriter();
    BitMatrix bitMatrixVicerrector = qrCodeWriterVicerrector.encode(qrContentVicerrector, BarcodeFormat.QR_CODE, 200, 200);
     
    int widthVicerrector = bitMatrixVicerrector.getWidth();
    int heightVicerrector = bitMatrixVicerrector.getHeight();
    BufferedImage qrImageVicerrector = new BufferedImage(widthVicerrector, heightVicerrector, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < widthVicerrector; x++) {
        for (int y = 0; y < heightVicerrector; y++) {
            qrImageVicerrector.setRGB(x, y, bitMatrixVicerrector.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
        }
    }
    //

     //GENERAR QR DEL SECRETARIO
     String qrContentSecretario = "Firmado por: MSc. Ariz Humerez Alvez";
    QRCodeWriter qrCodeWriterSecretario = new QRCodeWriter();
    BitMatrix bitMatrixSecretario = qrCodeWriterSecretario.encode(qrContentSecretario, BarcodeFormat.QR_CODE, 200, 200);
     
    int widthSecretario = bitMatrixSecretario.getWidth();
    int heightSecretario = bitMatrixSecretario.getHeight();
    BufferedImage qrImageSecretario = new BufferedImage(widthSecretario, heightSecretario, BufferedImage.TYPE_INT_RGB);
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
    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
        float x = 15; // Ajusta esta coordenada x según tus necesidades
        float y = 830; // Ajusta esta coordenada y según tus necesidades
        float widthj = 100; // Ajusta el ancho de la imagen
        float heightj = 100; // Ajusta la altura de la imagen
        
        contentStream.drawImage(pdImage, x, y, widthj, heightj);
    }
     // Agregar la imagen del código QR al contenido del PDF
    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
      float x = 201; // Ajusta esta coordenada x según tus necesidades
      float y = 109; // Ajusta esta coordenada y según tus necesidades
      float widthj = 50; // Ajusta el ancho de la imagen
      float heightj = 50; // Ajusta la altura de la imagen
        
        contentStream.drawImage(pdImageRector, x, y, widthj, heightj);
    }
      // Agregar la imagen del código QR al contenido del PDF
    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
      float x = 20; // Ajusta esta coordenada x según tus necesidades
      float y = 77; // Ajusta esta coordenada y según tus necesidades
      float widthj = 50; // Ajusta el ancho de la imagen
      float heightj = 50; // Ajusta la altura de la imagen
        
        contentStream.drawImage(pdImageVicerrector, x, y, widthj, heightj);
    }
       // Agregar la imagen del código QR al contenido del PDF
    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
      float x = 390; // Ajusta esta coordenada x según tus necesidades
      float y = 77;
      float widthj = 50; // Ajusta el ancho de la imagen
      float heightj = 50; // Ajusta la altura de la imagen
        
        contentStream.drawImage(pdImageSecretario, x, y, widthj, heightj);
    }
    
    
    // Guardar el PDF con la imagen del código QR agregada
    pdfDocument.save(rutaCompleta); // Reemplaza con la ruta y el nombre adecuados
    pdfDocument.close();
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
    String plantilla = rootAbsolutPathPlantilla + "/tituloProvision.pdf";
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
      String rutaCompletaP = rootAbsolutPathPlantillasPath + "/tituloProvision.jpg";

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

  }

}