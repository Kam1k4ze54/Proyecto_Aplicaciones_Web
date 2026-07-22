package com.proyectoweb.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.UsuarioServicio;
import com.proyectoweb.modelo.servicios.excepciones.CuentaInhabilitadaException;

/**
 * CU02 — Iniciar Sesión (Servlet + JSP): autentica y redirige según el rol.
 */
@WebServlet("/IniciarSesionController")
public class IniciarSesionController extends HttpServlet {
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
        String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "iniciarSesion";
        switch (ruta) {
            case "iniciarSesion":
                this.iniciarSesion(req, resp);
                break;
            case "ingresar":
                this.ingresar(req, resp);
                break;
            case "cerrarSesion":
                this.cerrarSesion(req, resp);
                break;
        }
    }

    // CU02 pasos 1-2: presenta el formulario de inicio de sesión
    private void iniciarSesion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
    }

    // CU02 pasos 3-6: autentica, verifica el estado y enruta según el rol
    private void ingresar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        Usuario usuario;
        try {
            usuario = usuarioServicio.autenticar(correo, contrasena);
        } catch (CuentaInhabilitadaException e) {
            // Curso alterno b: cuenta inhabilitada
            req.setAttribute("mensajeError", e.getMessage());
            req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
            return;
        }

        if (usuario == null) {
            // Curso alterno a: credenciales incorrectas
            req.setAttribute("mensajeError", "Correo o contraseña incorrectos.");
            req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
            return;
        }

        req.getSession().setAttribute("usuario", usuario);

        if ("administrador".equals(usuario.getRol())) {
            resp.sendRedirect(req.getContextPath() + "/GestionarContenidoController?ruta=panel");
        } else {
            resp.sendRedirect(req.getContextPath() + "/DescubrirContenidoController?ruta=descubrir");
        }
    }

    // Cierra la sesión activa y regresa al inicio de sesión
    private void cerrarSesion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession().invalidate();
        req.setAttribute("mensajeExito", "Sesión cerrada correctamente.");
        req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
    }
}
