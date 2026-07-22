package com.proyectoweb.recursos;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Activa JAX-RS bajo /api (peticiones AJAX de las vistas).
 */
@ApplicationPath("/api")
public class AplicacionRest extends Application {

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("jersey.config.disableMoxy", true);
        return props;
    }
}
