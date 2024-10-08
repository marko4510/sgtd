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

import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Service.ICarreraService;

@Controller
public class CarreraController {
    @Autowired
    private ICarreraService carreraService;

    @RequestMapping(value = "/CarreraR", method = RequestMethod.GET) // Pagina principal
    public String Carrera(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("carrera", new Carrera());
            model.addAttribute("carreras", carreraService.findAll());

            return "persona/gestionarCarrera";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/CarreraF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String CarreraF(@Validated Carrera carrera, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        carrera.setEstado("A");
        carreraService.save(carrera);
        redirectAttrs
				.addFlashAttribute("mensaje", "Registro Exitoso de la Carrera")
				.addFlashAttribute("clase", "success alert-dismissible fade show");

        return "redirect:/CarreraR";
    }

    @RequestMapping(value = "/editar-carrera/{id_carrera}")
    public String editar_carrera(@PathVariable("id_carrera") Long id_carrera, Model model) {

        Carrera carrera = carreraService.findOne(id_carrera);

        model.addAttribute("carrera", carrera);
        model.addAttribute("carreras", carreraService.findAll());
        model.addAttribute("edit", "true");

        return "persona/gestionarCarrera";

    }

    @RequestMapping(value = "/CarreraModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String carrera_mod(@Validated Carrera carrera, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        carrera.setEstado("A");
        carreraService.save(carrera);
        redirectAttrs
            .addFlashAttribute("mensaje2", "Datos de la Carrera Actualizados Correctamente")
            .addFlashAttribute("clase2", "success alert-dismissible fade show");


        return "redirect:/CarreraR";
    }

    @RequestMapping(value = "/eliminar-carrera/{id_carrera}")
    public String eliminar_carrera(@PathVariable("id_carrera") Long id_carrera) {

        Carrera carrera = carreraService.findOne(id_carrera);

        carrera.setEstado("X");

        carreraService.save(carrera);
        return "redirect:/CarreraR";

    }
}
