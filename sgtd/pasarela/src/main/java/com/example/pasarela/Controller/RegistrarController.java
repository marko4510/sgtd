package com.example.pasarela.Controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.pasarela.Controller.PersonaControllers.RandomAlfanumeric;
import com.example.pasarela.Models.Dao.IPersonaDao;
import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Entity.GradoAcademico;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IEmailService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;
import com.example.pasarela.Models.Service.INacionalidadService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IProvinciaService;
import com.example.pasarela.Models.Service.IUsuarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
public class RegistrarController {
    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private INacionalidadService nacionalidadService;

    @Autowired
    private IDepartamentoService departamentoService;

    @Autowired
    private IProvinciaService provinciaService;

    @Autowired
    private IGradoAcademicoService gradoAcademicoService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IPersonaDao personaDao;

    // Vista del Formulario del boton Registrarse (layout2 - linea 76)
    @RequestMapping(value = "/Registrarse", method = RequestMethod.GET) // Pagina principal
    public String Registrarse(HttpServletRequest request, Model model) {

        model.addAttribute("persona", new Persona());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("nacionalidades", nacionalidadService.findAll());
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("gradoAcademicos", gradoAcademicoService.findAll());
        model.addAttribute("carreras", carreraService.findAll());
        model.addAttribute("provincias", provinciaService.findAll());

        return "publico/formularioRegistrarse";
    }

    @RequestMapping(value = "/deptos", method = RequestMethod.GET)
    public @ResponseBody List<Departamento> findAllAgencies(
            @RequestParam(value = "nacionalidadId", required = true) Long cityId) {
        List<Departamento> departamentos = departamentoService.departamentosPorIdPais(cityId);
        return departamentos;
    }

    @RequestMapping(value = "/ggrados", method = RequestMethod.GET)
    public @ResponseBody List<GradoAcademico> findAllAgencie(
            @RequestParam(value = "carrerId", required = true) Long citId) {
        List<GradoAcademico> gradoAcademicos = gradoAcademicoService.gradoPorIdCarrera(citId);
        return gradoAcademicos;
    }

    @RequestMapping(value = "/provincias", method = RequestMethod.GET)
    public @ResponseBody List<Provincia> findAllAgenci(
            @RequestParam(value = "departaId", required = true) Long citId) {
        List<Provincia> provincias = provinciaService.provinPorIdDeparta(citId);
        return provincias;
    }

    @RequestMapping(value = "/RegistroF", method = RequestMethod.POST) // Pagina principal
    public RedirectView RegistrarseF(@Validated @RequestBody Persona persona, RedirectAttributes redirectAttrs, Model model,

            @RequestParam(value = "id_provincia") Long id_p,
            @RequestParam(value = "id_grado_academico") Long id_g) {

        RandomAlfanumeric randomAlfanumeric = new RandomAlfanumeric();
        Optional<Persona> personaExistente = personaDao.findByCorreo(persona.getCorreo());
        
        if (personaExistente.isPresent()) {
            redirectAttrs
				.addFlashAttribute("mensaje", "El correo fue utilizado")
				.addFlashAttribute("clase", "danger alert-dismissible fade show");
				//return "redirect:/albergueR";
            return new RedirectView("/Registrarse");
        }
        

        int i = 5;
        String pass = randomAlfanumeric.getRandomString(i);

        Provincia provincia = provinciaService.findOne(id_p);
        GradoAcademico gradoAcademico = gradoAcademicoService.findOne(id_g);
        persona.setEstado("A");
        persona.setProvincia(provincia);
        persona.setGradoAcademico(gradoAcademico);
        //personaService.save(persona);
        personaDao.save(persona);
        //return ResponseEntity.ok(personaRegistrado);

        Usuario usuario = new Usuario();
        usuario.setContrasena(pass + "*");
        usuario.setUsuario_nom(persona.getCi());
        usuario.setEstado("C");
        usuario.setPersona(persona);
        emailService.enviarMensajeRegistro(usuario, usuario.getContrasena());
        usuarioService.save(usuario);
        redirectAttrs
				.addFlashAttribute("mensaje", "Registro Exitoso de Credencial de acceso en su correo")
				.addFlashAttribute("clase", "success alert-dismissible fade show");
        return new RedirectView("/Registrarse");
        
        //return "redirect:/Inicio";
    }

}
