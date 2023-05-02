package com.example.pasarela.Controller.TramiteControllers;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.pasarela.Models.Entity.Nacionalidad;
import com.example.pasarela.Models.Service.INacionalidadService;



@Controller
public class NacionalidadController {
    
    @Autowired
    private INacionalidadService nacionalidadService;


	//FUNCION PARA LA VISUALIZACION DE REGISTRO DE MNACIONALIDAD
	@RequestMapping(value = "/NacionalidadR", method = RequestMethod.GET) // Pagina principal
	public String Persona(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
			
			model.addAttribute("nacionalidad", new Nacionalidad());
            model.addAttribute("nacionalidades", nacionalidadService.findAll());
			
			return "persona/gestionarNacionalidad";
		}else{
			return "redirect:LoginR";
		}
	}
	//FUNCION PARA GUARDAR LA NACIONALIDAD
	@RequestMapping(value = "/NacionalidadF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String PersonaF(@Validated Nacionalidad nacionalidad) { //validar los datos capturados (1)
		
        nacionalidad.setEstado("A");
        nacionalidadService.save(nacionalidad);
		
		return "redirect:/NacionalidadR";
	}


	//FUNCION PARA EDITAR EL REGISTRO DE NACIONALIDAD
	@RequestMapping(value = "/editar-nacionalidad/{id_nacionalidad}")
    public String editar_p(@PathVariable("id_nacionalidad")Long id_nacionalidad, Model model){
          
        Nacionalidad nacionalidad  = nacionalidadService.findOne(id_nacionalidad);
          
        model.addAttribute("nacionalidad", nacionalidad);
        model.addAttribute("nacionalidades",  nacionalidadService.findAll());
		model.addAttribute("edit", "true");
          
        return "persona/gestionarNacionalidad";
          
    }

	//FUNCION PARA GUARDAR LA NACIONALIDAD
	@RequestMapping(value = "/NacionalidadModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String NacionalidadModF(@Validated Nacionalidad nacionalidad) { //validar los datos capturados (1)
		
       
        nacionalidadService.save(nacionalidad);
		
		return "redirect:/NacionalidadR";
	}


	//FUNCION PARA ELIMINAR EL REGISTRO DE NACIONALIDAD
	@RequestMapping(value = "/eliminar-nacionalidad/{id_nacionalidad}")
	public String eliminar_p(@PathVariable("id_nacionalidad")Long id_nacionalidad) {
						
		
		Nacionalidad nacionalidad  = nacionalidadService.findOne(id_nacionalidad);
		
		nacionalidad.setEstado("X");
		
		nacionalidadService.save(nacionalidad);
		return "redirect:/NacionalidadR";
		
}
}