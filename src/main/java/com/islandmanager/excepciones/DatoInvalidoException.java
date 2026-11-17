package com.islandmanager.excepciones;

/**
 * Excepción NO verificada (unchecked, hereda de RuntimeException).
 * Se usa para datos que nunca deberían llegar mal a un constructor
 * (nombre vacío, edad fuera de rango, ubicación nula). Es un error de programación,
 * por eso no obligamos a capturarla con try-catch.
 */
public class DatoInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
