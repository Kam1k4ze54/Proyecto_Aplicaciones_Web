package com.proyectoweb.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.dao.UsuarioDAO;
import com.proyectoweb.modelo.dao.UsuarioDAOImpl;
import com.proyectoweb.modelo.entities.Usuario;

@WebServlet("/IniciarSesionController")
public class IniciarSesionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

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
        }
    }

    // CU02 pasos 1-2: presenta el formulario de inicio de sesión
    private void iniciarSesion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
    }

    // CU02 pasos 3-7: valida credenciales, estado de cuenta y enruta según el rol
    private void ingresar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        Usuario usuario = usuarioDAO.autenticar(correo, contrasena);

        if (usuario == null) {
            // Credenciales incorrectas
            req.setAttribute("mensajeError", "Correo o contraseña incorrectos.");
            req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
            return;
        }

        if (!usuario.isEstado()) {
            // Cuenta inhabilitada
            req.setAttribute("mensajeError", "Su cuenta se encuentra inhabilitada.");
            req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
            return;
        }

        String rol = usuarioDAO.obtenerRol(usuario);
        req.getSession().setAttribute("usuario", usuario);

        if ("administrador".equals(rol)) {
            req.setAttribute("usuario", usuario);
            req.getRequestDispatcher("/jsp/panelAdmin.jsp").forward(req, resp);
        } else {
            req.setAttribute("usuario", usuario);
            req.getRequestDispatcher("/jsp/panelUsuario.jsp").forward(req, resp);
        }
    }
}
