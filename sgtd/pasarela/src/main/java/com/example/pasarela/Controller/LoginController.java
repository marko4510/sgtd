package com.example.pasarela.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Entity.GradoAcademico;
import com.example.pasarela.Models.Entity.Nacionalidad;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Entity.Tramite;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.IGradoAcademicoService;
import com.example.pasarela.Models.Service.INacionalidadService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IProvinciaService;
import com.example.pasarela.Models.Service.ITramiteService;
import com.example.pasarela.Models.Service.IUsuarioService;

@Controller
public class LoginController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IPersonaService personaService;

	@Autowired
	private IDepartamentoService departamentoService;

	@Autowired
	private INacionalidadService nacionalidadService;

	@Autowired
	private ICarreraService carreraService;

	@Autowired
	private IGradoAcademicoService gradoAcademicoService;

	@Autowired
	private IProvinciaService provinciaService;

	@Autowired
    private ITramiteService tramiteService;

	// Funcion de visualizacion de iniciar sesiòn administrador
	@RequestMapping(value = "/LoginR", method = RequestMethod.GET)
	public String LoginR(Model model) {

		return "login";
	}

	// Funciòn de iniciar sesiòn administrador
	@RequestMapping(value = "/LogearseF", method = RequestMethod.POST)
	public String logearseF(@RequestParam(value = "usuario") String user,
			@RequestParam(value = "contrasena") String contrasena, Model model, HttpServletRequest request,
			RedirectAttributes flash) {
		// los dos parametros de usuario, contraseña vienen del formulario html
		Usuario usuario = usuarioService.getUsuarioContraseña(user, contrasena);

		if (usuario != null) {
			if (usuario.getEstado().equals("C")) {
				return "redirect:/cerrar_sesionAdm";
			}
			HttpSession sessionAdministrador = request.getSession(true);

			sessionAdministrador.setAttribute("usuario", usuario);
			sessionAdministrador.setAttribute("persona", usuario.getPersona());

			flash.addAttribute("success", usuario.getPersona().getNombre());

			
			
			return "redirect:/AdmPG/";

		} else {
			return "redirect:/LoginR";
		}

	}

	// Funcion de visualizaciòn de la pagina principal
	@RequestMapping(value = "/AdmPG", method = RequestMethod.GET) // Pagina principal
	public String Inicio(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("usuario") != null) {
			List<Tramite> listL = tramiteService.listaCarpetaLegalizacion();
			List<Tramite> listS = tramiteService.listaCarpetaSupletorio();
			List<Tramite> listT = tramiteService.listaCarpetaTitulos();
			List<Tramite> listP = tramiteService.listaCarpetaProvision();
            model.addAttribute("listL", listL.size());
			model.addAttribute("listS", listS.size());
			model.addAttribute("listT", listT.size());
			model.addAttribute("listP", listP.size());
			return "adm";
		} else {
			return "redirect:LoginR";
		}
	}

	// Funcion de cerrar sesion de administrador
	@RequestMapping("/cerrar_sesionAdm")
	public String cerrarSesion2(HttpServletRequest request, RedirectAttributes flash) {
		HttpSession sessionAdministrador = request.getSession();
		if (sessionAdministrador != null) {
			sessionAdministrador.invalidate();
			flash.addAttribute("validado", "Se cerro sesion con exito!");
		}
		return "redirect:/LoginR";
	}

	// Funciòn de iniciar sesiòn usuario
	@RequestMapping(value = "/LogearseC", method = RequestMethod.POST)
	public String logearseC(@RequestParam(value = "usuario") String user,
			@RequestParam(value = "contrasena") String contrasena, Model model, HttpServletRequest request,
			RedirectAttributes flash) throws ParseException {
		// los dos parametros de usuario, contraseña vienen del formulario html

		// condicional para determinar si accede por api o registrado por el sistema
		// Registrar usuario desde el siringuero
		// if (usuarioService.getUsuarioContraseña(user, contrasena) == null) {

		// Proceso pra ingreso por medio del api
		Map<String, Object> requests = new HashMap<String, Object>();

		requests.put("usuario", user);
		requests.put("clave", contrasena);

		String url = "http://181.115.188.250:9993/v1/service/api/3e958d74203b465abf8ee8b253cce422";
		String key = "key e73b1991c59a67fe182524e4d12da556136ced8a9da310c3af4c4efbde804a10";

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-api-key", key);

		HttpEntity<HashMap> req = new HttpEntity(requests, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

		if (resp.getBody().get("status").toString().equals("200")) {

			Map<String, Object> data = (Map) resp.getBody().get("data");

			// CONDICIONAL PARA EL REGISTRO DE USUSRIO DEL SIRINGUERO
			Persona persona = personaService.getPersonaCI(data.get("dip").toString());
			if (persona == null) {

				// ====================== INICIO CAPTURA Y REGISTRA
				// CARRERA================================
				String carrera = data.get("programa").toString();
				List<Carrera> lCarreras = carreraService.findAll();
				Carrera c = new Carrera();

				if (lCarreras.size() == 0) {
					c.setNombre_carrera(data.get("programa").toString());
					c.setEstado("A");
					carreraService.save(c);

				} else {

					for (Carrera carrera2 : lCarreras) {
						if (carrera.equals(carrera2.getNombre_carrera())) {
							c = carrera2;
						}
					}
					for (Carrera carrera2 : lCarreras) {

						if (!carrera.equals(carrera2.getNombre_carrera())) {
							c.setNombre_carrera(data.get("programa").toString());
							c.setEstado("A");
							carreraService.save(c);
						}
					}

				}

				// ====================== FIN CAPTURA Y REGISTRA
				// CARRERA================================

				// ====================== INICIO CAPTURA Y REGISTRA GRADO
				// ACADEMICO================================
				String grado = data.get("tipo_grado").toString();
				List<GradoAcademico> lGradoAcademicos = gradoAcademicoService.findAll();
				GradoAcademico g = new GradoAcademico();

				if (lGradoAcademicos.size() == 0) {
					g.setNombre(data.get("tipo_grado").toString());
					g.setEstado("A");
					g.setCarrera(c);
					gradoAcademicoService.save(g);

				} else {

					for (GradoAcademico gradoAcademico2 : lGradoAcademicos) {
						if (grado.equals(gradoAcademico2.getNombre())) {
							g = gradoAcademico2;
						}
					}
					for (GradoAcademico gradoAcademico2 : lGradoAcademicos) {

						if (!grado.equals(gradoAcademico2.getNombre())) {
							g.setNombre(data.get("tipo_grado").toString());
							g.setEstado("A");
							g.setCarrera(c);
							gradoAcademicoService.save(g);
						}
					}

				}

				// ====================== FIN CAPTURA Y REGISTRA GRADO
				// ACADEMICO================================

				String naciona = data.get("pais").toString();
				List<Nacionalidad> lNacionalidades = nacionalidadService.findAll();
				Nacionalidad n = new Nacionalidad();

				if (lNacionalidades.size() == 0) {
					n.setNombre_nacionalidad(data.get("pais").toString());
					n.setEstado("A");
					nacionalidadService.save(n);

				} else {

					for (Nacionalidad nacionalidad2 : lNacionalidades) {
						if (naciona.equals(nacionalidad2.getNombre_nacionalidad())) {
							n = nacionalidad2;
							System.out.println("nombre exp desde bd " + nacionalidad2.getNombre_nacionalidad());
						}
					}
					for (Nacionalidad nacionalidad2 : lNacionalidades) {

						if (!naciona.equals(nacionalidad2.getNombre_nacionalidad())) {
							n.setNombre_nacionalidad(data.get("pais").toString());
							n.setEstado("A");
							nacionalidadService.save(n);
							System.out.println("Dato en otra fila");
						}
					}

				}

				// ====================== INICIO CAPTURA Y REGISTRA
				// DEPARTAMENTO================================
				String e = data.get("departamento").toString();
				List<Departamento> lDepartamentos = departamentoService.findAll();

				Departamento a = new Departamento();

				if (lDepartamentos.size() == 0) {
					a.setNombre(data.get("departamento").toString());
					a.setNacionalidad(n);
					a.setEstado("A");
					departamentoService.save(a);

				} else {

					for (Departamento departamento2 : lDepartamentos) {
						if (e.equals(departamento2.getNombre())) {
							a = departamento2;
							System.out.println("nombre exp desde bd " + departamento2.getNombre());
						}
					}
					for (Departamento departamento2 : lDepartamentos) {

						if (!e.equals(departamento2.getNombre())) {
							a.setNombre(data.get("departamento").toString());
							a.setNacionalidad(n);
							a.setEstado("A");
							departamentoService.save(a);
							System.out.println("Dato en otra fila");
						}
					}

				}
				// ====================== FIN CAPTURA Y REGISTRA
				// DEPARTAMENTO================================

				// ====================== INICIO CAPTURA Y REGISTRA
				// PROVINCIA================================
				// e=pr a=prov
				String pr = data.get("provincia").toString();
				List<Provincia> lProvincias = provinciaService.findAll();

				Provincia prov = new Provincia();

				if (lProvincias.size() == 0) {
					prov.setNombre_provincia(data.get("provincia").toString());
					prov.setDepartamento(a);
					prov.setEstado("A");
					provinciaService.save(prov);

				} else {

					for (Provincia provincia2 : lProvincias) {
						if (pr.equals(provincia2.getNombre_provincia())) {
							prov = provincia2;
							System.out.println("nombre exp desde bd " + provincia2.getNombre_provincia());
						}
					}
					for (Provincia provincia2 : lProvincias) {

						if (!pr.equals(provincia2.getNombre_provincia())) {
							prov.setNombre_provincia(data.get("provincia").toString());
							prov.setDepartamento(a);
							prov.setEstado("A");
							provinciaService.save(prov);
							System.out.println("Dato en otra fila");
						}
					}

				}
				// ====================== FIN CAPTURA Y REGISTRA
				// PROVINCIA================================

				persona = new Persona();
				persona.setCi(data.get("dip").toString());
				persona.setNombre(data.get("nombres").toString());
				persona.setAp_paterno(data.get("paterno").toString());
				persona.setAp_materno(data.get("materno").toString());
				persona.setNumero_contacto("0");
				persona.setEstado("A");

				String dDate = data.get("fec_nacimiento").toString();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date cDate = df.parse(dDate);

				persona.setFecha_nacimiento(cDate);
				persona.setProvincia(prov);
				persona.setGradoAcademico(g);
				personaService.save(persona);
			} else {

			}

			/* CONDICIONAL PARA EL REGISTRO DE USUARIO DESDE SIRINGUERO */
			Usuario usuario = usuarioService.getUsuarioContraseña(user, contrasena);

			if (usuario == null) {
				usuario = new Usuario();
				usuario.setUsuario_nom(user);
				usuario.setContrasena(contrasena);
				usuario.setPersona(persona);
				usuario.setEstado("C");
				usuarioService.save(usuario);
			}
		}

		if (usuarioService.getUsuarioContraseña(user, contrasena) != null) {

			HttpSession session = request.getSession(true);

			Usuario usuario = usuarioService.getUsuarioContraseña(user, contrasena);

			session.setAttribute("usuario", usuario);
			session.setAttribute("persona", usuario.getPersona());

			flash.addAttribute("success", usuario.getPersona().getNombre());

			return "redirect:/Inicio";
		} else {

			return "redirect:/Inicio";
		}

		/*
		 * } else {
		 * 
		 * //proceso de ingreso atraves del registro por el sistema
		 * HttpSession session = request.getSession(true);
		 * 
		 * Usuario usuario = usuarioService.getUsuarioContraseña(user, contrasena);
		 * 
		 * session.setAttribute("usuario", usuario);
		 * 
		 * flash.addAttribute("success", usuario.getPersona().getNombre());
		 * 
		 * return "redirect:/Inicio";
		 * }
		 */

	}

	// Funcion de cerrar sesion de administrador
	@RequestMapping("/cerrar_sesion2")
	public String cerrarSesion(HttpServletRequest request, RedirectAttributes flash) {
		HttpSession sessionAdministrador = request.getSession();
		if (sessionAdministrador != null) {
			sessionAdministrador.invalidate();
			flash.addAttribute("validado", "Se cerro sesion con exito!");
		}
		return "redirect:/Inicio";
	}
}
