package com.proyectoweb.dao;

import java.util.List;
import jakarta.persistence.*;

import com.proyectoweb.modelo.Preferencia;

public class PreferenciaDAOImpl implements PreferenciaDAO {

    private EntityManager em = Persistence
            .createEntityManagerFactory("ProyectoWebPU")
            .createEntityManager();

    @Override
    public List<Preferencia> obtener() {
        // CU paso 6: trae todo el catálogo (turísticas y gastronómicas) de forma
        // polimórfica
        return em.createQuery("SELECT p FROM Preferencia p", Preferencia.class).getResultList();
    }
}
