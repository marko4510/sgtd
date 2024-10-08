package com.example.pasarela.Controller.firmaControllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.pasarela.Models.Entity.Autoridad;
import com.example.pasarela.Models.Entity.Firma;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IAutoridadService;
import com.example.pasarela.Models.Service.IFirmaService;
import com.example.pasarela.Models.Service.ITituloGeneradoService;
import com.example.pasarela.Models.Service.ITituloService;
import com.example.pasarela.Models.Utils.Archive;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.io.File;
import java.security.Security;

import java.util.List;

@Controller
public class firmaController {
    @Autowired
    private ITituloService tituloService;

    @Autowired
    private ITituloGeneradoService tituloGeneradoService;
    @Autowired
    private IAutoridadService autoridadService;
    @Autowired
    private IFirmaService firmaService;
    Archive archive = new Archive();

    @RequestMapping(value = "/FirmaR", method = RequestMethod.GET) // Pagina principal
    public String Carrera(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
			// Obtener la entidad "Persona" asociada al usuario
			Persona persona = usuario.getPersona();
			Long id_p = persona.getId_persona();
			Autoridad autoridad = autoridadService.autoridadPorIdPersona(id_p);
            model.addAttribute("titulos", tituloService.findAll());
       
            model.addAttribute("autoridad", autoridad);
            model.addAttribute("titulosR", tituloService.titulosSinFirmarRector());
            model.addAttribute("titulosV", tituloService.titulosSinFirmarVicerrector());
            model.addAttribute("titulosS", tituloService.titulosSinFirmarSecretario());
            return "firmar/firmaTitulos";
        } else {
            return "redirect:LoginR";
        }
    }

    @PostMapping("/firmarDocumentoRector")
    public String firmarDocumentoRector(@RequestParam("clavePrivada") String clavePrivada,
    @RequestParam(value = "tituloSeleccionado", required = false) List<Long> titulosSeleccionados,
    HttpServletRequest request, RedirectAttributes redirectAttrs,Model model)
            throws GeneralSecurityException, IOException, DocumentException {
        // Obtener la entidad "Usuario" a partir del usuario en sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // Obtener la entidad "Persona" asociada al usuario
        Persona persona = usuario.getPersona();
        Long id_p = persona.getId_persona();
		Autoridad autoridad = autoridadService.autoridadPorIdPersona(id_p);
        // Validar la clave privada ingresada
        if (!persona.getClaveP().equals(clavePrivada)) {
            redirectAttrs
            .addFlashAttribute("mensaje3", "Clave Privada Incorrecta!")
            .addFlashAttribute("clase3", "warning alert-dismissible fade show");
            return "redirect:/FirmaR";
        }

        Path rootPathFirmas = Paths.get("archivos/firmas/");
        Path rootAbsolutPathFirmas = rootPathFirmas.toAbsolutePath();
        String rutaDirectorioFirmas = rootAbsolutPathFirmas+"";
         try {
                if (!Files.exists(rootPathFirmas)) {
                    Files.createDirectories(rootPathFirmas);
                    System.out.println("Directorio creado: " + rutaDirectorioFirmas);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorioFirmas);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
        

        Path rootPathFirmados = Paths.get("archivos/firmados/");
        Path rootAbsolutPathFirmados = rootPathFirmados.toAbsolutePath();
        String rutaDirectorioFirmados = rootAbsolutPathFirmados+"";
         try {
                if (!Files.exists(rootPathFirmados)) {
                    Files.createDirectories(rootPathFirmados);
                    System.out.println("Directorio creado: " + rutaDirectorioFirmados);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorioFirmados);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
       
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

       
        if (titulosSeleccionados != null && !titulosSeleccionados.isEmpty()) {
        for (Long tituloId : titulosSeleccionados) {
            // Obtener el objeto Titulo correspondiente al ID
            Titulo titulo = tituloService.findOne(tituloId);
              try {
                archive.sign(rootAbsolutPathFirmas.toString() + "/" + persona.getDigital(),
                            persona.getClaveP().toCharArray(), PdfSignatureAppearance.NOT_CERTIFIED,
                            titulo.getTituloGenerado().getRuta_archivo(),
                            rootAbsolutPathFirmados.toString() + "/rector" + titulo.getTituloGenerado().getNombre_archivo());
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           






                        TituloGenerado tituloGenerado = new TituloGenerado();
                        Firma firma = new Firma();
                // Registrar titulo Generado

                tituloGenerado.setNombre_archivo( titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setRuta_archivo(rootAbsolutPathFirmados.toString()+"/rector" +titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setEstado("A");
                TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);
                firma.setTitulo(titulo);
                firma.setAutoridad(autoridad);
                firmaService.save(firma);
                titulo.setEstado("A");
                titulo.setFirma_rector("A");
                titulo.setTituloGenerado(tituloGenerado2);
                titulo.setDocumento_firmado(rootAbsolutPathFirmados.toString() + "/I" + titulo.getTituloGenerado().getNombre_archivo());
                tituloService.save(titulo);
            }
            redirectAttrs
            .addFlashAttribute("mensaje", "Documentos Firmados con Exito!")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
         
        }else{
             redirectAttrs
        .addFlashAttribute("mensaje2", "No hay Documentos para Firmar!")
        .addFlashAttribute("clase2", "danger alert-dismissible fade show"); 
        }
        

      
          
        
        
       

        return "redirect:/FirmaR";

    }


    @PostMapping("/firmarDocumentoVicerrector")
    public String firmarDocumentoVicerrector(@RequestParam("clavePrivada") String clavePrivada, 
    @RequestParam(value = "tituloSeleccionado", required = false) List<Long> titulosSeleccionados,
    HttpServletRequest request, RedirectAttributes redirectAttrs,Model model)
            throws GeneralSecurityException, IOException, DocumentException {
        // Obtener la entidad "Usuario" a partir del usuario en sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // Obtener la entidad "Persona" asociada al usuario
        Persona persona = usuario.getPersona();
        Long id_p = persona.getId_persona();
		Autoridad autoridad = autoridadService.autoridadPorIdPersona(id_p);
        // Validar la clave privada ingresada
        // Validar la clave privada ingresada
        if (!persona.getClaveP().equals(clavePrivada)) {
            redirectAttrs
            .addFlashAttribute("mensaje3", "Clave Privada Incorrecta!")
            .addFlashAttribute("clase3", "warning alert-dismissible fade show");
            return "redirect:/FirmaR";
        }

       Path rootPathFirmas = Paths.get("archivos/firmas/");
        Path rootAbsolutPathFirmas = rootPathFirmas.toAbsolutePath();
        String rutaDirectorioFirmas = rootAbsolutPathFirmas+"";
         try {
                if (!Files.exists(rootPathFirmas)) {
                    Files.createDirectories(rootPathFirmas);
                    System.out.println("Directorio creado: " + rutaDirectorioFirmas);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorioFirmas);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
        

        Path rootPathFirmados = Paths.get("archivos/firmados/");
        Path rootAbsolutPathFirmados = rootPathFirmados.toAbsolutePath();
        String rutaDirectorioFirmados = rootAbsolutPathFirmados+"";
         try {
                if (!Files.exists(rootPathFirmados)) {
                    Files.createDirectories(rootPathFirmados);
                    System.out.println("Directorio creado: " + rutaDirectorioFirmados);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorioFirmados);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
       
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

         if (titulosSeleccionados != null && !titulosSeleccionados.isEmpty()) {
        for (Long tituloId : titulosSeleccionados) {
            // Obtener el objeto Titulo correspondiente al ID
            Titulo titulo = tituloService.findOne(tituloId);
              try {
                archive.sign(rootAbsolutPathFirmas.toString() + "/" + persona.getDigital(),
                            persona.getClaveP().toCharArray(), PdfSignatureAppearance.NOT_CERTIFIED,
                            titulo.getTituloGenerado().getRuta_archivo(),
                            rootAbsolutPathFirmados.toString() + "/vicerrector" + titulo.getTituloGenerado().getNombre_archivo());
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                        TituloGenerado tituloGenerado = new TituloGenerado();
                        Firma firma = new Firma();
                // Registrar titulo Generado

             
        





                tituloGenerado.setNombre_archivo( titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setRuta_archivo(rootAbsolutPathFirmados.toString()+"/vicerrector" +titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setEstado("A");
                TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);
                firma.setTitulo(titulo);
                firma.setAutoridad(autoridad);
                firmaService.save(firma);
                titulo.setEstado("A");
                titulo.setFirma_vicerrector("A");
                titulo.setTituloGenerado(tituloGenerado2);
                titulo.setDocumento_firmado(rootAbsolutPathFirmados.toString() + "/I" + titulo.getTituloGenerado().getNombre_archivo());
                tituloService.save(titulo);
            }
            redirectAttrs
            .addFlashAttribute("mensaje", "Documentos Firmados con Exito!")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
         
        }else{
             redirectAttrs
        .addFlashAttribute("mensaje2", "No hay Documentos para Firmar!")
        .addFlashAttribute("clase2", "danger alert-dismissible fade show"); 
        }
        
       

        return "redirect:/FirmaR";

    }

    @PostMapping("/firmarDocumentoSecretario")
    public String firmarDocumentoSecretario(@RequestParam("clavePrivada") String clavePrivada, 
     @RequestParam(value = "tituloSeleccionado", required = false) List<Long> titulosSeleccionados,
    HttpServletRequest request, RedirectAttributes redirectAttrs,Model model)
            throws GeneralSecurityException, IOException, DocumentException {
        // Obtener la entidad "Usuario" a partir del usuario en sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // Obtener la entidad "Persona" asociada al usuario
        Persona persona = usuario.getPersona();
        Long id_p = persona.getId_persona();
		Autoridad autoridad = autoridadService.autoridadPorIdPersona(id_p);
        // Validar la clave privada ingresada
        if (!persona.getClaveP().equals(clavePrivada)) {
            redirectAttrs
            .addFlashAttribute("mensaje3", "Clave Privada Incorrecta!")
            .addFlashAttribute("clase3", "warning alert-dismissible fade show");
            return "redirect:/FirmaR";
        }

        Path rootPathFirmas = Paths.get("archivos/firmas/");
        Path rootAbsolutPathFirmas = rootPathFirmas.toAbsolutePath();
        String rutaDirectorioFirmas = rootAbsolutPathFirmas+"";
         try {
                if (!Files.exists(rootPathFirmas)) {
                    Files.createDirectories(rootPathFirmas);
                    System.out.println("Directorio creado: " + rutaDirectorioFirmas);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorioFirmas);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
        

        Path rootPathFirmados = Paths.get("archivos/firmados/");
        Path rootAbsolutPathFirmados = rootPathFirmados.toAbsolutePath();
        String rutaDirectorioFirmados = rootAbsolutPathFirmados+"";
         try {
                if (!Files.exists(rootPathFirmados)) {
                    Files.createDirectories(rootPathFirmados);
                    System.out.println("Directorio creado: " + rutaDirectorioFirmados);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorioFirmados);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
       
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

         if (titulosSeleccionados != null && !titulosSeleccionados.isEmpty()) {
        for (Long tituloId : titulosSeleccionados) {
            // Obtener el objeto Titulo correspondiente al ID
            Titulo titulo = tituloService.findOne(tituloId);
              try {
                archive.sign(rootAbsolutPathFirmas.toString() + "/" + persona.getDigital(),
                            persona.getClaveP().toCharArray(), PdfSignatureAppearance.NOT_CERTIFIED,
                            titulo.getTituloGenerado().getRuta_archivo(),
                            rootAbsolutPathFirmados.toString() + "/secretario" + titulo.getTituloGenerado().getNombre_archivo());
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                        TituloGenerado tituloGenerado = new TituloGenerado();
                        Firma firma = new Firma();
                // Registrar titulo Generado

           






                tituloGenerado.setNombre_archivo( titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setRuta_archivo(rootAbsolutPathFirmados.toString()+"/secretario" +titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setEstado("A");
                TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);
                firma.setTitulo(titulo);
                firma.setAutoridad(autoridad);
                firmaService.save(firma);
                titulo.setEstado("A");
                titulo.setFirma_secretario("A");
                titulo.setTituloGenerado(tituloGenerado2);
                titulo.setDocumento_firmado(rootAbsolutPathFirmados.toString() + "/I" + titulo.getTituloGenerado().getNombre_archivo());
                tituloService.save(titulo);
            }
            redirectAttrs
            .addFlashAttribute("mensaje", "Documentos Firmados con Exito!")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
         
        }else{
             redirectAttrs
        .addFlashAttribute("mensaje2", "No hay Documentos para Firmar!")
        .addFlashAttribute("clase2", "danger alert-dismissible fade show"); 
        }
        
       

        return "redirect:/FirmaR";

    }


    public int contarFirmas(String rutaDocumento)
        throws IOException, GeneralSecurityException {
    PdfReader reader = new PdfReader(rutaDocumento);
    int numFirmas = reader.getAcroFields().getSignatureNames().size();
    reader.close();
    return numFirmas;
}


}
