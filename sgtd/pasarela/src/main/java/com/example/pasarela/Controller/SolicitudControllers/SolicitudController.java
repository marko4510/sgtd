package com.example.pasarela.Controller.SolicitudControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Recibo;
import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IDocumentoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IReciboService;
import com.example.pasarela.Models.Service.ISolicitudLegalizacionService;
import com.example.pasarela.Models.Service.ISolicitudSupletorioService;
import com.example.pasarela.Models.Service.ISolicitudTituloService;
import com.example.pasarela.Models.Service.IUsuarioService;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
public class SolicitudController {
    @Autowired
    private IDocumentoService documentoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IDepartamentoService departamentoService;

    @Autowired
    private ISolicitudLegalizacionService solicitudLegalizacionService;

    @Autowired
    private ISolicitudSupletorioService solicitudSupletorioService;

    @Autowired
    private ISolicitudTituloService solicitudTituloService;

    @Autowired
    private IReciboService reciboService;

  

    @GetMapping(value = "/Historial/{id_usuario}")
    public String rec_formLegalizacion(@PathVariable("id_usuario") Long id_usuario, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Usuario usuario = usuarioService.findOne(id_usuario);

             model.addAttribute("recibos", reciboService.lista_recibo_usuario(id_usuario));

            model.addAttribute("solicitudesLegalizacion",
                    solicitudLegalizacionService.lista_solicitudes_usuario(id_usuario));
            model.addAttribute("solicitudesSupletorio",
                    solicitudSupletorioService.lista_solicitudes_supletorio_usuario(id_usuario));
            model.addAttribute("solicitudesTitulo",
                    solicitudTituloService.lista_solicitudes_titulo_usuario(id_usuario));
            model.addAttribute("usuario", usuario);

            return "publico/historial";
        } else {
            return "redirect:/Inicio";
        }

    }

    @GetMapping(value = "/Pagar/{id_solicitud}")
    public String pagar_legalizacion(@PathVariable("id_solicitud") Long id_solicitud, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("solicitud", solicitudLegalizacionService.findOne(id_solicitud));

            return "publico/FormularioPago";
        } else {
            return "redirect:/pb";
        }

    }

    @RequestMapping(value = "/openFileRecibo", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<ByteArrayResource> abrirArchivoMedianteRuta(HttpServletResponse response,
            @RequestParam("ruta") String ruta) throws IOException {
        File file = new File(ruta);

        if (file.exists() && file.isFile()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
