package com.proyectoweb.modelo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EstablecimientoGastronomico")
public class EstablecimientoGastronomico extends ElementoContenido {
    private static final long serialVersionUID = 1L;

    @Column(name = "especialidad")
    private String especialidad;

    @Column(name = "horario")
    private String horario;

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
