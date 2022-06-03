package com.grupo1.aplicacionweb.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cartas")
public class Carta implements Serializable {

    private static long serialVersionUID = -7819236374992397102L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date semana;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "carta_receta",
            joinColumns = {@JoinColumn(name = "carta_id")},
            inverseJoinColumns = {@JoinColumn(name = "receta_id")})
    private List<Receta> lunes = new ArrayList<>();
//    @OneToMany(mappedBy = "cartas", cascade = CascadeType.ALL)
//    private List<Receta> martes = new ArrayList<>();
//    @OneToMany(mappedBy = "cartas", cascade = CascadeType.ALL)
//    private List<Receta> miercoles = new ArrayList<>();
//    @OneToMany(mappedBy = "cartas", cascade = CascadeType.ALL)
//    private List<Receta> jueves = new ArrayList<>();
//    @OneToMany(mappedBy = "cartas", cascade = CascadeType.ALL)
//    private List<Receta> viernes = new ArrayList<>();
//    @OneToMany(mappedBy = "cartas", cascade = CascadeType.ALL)
//    private List<Receta> sabado = new ArrayList<>();
//    @OneToMany(mappedBy = "cartas", cascade = CascadeType.ALL)
//    private List<Receta> domingo = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSemana() {
        return semana;
    }

    public void setSemana(Date semana) {
        this.semana = semana;
    }

    public List<Receta> getLunes() {
        return lunes;
    }

    public void setLunes(List<Receta> lunes) {
        this.lunes = lunes;
    }
}

