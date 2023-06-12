package com.example.pasarela.Controller.PersonaControllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IProvinciaService;

@Controller
public class ProvinciaController {

	@Autowired
	private IProvinciaService provinciaService;

	@Autowired
	private IDepartamentoService departamentoService;

	@RequestMapping(value = "/ProvinciaR", method = RequestMethod.GET) // Pagina principal
	public String Provincia(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("usuario") != null) {

			model.addAttribute("provincia", new Provincia());
			model.addAttribute("provincias", provinciaService.findAll());
			model.addAttribute("departamentos", departamentoService.findAll());

			return "persona/gestionarProvincia";
		} else {
			return "redirect:LoginR";
		}
	}

	// FUNCION PARA GUARDAR EL departamento
	@RequestMapping(value = "/ProvinciaF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String PersonaF(@Validated Provincia provincia, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

		provincia.setEstado("A");
		provinciaService.save(provincia);
		redirectAttrs
				.addFlashAttribute("mensaje", "Registro Exitoso de la Provincia")
				.addFlashAttribute("clase", "success alert-dismissible fade show");

		return "redirect:/ProvinciaR";
	}

	// FUNCION PARA EDITAR EL REGISTRO DE departamento
	@RequestMapping(value = "/editar-provincia/{id_provincia}")
	public String editar_p(@PathVariable("id_provincia") Long id_provincia, Model model) {

		Provincia provincia = provinciaService.findOne(id_provincia);

		model.addAttribute("provincia", provincia);
		model.addAttribute("provincias", provinciaService.findAll());
		model.addAttribute("departamentos", departamentoService.findAll());
		model.addAttribute("edit", "true");

		return "persona/gestionarProvincia";

	}

	// FUNCION PARA GUARDAR EL departamento
	@RequestMapping(value = "/ProvinciaModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String provinciaModF(@Validated Provincia provincia) { // validar los datos capturados (1)

		provincia.setEstado("A");
		provinciaService.save(provincia);

		return "redirect:/ProvinciaR";
	}

	// FUNCION PARA ELIMINAR EL REGISTRO DE departamento
	@RequestMapping(value = "/eliminar-provincia/{id_provincia}")
	public String eliminar_p(@PathVariable("id_provincia") Long id_provincia) {

		Provincia provincia = provinciaService.findOne(id_provincia);

		provincia.setEstado("X");

		provinciaService.save(provincia);
		return "redirect:/ProvinciaR";

	}
}
