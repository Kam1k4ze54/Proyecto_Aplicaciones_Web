package com.proyectoweb.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.ContenidoServicio;
import com.proyectoweb.modelo.servicios.EvaluacionServicio;
import com.proyectoweb.modelo.servicios.FavoritoServicio;
import com.proyectoweb.modelo.servicios.VistaSeccion;

/**
 * CU04 — Descubrir Contenido (Servlet + JSP para carga inicial y detalle;
 * la búsqueda y el "ver más" son AJAX contra RecursoContenido).
 */
@WebServlet("/DescubrirContenidoController")
public class DescubrirContenidoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int LIMITE_VISTA_PREVIA = 3;

    private ContenidoServicio contenidoServicio = new ContenidoServicio();
    private EvaluacionServicio evaluacionServicio = new EvaluacionServicio();
    private FavoritoServicio favoritoServicio = new FavoritoServicio();

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
        if (req.getSession().getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/IniciarSesionController?ruta=iniciarSesion");
            return;
        }
        String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "inicio";
        switch (ruta) {
            case "inicio":
                this.inicio(req, resp);
                break;
            case "detalle":
                this.detalle(req, resp);
                break;
        }
    }

    // CU04-A pasos 1-2: panel con 3 recomendaciones por sección (o destacados)
    private void inicio(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        Map<String, VistaSeccion> vistaPrevia =
                contenidoServicio.obtenerVistaPrevia(usuario, LIMITE_VISTA_PREVIA);

        req.setAttribute("usuario", usuario);
        req.setAttribute("vistaPrevia", vistaPrevia);
        if (req.getParameter("mensajeExito") != null) {
            req.setAttribute("mensajeExito", req.getParameter("mensajeExito"));
        }
        req.getRequestDispatcher("/jsp/panelUsuario.jsp").forward(req, resp);
    }

    // CU04-A pasos 3-4: GET /descubrir/detalle?id={id}&tipo={tipo}
    private void detalle(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        int id = Integer.parseInt(req.getParameter("id"));
        String tipo = req.getParameter("tipo");

        ElementoContenido elemento = contenidoServicio.buscarPorId(id, tipo);
        if (elemento == null) {
            resp.sendRedirect(req.getContextPath() + "/DescubrirContenidoController?ruta=inicio");
            return;
        }

        req.setAttribute("usuario", usuario);
        req.setAttribute("elemento", elemento);
        req.setAttribute("evaluaciones", evaluacionServicio.listarPorElemento(id));
        // Los eventos no son favoriteables (regla CU06)
        boolean esFavoriteable = !"Evento".equals(elemento.getTipo());
        req.setAttribute("esFavoriteable", esFavoriteable);
        req.setAttribute("estaEnFavoritos", esFavoriteable && favoritoServicio.estaEnFavoritos(usuario, id));
        req.getRequestDispatcher("/jsp/detalle.jsp").forward(req, resp);
    }
}
