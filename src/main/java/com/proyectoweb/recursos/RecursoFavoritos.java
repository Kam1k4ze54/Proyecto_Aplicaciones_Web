package com.proyectoweb.recursos;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.FavoritoServicio;
import com.proyectoweb.modelo.servicios.excepciones.FavoritoDuplicadoException;
import com.proyectoweb.modelo.servicios.excepciones.TipoNoFavoriteableException;

/**
 * CU06-A (agregar) y CU06-C (eliminar) vía AJAX.
 */
@Path("/favoritos")
@Produces(MediaType.APPLICATION_JSON)
public class RecursoFavoritos {

    private FavoritoServicio favoritoServicio = new FavoritoServicio();

    // POST /api/favoritos  {"elementoId": n, "tipo": "LugarTuristico"}
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response agregar(Map<String, Object> datos, @Context HttpServletRequest req) {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        int elementoId = ((Number) datos.getOrDefault("elementoId", 0)).intValue();
        String tipo = datos.get("tipo") != null ? datos.get("tipo").toString() : "";

        try {
            favoritoServicio.agregar(usuario, elementoId, tipo);
            return Response.status(Response.Status.CREATED)
                    .entity(mensaje("Agregado a favoritos."))
                    .build();
        } catch (TipoNoFavoriteableException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(mensaje(e.getMessage())).build();
        } catch (FavoritoDuplicadoException e) {
            return Response.status(Response.Status.CONFLICT).entity(mensaje(e.getMessage())).build();
        }
    }

    // DELETE /api/favoritos/{elementoId}
    @DELETE
    @Path("/{elementoId}")
    public Response eliminar(@PathParam("elementoId") int elementoId, @Context HttpServletRequest req) {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        favoritoServicio.eliminar(usuario, elementoId);
        return Response.noContent().build();
    }

    private Map<String, Object> mensaje(String texto) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("mensaje", texto);
        return cuerpo;
    }
}
