package com.islandmanager.excepciones;

/**
 * Excepción verificada (checked) de la lógica del juego.
 * Se lanza cuando el jugador intenta entregar un recurso que no tiene
 * o del que no alcanza la cantidad pedida en un trueque.
 */
public class RecursoInsuficienteException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String recurso;
    private final int cantidadPedida;
    private final int cantidadDisponible;

    public RecursoInsuficienteException(String recurso, int cantidadPedida, int cantidadDisponible) {
        super("Recurso insuficiente: se necesitan " + cantidadPedida + " de " + recurso
                + " y solo hay " + cantidadDisponible);
        this.recurso = recurso;
        this.cantidadPedida = cantidadPedida;
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getRecurso() {
        return recurso;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
}
