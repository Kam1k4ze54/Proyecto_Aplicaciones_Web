package com.proyectoweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.proyectoweb.dao.PreferenciaDAO;
import com.proyectoweb.dao.PreferenciaDAOImpl;
import com.proyectoweb.dao.PreferenciaUsuarioDAO;
import com.proyectoweb.dao.PreferenciaUsuarioDAOImpl;
import com.proyectoweb.dao.UsuarioDAO;
import com.proyectoweb.dao.UsuarioDAOImpl;
import com.proyectoweb.modelo.Preferencia;
import com.proyectoweb.modelo.Usuario;

@WebServlet("/RegistrarseController")
public class RegistrarseController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private PreferenciaDAO preferenciaDAO = new PreferenciaDAOImpl();
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

    // CU pasos 1-2: presenta el formulario en blanco (el control no necesita datos)
    private void registrarse(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/vistas/formularioRegistro.jsp").forward(req, resp);
    }

    // CU pasos 3-7: valida, registra la cuenta, obtiene preferencias y las presenta
    private void guardarRegistro(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        // CU 4: validación (campos completos y correo no duplicado)
        if (nombres == null || nombres.isBlank() || apellidos == null || apellidos.isBlank()
                || correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            // CU 4.2: campos obligatorios vacíos
            req.setAttribute("mensajeError", "Debe completar todos los campos obligatorios.");
            req.getRequestDispatcher("/WEB-INF/vistas/formularioRegistro.jsp").forward(req, resp);
            return;
        }
        if (usuarioDAO.existeUsuario(correo)) {
            // CU 4.1/4.2: correo ya registrado
            req.setAttribute("mensajeError", "Ya existe un usuario con ese correo.");
            req.getRequestDispatcher("/WEB-INF/vistas/formularioRegistro.jsp").forward(req, resp);
            return;
        }

        // CU 5: registra la cuenta y recibe el usuario creado (con su id)
        Usuario usuario = usuarioDAO.guardarCuenta(new Usuario(0, nombres, apellidos, correo, contrasena));

        // El usuario se conserva en sesión para asociarlo a las preferencias (CU paso
        // 9)
        req.getSession().setAttribute("usuarioRegistrado", usuario);

        // CU 6: obtiene el catálogo de preferencias
        List<Preferencia> preferencias = preferenciaDAO.obtener();
        req.setAttribute("preferencias", preferencias);

        // CU 7: presenta las categorías
        req.getRequestDispatcher("/WEB-INF/vistas/seleccionPreferencias.jsp").forward(req, resp);
    }

    // CU pasos 8-10: registra las preferencias seleccionadas y notifica
    private void guardarPreferencias(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Recupera el usuario creado en el paso anterior (fuente: sesión)
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioRegistrado");

        // CU 8: preferencias seleccionadas por el usuario
        String[] seleccionParam = req.getParameterValues("preferencias");
        List<Integer> seleccion = new ArrayList<>();
        if (seleccionParam != null) {
            for (String id : seleccionParam) {
                seleccion.add(Integer.parseInt(id));
            }
        }

        // CU 9: registra las preferencias del usuario
        preferenciaUsuarioDAO.guardarPreferencias(usuario, seleccion);

        // CU 10: notifica el registro exitoso
        req.getSession().removeAttribute("usuarioRegistrado");
        req.setAttribute("mensajeExito", "Registro exitoso. Ya puede iniciar sesión.");
        req.getRequestDispatcher("/WEB-INF/vistas/inicioSesion.jsp").forward(req, resp);
    }
}
