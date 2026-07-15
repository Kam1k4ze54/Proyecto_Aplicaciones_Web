package com.proyectoweb.dao;

import com.proyectoweb.modelo.Usuario;

public interface UsuarioDAO {
    boolean existeUsuario(String correo); // CU paso 4 (validación)

    Usuario guardarCuenta(Usuario usuario); // CU paso 5 (devuelve el usuario creado)
}