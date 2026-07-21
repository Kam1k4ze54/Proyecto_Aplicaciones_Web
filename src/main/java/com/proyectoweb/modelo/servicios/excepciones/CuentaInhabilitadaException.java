package com.proyectoweb.modelo.servicios.excepciones;

// CU02: la cuenta existe pero está inhabilitada
public class CuentaInhabilitadaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CuentaInhabilitadaException(String mensaje) {
        super(mensaje);
    }
}
