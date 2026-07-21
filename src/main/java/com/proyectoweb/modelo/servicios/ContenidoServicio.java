package com.proyectoweb.modelo.servicios;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.proyectoweb.modelo.dao.ContenidoDAO;
import com.proyectoweb.modelo.dao.ContenidoDAOImpl;
import com.proyectoweb.modelo.dao.EvaluacionDAO;
import com.proyectoweb.modelo.dao.EvaluacionDAOImpl;
import com.proyectoweb.modelo.dao.FavoritoDAO;
import com.proyectoweb.modelo.dao.FavoritoDAOImpl;
import com.proyectoweb.modelo.dao.PreferenciaUsuarioDAO;
import com.proyectoweb.modelo.dao.PreferenciaUsuarioDAOImpl;
import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Evento;
import com.proyectoweb.modelo.entities.Usuario;
import com.proyectoweb.modelo.servicios.excepciones.ValidacionException;

/**
 * Lógica de negocio del catálogo de contenido (CU04, CU07).
 */
public class ContenidoServicio {

    public static final String[] TIPOS = { "LugarTuristico", "EstablecimientoGastronomico", "Evento" };

    private ContenidoDAO contenidoDAO = new ContenidoDAOImpl();
    private PreferenciaUsuarioDAO preferenciaUsuarioDAO = new PreferenciaUsuarioDAOImpl();
    private EvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl();
    private FavoritoDAO favoritoDAO = new FavoritoDAOImpl();

    /**
     * CU04-A: vista previa del panel — por cada tipo, los primeros N elementos
     * según las preferencias del usuario; si no hay, los destacados del tipo
     * (alterno 7); si tampoco hay, la sección queda marcada como vacía.
     */
    public Map<String, VistaSeccion> obtenerVistaPrevia(Usuario usuario, int limite) {
        List<Categoria> preferidas = preferenciaUsuarioDAO.obtenerPreferencias(usuario);
        Map<String, VistaSeccion> vistaPrevia = new LinkedHashMap<>();

        for (String tipo : TIPOS) {
            List<ElementoContenido> porPreferencia =
                    contenidoDAO.obtenerPorCategoriasLimitado(preferidas, tipo, limite);
            if (!porPreferencia.isEmpty()) {
                vistaPrevia.put(tipo, new VistaSeccion(porPreferencia, VistaSeccion.ORIGEN_PREFERENCIAS));
                continue;
            }
            List<ElementoContenido> destacados = contenidoDAO.obtenerDestacadosPorTipo(tipo, limite);
            if (!destacados.isEmpty()) {
                vistaPrevia.put(tipo, new VistaSeccion(destacados, VistaSeccion.ORIGEN_DESTACADOS));
            } else {
                vistaPrevia.put(tipo, new VistaSeccion(new ArrayList<>(), VistaSeccion.ORIGEN_VACIO));
            }
        }
        return vistaPrevia;
    }

    // CU04: ficha de detalle
    public ElementoContenido buscarPorId(int id) {
        return contenidoDAO.buscarPorId(id);
    }

    // CU04 alterno 5: búsqueda por nombre en los tres tipos
    public List<ElementoContenido> buscarPorNombre(String termino) {
        return contenidoDAO.buscarPorNombre(termino);
    }

    // CU04 alterno 6: lista completa de un tipo (solo activos, vista de usuario)
    public List<ElementoContenido> listarTodosPorTipo(String tipo) {
        return contenidoDAO.listarTodosPorTipo(tipo, true);
    }

    // CU07-A: listado administrativo (incluye inactivos)
    public List<ElementoContenido> listarTodosPorTipoAdmin(String tipo) {
        return contenidoDAO.listarTodosPorTipo(tipo, false);
    }

    // CU07-B paso 4-5: valida y registra
    public ElementoContenido crear(ElementoContenido elemento) {
        validar(elemento);
        return contenidoDAO.guardar(elemento);
    }

    // CU07-C paso 4-5: valida y actualiza
    public ElementoContenido actualizar(ElementoContenido elemento) {
        validar(elemento);
        return contenidoDAO.actualizar(elemento);
    }

    // CU07 paso 4: campos obligatorios y coherencia de fechas en eventos
    private void validar(ElementoContenido elemento) {
        List<String> errores = new ArrayList<>();
        if (elemento.getNombre() == null || elemento.getNombre().isBlank()) {
            errores.add("El nombre es obligatorio.");
        }
        if (elemento.getDescripcion() == null || elemento.getDescripcion().isBlank()) {
            errores.add("La descripción es obligatoria.");
        }
        if (elemento.getSector() == null || elemento.getSector().isBlank()) {
            errores.add("El sector/ubicación es obligatorio.");
        }
        if (elemento.getCategoria() == null) {
            errores.add("Debe seleccionar una categoría.");
        }
        if (elemento instanceof Evento) {
            Evento evento = (Evento) elemento;
            if (evento.getFechaInicio() == null || evento.getFechaFin() == null) {
                errores.add("Las fechas de inicio y fin son obligatorias.");
            } else if (evento.getFechaFin().isBefore(evento.getFechaInicio())) {
                errores.add("La fecha de fin no puede ser anterior a la fecha de inicio.");
            }
        }
        if (!errores.isEmpty()) {
            throw new ValidacionException(errores);
        }
    }

    // CU07-D pasos 1-2: reseñas y favoritos vinculados antes de eliminar
    public DependenciasInfo verificarDependencias(int id) {
        return new DependenciasInfo(
                evaluacionDAO.contarPorElemento(id),
                favoritoDAO.contarPorElemento(id));
    }

    // CU07-D 3.A: elimina el elemento junto con sus dependencias
    public boolean eliminar(int id) {
        evaluacionDAO.eliminarPorElemento(id);
        favoritoDAO.eliminarPorElemento(id);
        return contenidoDAO.eliminar(id);
    }

    // CU07-E: marca/desmarca como destacado (toggle)
    public boolean toggleDestacado(int id) {
        ElementoContenido elemento = contenidoDAO.buscarPorId(id);
        if (elemento == null) {
            return false;
        }
        return contenidoDAO.actualizarDestacado(id, !elemento.isDestacado());
    }
}
