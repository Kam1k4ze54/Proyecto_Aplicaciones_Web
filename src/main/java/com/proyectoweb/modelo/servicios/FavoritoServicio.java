package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.dao.FavoritoDAO;
import com.proyectoweb.modelo.dao.FavoritoDAOImpl;
import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.excepciones.FavoritoDuplicadoException;
import com.proyectoweb.modelo.servicios.excepciones.TipoNoFavoriteableException;

/**
 * Lógica de negocio de favoritos (CU06).
 * Regla: los eventos no son favoriteables.
 */
public class FavoritoServicio {

    private FavoritoDAO favoritoDAO = new FavoritoDAOImpl();

    // CU06-A: agrega con validación de tipo y de duplicado
    public boolean agregar(Usuario usuario, int elementoId, String tipo) {
        if ("Evento".equals(tipo)) {
            throw new TipoNoFavoriteableException("Los eventos no se pueden agregar a favoritos.");
        }
        if (favoritoDAO.existe(usuario.getId(), elementoId)) {
            throw new FavoritoDuplicadoException("El elemento ya se encuentra en favoritos.");
        }
        return favoritoDAO.guardar(usuario.getId(), elementoId);
    }

    // CU06-C
    public boolean eliminar(Usuario usuario, int elementoId) {
        return favoritoDAO.eliminar(usuario.getId(), elementoId);
    }

    // CU06-B
    public List<ElementoContenido> listarFavoritos(Usuario usuario) {
        return favoritoDAO.listarPorUsuario(usuario.getId());
    }

    // Detalle: estado del botón de favorito
    public boolean estaEnFavoritos(Usuario usuario, int elementoId) {
        return favoritoDAO.existe(usuario.getId(), elementoId);
    }
}
