package com.proyectoweb.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.UsuarioServicio;

/**
 * CU08 — Gestionar Usuarios (Servlet + JSP): listado y habilitar/inhabilitar.
 */
@WebServlet("/GestionarUsuariosController")
public class GestionarUsuariosController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioServicio usuarioServicio = new UsuarioServicio();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        if (sesion == null || !"administrador".equals(sesion.getRol())) {
            resp.sendRedirect(req.getContextPath() + "/IniciarSesionController?ruta=iniciarSesion");
            return;
        }
        String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "listar";
        switch (ruta) {
            case "listar":
                this.listar(req, resp, null, null);
                break;
            case "inhabilitar":
                this.cambiarEstado(req, resp, false);
                break;
            case "habilitar":
                this.cambiarEstado(req, resp, true);
                break;
        }
    }

    // CU08 pasos 1-2: listado de cuentas con estado y acciones
    private void listar(HttpServletRequest req, HttpServletResponse resp,
            String mensajeExito, String mensajeAviso) throws ServletException, IOException {
        req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
        req.setAttribute("usuarios", usuarioServicio.listarTodos());
        if (mensajeExito != null) {
            req.setAttribute("mensajeExito", mensajeExito);
        } else if (req.getParameter("mensajeExito") != null) {
            req.setAttribute("mensajeExito", req.getParameter("mensajeExito"));
        }
        if (mensajeAviso != null) {
            req.setAttribute("mensajeAviso", mensajeAviso);
        }
        req.getRequestDispatcher("/jsp/listaUsuariosAdmin.jsp").forward(req, resp);
    }

    // CU08 cursos alternos: habilitar/inhabilitar con validación de idempotencia
    private void cambiarEstado(HttpServletRequest req, HttpServletResponse resp, boolean nuevoEstado)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean cambiado = usuarioServicio.cambiarEstado(id, nuevoEstado);
        if (!cambiado) {
            // 1.1: la cuenta ya estaba en ese estado
            this.listar(req, resp, null, "La cuenta ya se encuentra "
                    + (nuevoEstado ? "habilitada." : "inhabilitada."));
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/GestionarUsuariosController?ruta=listar&mensajeExito="
                + (nuevoEstado ? "Cuenta+habilitada+correctamente." : "Cuenta+inhabilitada+correctamente."));
    }
}
