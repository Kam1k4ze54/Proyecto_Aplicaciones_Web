package com.proyectoweb.modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Fábrica compartida de EntityManager: evita crear un
 * EntityManagerFactory por cada DAO instanciado.
 */
public final class JPAUtil {

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("ProyectoWebPU");

    private JPAUtil() {
    }

    public static EntityManager crearEntityManager() {
        return EMF.createEntityManager();
    }
}
