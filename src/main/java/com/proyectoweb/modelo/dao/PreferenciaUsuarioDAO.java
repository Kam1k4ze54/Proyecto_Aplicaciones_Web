package com.proyectoweb.modelo.dao;

import java.util.List;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.Usuario;

public interface PreferenciaUsuarioDAO {
    boolean guardarPreferencias(Usuario usuario, List<Integer> seleccion); // CU01 paso 9

    List<Categoria> obtenerPreferencias(Usuario usuario); // CU03 (preferencias actuales del usuario)

    List<Categoria> obtenerCategoriasPreferidas(Usuario usuario); // CU04-A (recomendaciones del panel)

    boolean actualizarPreferencias(Usuario usuario, List<Integer> seleccion); // CU03 (reemplaza las preferencias)
}
