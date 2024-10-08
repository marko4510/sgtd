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

import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IUsuarioService;

@Controller
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPersonaService personaService;

    @RequestMapping(value = "/UsuarioL", method = RequestMethod.GET) // Pagina principal
    public String Carrera(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("usuario", new Usuario());
            model.addAttribute("usuarios", usuarioService.findAll());
             model.addAttribute("personas", personaService.findAll());

            return "persona/gestionarUsuario";
        } else {
            return "redirect:LoginR";
        }
    }
    
    @RequestMapping(value = "/UsuarioF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String UsuarioF(@Validated Usuario usuario, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        usuarioService.save(usuario);
        redirectAttrs
				.addFlashAttribute("mensaje", "Registro Exitoso del Usuario")
				.addFlashAttribute("clase", "success alert-dismissible fade show");

        return "redirect:/UsuarioL";
    }

    @RequestMapping(value = "/editar-usuario/{id_usuario}")
    public String editar_usuario(@PathVariable("id_usuario") Long id_usuario, Model model) {

        Usuario usuario = usuarioService.findOne(id_usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.findAll());
          model.addAttribute("personas", personaService.findAll());
        model.addAttribute("edit", "true");

        return "persona/gestionarUsuario";

    }
    @RequestMapping(value = "/UsuarioModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String usuario_mod(@Validated Usuario usuario, RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        usuarioService.save(usuario);
        redirectAttrs
				.addFlashAttribute("mensaje2", "Datos del Usuario Actualizados Correctamente")
				.addFlashAttribute("clase2", "success alert-dismissible fade show");
        return "redirect:/UsuarioL";
    }
}
