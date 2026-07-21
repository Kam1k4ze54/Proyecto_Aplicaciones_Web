package com.proyectoweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * CU03 — Gestionar Perfil (Servlet + JSP).
 */
@WebServlet("/GestionarPerfilController")
public class GestionarPerfilController extends HttpServlet {
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
        if (req.getSession().getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/IniciarSesionController?ruta=iniciarSesion");
            return;
        }
        String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "gestionarPerfil";
        switch (ruta) {
            case "gestionarPerfil":
                this.gestionarPerfil(req, resp);
                break;
            case "editarPerfil":
                this.editarPerfil(req, resp);
                break;
            case "guardarPerfil":
                this.guardarPerfil(req, resp);
                break;
        }
    }

    // CU03 pasos 1-2: presenta datos personales y preferencias actuales
    private void gestionarPerfil(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        Usuario usuario = usuarioServicio.buscarPorId(sesion.getId());
        List<Categoria> preferencias = preferenciaUsuarioServicio.obtenerPreferencias(usuario);

        req.setAttribute("usuario", usuario);
        req.setAttribute("preferencias", preferencias);
        req.getRequestDispatcher("/jsp/perfil.jsp").forward(req, resp);
    }

    // Habilita la edición con el catálogo completo de categorías
    private void editarPerfil(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        Usuario usuario = usuarioServicio.buscarPorId(sesion.getId());
        List<Categoria> preferencias = preferenciaUsuarioServicio.obtenerPreferencias(usuario);
        List<Categoria> categorias = categoriaServicio.obtenerCategorias();

        req.setAttribute("usuario", usuario);
        req.setAttribute("preferencias", preferencias);
        req.setAttribute("categorias", categorias);
        req.setAttribute("preferenciaIds", this.idsDe(preferencias));
        req.setAttribute("modoEdicion", true);
        req.getRequestDispatcher("/jsp/perfil.jsp").forward(req, resp);
    }

    // Ids de las categorías preferidas, para marcar los checkbox de la vista
    private Set<Integer> idsDe(List<Categoria> categorias) {
        Set<Integer> ids = new HashSet<>();
        for (Categoria categoria : categorias) {
            ids.add(categoria.getId());
        }
        return ids;
    }

    // CU03 pasos 3-6: valida y guarda los cambios de datos y preferencias
    private void guardarPerfil(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        Usuario usuario = usuarioServicio.buscarPorId(sesion.getId());

        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String correo = req.getParameter("correo");

        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);

        try {
            usuarioServicio.actualizarPerfil(usuario);
        } catch (CorreoDuplicadoException e) {
            // Curso alterno: correo ya en uso por otra cuenta
            Usuario original = usuarioServicio.buscarPorId(sesion.getId());
            List<Categoria> preferencias = preferenciaUsuarioServicio.obtenerPreferencias(original);
            req.setAttribute("usuario", original);
            req.setAttribute("preferencias", preferencias);
            req.setAttribute("categorias", categoriaServicio.obtenerCategorias());
            req.setAttribute("preferenciaIds", this.idsDe(preferencias));
            req.setAttribute("modoEdicion", true);
            req.setAttribute("mensajeCorreoEnUso", e.getMessage());
            req.getRequestDispatcher("/jsp/perfil.jsp").forward(req, resp);
            return;
        }

        String[] seleccionParam = req.getParameterValues("preferencias");
        List<Integer> seleccion = new ArrayList<>();
        if (seleccionParam != null) {
            for (String id : seleccionParam) {
                seleccion.add(Integer.parseInt(id));
            }
        }
        preferenciaUsuarioServicio.actualizarPreferencias(usuario, seleccion);

        req.getSession().setAttribute("usuario", usuario);
        resp.sendRedirect(req.getContextPath()
                + "/DescubrirContenidoController?ruta=inicio&mensajeExito=Perfil+actualizado+correctamente.");
    }
}
