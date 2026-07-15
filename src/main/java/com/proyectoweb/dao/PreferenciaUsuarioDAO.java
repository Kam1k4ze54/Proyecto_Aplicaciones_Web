package com.proyectoweb.dao;

import java.util.List;

import com.proyectoweb.modelo.Usuario;

public interface PreferenciaUsuarioDAO {
    boolean guardarPreferencias(Usuario usuario, List<Integer> seleccion); // CU paso 9
}