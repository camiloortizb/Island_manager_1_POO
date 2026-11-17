package com.islandmanager.excepciones;

/**
 * Excepción verificada (checked) de la capa de persistencia.
 * Envuelve a SQLException para que el resto del programa no dependa de JDBC.
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    public DAOException(String mensaje) {
        super(mensaje);
    }

    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
