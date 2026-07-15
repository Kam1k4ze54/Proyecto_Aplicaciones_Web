package com.proyectoweb.dao;

import java.util.List;
import jakarta.persistence.*;

import com.proyectoweb.modelo.Preferencia;
import com.proyectoweb.modelo.PreferenciaUsuario;
import com.proyectoweb.modelo.Usuario;

public class PreferenciaUsuarioDAOImpl implements PreferenciaUsuarioDAO {

    private EntityManager em = Persistence
            .createEntityManagerFactory("ProyectoWebPU")
            .createEntityManager();

    @Override
    public boolean guardarPreferencias(Usuario usuario, List<Integer> seleccion) {
        // CU paso 9: registra las preferencias seleccionadas del usuario recién creado
        try {
            em.getTransaction().begin();
            for (Integer idPreferencia : seleccion) {
                Preferencia preferencia = em.find(Preferencia.class, idPreferencia);
                if (preferencia != null) {
                    em.persist(new PreferenciaUsuario(usuario, preferencia));
                }
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }
}
