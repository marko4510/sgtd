package com.example.pasarela.Controller.SolicitudControllers;

import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IArchivoAdjuntoService;
import com.example.pasarela.Models.Service.ICostoDocumentoService;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ISolicitudLegalizacionService;
import com.example.pasarela.Models.Service.IUsuarioService;
import com.example.pasarela.Models.Utils.AdjuntarArchivo;

@Controller
public class SolicitudLegalizacionController {

    @Autowired
    private IDepartamentoService departamentoService;

    @Autowired
    private ISolicitudLegalizacionService solicitudService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IArchivoAdjuntoService archivoAdjuntoService;

    @Autowired
    private ICostoDocumentoService costoDocService;

    @Autowired
    private ISolicitudLegalizacionService solicitudLegService;

    // VISTA ADMIN, LISTAR SOLICITUDES DE LEGALIZACIONES
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

    // VISTA ADMIN, APROBAR SOLICITUDES DE LEGALIZACIONES
    @RequestMapping(value = "/aprobar-solicitud-legalizacion/{id_solicitud_legalizacion}")
    public String eliminar_p(@PathVariable("id_solicitud_legalizacion") Long id_solicitud_legalizacion) {

        SolicitudLegalizacion solicitud = solicitudService.findOne(id_solicitud_legalizacion);

        solicitud.setEstado("Aprobado");

        solicitudService.save(solicitud);
        return "redirect:/SolicitudesLegalizaciones";

    }

    // VISTA ADMIN, RECHAZAR SOLICITUDES DE LEGALIZACIONES
    @RequestMapping(value = "/rechazar-solicitud-legalizacion/{id_solicitud_legalizacion}")
    public String rechazar_p(@PathVariable("id_solicitud_legalizacion") Long id_solicitud_legalizacion) {

        SolicitudLegalizacion solicitud = solicitudService.findOne(id_solicitud_legalizacion);

        solicitud.setEstado("Rechazado");

        solicitudService.save(solicitud);
        return "redirect:/SolicitudesLegalizaciones";

    }

    // Vista Publica de Menu Legalizacion
    @RequestMapping(value = "TLegalizacion", method = RequestMethod.GET) // Pagina principal
    public String Tlegalizacion(Model model) {
        return "publico/legalizacion/TLegalizacion";

    }

    // Boton Iniciar Solicitud Legalizacion para vista de Formulario
    @GetMapping(value = "/rec-formLegalizacion/{id_persona}")
    public String rec_formLegalizacion(@PathVariable("id_persona") Long id_persona, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Persona persona = personaService.findOne(id_persona);
            Long nacionalidad = persona.getProvincia().getDepartamento().getNacionalidad().getId_nacionalidad();
            model.addAttribute("costodocumentos", costoDocService.lista_costo_documento_legalizaciones(nacionalidad));
            model.addAttribute("persona", persona);
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("archivosAdjuntos", archivoAdjuntoService.listarArchivoAdjunto().size());
            model.addAttribute("solicitudLegalizacion", new SolicitudLegalizacion());
            return "publico/legalizacion/FormularioLegalizacion";
        } else {
            return "redirect:/Inicio";
        }

    }
    /*   
    */

    // Boton para guardar del Formulario de Solicitud de Legalizacion
    @RequestMapping(value = "/LegalizacionPersonaF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String LegalizacionPersonaF(@Validated Persona persona, SolicitudLegalizacion solicitudLegalizacion,
            Model model, HttpServletRequest request, RedirectAttributes attr)
            throws FileNotFoundException, IOException, ParseException { // validar los datos capturados (1)

        // Adjuntar Archivo en SolicitudLegalizacion
        MultipartFile multipartFile = solicitudLegalizacion.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGD/legalizacion");
        model.addAttribute("di", rutaArchivo);
        List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
        solicitudLegalizacion.setNombreArchivo((listArchivos.size() + 1) + "-" + multipartFile.getOriginalFilename());
        Integer ad = adjuntarArchivo.adjuntarArchivoLegalizaciones(solicitudLegalizacion, rutaArchivo);

        archivoAdjunto.setNombre_archivo((listArchivos.size() + 1) + "-" + multipartFile.getOriginalFilename());
        System.out.println(solicitudLegalizacion.getNombreArchivo() + multipartFile.getOriginalFilename()
                + " +++++++++++++++++++++");
        archivoAdjunto.setTipo_archivo(multipartFile.getContentType());
        archivoAdjunto.setRuta_archivo("sGD/legalizacion/");
        archivoAdjunto.setEstado("A");
        ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
        // FIN Adjuntar Archivo en SolicitudLegalizacion

        // Capturar Usuario de la Sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        // Capturar Fecha de Registro de SolicitudLegalizacion
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeStamp);

        solicitudLegalizacion.setArchivoAdjunto(archivoAdjunto2);
        solicitudLegalizacion.setFecha_solicitud(date1);
        solicitudLegalizacion.setEstado("Pendiente");
        solicitudLegalizacion.setTipo_solicitud("Legalización");
        solicitudLegalizacion.setUsuario(usuario);
        solicitudLegService.save(solicitudLegalizacion);

        persona.setEstado("A");
        personaService.save(persona);

        Long id_usuario = usuario.getId_usuario();

        return "redirect:/Historial/" + id_usuario;
    }

    @RequestMapping(value = "/openFile/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_solicitud_legalizacion) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService
                .buscarArchivoAdjuntoPorSolicitud(id_solicitud_legalizacion);
        File file = new File("C:/" + ArchivoAdjuntos.getRuta_archivo() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }
}
