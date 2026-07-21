package com.proyectoweb.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Evaluacion;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.ContenidoServicio;
import com.proyectoweb.modelo.servicios.EvaluacionServicio;

/**
 * CU05-A — Abrir formulario de evaluación (Servlet + JSP).
 * La publicación es AJAX contra RecursoEvaluacion (CU05-B).
 */
@WebServlet("/EvaluarContenidoController")
public class EvaluarContenidoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EvaluacionServicio evaluacionServicio = new EvaluacionServicio();
    private ContenidoServicio contenidoServicio = new ContenidoServicio();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/IniciarSesionController?ruta=iniciarSesion");
            return;
        }

        // CU05-A: GET /evaluar?elementoId={id}&tipo={tipo}
        int elementoId = Integer.parseInt(req.getParameter("elementoId"));
        String tipo = req.getParameter("tipo");
        ElementoContenido elemento = contenidoServicio.buscarPorId(elementoId, tipo);
        if (elemento == null) {
            resp.sendRedirect(req.getContextPath() + "/DescubrirContenidoController?ruta=inicio");
            return;
        }

        // Alterno "Evaluación previa": el formulario llega precargado y editable
        Evaluacion evaluacionPrevia = evaluacionServicio.obtenerEvaluacionPrevia(usuario, elementoId);

        req.setAttribute("usuario", usuario);
        req.setAttribute("elemento", elemento);
        req.setAttribute("evaluacion", evaluacionPrevia);
        req.getRequestDispatcher("/jsp/formularioEvaluacion.jsp").forward(req, resp);
    }
}
