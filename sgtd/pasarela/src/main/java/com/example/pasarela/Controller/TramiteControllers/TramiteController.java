package com.example.pasarela.Controller.TramiteControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;
import com.example.pasarela.Models.Service.IArchivoAdjuntoService;
import com.example.pasarela.Models.Service.IDocumentoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ITramiteService;
import com.example.pasarela.Models.Utils.AdjuntarArchivo;
import com.example.pasarela.Models.Entity.Tramite;

@Controller
public class TramiteController {
    @Autowired
    private ITramiteService tramiteService;

    @Autowired
    private IArchivoAdjuntoService archivoAdjuntoService;

    @Autowired
    private IDocumentoService documentoService;

    @Autowired
    private IPersonaService personaService;

    // Formulario para Registrar Tramite
    @RequestMapping(value = "/TramiteR", method = RequestMethod.GET) // Pagina principal
    public String Tramite(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
            model.addAttribute("documentos", documentoService.findAll());
            model.addAttribute("tramite", new Tramite());
            model.addAttribute("tramites", tramiteService.findAll());
            model.addAttribute("personas", personaService.findAll());

            return "tramite/registrarTramites";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/TramiteF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TramiteF(@Validated Tramite tramite, Model model) throws FileNotFoundException, IOException { // validar
                                                                                                                // los
                                                                                                                // datos
                                                                                                                // capturados
                                                                                                                // (1)

        MultipartFile multipartFile = tramite.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();

        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();

        String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGD/tramite");
        model.addAttribute("di", rutaArchivo);
        List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
        tramite.setNombreArchivo((listArchivos.size() + 1) + "-" + multipartFile.getOriginalFilename());
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        archivoAdjunto.setNombre_archivo((listArchivos.size() + 1) + "-" + multipartFile.getOriginalFilename());
        archivoAdjunto.setTipo_archivo(multipartFile.getContentType());
        archivoAdjunto.setRuta_archivo("sGD/tramite/");
        archivoAdjunto.setEstado("A");
        ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
        List<Tramite> listaT = tramiteService.findAll();
        tramite.setNro_tramite("" + (listaT.size() + 1));
        tramite.setArchivoAdjunto(archivoAdjunto2);
        tramite.setEstado("A");

        tramiteService.save(tramite);

        return "redirect:/TramiteL";
    }

    // Listar Tramites
    @RequestMapping(value = "/TramiteL", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            model.addAttribute("tramites", tramiteService.findAll());
            return "tramite/listarTramites";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/editar-tramite/{id_tramite}")
    public String editar_p(@PathVariable("id_tramite") Long id_tramite, Model model) {

        Tramite tramite = tramiteService.findOne(id_tramite);

        model.addAttribute("tramite", tramite);
        model.addAttribute("documentos", documentoService.findAll());
        model.addAttribute("tramites", tramiteService.findAll());
        model.addAttribute("personas", personaService.findAll());
        model.addAttribute("edit", "true");
        return "tramite/registrarTramites";

    }

    @PostMapping(value = "/TramiteModF")
    public String modificarContratacionesSicoes(@Validated Tramite tramite, Model model, HttpServletRequest request)
            throws IOException {

        MultipartFile multipartFile = tramite.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGD/tramite");
        List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();

        tramite.setNombreArchivo((listArchivos.size() + 1) + "-" + multipartFile.getOriginalFilename());
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorTramite(tramite.getArchivoAdjunto().getId_archivo_adjunto());
            barchivoAdjunto.setNombre_archivo(tramite.getNombreArchivo());
            barchivoAdjunto.setRuta_archivo("SGD/tramite/");
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }
        tramite.setNro_tramite(tramite.getNro_tramite());
        tramite.setEstado("A");
        tramiteService.save(tramite);

        return "redirect:/TramiteL";

    }

    @RequestMapping(value = "/openFileTramite/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_tramite) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService.buscarArchivoAdjuntoPorTramite(id_tramite);
        System.out.println(ArchivoAdjuntos + "+++++++++++++++++++++++++++++++++");
        File file = new File("C:/" + ArchivoAdjuntos.getRuta_archivo() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }
}
