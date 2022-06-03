package com.grupo1.aplicacionweb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/")
    public String loginIndex(Model model, @RequestParam(required = false) String error,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String logout,
            RedirectAttributes attribute){
        if (error != null) {
            model.addAttribute("error", "el usuario ingresado o la contraseña son incorrectos");
        }
        if (email != null) {
            model.addAttribute("email", email);
        }
        if (logout != null) {
            attribute.addFlashAttribute("success", "SESION FINALIZADA CON EXITO.");
            model.addAttribute("success", "SESION FINALIZADA CON EXITO.");
        }
        model.addAttribute("titulo", "Inicio");

        return "/index";
    }

}