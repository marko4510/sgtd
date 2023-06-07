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
import com.example.pasarela.Models.Entity.SolicitudTitulo;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.ICostoDocumentoService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ISolicitudTituloService;

@Controller
public class SolicitudTituloDiplomaController {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private ICostoDocumentoService costoDocService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IGradoAcademicoService gradoAcademicoService;


    @Autowired
    private ISolicitudTituloService solicitudTituloService;

    // Vista Publica de Menu Supletorio
    @RequestMapping(value = "TituloDiploma", method = RequestMethod.GET) // Pagina principal
    public String Tlegalizacion(Model model) {
        return "publico/titulod/tituloDiploma";

    }

    // VISTA ADMIN, LISTAR SOLICITUDES DE LEGALIZACIONES
    @RequestMapping(value = "/SolicitudesTitulosDiplomas", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            // model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());

            model.addAttribute("solicitudes", solicitudTituloService.findAll());

            return "solicitud/gestionarSolicitudTituloDiploma";
        } else {
            return "redirect:LoginR";
        }
    }

    @GetMapping(value = "/form-TituloDiploma/{id_persona}")
    public String rec_formLegalizacion(@PathVariable("id_persona") Long id_persona, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Persona persona = personaService.findOne(id_persona);
            Long nacionalidad = persona.getProvincia().getDepartamento().getNacionalidad().getId_nacionalidad();
            model.addAttribute("costodocumentos", costoDocService.lista_costo_documento_titulo(nacionalidad));
            model.addAttribute("persona", persona);
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("carreras", carreraService.findAll());
            model.addAttribute("gradoAcademicos", gradoAcademicoService.findAll());
            model.addAttribute("solicitudTitulo", new SolicitudTitulo());
            return "publico/titulod/formTituloDiploma";
        } else {
            return "redirect:/Inicio";
        }

    }

    @GetMapping(value = "/form-TituloDiplomaProvision/{id_persona}")
    public String rec_formLegalizacionProvision(@PathVariable("id_persona") Long id_persona, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Persona persona = personaService.findOne(id_persona);
            Long nacionalidad = persona.getProvincia().getDepartamento().getNacionalidad().getId_nacionalidad();
            model.addAttribute("costodocumentos", costoDocService.lista_costo_documento_titulo_provision(nacionalidad));
            model.addAttribute("persona", persona);
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("carreras", carreraService.findAll());
            model.addAttribute("gradoAcademicos", gradoAcademicoService.findAll());
            model.addAttribute("solicitudTitulo", new SolicitudTitulo());
            
            return "publico/titulod/formTituloDiplomaProvision";
        } else {
            return "redirect:/Inicio";
        }

    }

    // Boton para guardar del Formulario de Solicitud de Legalizacion
    @RequestMapping(value = "/TituloDiplomaF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TituloDiplomaPersonaF(@Validated Persona persona, SolicitudTitulo solicitudTitulo,
            Model model, HttpServletRequest request, RedirectAttributes attr)
            throws FileNotFoundException, IOException, ParseException { // validar los datos capturados (1)

        // Capturar Usuario de la Sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        // Capturar Fecha de Registro de SolicitudLegalizacion
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeStamp);

        solicitudTitulo.setFecha_solicitud(date1);
        solicitudTitulo.setEstado("Aprobado");
      
        solicitudTitulo.setTipo_solicitud("Titulo o Diploma");
        solicitudTitulo.setUsuario(usuario);
        solicitudTituloService.save(solicitudTitulo);

        persona.setEstado("A");
        personaService.save(persona);

        Long id_usuario = usuario.getId_usuario();

        return "redirect:/Historial/" + id_usuario;
    }

    
    // Boton para guardar del Formulario de Solicitud de Legalizacion
    @RequestMapping(value = "/TituloDiplomaProvisionF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TituloDiplomaProvisionaF(@Validated Persona persona, SolicitudTitulo solicitudTitulo,
            Model model, HttpServletRequest request, RedirectAttributes attr)
            throws FileNotFoundException, IOException, ParseException { // validar los datos capturados (1)

        // Capturar Usuario de la Sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        // Capturar Fecha de Registro de SolicitudLegalizacion
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeStamp);

        solicitudTitulo.setFecha_solicitud(date1);
        solicitudTitulo.setEstado("Aprobado");
      
        solicitudTitulo.setTipo_solicitud("Titulo En Provisión Nacional");
        solicitudTitulo.setUsuario(usuario);
        solicitudTituloService.save(solicitudTitulo);

        persona.setEstado("A");
        personaService.save(persona);

        Long id_usuario = usuario.getId_usuario();

        return "redirect:/Historial/" + id_usuario;
    }


    @GetMapping(value = "/formularioPagarT")
    public String pagarT( Model model,HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Long id_usuario = usuario.getId_usuario();
            SolicitudTitulo solicitudTitulo = solicitudTituloService.SolicitudPorUsuario(id_usuario);
            model.addAttribute("solicitudTitulo", solicitudTitulo);
            return "publico/titulod/formularioPagarT";
        } else {
            return "redirect:/Inicio";
        }

    }

    @RequestMapping(value = "/pagoSolicitudTitulo", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String pagoSolicitudTitulo(@Validated  SolicitudTitulo solicitudTitulo, HttpServletRequest request) { // validar los datos capturados (1)
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Long id_usuario = usuario.getId_usuario();
        solicitudTitulo.setEstado("Pagado");

        solicitudTituloService.save(solicitudTitulo);

        return "redirect:/Historial/" + id_usuario;
    }

}
