package com.example.pasarela.Controller.RevalidacionControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.pasarela.Models.Service.IRevalidacionGeneradoService;
import com.example.pasarela.Models.Service.IRevalidacionService;

@Controller
public class RevalidacionController {
    
    @Autowired
    private IRevalidacionService revalidacionService;

    @Autowired
    private IRevalidacionGeneradoService revalidacionGeneradoService;


    @GetMapping("/inicioGenerarRevalidacion")
    public String inicioGenerarCertificado(Model model) {
    

    return "revalidacion/generarRevalidacion";
  }
}
