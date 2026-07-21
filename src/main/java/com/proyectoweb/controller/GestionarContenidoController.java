package com.proyectoweb.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.EstablecimientoGastronomico;
import com.proyectoweb.modelo.entities.Evento;
import com.proyectoweb.modelo.entities.LugarTuristico;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.CategoriaServicio;
import com.proyectoweb.modelo.servicios.ContenidoServicio;
import com.proyectoweb.modelo.servicios.DependenciasInfo;
import com.proyectoweb.modelo.servicios.excepciones.ValidacionException;

/**
 * CU07 — Gestionar Contenido (CRUD polimórfico del administrador, Servlet + JSP).
 */
@WebServlet("/GestionarContenidoController")
public class GestionarContenidoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ContenidoServicio contenidoServicio = new ContenidoServicio();
    private CategoriaServicio categoriaServicio = new CategoriaServicio();

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
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        if (usuario == null || !"administrador".equals(usuario.getRol())) {
            resp.sendRedirect(req.getContextPath() + "/IniciarSesionController?ruta=iniciarSesion");
            return;
        }
        String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "panel";
        switch (ruta) {
            case "panel":
                this.panel(req, resp);
                break;
            case "listar":
                this.listar(req, resp);
                break;
            case "nuevo":
                this.nuevo(req, resp);
                break;
            case "guardar":
                this.guardar(req, resp);
                break;
            case "editar":
                this.editar(req, resp);
                break;
            case "actualizar":
                this.actualizar(req, resp);
                break;
            case "eliminar":
                this.eliminar(req, resp);
                break;
            case "eliminarConfirmar":
                this.eliminarConfirmar(req, resp);
                break;
            case "eliminarCancelar":
                this.eliminarCancelar(req, resp);
                break;
            case "destacar":
                this.destacar(req, resp);
                break;
        }
    }

    // Panel principal del administrador
    private void panel(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
        req.getRequestDispatcher("/jsp/panelAdmin.jsp").forward(req, resp);
    }

    // CU07-A pasos 1-2: listado del tipo seleccionado
    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
        req.setAttribute("tipo", tipo);
        req.setAttribute("elementos", contenidoServicio.listarTodosPorTipoAdmin(tipo));
        if (req.getParameter("mensajeExito") != null) {
            req.setAttribute("mensajeExito", req.getParameter("mensajeExito"));
        }
        if (req.getParameter("mensajeCancelado") != null) {
            req.setAttribute("mensajeCancelado", req.getParameter("mensajeCancelado"));
        }
        req.getRequestDispatcher("/jsp/listaContenidoAdmin.jsp").forward(req, resp);
    }

    // CU07-B pasos 1-2: formulario de creación con categorías del tipo
    private void nuevo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
        req.setAttribute("tipo", tipo);
        req.setAttribute("categorias", categoriaServicio.obtenerCategoriasPorTipo(tipo));
        req.getRequestDispatcher("/jsp/formularioContenido.jsp").forward(req, resp);
    }

    // CU07-B pasos 3-5: valida y guarda el elemento nuevo
    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        ElementoContenido elemento = construirDesdeFormulario(req, tipo, null);
        try {
            contenidoServicio.crear(elemento);
        } catch (ValidacionException e) {
            // CU07 paso 4.1: regresa al formulario con los datos y errores
            req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
            req.setAttribute("tipo", tipo);
            req.setAttribute("elemento", elemento);
            req.setAttribute("listaErrores", e.getErrores());
            req.setAttribute("categorias", categoriaServicio.obtenerCategoriasPorTipo(tipo));
            req.getRequestDispatcher("/jsp/formularioContenido.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/GestionarContenidoController?ruta=listar&tipo=" + tipo
                + "&mensajeExito=Elemento+creado+correctamente.");
    }

    // CU07-C pasos 1-2: formulario precargado
    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
        req.setAttribute("tipo", tipo);
        req.setAttribute("elemento", contenidoServicio.buscarPorId(id));
        req.setAttribute("categorias", categoriaServicio.obtenerCategoriasPorTipo(tipo));
        req.getRequestDispatcher("/jsp/formularioContenido.jsp").forward(req, resp);
    }

    // CU07-C pasos 3-5: valida y actualiza
    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        int id = Integer.parseInt(req.getParameter("id"));
        ElementoContenido elemento = construirDesdeFormulario(req, tipo, contenidoServicio.buscarPorId(id));
        try {
            contenidoServicio.actualizar(elemento);
        } catch (ValidacionException e) {
            // CU07 paso 4.1: regresa al formulario con los datos editados y errores
            req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
            req.setAttribute("tipo", tipo);
            req.setAttribute("elemento", elemento);
            req.setAttribute("listaErrores", e.getErrores());
            req.setAttribute("categorias", categoriaServicio.obtenerCategoriasPorTipo(tipo));
            req.getRequestDispatcher("/jsp/formularioContenido.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/GestionarContenidoController?ruta=listar&tipo=" + tipo
                + "&mensajeExito=Elemento+actualizado+correctamente.");
    }

    // CU07-D pasos 1-2: evalúa dependencias y solicita confirmación
    private void eliminar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        int id = Integer.parseInt(req.getParameter("id"));
        DependenciasInfo dependencias = contenidoServicio.verificarDependencias(id);

        req.setAttribute("usuario", req.getSession().getAttribute("usuario"));
        req.setAttribute("tipo", tipo);
        req.setAttribute("elemento", contenidoServicio.buscarPorId(id));
        req.setAttribute("dependencias", dependencias);
        req.getRequestDispatcher("/jsp/confirmarEliminacion.jsp").forward(req, resp);
    }

    // CU07-D 3.A: el administrador confirma la eliminación
    private void eliminarConfirmar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        int id = Integer.parseInt(req.getParameter("id"));
        contenidoServicio.eliminar(id);
        resp.sendRedirect(req.getContextPath() + "/GestionarContenidoController?ruta=listar&tipo=" + tipo
                + "&mensajeExito=Elemento+eliminado+correctamente.");
    }

    // CU07-D 3.B: el administrador cancela — no se toca el catálogo
    private void eliminarCancelar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        resp.sendRedirect(req.getContextPath() + "/GestionarContenidoController?ruta=listar&tipo=" + tipo
                + "&mensajeCancelado=Eliminaci%C3%B3n+cancelada.+El+elemento+no+fue+modificado.");
    }

    // CU07-E: toggle destacado
    private void destacar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = tipoDe(req);
        int id = Integer.parseInt(req.getParameter("id"));
        contenidoServicio.toggleDestacado(id);
        resp.sendRedirect(req.getContextPath() + "/GestionarContenidoController?ruta=listar&tipo=" + tipo);
    }

    private String tipoDe(HttpServletRequest req) {
        String tipo = req.getParameter("tipo");
        return (tipo != null && !tipo.isBlank()) ? tipo : "LugarTuristico";
    }

    /**
     * Construye la subclase adecuada a partir del formulario. Si se pasa un
     * elemento existente (edición) se actualizan sus atributos.
     */
    private ElementoContenido construirDesdeFormulario(HttpServletRequest req, String tipo,
            ElementoContenido existente) {
        ElementoContenido elemento;
        if (existente != null) {
            elemento = existente;
        } else {
            switch (tipo) {
                case "EstablecimientoGastronomico":
                    elemento = new EstablecimientoGastronomico();
                    break;
                case "Evento":
                    elemento = new Evento();
                    break;
                default:
                    elemento = new LugarTuristico();
            }
        }

        elemento.setNombre(req.getParameter("nombre"));
        elemento.setDescripcion(req.getParameter("descripcion"));
        elemento.setSector(req.getParameter("sector"));

        String categoriaId = req.getParameter("categoriaId");
        if (categoriaId != null && !categoriaId.isBlank()) {
            Categoria categoria = categoriaServicio.buscarPorId(Integer.parseInt(categoriaId));
            elemento.setCategoria(categoria);
        } else {
            elemento.setCategoria(null);
        }

        if (elemento instanceof LugarTuristico) {
            ((LugarTuristico) elemento).setHorario(req.getParameter("horario"));
        } else if (elemento instanceof EstablecimientoGastronomico) {
            EstablecimientoGastronomico est = (EstablecimientoGastronomico) elemento;
            est.setEspecialidad(req.getParameter("especialidad"));
            est.setHorario(req.getParameter("horario"));
        } else if (elemento instanceof Evento) {
            Evento evento = (Evento) elemento;
            evento.setFechaInicio(parseFecha(req.getParameter("fechaInicio")));
            evento.setFechaFin(parseFecha(req.getParameter("fechaFin")));
        }
        return elemento;
    }

    private LocalDate parseFecha(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(valor); // formato yyyy-MM-dd del input type=date
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
