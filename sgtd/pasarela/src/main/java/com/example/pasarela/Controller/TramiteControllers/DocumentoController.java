package com.example.pasarela.Controller.TramiteControllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pasarela.Models.Service.IDocumentoService;
import com.example.pasarela.Models.Service.ITipoDocumentoService;
import com.example.pasarela.Models.Entity.Documento;

@Controller
public class DocumentoController {

    @Autowired
    private IDocumentoService documentoService;

    @Autowired
    private ITipoDocumentoService tipoDocumentoService;

    // Formulario para Registrar Documento
    @RequestMapping(value = "/DocumentoR", method = RequestMethod.GET) // Pagina principal
    public String Documento(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("documento", new Documento());
            model.addAttribute("documentos", documentoService.findAll());
            model.addAttribute("tiposdoc", tipoDocumentoService.findAll());
            return "tramite/gestionarDocumento";
        } else {
            return "redirect:LoginR";
        }
    }

    // Boton para Guardar Documento
    @RequestMapping(value = "/DocumentoF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String DocumentoF(@Validated Documento documento,
            @RequestParam(value = "tipoDocumento") Long[] id_tipo_documento) { // validar los datos capturados (1)
        for (int i = 0; i < id_tipo_documento.length; i++) {
            System.out.println(id_tipo_documento[i]);

        }
        documento.setEstado("A");
        documentoService.save(documento);

        return "redirect:/DocumentoR";
    }

    // Vista Listar Documentos
    @RequestMapping(value = "/DocumentoL", method = RequestMethod.GET) // Pagina principal
    public String DocumentoL(Model model) {
        model.addAttribute("documentos", documentoService.findAll());
        return "Tramite/ListarDocumentos";
    }

    // Boton para Editar Documentos
    @RequestMapping(value = "/editar-documento/{id_documento}")
    public String editar_p(@PathVariable("id_documento") Long id_documento, Model model) {

        Documento documento = documentoService.findOne(id_documento);

        model.addAttribute("documento", documento);
        model.addAttribute("documentos", documentoService.findAll());
        model.addAttribute("tiposdoc", tipoDocumentoService.findAll());
        model.addAttribute("edit", "true");
        return "tramite/gestionarDocumento";

    }

    // Boton para Guardar Documento
    @RequestMapping(value = "/DocumentoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String DocumentoModF(@Validated Documento documento,
            @RequestParam(value = "tipoDocumento") Long[] id_tipo_documento) { // validar los datos capturados (1)
        for (int i = 0; i < id_tipo_documento.length; i++) {
            System.out.println(id_tipo_documento[i]);

        }
        documento.setEstado("A");
        documentoService.save(documento);

        return "redirect:/DocumentoR";
    }

    // boton para eliminar Documentos
    @RequestMapping(value = "/eliminar-documento/{id_documento}")
    public String eliminar_p(@PathVariable("id_documento") Long id_documento) {

        Documento documento = documentoService.findOne(id_documento);

        documento.setEstado("X");

        documentoService.save(documento);
        return "redirect:/DocumentoR";

    }
}
