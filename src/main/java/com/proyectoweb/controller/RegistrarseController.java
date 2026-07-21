package com.proyectoweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.CategoriaServicio;
import com.proyectoweb.modelo.servicios.PreferenciaUsuarioServicio;
import com.proyectoweb.modelo.servicios.UsuarioServicio;
import com.proyectoweb.modelo.servicios.excepciones.CorreoDuplicadoException;

/**
 * CU01 — Registrarse (Servlet + JSP).
 */
@WebServlet("/RegistrarseController")
public class RegistrarseController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioServicio usuarioServicio = new UsuarioServicio();
    private CategoriaServicio categoriaServicio = new CategoriaServicio();
    private PreferenciaUsuarioServicio preferenciaUsuarioServicio = new PreferenciaUsuarioServicio();

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
        String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "registrarse";
        switch (ruta) {
            case "registrarse":
                this.registrarse(req, resp);
                break;
            case "guardarRegistro":
                this.guardarRegistro(req, resp);
                break;
            case "guardarPreferencias":
                this.guardarPreferencias(req, resp);
                break;
        }
    }

    // CU01 pasos 1-2: presenta el formulario de registro
    private void registrarse(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/formularioRegistro.jsp").forward(req, resp);
    }

    // CU01 pasos 3-4: valida, registra la cuenta y presenta las categorías
    private void guardarRegistro(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        // Curso alterno: campos obligatorios vacíos
        if (nombres == null || nombres.isBlank() || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            req.setAttribute("mensajeError", "Debe completar todos los campos obligatorios.");
            req.getRequestDispatcher("/jsp/formularioRegistro.jsp").forward(req, resp);
            return;
        }

        Usuario usuario;
        try {
            usuario = usuarioServicio.registrarCuenta(new Usuario(nombres, apellidos, correo, contrasena));
        } catch (CorreoDuplicadoException e) {
            // Curso alterno: correo ya registrado
            req.setAttribute("mensajeError", e.getMessage());
            req.getRequestDispatcher("/jsp/formularioRegistro.jsp").forward(req, resp);
            return;
        }

        // El usuario se conserva en sesión para asociarlo a las preferencias
        req.getSession().setAttribute("usuarioRegistrado", usuario);

        List<Categoria> categorias = categoriaServicio.obtenerCategorias();
        req.setAttribute("categorias", categorias);
        req.getRequestDispatcher("/jsp/seleccionPreferencias.jsp").forward(req, resp);
    }

    // CU01 pasos 5-6: registra las preferencias seleccionadas y notifica
    private void guardarPreferencias(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioRegistrado");

        String[] seleccionParam = req.getParameterValues("preferencias");
        List<Integer> seleccion = new ArrayList<>();
        if (seleccionParam != null) {
            for (String id : seleccionParam) {
                seleccion.add(Integer.parseInt(id));
            }
        }

        // Curso alterno: ninguna preferencia seleccionada
        if (seleccion.isEmpty()) {
            req.setAttribute("mensajeError", "Debe seleccionar al menos una preferencia.");
            req.setAttribute("categorias", categoriaServicio.obtenerCategorias());
            req.getRequestDispatcher("/jsp/seleccionPreferencias.jsp").forward(req, resp);
            return;
        }

        preferenciaUsuarioServicio.guardarPreferencias(usuario, seleccion);

        req.getSession().removeAttribute("usuarioRegistrado");
        req.setAttribute("mensajeExito", "Registro exitoso. Ya puede iniciar sesión.");
        req.getRequestDispatcher("/jsp/inicioSesion.jsp").forward(req, resp);
    }
}
