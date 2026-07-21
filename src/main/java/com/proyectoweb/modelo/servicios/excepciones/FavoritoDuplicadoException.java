package com.proyectoweb.modelo.servicios.excepciones;

// CU06-A: el elemento ya está en la lista de favoritos del usuario
public class FavoritoDuplicadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FavoritoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
