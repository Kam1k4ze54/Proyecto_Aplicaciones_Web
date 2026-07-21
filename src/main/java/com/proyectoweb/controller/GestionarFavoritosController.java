package com.proyectoweb.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.FavoritoServicio;

/**
 * CU06-B — Consultar lista de favoritos (Servlet + JSP).
 * Agregar y eliminar son AJAX contra RecursoFavoritos.
 */
@WebServlet("/GestionarFavoritosController")
public class GestionarFavoritosController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FavoritoServicio favoritoServicio = new FavoritoServicio();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/IniciarSesionController?ruta=iniciarSesion");
            return;
        }

        List<ElementoContenido> favoritos = favoritoServicio.listarFavoritos(usuario);
        req.setAttribute("usuario", usuario);
        req.setAttribute("favoritos", favoritos);
        // Curso alterno "Lista vacía": la vista muestra el mensaje e invita a explorar
        req.getRequestDispatcher("/jsp/listaFavoritos.jsp").forward(req, resp);
    }
}
