package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.dao.UsuarioDAO;
import com.proyectoweb.modelo.dao.UsuarioDAOImpl;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.excepciones.CorreoDuplicadoException;
import com.proyectoweb.modelo.servicios.excepciones.CuentaInhabilitadaException;

/**
 * Lógica de negocio de cuentas de usuario (CU01, CU02, CU03, CU08).
 */
public class UsuarioServicio {

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    // CU01 paso 4: valida el correo no duplicado y registra la cuenta
    public Usuario registrarCuenta(Usuario usuario) {
        if (usuarioDAO.existeUsuario(usuario.getCorreo())) {
            throw new CorreoDuplicadoException("Ya existe un usuario con ese correo.");
        }
        usuario.setRol("usuario");
        usuario.setEstado(true);
        return usuarioDAO.guardarCuenta(usuario);
    }

    // CU02 pasos 4-5: autentica, verifica el estado y resuelve el rol
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = usuarioDAO.autenticar(correo, contrasena);
        if (usuario == null) {
            return null; // credenciales incorrectas
        }
        if (!usuario.isEstado()) {
            throw new CuentaInhabilitadaException("Su cuenta se encuentra inhabilitada. Contacte al administrador.");
        }
        return usuario;
    }

    // CU03: carga el perfil
    public Usuario buscarPorId(int idUsuario) {
        return usuarioDAO.buscarPorId(idUsuario);
    }

    // CU03 paso 5: valida el correo y actualiza los datos personales
    public boolean actualizarPerfil(Usuario usuario) {
        if (usuarioDAO.existeCorreo(usuario.getCorreo(), usuario.getId())) {
            throw new CorreoDuplicadoException("Ese correo ya está en uso por otra cuenta.");
        }
        return usuarioDAO.actualizar(usuario);
    }

    // CU08 pasos 1-2: listado de cuentas
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    // CU08: cambia el estado con validación de idempotencia
    // (devuelve false si la cuenta ya estaba en ese estado)
    public boolean cambiarEstado(int id, boolean nuevoEstado) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null || usuario.isEstado() == nuevoEstado) {
            return false;
        }
        return usuarioDAO.actualizarEstado(id, nuevoEstado);
    }
}
