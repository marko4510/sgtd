package com.example.pasarela.Controller.TramiteControllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pasarela.Models.Entity.TipoDocumento;
import com.example.pasarela.Models.Service.ITipoDocumentoService;

@Controller
public class TipoDocumentoController {
    @Autowired
    private ITipoDocumentoService tipoDocumentoService;

    // Formulario para Registrar TipoDocumento
    @RequestMapping(value = "/TipoDocumentoR", method = RequestMethod.GET) // Pagina principal
    public String Persona(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("tipoDocumento", new TipoDocumento());
            model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());

            return "tramite/gestionarTipoDocumento";
        } else {
            return "redirect:LoginR";
        }
    }

    // Boton para Guardar TipoDocumento
    @RequestMapping(value = "/TipoDocumentoF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String PersonaF(@Validated TipoDocumento tipoDocumento, RedirectAttributes redirectAttrs) {

        tipoDocumento.setEstado("A");
        tipoDocumentoService.save(tipoDocumento);
        redirectAttrs
                .addFlashAttribute("mensaje", "Registro Exitoso del Tipo Documento")
                .addFlashAttribute("clase", "success alert-dismissible fade show");

        return "redirect:/TipoDocumentoR"; // cambiar a TipoDocumentoL
    }

    // Listar los tipos de documentos
    @RequestMapping(value = "/TipoDocumentoL", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model) {
        model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());
        return "Tramite/ListarTipoDocumento";
    }

    // Boton para editar tipo de documento
    @RequestMapping(value = "/editar-tdocumento/{id_tipo_documento}")
    public String editar_p(@PathVariable("id_tipo_documento") Long id_tipo_documento, Model model) {

        TipoDocumento tipoDocumento = tipoDocumentoService.findOne(id_tipo_documento);

        model.addAttribute("tipoDocumento", tipoDocumento);
        model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());
        model.addAttribute("edit", "true");

        return "tramite/gestionarTipoDocumento";

    }

    // Boton para Guardar Modificacion de TipoDocumento

    @RequestMapping(value = "/TipoDocumentoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TipoDocumentoModF(@Validated TipoDocumento tipoDocumento, RedirectAttributes redirectAttrs) {

        tipoDocumento.setEstado("A");
        tipoDocumentoService.save(tipoDocumento);
        redirectAttrs
                .addFlashAttribute("mensaje2", "Datos del Tipo Documento Actualizados Correctamente")
                .addFlashAttribute("clase2", "success alert-dismissible fade show");

        return "redirect:/TipoDocumentoR";
    }

    // boton para eliminar tipo de documento
    @RequestMapping(value = "/eliminar-tdocumento/{id_tipo_documento}")
    public String eliminar_p(@PathVariable("id_tipo_documento") Long id_tipo_documento) {

        TipoDocumento tipoDocumento = tipoDocumentoService.findOne(id_tipo_documento);

        tipoDocumento.setEstado("X");

        tipoDocumentoService.save(tipoDocumento);
        return "redirect:/TipoDocumentoR";

    }
}
