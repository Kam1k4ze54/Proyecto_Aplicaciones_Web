package com.proyectoweb.recursos;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Evaluacion;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.ContenidoServicio;
import com.proyectoweb.modelo.servicios.EvaluacionServicio;

/**
 * CU05-B: publicar/actualizar evaluación vía AJAX.
 * Cuerpo esperado: {"elementoId": n, "tipo": "...", "calificacion": 1-5, "resena": "..."}
 */
@Path("/evaluaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecursoEvaluacion {

    private EvaluacionServicio evaluacionServicio = new EvaluacionServicio();
    private ContenidoServicio contenidoServicio = new ContenidoServicio();

    @POST
    public Response publicar(Map<String, Object> datos, @Context HttpServletRequest req) {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        int elementoId = ((Number) datos.getOrDefault("elementoId", 0)).intValue();
        String tipo = datos.get("tipo") != null ? datos.get("tipo").toString() : "";
        int calificacion = ((Number) datos.getOrDefault("calificacion", 0)).intValue();
        String resena = datos.get("resena") != null ? datos.get("resena").toString().trim() : "";

        // CU05 paso 4: calificación seleccionada y reseña no vacía
        ElementoContenido elemento = elementoId > 0 ? contenidoServicio.buscarPorId(elementoId, tipo) : null;
        if (elemento == null || calificacion < 1 || calificacion > 5 || resena.isEmpty()) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("mensaje", "Debe seleccionar una calificación (1 a 5) y escribir una reseña.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        // CU05-B: registrarOActualizar(Evaluacion e): Evaluacion
        Evaluacion evaluacion = evaluacionServicio.registrarOActualizar(
                new Evaluacion(usuario, elemento, calificacion, resena));

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("calificacion", evaluacion.getCalificacion());
        respuesta.put("nuevoPromedio", evaluacion.getElemento().getCalificacionPromedio());
        respuesta.put("mensaje", "Evaluación publicada correctamente.");
        return Response.ok(respuesta).build();
    }
}
