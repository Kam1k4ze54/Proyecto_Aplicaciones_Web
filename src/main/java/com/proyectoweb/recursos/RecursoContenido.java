package com.proyectoweb.recursos;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.EstablecimientoGastronomico;
import com.proyectoweb.modelo.entities.Evento;
import com.proyectoweb.modelo.entities.LugarTuristico;
import com.proyectoweb.modelo.servicios.ContenidoServicio;

/**
 * CU04-B (búsqueda por nombre) y CU04-C (ver más) vía AJAX.
 */
@Path("/contenido")
@Produces(MediaType.APPLICATION_JSON)
public class RecursoContenido {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ContenidoServicio contenidoServicio = new ContenidoServicio();

    // CU04-B: GET /api/contenido/buscar?termino={termino}
    @GET
    @Path("/buscar")
    public Response buscar(@QueryParam("termino") String termino) {
        if (termino == null || termino.isBlank()) {
            return Response.ok(new ArrayList<>()).build();
        }
        List<ElementoContenido> resultados = contenidoServicio.buscarPorNombre(termino.trim());
        return Response.ok(aJson(resultados)).build();
    }

    // CU04-C: GET /api/contenido/tipo/{tipo}
    @GET
    @Path("/tipo/{tipo}")
    public Response listarPorTipo(@PathParam("tipo") String tipo) {
        List<ElementoContenido> elementos = contenidoServicio.listarTodosPorTipo(tipo);
        return Response.ok(aJson(elementos)).build();
    }

    // DTOs planos para el JSON de respuesta (evita ciclos de la entidad)
    static List<Map<String, Object>> aJson(List<ElementoContenido> elementos) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (ElementoContenido e : elementos) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", e.getId());
            dto.put("tipo", e.getTipo());
            dto.put("nombre", e.getNombre());
            dto.put("descripcion", e.getDescripcion());
            dto.put("sector", e.getSector());
            dto.put("calificacionPromedio", e.getCalificacionPromedio());
            dto.put("destacado", e.isDestacado());
            dto.put("urlImagen", e.getUrlImagen());
            dto.put("categoria", e.getCategoria() != null ? e.getCategoria().getNombre() : null);
            if (e instanceof LugarTuristico) {
                dto.put("horario", ((LugarTuristico) e).getHorario());
            } else if (e instanceof EstablecimientoGastronomico) {
                EstablecimientoGastronomico est = (EstablecimientoGastronomico) e;
                dto.put("especialidad", est.getEspecialidad());
                dto.put("horario", est.getHorario());
            } else if (e instanceof Evento) {
                Evento ev = (Evento) e;
                dto.put("fechaInicio", ev.getFechaInicio() != null ? FORMATO_FECHA.format(ev.getFechaInicio()) : null);
                dto.put("fechaFin", ev.getFechaFin() != null ? FORMATO_FECHA.format(ev.getFechaFin()) : null);
            }
            lista.add(dto);
        }
        return lista;
    }
}
