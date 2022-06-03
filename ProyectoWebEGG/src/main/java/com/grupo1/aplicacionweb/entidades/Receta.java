package com.grupo1.aplicacionweb.entidades;

import com.grupo1.aplicacionweb.enumeraciones.CategoriaPlato;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recetas")
public class Receta implements Serializable {

    private static long serialVersionUID = -3210120901686041769L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @NotEmpty(message = "Este campo es obligatorio.")
    @Column(name = "nombre")
    private String nombre;

    // @NotEmpty(message = "Este campo es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private CategoriaPlato categoria;

    //    @NotEmpty(message = "Este campo es obligatorio.")
    @Column(name = "foto")
    private String foto;

    //    @NotEmpty(message = "Este campo es obligatorio.")
    @Column(name = "comentario")
    private String comentario;

    // @NotNull (message = "Este campo es obligatorio.")
    @Column(name = "tiempo_coccion")
    private Integer tiempoDeCoccion;

    // @NotNull (message = "Este campo es obligatorio.")
    @Column(name = "tiempo_preparacion")
    private Integer tiempoDePreparacion;

    // @NotNull (message = "Este campo es obligatorio.")
    @Column(name = "tiempo_total")
    private Integer tiempoTotal;

    // @NotNull (message = "Este campo es obligatorio.")
    @Column(name = "porcion")
    private Integer porcion;

    @OneToMany( mappedBy = "receta" ,cascade = CascadeType.ALL)
    private List<Paso> pasos = new ArrayList<>(); // no persiste el id de la receta

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "receta_ingrediente",
            joinColumns = {@JoinColumn(name = "receta_id")},
            inverseJoinColumns = {@JoinColumn(name = "ingrediente_id")})
    private List<Ingrediente> ingredientes = new ArrayList<>();

    @ManyToMany( mappedBy = "lunes" ,cascade = CascadeType.ALL)
    private List<Carta> cartas = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        Receta.serialVersionUID = serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaPlato getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaPlato categoria) {
        this.categoria = categoria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getTiempoDeCoccion() {
        return tiempoDeCoccion;
    }

    public void setTiempoDeCoccion(Integer tiempoDeCoccion) {
        this.tiempoDeCoccion = tiempoDeCoccion;
    }

    public Integer getTiempoDePreparacion() {
        return tiempoDePreparacion;
    }

    public void setTiempoDePreparacion(Integer tiempoDePreparacion) {
        this.tiempoDePreparacion = tiempoDePreparacion;
    }

    public Integer getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(Integer tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public Integer getPorcion() {
        return porcion;
    }

    public void setPorcion(Integer porcion) {
        this.porcion = porcion;
    }

    public List<Paso> getPasos() {
        return pasos;
    }

    public void setPasos(List<Paso> pasos) {
        this.pasos = pasos;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria=" + categoria +
                ", foto='" + foto + '\'' +
                ", comentario='" + comentario + '\'' +
                ", tiempoDeCoccion=" + tiempoDeCoccion +
                ", tiempoDePreparacion=" + tiempoDePreparacion +
                ", tiempoTotal=" + tiempoTotal +
                ", porcion=" + porcion +
                ", pasos=" + pasos +
                ", ingredientes=" + ingredientes +
                ", cartas=" + cartas +
                '}';
    }
}
