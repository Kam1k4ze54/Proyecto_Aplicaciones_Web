package com.proyectoweb.modelo.servicios;

/**
 * Reseñas y favoritos vinculados a un elemento (CU07-D paso 2.1).
 */
public class DependenciasInfo {

    private final int cantidadResenias;
    private final int cantidadFavoritos;

    public DependenciasInfo(int cantidadResenias, int cantidadFavoritos) {
        this.cantidadResenias = cantidadResenias;
        this.cantidadFavoritos = cantidadFavoritos;
    }

    public int getCantidadResenias() {
        return cantidadResenias;
    }

    public int getCantidadFavoritos() {
        return cantidadFavoritos;
    }

    public boolean isTieneDependencias() {
        return cantidadResenias > 0 || cantidadFavoritos > 0;
    }
}
