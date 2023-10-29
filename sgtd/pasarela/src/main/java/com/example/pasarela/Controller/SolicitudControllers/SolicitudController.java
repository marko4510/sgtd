package com.example.pasarela.Controller.SolicitudControllers;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IDocumentoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ISolicitudLegalizacionService;
import com.example.pasarela.Models.Service.ISolicitudSupletorioService;
import com.example.pasarela.Models.Service.ISolicitudTituloService;
import com.example.pasarela.Models.Service.IUsuarioService;

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

    @GetMapping(value = "/Historial/{id_usuario}")
    public String rec_formLegalizacion(@PathVariable("id_usuario") Long id_usuario, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Usuario u  model.addAttribute("solicitudesLegalizacion", solicitudLegalizacionService.lista_solicitudes_usuario(id_usuario));suario = usuarioService.findOne(id_usuario);

            model.addAttribute("solicitudesLegalizacion", solicitudLegalizacionService.lista_solicitudes_usuario(id_usuario));
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
}
