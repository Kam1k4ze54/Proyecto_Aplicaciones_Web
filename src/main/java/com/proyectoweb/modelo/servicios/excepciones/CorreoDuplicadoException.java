package com.proyectoweb.modelo.servicios.excepciones;

// CU01 / CU03: el correo ya pertenece a otra cuenta
public class CorreoDuplicadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CorreoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
