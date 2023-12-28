package com.example.pasarela.Controller.RevalidacionControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pasarela.Models.Entity.Firma;
import com.example.pasarela.Models.Entity.Revalidacion;
import com.example.pasarela.Models.Entity.RevalidacionGenerado;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Service.IRevalidacionGeneradoService;
import com.example.pasarela.Models.Service.IRevalidacionService;

@Controller
public class RevalidacionController {
    
    @Autowired
    private IRevalidacionService revalidacionService;

    @Autowired
    private IRevalidacionGeneradoService revalidacionGeneradoService;


    @GetMapping("/inicioGenerarRevalidacion")
    public String inicioGenerarCertificado(Model model , HttpServletRequest request) {
    if (request.getSession().getAttribute("usuario") != null) {

    return "revalidacion/generarRevalidacion";
     } else {
            return "redirect:LoginR";
        }
  }


  // VISTA ADMIN, LISTAR SOLICITUDES DE LEGALIZACIONES
    @RequestMapping(value = "/listarRevalidaciones", method = RequestMethod.GET) // Pagina principal
    public String listarRevalidaciones(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

         
            
            model.addAttribute("revalidaciones", revalidacionService.findAll());

            return "revalidacion/listarRevalidacion";
        } else {
            return "redirect:LoginR";
        }
    }
    @RequestMapping(value = "/openFileRevalidacion/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_revalidacion) throws FileNotFoundException {
              RevalidacionGenerado revalidacionGenerado = revalidacionGeneradoService.buscarRevalidacionGeneradoPorRevalidacion(id_revalidacion);
       

        File file = new File(revalidacionGenerado.getRuta_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }


    // FUNCION PARA ELIMINAR EL REGISTRO DE departamento
	@RequestMapping(value = "/eliminar-revalidacion/{id_revalidacion}")
	public String eliminar_reva(@PathVariable("id_revalidacion") Long id_revalidacion) {
        Revalidacion revalidacion = revalidacionService.findOne(id_revalidacion);
   
	

		revalidacion.setEstado("X");

		revalidacionService.save(revalidacion);
		return "redirect:/listarRevalidaciones";

	}
}
