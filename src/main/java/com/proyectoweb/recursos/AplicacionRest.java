package com.proyectoweb.recursos;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Activa JAX-RS bajo /api (peticiones AJAX de las vistas).
 */
@ApplicationPath("/api")
public class AplicacionRest extends Application {
}
