package com.proyectoweb.dao;

import java.util.List;
import jakarta.persistence.*;

import com.proyectoweb.modelo.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

    private EntityManager em = Persistence
            .createEntityManagerFactory("ProyectoWebPU")
            .createEntityManager();

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
}
