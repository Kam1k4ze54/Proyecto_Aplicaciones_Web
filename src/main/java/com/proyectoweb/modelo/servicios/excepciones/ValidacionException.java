package com.proyectoweb.modelo.servicios.excepciones;

import java.util.List;

// CU07 paso 4.1: datos inválidos, campos vacíos o fechas incoherentes
public class ValidacionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final List<String> errores;

    public ValidacionException(List<String> errores) {
        super(String.join(" · ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
