package com.proyectoweb.modelo.servicios.excepciones;

// CU06-A: regla de negocio — los eventos no son favoriteables
public class TipoNoFavoriteableException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TipoNoFavoriteableException(String mensaje) {
        super(mensaje);
    }
}
