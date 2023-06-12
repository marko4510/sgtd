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

import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.INacionalidadService;

@Controller
public class DepartamentoController {
	@Autowired
	private IDepartamentoService departamentoService;

	@Autowired
	private INacionalidadService nacionalidadService;

	// FUNCION PARA LA VISUALIZACION DE departamento
	@RequestMapping(value = "/DepartamentoR", method = RequestMethod.GET) // Pagina principal
	public String Persona(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("usuario") != null) {

			model.addAttribute("departamento", new Departamento());
			model.addAttribute("departamentos", departamentoService.findAll());
			model.addAttribute("nacionalidades", nacionalidadService.findAll());

			return "persona/gestionarDepartamento";
		} else {
			return "redirect:LoginR";
		}
	}

	// FUNCION PARA GUARDAR EL departamento
	@RequestMapping(value = "/DepartamentoF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String PersonaF(@Validated Departamento departamento, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

		departamento.setEstado("A");
		departamentoService.save(departamento);
		redirectAttrs
				.addFlashAttribute("mensaje", "Registro Exitoso del Departamento")
				.addFlashAttribute("clase", "success alert-dismissible fade show");

		return "redirect:/DepartamentoR";
	}

	// FUNCION PARA EDITAR EL REGISTRO DE departamento
	@RequestMapping(value = "/editar-departamento/{id_departamento}")
	public String editar_p(@PathVariable("id_departamento") Long id_departamento, Model model) {

		Departamento departamento = departamentoService.findOne(id_departamento);

		model.addAttribute("departamento", departamento);
		model.addAttribute("departamentos", departamentoService.findAll());
		model.addAttribute("nacionalidades", nacionalidadService.findAll());
		model.addAttribute("edit", "true");

		return "persona/gestionarDepartamento";

	}

	// FUNCION PARA GUARDAR EL departamento
	@RequestMapping(value = "/DepartamentoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String departamentoModF(@Validated Departamento departamento, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

		departamentoService.save(departamento);
		redirectAttrs
				.addFlashAttribute("mensaje2", "Datos del Departamento Actualizados Correctamente")
				.addFlashAttribute("clase2", "success alert-dismissible fade show");

		return "redirect:/DepartamentoR";
	}

	// FUNCION PARA ELIMINAR EL REGISTRO DE departamento
	@RequestMapping(value = "/eliminar-departamento/{id_departamento}")
	public String eliminar_p(@PathVariable("id_departamento") Long id_departamento) {

		Departamento departamento = departamentoService.findOne(id_departamento);

		departamento.setEstado("X");

		departamentoService.save(departamento);
		return "redirect:/DepartamentoR";

	}
}
