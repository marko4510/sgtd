package com.example.pasarela.Controller.TituloControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pasarela.Models.Entity.Firma;
import com.example.pasarela.Models.Entity.Provincia;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Service.IFirmaService;
import com.example.pasarela.Models.Service.ITituloGeneradoService;
import com.example.pasarela.Models.Service.ITituloService;

@Controller
public class TituloController {

    @Autowired
    private ITituloService tituloService;

    @Autowired
    private ITituloGeneradoService tituloGeneradoService;

    @Autowired
    private IFirmaService firmaService;

    // VISTA ADMIN, LISTAR SOLICITUDES DE LEGALIZACIONES
    @RequestMapping(value = "/listarTitulos", method = RequestMethod.GET) // Pagina principal
    public String listarTitulos(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            List<Titulo> titulosA = tituloService.titulosAcademicos();
            Map<Titulo, List<Firma>> titulosConFirmasA = new LinkedHashMap<>(); // Usar LinkedHashMap en lugar de HashMap
            List<Titulo> titulosB = tituloService.titulosBachiller();
            Map<Titulo, List<Firma>> titulosConFirmasB = new LinkedHashMap<>();
            List<Titulo> titulosP = tituloService.titulosProvision();
            Map<Titulo, List<Firma>> titulosConFirmasP = new LinkedHashMap<>();
            List<Titulo> titulosPR = tituloService.titulosProvisionRevalidacion();
            Map<Titulo, List<Firma>> titulosConFirmasPR = new LinkedHashMap<>();

            for (Titulo titulo : titulosA) {
                List<Firma> firmasA = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasA.put(titulo, firmasA); // Agregar el título y las firmas al mapa
            }
            for (Titulo titulo : titulosB) {
                List<Firma> firmasB = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasB.put(titulo, firmasB); // Agregar el título y las firmas al mapa
            }
            for (Titulo titulo : titulosP) {
                List<Firma> firmasP = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasP.put(titulo, firmasP); // Agregar el título y las firmas al mapa
            }
            for (Titulo titulo : titulosPR) {
                List<Firma> firmasPR = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasPR.put(titulo, firmasPR); // Agregar el título y las firmas al mapa
            }
            
            model.addAttribute("titulosA", titulosConFirmasA);
            model.addAttribute("titulosB", titulosConFirmasB);
            model.addAttribute("titulosP", titulosConFirmasP); // Agregar el mapa al modelo
            model.addAttribute("titulosPR", titulosConFirmasPR);
            return "certificado/listarTitulos";
        } else {
            return "redirect:LoginR";
        }
    }

     @RequestMapping(value = "/listarTitulosPosgrado", method = RequestMethod.GET) // Pagina principal
    public String listarTitulosPosgrado(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            List<Titulo> titulosA = tituloService.titulosDiplomado();
            Map<Titulo, List<Firma>> titulosConFirmasA = new LinkedHashMap<>(); // Usar LinkedHashMap en lugar de HashMap
            List<Titulo> titulosB = tituloService.titulosEspecialidad();
            Map<Titulo, List<Firma>> titulosConFirmasB = new LinkedHashMap<>();
             List<Titulo> titulosC = tituloService.titulosMaestria();
            Map<Titulo, List<Firma>> titulosConFirmasC = new LinkedHashMap<>();
            List<Titulo> titulosP = tituloService.titulosDoctorado();
            Map<Titulo, List<Firma>> titulosConFirmasP = new LinkedHashMap<>();

            for (Titulo titulo : titulosA) {
                List<Firma> firmasA = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasA.put(titulo, firmasA); // Agregar el título y las firmas al mapa
            }
            for (Titulo titulo : titulosB) {
                List<Firma> firmasB = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasB.put(titulo, firmasB); // Agregar el título y las firmas al mapa
            }
             for (Titulo titulo : titulosC) {
                List<Firma> firmasC = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasC.put(titulo, firmasC); // Agregar el título y las firmas al mapa
            }
            for (Titulo titulo : titulosP) {
                List<Firma> firmasP = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmasP.put(titulo, firmasP); // Agregar el título y las firmas al mapa
            }
            
            model.addAttribute("titulosA", titulosConFirmasA);
            model.addAttribute("titulosB", titulosConFirmasB);
            model.addAttribute("titulosC", titulosConFirmasC);
            model.addAttribute("titulosP", titulosConFirmasP); 
            return "certificado/listarTitulosPosgrado";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/openFileTitulo/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_titulo) throws FileNotFoundException {
        TituloGenerado tituloGenerado = tituloGeneradoService.buscarTituloGeneradoPorTitulo(id_titulo);

        File file = new File(tituloGenerado.getRuta_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }


    // FUNCION PARA ELIMINAR EL REGISTRO DE departamento
	@RequestMapping(value = "/eliminar-titulo/{id_titulo}")
	public String eliminar_t(@PathVariable("id_titulo") Long id_titulo) {
        Titulo titulo = tituloService.findOne(id_titulo);
	

		titulo.setEstado("X");

		tituloService.save(titulo);
		return "redirect:/listarTitulos";

	}
}
