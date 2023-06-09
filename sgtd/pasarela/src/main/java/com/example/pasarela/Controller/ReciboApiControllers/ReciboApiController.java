package com.example.pasarela.Controller.ReciboApiControllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Entity.Departamento;
import com.example.pasarela.Models.Entity.Nacionalidad;
import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IDepartamentoService;
import com.example.pasarela.Models.Service.INacionalidadService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.IProvinciaService;



@Controller
public class ReciboApiController {
    
	@Autowired
	private IPersonaService personaService;

    @Autowired
	private ICarreraService carreraService;

    @Autowired
	private INacionalidadService nacionalidadService;

    @Autowired
	private IDepartamentoService departamentoService;

    @Autowired
	private IProvinciaService provinciaService;
    

	 // Formulario para Registrar TipoDocumento
	 @RequestMapping(value = "/InicioRecibo", method = RequestMethod.GET) // Pagina principal
	 public String InicioRecibo(HttpServletRequest request, Model model) {
		 if (request.getSession().getAttribute("usuario") != null) {
 
		
 
			 return "tramite/gestionarRecibo";
		 } else {
			 return "redirect:LoginR";
		 }
	 }

	

    @RequestMapping(value = "/reciboApi", method = RequestMethod.POST)
    public String reciboApi(@RequestParam(value = "codigo") String codigo, Model model, HttpServletRequest request,
	RedirectAttributes redirectAttrs) throws ParseException {

        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("code", codigo);
        String url = "http://181.115.188.250:9993/v1/service/api/abd0b66440194985807c94c918fa319c";
        String key = "key 4d177e5f89ef62d1ce4b3fab51a58b77640d04246d38ccde163b3855e7237828";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", key);

        HttpEntity<HashMap> req = new HttpEntity(requests, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);
		

        if (resp.getBody().get("status").toString().equals("200")) {

			Map<String, Object> data = (Map) resp.getBody().get("data");
            Persona persona = personaService.getPersonaCI(data.get("ci").toString());
			
			
            if (persona == null) {
                
                String carrera = data.get("carrera").toString();
				List<Carrera> lCarreras = carreraService.findAll();
				Carrera c = new Carrera();
                
                if (lCarreras.size() == 0) {
					c.setNombre_carrera(data.get("carrera").toString());
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
							c.setNombre_carrera(data.get("carrera").toString());
							c.setEstado("A");
							carreraService.save(c);
						}
					}

				}
                
                String naciona = data.get("nacionalidad").toString();
				List<Nacionalidad> lNacionalidades = nacionalidadService.findAll();
				Nacionalidad n = new Nacionalidad();
                
                if (lNacionalidades.size() == 0) {
					n.setNombre_nacionalidad(data.get("nacionalidad").toString());
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
							n.setNombre_nacionalidad(data.get("nacionalidad").toString());
							n.setEstado("A");
							nacionalidadService.save(n);
							System.out.println("Dato en otra fila");
						}
					}

				}

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
                persona = new Persona();
				persona.setCi(data.get("ci").toString());
				persona.setNombre(data.get("nombre").toString());
				persona.setAp_paterno(data.get("paterno").toString());
				persona.setAp_materno(data.get("materno").toString());
				persona.setNumero_contacto(data.get("celular").toString());
                persona.setCorreo(data.get("correo").toString());
				persona.setSexo(data.get("sexo").toString());
				persona.setEstado("A");

				String dDate = data.get("fecha_nacimiento").toString();
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				Date cDate = df.parse(dDate);

				persona.setFecha_nacimiento(cDate);
				persona.setProvincia(prov);
			
				personaService.save(persona);
				model.addAttribute("edit", "true");
				redirectAttrs
				.addFlashAttribute("mensaje2", "Debe actualizar los datos de carrera y grado academico de la Persona")
				.addFlashAttribute("clase2", "warning alert-dismissible fade show");
		

				return "redirect:/editar-persona/"+ persona.getId_persona();
            }
			if (persona != null) {
				redirectAttrs
				.addFlashAttribute("mensaje", "La persona que intenta registrar ya existe!")
				.addFlashAttribute("clase", "warning alert-dismissible fade show");
				return "redirect:/InicioRecibo";
			}
			
        }
		return "redirect:/LoginR";
       
    }
}
