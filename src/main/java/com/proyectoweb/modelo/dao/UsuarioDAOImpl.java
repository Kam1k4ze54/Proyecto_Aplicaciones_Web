package com.proyectoweb.modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import com.proyectoweb.modelo.entities.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

    private EntityManager em = JPAUtil.crearEntityManager();

    @Override
    public boolean existeUsuario(String correo) {
        // CU paso 4: valida que el correo no esté duplicado
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correo = :correo", Usuario.class);
        query.setParameter("correo", correo);
        return !query.getResultList().isEmpty();
    }

    @Override
    public Usuario guardarCuenta(Usuario usuario) {
        // CU paso 5: registra la cuenta y devuelve el usuario con su id generado
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        return usuario;
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) {
        em.clear();
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contrasena = :contrasena", Usuario.class);
        query.setParameter("correo", correo);
        query.setParameter("contrasena", contrasena);
        List<Usuario> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public Usuario buscarPorId(int id) {
        // CU03: carga el perfil del usuario
        return em.find(Usuario.class, id);
    }

    @Override
    public boolean existeCorreo(String correo, int id) {
        // CU03: valida que el correo no esté en uso por otro usuario
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correo = :correo AND u.id <> :id", Usuario.class);
        query.setParameter("correo", correo);
        query.setParameter("id", id);
        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        // CU03: guarda los cambios del perfil
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        // CU08: listado completo de cuentas para el administrador
        return em.createQuery("SELECT u FROM Usuario u ORDER BY u.apellidos, u.nombres", Usuario.class)
                .getResultList();
    }

    @Override
    public boolean actualizarEstado(int id, boolean nuevoEstado) {
        // CU08: habilita o inhabilita la cuenta
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                usuario.setEstado(nuevoEstado);
            }
            em.getTransaction().commit();
            return usuario != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }
}
