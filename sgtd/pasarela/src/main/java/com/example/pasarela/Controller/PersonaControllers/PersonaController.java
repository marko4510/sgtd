package com.example.pasarela.Controller.PersonaControllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Entity.GradoAcademico;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;
import com.example.pasarela.Models.Service.INacionalidadService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IProvinciaService;
import com.example.pasarela.Models.Service.IUsuarioService;

@Controller
public class PersonaController {
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

    // FUNCION PARA LA VISUALIZACION DEL REGISTRO DE PERSONA
    @RequestMapping(value = "/PersonaR", method = RequestMethod.GET) // Pagina principal
    public String Persona(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("persona", new Persona());
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("nacionalidades", nacionalidadService.findAll());
            model.addAttribute("departamentos", departamentoService.findAll());
            model.addAttribute("gradoAcademicos", gradoAcademicoService.findAll());
            model.addAttribute("carreras", carreraService.findAll());
            model.addAttribute("provincias", provinciaService.findAll());

            return "persona/gestionarPersona";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/dep", method = RequestMethod.GET)
    public @ResponseBody List<Departamento> findAllAgencies(
            @RequestParam(value = "nacionalidadId", required = true) Long cityId) {
        List<Departamento> departamentos = departamentoService.departamentosPorIdPais(cityId);
        return departamentos;
    }

    @RequestMapping(value = "/grados", method = RequestMethod.GET)
    public @ResponseBody List<GradoAcademico> findAllAgencie(
            @RequestParam(value = "carrerId", required = true) Long citId) {
        List<GradoAcademico> gradoAcademicos = gradoAcademicoService.gradoPorIdCarrera(citId);
        return gradoAcademicos;
    }

    @RequestMapping(value = "/provin", method = RequestMethod.GET)
    public @ResponseBody List<Provincia> findAllAgenci(
            @RequestParam(value = "departaId", required = true) Long citId) {
        List<Provincia> provincias = provinciaService.provinPorIdDeparta(citId);
        return provincias;
    }

    // FUNCION PARA GUARDAR EL REGISTRO DE PERSONA
    @RequestMapping(value = "/PersonaF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String PersonaF(@Validated Persona persona, Model model,
            @RequestParam(value = "id_provincia") Long id_pro,
            @RequestParam(value = "id_grado_academico") Long id_gra) { // validar los datos capturados (1)

        RandomAlfanumeric randomAlfanumeric = new RandomAlfanumeric();

        int i = 5;
        String passw = randomAlfanumeric.getRandomString(i);

        Provincia provincia = provinciaService.findOne(id_pro);
        GradoAcademico gradoAcademico = gradoAcademicoService.findOne(id_gra);
        persona.setEstado("A");
        persona.setProvincia(provincia);
        persona.setGradoAcademico(gradoAcademico);
        personaService.save(persona);

        Usuario usuario = new Usuario();
        usuario.setContrasena(passw + "*");
        usuario.setUsuario_nom(persona.getCi());
        usuario.setEstado("C");
        usuario.setPersona(persona);
        usuarioService.save(usuario);

        return "redirect:/PersonasL";
    }

    @RequestMapping(value = "/PersonaModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String PersonaMod(@Validated Persona persona, Model model,
            @RequestParam(value = "id_provincia") Long id_pro,
            @RequestParam(value = "id_grado_academico") Long id_gra) { // validar los datos capturados (1)

        Provincia provincia = provinciaService.findOne(id_pro);
        GradoAcademico gradoAcademico = gradoAcademicoService.findOne(id_gra);
        persona.setEstado("A");
        persona.setProvincia(provincia);
        persona.setGradoAcademico(gradoAcademico);
        personaService.save(persona);

        return "redirect:/PersonasL";
    }

    // FUNCION PARA LISTAR LOS REGISTRO DE PERSONA
    @RequestMapping(value = "/PersonasL", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model) {
        model.addAttribute("personas", personaService.findAll());
        model.addAttribute("nacionalidades", nacionalidadService.findAll());
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("gradoAcademicos", gradoAcademicoService.findAll());
        model.addAttribute("carreras", carreraService.findAll());
        model.addAttribute("provincias", provinciaService.findAll());
        return "persona/listarPersona";
    }

    // FUNCION PARA EDITAR EL REGISTRO DE PERSONA
    @RequestMapping(value = "/editar-persona/{id_persona}")
    public String editar_p(@PathVariable("id_persona") Long id_persona, Model model) {

        Persona persona = personaService.findOne(id_persona);

        model.addAttribute("persona", persona);
        model.addAttribute("personas", personaService.findAll());
        model.addAttribute("nacionalidades", nacionalidadService.findAll());
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("carreras", carreraService.findAll());
        model.addAttribute("edit", "true");

        return "persona/gestionarPersona";

    }

    // FUNCION PARA ELIMINAR EL REGISTRO DE PERSONA
    @RequestMapping(value = "/eliminar-persona/{id_persona}")
    public String eliminar_p(@PathVariable("id_persona") Long id_persona) {

        Persona persona = personaService.findOne(id_persona);

        persona.setEstado("X");

        personaService.save(persona);
        return "redirect:/PersonasL";

    }
}
