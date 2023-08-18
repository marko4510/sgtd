package com.example.pasarela.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.example.pasarela.Models.Entity.Tramite;
import com.example.pasarela.Models.Service.ITramiteService;

@Controller
public class InicioController {

	@Autowired
    private ITramiteService tramiteService;

	// Funcion de visualizaciòn de la pagina principal
	@RequestMapping(value = "/Adm", method = RequestMethod.GET) // Pagina principal
	public String Inicio2(Model model, HttpServletRequest request) {
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

	// Funcion de visualizaciòn de la pagina principal
	@RequestMapping(value = "/Inicio", method = RequestMethod.GET) // Pagina principal
	public String Inicio2(Model model) {
		return "login";

	}

	// Funcion de visualizaciòn de la pagina principal
	@RequestMapping(value = "/", method = RequestMethod.GET) // Pagina principal
	public String Inicio(Model model) {
		return "login";

	}

	


}
