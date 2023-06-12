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

import com.example.pasarela.Models.Entity.GradoAcademico;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;

@Controller
public class GradoAcademicoController {
	@Autowired
	private IGradoAcademicoService gradoAcademicoService;

	@Autowired
	private ICarreraService carreraService;

	// FUNCION PARA LA VISUALIZACION DE Grado Academico
	@RequestMapping(value = "/gradoAcademicoR", method = RequestMethod.GET) // Pagina principal
	public String GradoAca(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("usuario") != null) {

			model.addAttribute("gradoAcademico", new GradoAcademico());
			model.addAttribute("gradosAcademicos", gradoAcademicoService.findAll());
			model.addAttribute("carreras", carreraService.findAll());

			return "persona/gestionarGradoAcademico";
		} else {
			return "redirect:LoginR";
		}
	}

	// FUNCION PARA GUARDAR El Grado Academico
	@RequestMapping(value = "/gradoAcademicoF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String gradoAcademicoF(@Validated GradoAcademico gradoAcademico, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

		gradoAcademico.setEstado("A");
		gradoAcademicoService.save(gradoAcademico);
		redirectAttrs
				.addFlashAttribute("mensaje", "Registro Exitoso del Grado Academico")
				.addFlashAttribute("clase", "success alert-dismissible fade show");

		return "redirect:/gradoAcademicoR";
	}

	// FUNCION PARA EDITAR EL REGISTRO DE departamento
	@RequestMapping(value = "/editar-gradoAca/{id_grado_academico}")
	public String editar_p(@PathVariable("id_grado_academico") Long id_grado_academico, Model model) {

		GradoAcademico gradoAcademico = gradoAcademicoService.findOne(id_grado_academico);

		model.addAttribute("gradoAcademico", gradoAcademico);
		model.addAttribute("gradosAcademicos", gradoAcademicoService.findAll());
		// model.addAttribute("nacionalidades", nacionalidadService.findAll());

		return "persona/gestionarGradoAcademico";

	}

	// FUNCION PARA GUARDAR El Grado Academico
	@RequestMapping(value = "/gradoAcademicoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String gradoAcademicoModF(@Validated GradoAcademico gradoAcademico) { // validar los datos capturados (1)

		gradoAcademico.setEstado("A");
		gradoAcademicoService.save(gradoAcademico);

		return "redirect:/gradoAcademicoR";
	}

	// FUNCION PARA ELIMINAR EL REGISTRO DE Grado Academico
	@RequestMapping(value = "/eliminar-gradoAca/{id_grado_academico}")
	public String eliminar_p(@PathVariable("id_grado_academico") Long id_grado_academico) {

		GradoAcademico gradoAcademico = gradoAcademicoService.findOne(id_grado_academico);

		gradoAcademico.setEstado("X");

		gradoAcademicoService.save(gradoAcademico);
		return "redirect:/gradoAcademicoR";

	}
}
