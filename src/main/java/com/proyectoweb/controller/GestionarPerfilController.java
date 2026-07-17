package com.proyectoweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.dao.CategoriaDAO;
import com.proyectoweb.modelo.dao.CategoriaDAOImpl;
import com.proyectoweb.modelo.dao.PreferenciaUsuarioDAO;
import com.proyectoweb.modelo.dao.PreferenciaUsuarioDAOImpl;
import com.proyectoweb.modelo.dao.UsuarioDAO;
import com.proyectoweb.modelo.dao.UsuarioDAOImpl;
import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.Usuario;

@WebServlet("/GestionarPerfilController")
public class GestionarPerfilController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private PreferenciaUsuarioDAO preferenciaUsuarioDAO = new PreferenciaUsuarioDAOImpl();

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

    // Precondición CU03: usuario con sesión activa en el panel principal
    private void gestionarPerfil(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        Usuario usuario = usuarioDAO.buscarPorId(sesion.getId());
        List<Categoria> preferencias = preferenciaUsuarioDAO.obtenerPreferencias(usuario);

        req.setAttribute("usuario", usuario);
        req.setAttribute("preferencias", preferencias);
        req.getRequestDispatcher("/jsp/perfil.jsp").forward(req, resp);
    }

    // Habilita la edición del perfil junto con el catálogo completo de categorías
    private void editarPerfil(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        Usuario usuario = usuarioDAO.buscarPorId(sesion.getId());
        List<Categoria> preferencias = preferenciaUsuarioDAO.obtenerPreferencias(usuario);
        List<Categoria> categorias = categoriaDAO.obtenerCategorias();

        req.setAttribute("usuario", usuario);
        req.setAttribute("preferencias", preferencias);
        req.setAttribute("categorias", categorias);
        req.setAttribute("preferenciaIds", this.idsDe(preferencias));
        req.setAttribute("modoEdicion", true);
        req.getRequestDispatcher("/jsp/perfil.jsp").forward(req, resp);
    }

    // Ids de las categorías preferidas, para que la vista marque los checkbox correspondientes
    private Set<Integer> idsDe(List<Categoria> categorias) {
        Set<Integer> ids = new HashSet<>();
        for (Categoria categoria : categorias) {
            ids.add(categoria.getId());
        }
        return ids;
    }

    // Valida el correo, actualiza los datos del perfil y sus preferencias
    private void guardarPerfil(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        Usuario usuario = usuarioDAO.buscarPorId(sesion.getId());

        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String correo = req.getParameter("correo");

        if (usuarioDAO.existeCorreo(correo, usuario.getId())) {
            // Correo ya registrado por otro usuario
            List<Categoria> preferencias = preferenciaUsuarioDAO.obtenerPreferencias(usuario);
            List<Categoria> categorias = categoriaDAO.obtenerCategorias();
            req.setAttribute("usuario", usuario);
            req.setAttribute("preferencias", preferencias);
            req.setAttribute("categorias", categorias);
            req.setAttribute("preferenciaIds", this.idsDe(preferencias));
            req.setAttribute("modoEdicion", true);
            req.setAttribute("mensajeCorreoEnUso", "Ese correo ya está en uso por otra cuenta.");
            req.getRequestDispatcher("/jsp/perfil.jsp").forward(req, resp);
            return;
        }

        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);
        usuarioDAO.actualizar(usuario);

        String[] seleccionParam = req.getParameterValues("preferencias");
        List<Integer> seleccion = new ArrayList<>();
        if (seleccionParam != null) {
            for (String id : seleccionParam) {
                seleccion.add(Integer.parseInt(id));
            }
        }
        preferenciaUsuarioDAO.actualizarPreferencias(usuario, seleccion);

        req.getSession().setAttribute("usuario", usuario);
        req.setAttribute("mensajeExito", "Perfil actualizado correctamente.");
        req.getRequestDispatcher("/jsp/panelUsuario.jsp").forward(req, resp);
    }
}
