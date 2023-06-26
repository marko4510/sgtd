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

import com.example.pasarela.Models.Entity.Autoridad;
import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Service.IAutoridadService;
import com.example.pasarela.Models.Service.IPersonaService;

@Controller
public class AutoridadController {
    @Autowired
    private IAutoridadService autoridadService;

    @Autowired
    private IPersonaService personaService;

    @RequestMapping(value = "/AutoridadR", method = RequestMethod.GET) // Pagina principal
    public String Autoridad(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("autoridad", new Autoridad());
            model.addAttribute("autoridades", autoridadService.findAll());
            model.addAttribute("personas", personaService.findAll());

            return "persona/gestionar-autoridad";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/AutoridadF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String AutoridadF(@Validated Autoridad autoridad, RedirectAttributes redirectAttrs) { 
                                                                                                
        autoridad.setEstado("A");
        autoridadService.save(autoridad);
        redirectAttrs
                .addFlashAttribute("mensaje", "Registro Exitoso de la Autoridad")
                .addFlashAttribute("clase", "success alert-dismissible fade show");

        return "redirect:/AutoridadL";
    }

    @RequestMapping(value = "/AutoridadL", method = RequestMethod.GET) // Pagina principal
    public String autoridadL(HttpServletRequest request, Model model) {

        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("autoridades", autoridadService.findAll());
           model.addAttribute("personas", personaService.findAll());

            return "persona/listar-autoridad";
        } else {
            return "redirect:LoginR";
        }

    }

    @RequestMapping(value = "/editar-autoridad/{id_autoridad}")
    public String editar_autoridad(@PathVariable("id_autoridad") Long id_autoridad, Model model) {

        Autoridad autoridad = autoridadService.findOne(id_autoridad);

        model.addAttribute("autoridad", autoridad);
        model.addAttribute("autoridades", autoridadService.findAll());
        model.addAttribute("personas", personaService.findAll());
        model.addAttribute("edit", "true");

        return "persona/gestionar-autoridad";

    }

    @RequestMapping(value = "/AutoridadModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String autoridad_mod(@Validated Autoridad autoridad, RedirectAttributes redirectAttrs) { 

        autoridad.setEstado("A");
        autoridadService.save(autoridad);
        redirectAttrs
                .addFlashAttribute("mensaje2", "Datos de la Autoridad Actualizados Correctamente")
                .addFlashAttribute("clase2", "success alert-dismissible fade show");

        return "redirect:/AutoridadR";
    }

    @RequestMapping(value = "/eliminar-autoridad/{id_autoridad}")
    public String eliminar_autoridad(@PathVariable("id_autoridad") Long id_autoridad) {

        Autoridad autoridad = autoridadService.findOne(id_autoridad);

        autoridad.setEstado("X");

        autoridadService.save(autoridad);
        return "redirect:/AutoridadR";

    }
}
