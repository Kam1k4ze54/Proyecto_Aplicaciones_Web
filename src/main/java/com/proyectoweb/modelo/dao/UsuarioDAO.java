package com.proyectoweb.modelo.dao;

import java.util.List;

import com.proyectoweb.modelo.entities.Usuario;

public interface UsuarioDAO {
    boolean existeUsuario(String correo); // CU01 paso 4 (validación)

    Usuario guardarCuenta(Usuario usuario); // CU01 paso 5 (devuelve el usuario creado)

    Usuario autenticar(String correo, String contrasena); // CU02 (verifica credenciales)

    Usuario buscarPorId(int id); // CU03 (carga el perfil del usuario)

    boolean existeCorreo(String correo, int id); // CU03 (correo duplicado, excluyendo al propio usuario)

    boolean actualizar(Usuario usuario); // CU03 (guarda los cambios del perfil)

    List<Usuario> listarTodos(); // CU08 (listado de cuentas para el admin)

    boolean actualizarEstado(int id, boolean nuevoEstado); // CU08 (habilitar/inhabilitar)
}
