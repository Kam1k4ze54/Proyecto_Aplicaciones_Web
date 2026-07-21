package com.proyectoweb.modelo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LugarTuristico")
public class LugarTuristico extends ElementoContenido {
    private static final long serialVersionUID = 1L;

    @Column(name = "horario")
    private String horario;

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
