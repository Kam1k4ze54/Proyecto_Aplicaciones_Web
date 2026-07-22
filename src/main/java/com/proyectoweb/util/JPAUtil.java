package com.proyectoweb.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "ProyectoWebPU";
    private static volatile EntityManagerFactory emf;

    private JPAUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (JPAUtil.class) {
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                }
            }
        }
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
