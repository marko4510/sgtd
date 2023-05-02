package com.example.pasarela.Controller.SolicitudControllers;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.ISolicitudLegalizacionService;
import com.example.pasarela.Models.Service.IUsuarioService;

@Controller
public class SolicitudLegalizacionController {

    @Autowired
    private IDepartamentoService departamentoService;

    @Autowired
    private ISolicitudLegalizacionService solicitudService;

    @Autowired
    private IUsuarioService usuarioService;

    // Listar los tipos de documentos
    @RequestMapping(value = "/SolicitudesLegalizaciones", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            // model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());

            model.addAttribute("solicitudes", solicitudService.findAll());

            return "solicitud/gestionarSolicitudLegalizacion";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/aprobar-solicitud-legalizacion/{id_solicitud_legalizacion}")
    public String eliminar_p(@PathVariable("id_solicitud_legalizacion") Long id_solicitud_legalizacion) {

        SolicitudLegalizacion solicitud = solicitudService.findOne(id_solicitud_legalizacion);

        solicitud.setEstado("Aprobado");

        solicitudService.save(solicitud);
        return "redirect:/SolicitudesLegalizaciones";

    }

    @RequestMapping(value = "/rechazar-solicitud-legalizacion/{id_solicitud_legalizacion}")
    public String rechazar_p(@PathVariable("id_solicitud_legalizacion") Long id_solicitud_legalizacion) {

        SolicitudLegalizacion solicitud = solicitudService.findOne(id_solicitud_legalizacion);

        solicitud.setEstado("Rechazado");

        solicitudService.save(solicitud);
        return "redirect:/SolicitudesLegalizaciones";

    }
}
