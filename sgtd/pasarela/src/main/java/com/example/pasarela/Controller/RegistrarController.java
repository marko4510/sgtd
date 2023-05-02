package com.example.pasarela.Controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pasarela.Controller.PersonaControllers.RandomAlfanumeric;
import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Entity.GradoAcademico;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;
import com.example.pasarela.Models.Service.INacionalidadService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IProvinciaService;
import com.example.pasarela.Models.Service.IUsuarioService;

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
    public String RegistrarseF(@Validated Persona persona, Model model,

            @RequestParam(value = "id_provincia") Long id_p,
            @RequestParam(value = "id_grado_academico") Long id_g) {

        RandomAlfanumeric randomAlfanumeric = new RandomAlfanumeric();

        int i = 5;
        String pass = randomAlfanumeric.getRandomString(i);

        Provincia provincia = provinciaService.findOne(id_p);
        GradoAcademico gradoAcademico = gradoAcademicoService.findOne(id_g);
        persona.setEstado("A");
        persona.setProvincia(provincia);
        persona.setGradoAcademico(gradoAcademico);
        personaService.save(persona);

        Usuario usuario = new Usuario();
        usuario.setContrasena(pass + "*");
        usuario.setUsuario(persona.getCi());
        usuario.setEstado("C");
        usuario.setPersona(persona);
        usuarioService.save(usuario);

        return "redirect:/Inicio";
    }

}
