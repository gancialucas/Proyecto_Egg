package com.grupo1.aplicacionweb.controladores;

import com.grupo1.aplicacionweb.entidades.Ingrediente;
import com.grupo1.aplicacionweb.entidades.Paso;
import com.grupo1.aplicacionweb.entidades.Receta;
import com.grupo1.aplicacionweb.enumeraciones.CategoriaPlato;
import com.grupo1.aplicacionweb.servicio.RecetaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.*;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/receta")
@SessionAttributes("receta")
public class RecetaController {
    @Autowired
    private RecetaServicio recetaServicio;
    

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/")
    public String listar(Model model) {
        List<Receta> listaRecetas = recetaServicio.listar();

        model.addAttribute("recetas", listaRecetas);
        model.addAttribute("titulo", "Listado de  Recetas");
        model.addAttribute("h1", "Lista de Recetas Existentes");
        return "/receta/lista";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/crear")
    public String crearReceta(Model model) {
        Receta receta = new Receta();
        for (int i = 0; i < 20; ++i) { // ingresar el numero de ingredientes que precisa el ususario
            receta.getIngredientes().add(new Ingrediente());
        }
        for (int i = 0; i < 20; ++i) { // ingresar el numero de pasos que precisa el ususario
            receta.getPasos().add(new Paso());
        }
        model.addAttribute("titulo", "Formulario");
        model.addAttribute("h1", "Formulario ingreso de recetas");
        model.addAttribute("receta", receta);
        model.addAttribute("listaCategorias", CategoriaPlato.values());

        return "/receta/nuevo";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Receta receta, SessionStatus ss, RedirectAttributes redirect, @RequestParam("file") MultipartFile imagen) {

        //codigo para GUARDAR IMAGEN //
        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//imagenes/receta");
            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            try {
                byte[] bytesImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                receta.setFoto(imagen.getOriginalFilename());
            } catch (IOException e) {
                redirect.addFlashAttribute("error", e.getMessage());
            }
        }

        // se asigna el orden de los pasos
        for (int i = 0; i < receta.getPasos().size(); ++i) {
            receta.getPasos().get(i).setNumero(i + 1);
        }

        //Se le asigna la receta al paso (receta_id)
        for (int i = 0; i < receta.getPasos().size(); ++i) {
            receta.getPasos().get(i).setReceta(receta);
        }

        //Se borran los pasos empty
        Iterator<Paso> it = receta.getPasos().iterator();
        while (it.hasNext()) {
            if (it.next().getPaso().isEmpty() || it.next().getPaso() == null) {
                it.remove();
            }
        }

        //Se borran los ingredientes vacios
        Iterator<Ingrediente> itIng = receta.getIngredientes().iterator();
        while (itIng.hasNext()) {
            if (itIng.next().getNombre().isEmpty() || itIng.next().getNombre() == null) {
                itIng.remove();
            }
        }

        // Se calcula el tiempo total de la receta
        if (receta.getTiempoDeCoccion() != null && receta.getTiempoDePreparacion() != null) {
            receta.setTiempoTotal(receta.getTiempoDeCoccion() + receta.getTiempoDePreparacion());
        }

        recetaServicio.crear(receta);
        //mailsend.enviar("");
        ss.setComplete();

        return "redirect:/receta/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, RedirectAttributes redirect, Model model) {
       // Receta receta = null;
        if (id == null || recetaServicio.findById(id) == null) {
            redirect.addFlashAttribute("error", "Error, no hay un receta con ese ID.");
            return "redirect:/receta/";
        } else {
            Receta receta = recetaServicio.findById(id);
            for (int i = 0; i < 20; ++i) { // ingresar el numero de ingredientes que precisa el ususario
                receta.getIngredientes().add(new Ingrediente());
            }
            for (int i = 0; i < 20; ++i) { // ingresar el numero de pasos que precisa el ususario
                receta.getPasos().add(new Paso());
            }
            model.addAttribute("receta", receta);
            model.addAttribute("ingredientes", receta.getIngredientes());
            model.addAttribute("pasos", receta.getPasos());
            model.addAttribute("listaCategorias", CategoriaPlato.values());
        }

        
        return "/receta/editar";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        if (id == null || recetaServicio.findById(id) == null) {
            redirect.addFlashAttribute("error", "Error, no hay un receta con ese ID.");
            return "redirect:/receta/";
        } else {
            recetaServicio.eliminar(id);
            redirect.addFlashAttribute("success", "Su receta se elimino con exito!");
        }
        return "redirect:/receta/";
    }

    @GetMapping("/detalle/{id}")
    public String detalleRecetas(@PathVariable("id") Integer id, Model model, RedirectAttributes atribute) {
        Receta receta = null;
        if (id != null) {
            receta = recetaServicio.findById(id);
        } else {
            atribute.addFlashAttribute("error", "Error con el id de la recera");
            return "redirect:/receta/";
        }
        List<Ingrediente> listIngredientes = receta.getIngredientes();

        model.addAttribute("titulo", "Detalle");
        model.addAttribute("h1", "Detalle de la receta");
        model.addAttribute("receta", receta);
        model.addAttribute("ingredientes", listIngredientes);

        return "/receta/detalles";
    }
}
