package com.example.pasarela.Controller.SolicitudControllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.SolicitudSupletorio;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.ICostoDocumentoService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ISolicitudSupletorioService;

@Controller
public class SolicitudSupletorioController {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private ICostoDocumentoService costoDocService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IGradoAcademicoService gradoAcademicoService;

    @Autowired
    private ISolicitudSupletorioService solicitudSupletorioService;

    // Vista Publica de Menu Supletorio
    @RequestMapping(value = "Supletorio", method = RequestMethod.GET) // Pagina principal
    public String Tlegalizacion(Model model) {
        return "publico/supletorio/supletorio";

    }

    // VISTA ADMIN, LISTAR SOLICITUDES DE SUPLETORIOS
    @RequestMapping(value = "/SolicitudesSupletorios", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            // model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());

            model.addAttribute("solicitudes", solicitudSupletorioService.findAll());

            return "solicitud/gestionarSolicitudSupletorio";
        } else {
            return "redirect:LoginR";
        }
    }

    @GetMapping(value = "/rec-formSupletorio/{id_persona}")
    public String rec_formLegalizacion(@PathVariable("id_persona") Long id_persona, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Persona persona = personaService.findOne(id_persona);
            Long nacionalidad = persona.getProvincia().getDepartamento().getNacionalidad().getId_nacionalidad();

            model.addAttribute("costodocumentos", costoDocService.lista_costo_documento_supletorio(nacionalidad));
            model.addAttribute("persona", persona);
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("carreras", carreraService.findAll());
            model.addAttribute("gradoAcademicos", gradoAcademicoService.findAll());
            model.addAttribute("solicitudSupletorio", new SolicitudSupletorio());
            return "publico/supletorio/formulariosupletorio";
        } else {
            return "redirect:/Inicio";
        }

    }

    // Boton para guardar del Formulario de Solicitud de Legalizacion
    @RequestMapping(value = "/SupletorioPersonaF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String SupletorioPersonaF(@Validated Persona persona, SolicitudSupletorio solicitudSupletorio,
            Model model, HttpServletRequest request, RedirectAttributes attr)
            throws FileNotFoundException, IOException, ParseException { // validar los datos capturados (1)

        // Capturar Usuario de la Sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        // Capturar Fecha de Registro de SolicitudLegalizacion
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeStamp);

        solicitudSupletorio.setFecha_solicitud(date1);
        solicitudSupletorio.setEstado("Aprobado");
        solicitudSupletorio.setTipo_solicitud("Supletorio");
        solicitudSupletorio.setUsuario(usuario);
        solicitudSupletorioService.save(solicitudSupletorio);

        persona.setEstado("A");
        personaService.save(persona);

        Long id_usuario = usuario.getId_usuario();

        return "redirect:/Historial/" + id_usuario;
    }

}
