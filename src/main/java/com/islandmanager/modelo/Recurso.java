package com.islandmanager.modelo;

/**
 * Representa un recurso material o insumo dentro de la isla.
 * Aplica el principio de encapsulamiento mediante atributos privados y métodos de acceso.
 *
 * @version 1.0
 */
public class Recurso {

    // --- Atributos privados ---
    private int id;    // Clave primaria en la base de datos (0 = todavía no se guardó)
    private String nombre;
    private int cantidad;
    private int valor; // Valor unitario para el intercambio o trueque

    /**
     * Constructor usado al leer un recurso desde la base de datos.
     */
    public Recurso(int id, String nombre, int cantidad, int valor) {
        this(nombre, cantidad, valor);
        this.id = id;
    }

    /**
     * Constructor para inicializar un recurso con sus valores iniciales.
     * 
     * @param nombre   Nombre identificador del recurso (ej. Madera, Agua).
     * @param cantidad Cantidad disponible inicial.
     * @param valor    Valor comercial o de intercambio por unidad.
     */
    public Recurso(String nombre, int cantidad, int valor) {
        this.nombre = nombre;
        this.cantidad = Math.max(0, cantidad);
        this.valor = Math.max(1, valor);
    }

    /**
     * Incrementa la cantidad actual del recurso.
     * Estructura de control: valida que la cantidad a agregar sea positiva.
     * 
     * @param cant Cantidad a sumar.
     */
    public void agregar(int cant) {
        if (cant > 0) {
            this.cantidad += cant;
        }
    }

    /**
     * Consume o descuenta una cantidad del recurso disponible.
     * Estructura de control: evalúa si hay stock suficiente antes de descontar.
     * 
     * @param cant Cantidad que se desea consumir.
     * @return true si se pudo consumir; false si la cantidad solicitada supera el stock.
     */
    public boolean consumir(int cant) {
        if (cant > 0 && cant <= this.cantidad) {
            this.cantidad -= cant;
            return true;
        } else {
            return false;
        }
    }

    // --- Métodos Getters ---

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public int getValor() {
        return this.valor;
    }

    @Override
    public String toString() {
        return this.nombre + ": " + this.cantidad + " unidades (Valor: " + this.valor + " c/u)";
    }
}
