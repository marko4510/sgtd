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

            List<Titulo> titulos = tituloService.findAll();
            Map<Titulo, List<Firma>> titulosConFirmas = new LinkedHashMap<>(); // Usar LinkedHashMap en lugar de HashMap
            
            for (Titulo titulo : titulos) {
                List<Firma> firmas = firmaService.Firmas(titulo.getId_titulo()); // Obtener las firmas relacionadas a cada título
                titulosConFirmas.put(titulo, firmas); // Agregar el título y las firmas al mapa
            }
            
            model.addAttribute("titulosConFirmas", titulosConFirmas); // Agregar el mapa al modelo
            return "certificado/listarTitulos";
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
}
