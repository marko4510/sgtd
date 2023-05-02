package com.example.pasarela.Controller.TramiteControllers;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.pasarela.Models.Entity.CostoDocumento;
import com.example.pasarela.Models.Service.ICostoDocumentoService;
import com.example.pasarela.Models.Service.IDocumentoService;
import com.example.pasarela.Models.Service.INacionalidadService;
import com.example.pasarela.Models.Service.ITipoDocumentoService;

import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

@Controller
public class CostoDocumentoController {
    
    @Autowired
	private ICostoDocumentoService costoDocumentoService;

	@Autowired
	private IDocumentoService documentoService;

	@Autowired
	private INacionalidadService nacionalidadService;

	@Autowired
	private ITipoDocumentoService tipoDocumentoService;

	@RequestMapping(value = "/CostoR", method = RequestMethod.GET) // Pagina principal
	public String Persona(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("usuario") != null) {

			model.addAttribute("costoDocumento", new CostoDocumento());
			model.addAttribute("costoDocumentos", costoDocumentoService.findAll());
			model.addAttribute("documentos", documentoService.findAll());
			model.addAttribute("nacionalidades", nacionalidadService.findAll());
			model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());

			return "tramite/gestionarCostoTramite";
		} else {
			return "redirect:LoginR";
		}
	}

	@RequestMapping(value = "/CostoF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String PersonaF(@Validated CostoDocumento costoDocumento) { // validar los datos capturados (1)

		costoDocumento.setEstado("A");
		costoDocumentoService.save(costoDocumento);

		return "redirect:/CostoR";
	}

	@RequestMapping(value = "/editar-costo/{id_costo_documento}")
	public String editar_p(@PathVariable("id_costo_documento") Long id_costo_documento, Model model) {

		CostoDocumento costoDocumento = costoDocumentoService.findOne(id_costo_documento);

		model.addAttribute("costoDocumento", costoDocumento);
		model.addAttribute("costoDocumentos", costoDocumentoService.findAll());
		model.addAttribute("documentos", documentoService.findAll());
		model.addAttribute("nacionalidades", nacionalidadService.findAll());
		model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());
		model.addAttribute("edit", "true");

		return "tramite/gestionarCostoTramite";

	}

	@RequestMapping(value = "/CostoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String CostoModF(@Validated CostoDocumento costoDocumento) { // validar los datos capturados (1)

		
		costoDocumentoService.save(costoDocumento);

		return "redirect:/CostoR";
	}

	@RequestMapping(value = "/eliminar-costo/{id_costo_documento}")
	public String eliminar_p(@PathVariable("id_costo_documento")Long id_costo_documento) {
						
		
		CostoDocumento costoDocumento  = costoDocumentoService.findOne(id_costo_documento);
		
		costoDocumento.setEstado("X");
		
		costoDocumentoService.save(costoDocumento);
		return "redirect:/CostoR";
		
	}
}
