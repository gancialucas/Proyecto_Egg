package com.grupo1.aplicacionweb.controladores;

import com.grupo1.aplicacionweb.entidades.Usuario;
import com.grupo1.aplicacionweb.enumeraciones.Roles;
import com.grupo1.aplicacionweb.repositorios.UsuarioDao;
import com.grupo1.aplicacionweb.servicio.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioDao usuarioDao;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/")
    public String listar(Model model) {
        List<Usuario> listaUsuario = usuarioServicio.listar();
        model.addAttribute("usuarios", listaUsuario);
        model.addAttribute("titulo", "Listado Usuaarios");
        model.addAttribute("h1", "Listado de Usuarios");
        return "/usuario/lista";
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')") ---> activar una vez
    // creado un usuario ADMIN o USER
    @GetMapping("/crear")
    public String crearUsuario(Usuario usuario, Model model) {
        model.addAttribute("titulo", "Formulario");
        model.addAttribute("h1", "Formulario ingreso nuevo usuario");
        model.addAttribute("usuario", usuario);
        return "/usuario/nuevo";
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')") ---> activar una vez
    // creado un usuario ADMIN o USER
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model,
            RedirectAttributes redirect,
            @RequestParam("file") MultipartFile imagen, @RequestParam("password2") String password2) {
        if (result.hasErrors()) {
            return "/usuario/nuevo";
        }
        if (usuario == null) {
            redirect.addFlashAttribute("error", "El usuario es nulo");
            return "/usuario/crear";
        }
        if (usuario.getId() == null) {
            if (!password2.equals(usuario.getPassword())) {
                redirect.addFlashAttribute("error", "Las contraseñas no coinciden");
                model.addAttribute(usuario);
                return "/usuario/crear";
            }
        }

        // CODIGO PARA RECIBIR Y GUARDAR LA FOTO

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//imagenes/usuario");
            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            try {
                byte[] bytesImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                usuario.setFoto(imagen.getOriginalFilename());
            } catch (IOException e) {
                redirect.addFlashAttribute("error", e.getMessage());
            }
        }
        try {
            usuarioServicio.crear(usuario);
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/crear";
        }

        return "redirect:/usuario/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, RedirectAttributes redirect, Model model) {

        // VALIDACION DE INGRESO DE ID

        if (id == null || usuarioServicio.findById(id) == null) {
            redirect.addFlashAttribute("error", "Error, no hay un usuario con ese ID.");
            return "redirect:/usuario/";
        } else {
            List<Roles> roles = new ArrayList<>(Arrays.asList(Roles.values()));
            model.addAttribute("usuario", usuarioServicio.findById(id));
            model.addAttribute("roles", roles);
        }

        return "/usuario/editar";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirect) {

        // VALIDACION DE INGRESO DE ID

        if (id == null || usuarioServicio.findById(id) == null) {
            redirect.addFlashAttribute("error", "Error, no hay un usuario con ese ID.");
            return "redirect:/usuario/";
        } else {
            usuarioServicio.eliminar(id);
            redirect.addFlashAttribute("success", "Su usuario se elimino con exito!");
        }

        return "redirect:/usuario/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/pass/{id}")
    public String nuevoPass(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("usuario", usuarioServicio.findById(id));
        return "/usuario/nuevo-pass";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/update-pass")
    public String updatePass(Usuario usuario, @RequestParam("password2") String password2,
            RedirectAttributes redirect) {

        if (usuario.getPassword().isEmpty() || password2.isEmpty()) {
            redirect.addFlashAttribute("error", "Debe llenar ambos campos");
            return "redirect:/usuario/pass/" + usuario.getId();
        }

        if (!usuario.getPassword().equals(password2)) {
            redirect.addFlashAttribute("error", "Las constraseñas no coinciden");
            return "redirect:/usuario/pass/" + usuario.getId();
        }

        try {
            usuario.setPassword(password2);
            System.out.println("el password nuevo es : " + usuario.getPassword());
            usuarioServicio.cambiarPass(usuario);
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/pass/" + usuario.getId();
        }

        return "redirect:/usuario/";
    }

    // -------------------------------------FIN
    // CRUD-----------------------------------------------------------------------//

}