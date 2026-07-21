package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.entities.ElementoContenido;

/**
 * Sección de la vista previa del panel (CU04-A): hasta 3 elementos
 * y el origen de los mismos (preferencias, destacados o sin contenido).
 */
public class VistaSeccion {

    public static final String ORIGEN_PREFERENCIAS = "preferencias";
    public static final String ORIGEN_DESTACADOS = "destacados";
    public static final String ORIGEN_VACIO = "vacio";

    private final List<ElementoContenido> elementos;
    private final String origen;

    public VistaSeccion(List<ElementoContenido> elementos, String origen) {
        this.elementos = elementos;
        this.origen = origen;
    }

    public List<ElementoContenido> getElementos() {
        return elementos;
    }

    public String getOrigen() {
        return origen;
    }
}
