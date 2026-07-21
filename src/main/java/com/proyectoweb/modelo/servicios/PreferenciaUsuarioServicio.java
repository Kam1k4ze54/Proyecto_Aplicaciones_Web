package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.dao.PreferenciaUsuarioDAO;
import com.proyectoweb.modelo.dao.PreferenciaUsuarioDAOImpl;
import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.Usuario;

/**
 * Preferencias turísticas y gastronómicas del usuario (CU01, CU03, CU04).
 */
public class PreferenciaUsuarioServicio {

    private PreferenciaUsuarioDAO preferenciaUsuarioDAO = new PreferenciaUsuarioDAOImpl();

    // CU01 paso 6
    public boolean guardarPreferencias(Usuario usuario, List<Integer> ids) {
        return preferenciaUsuarioDAO.guardarPreferencias(usuario, ids);
    }

    // CU03 / CU04: categorías preferidas del usuario
    public List<Categoria> obtenerPreferencias(Usuario usuario) {
        return preferenciaUsuarioDAO.obtenerPreferencias(usuario);
    }

    // CU03: reemplaza la selección
    public boolean actualizarPreferencias(Usuario usuario, List<Integer> ids) {
        return preferenciaUsuarioDAO.actualizarPreferencias(usuario, ids);
    }
}
