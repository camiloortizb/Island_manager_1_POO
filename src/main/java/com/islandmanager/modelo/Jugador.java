package com.islandmanager.modelo;

import com.islandmanager.excepciones.DatoInvalidoException;
import com.islandmanager.excepciones.RecursoInsuficienteException;

/**
 * Representa al usuario / superviviente que administra los recursos en la isla.
 * Demuestra relaciones de composición con Inventario y asociación con Ubicacion.
 * 
 * @version 1.0
 */
public class Jugador {

    // --- Atributos privados ---
    private int id;                // Clave primaria en la base (0 = partida no guardada)
    private String nombre;
    private int edad;
    private Inventario inventario; // Relación de Composición (un Jugador posee un Inventario)
    private Ubicacion ubicacion;   // Relación de Asociación (un Jugador está en una Ubicacion)

    /**
     * Constructor usado por el DAO al recuperar una partida guardada:
     * recibe el inventario ya cargado desde la base, sin recursos iniciales.
     */
    public Jugador(int id, String nombre, int edad, Ubicacion ubicacion, Inventario inventario) {
        validarDatos(nombre, edad, ubicacion);
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.ubicacion = ubicacion;
        this.inventario = (inventario != null) ? inventario : new Inventario();
    }

    /**
     * Constructor que crea al jugador, inicializa su inventario y le asigna su ubicación inicial.
     * 
     * @param nombre           Nombre del superviviente.
     * @param edad             Edad del jugador.
     * @param ubicacionInicial Ubicación inicial donde establece su campamento.
     */
    public Jugador(String nombre, int edad, Ubicacion ubicacionInicial) {
        validarDatos(nombre, edad, ubicacionInicial);
        this.nombre = nombre;
        this.edad = edad;
        this.ubicacion = ubicacionInicial;
        this.inventario = new Inventario();

        // Recursos iniciales de supervivencia para comenzar la partida
        this.inventario.agregarRecurso(new Recurso("Agua", 15, 2));
        this.inventario.agregarRecurso(new Recurso("Comida", 10, 3));
    }

    /**
     * Recolecta recursos en la ubicación actual del jugador según la intensidad elegida.
     * Calcula la cantidad base más el bonus de la ubicación y lo guarda en su inventario.
     * 
     * @param intensidad Nivel de intensidad de la expedición (1: Ligero, 2: Moderado, 3: Exhaustivo).
     */
    public void recolectar(int intensidad) {
        String tipoRecurso = this.ubicacion.getRecursoPrincipal();
        int base = this.ubicacion.getCantidadBase();
        int bonus = this.ubicacion.obtenerBonus(intensidad);
        int totalRecolectado = base + bonus;

        // Determinamos un valor unitario referencial según el recurso
        int valorUnitario = 5;
        if (tipoRecurso.equalsIgnoreCase("Madera")) {
            valorUnitario = 3;
        } else if (tipoRecurso.equalsIgnoreCase("Piedra")) {
            valorUnitario = 4;
        } else if (tipoRecurso.equalsIgnoreCase("Hierro")) {
            valorUnitario = 10;
        }

        Recurso nuevo = new Recurso(tipoRecurso, totalRecolectado, valorUnitario);
        this.inventario.agregarRecurso(nuevo);

        System.out.println("\n>> ¡Recoleccion exitosa en " + this.ubicacion.getNombre() + "!");
        System.out.println(">> Has obtenido: " + totalRecolectado + " unidades de " + tipoRecurso 
                + " (Base: " + base + " + Bonus: " + bonus + ").");
    }

    /**
     * Realiza un intercambio de trueque o comercio de recursos con mercaderes de la isla.
     * Estructura de control: valida si el recurso a entregar existe y si alcanza la cantidad.
     * 
     * @param recursoDar    Nombre del recurso que el jugador entrega.
     * @param cantDar       Cantidad que el jugador debe pagar.
     * @param recursoRecibir Nombre del recurso que el mercader entrega a cambio.
     * @param cantRecibir   Cantidad que el jugador recibe.
     * @param valorRecibir  Valor comercial asignado al nuevo recurso.
     * @throws RecursoInsuficienteException si no tiene el recurso o no le alcanza la cantidad.
     */
    public void negociar(String recursoDar, int cantDar, String recursoRecibir, int cantRecibir, int valorRecibir)
            throws RecursoInsuficienteException {
        Recurso recursoPropio = this.inventario.buscarRecurso(recursoDar);

        // Estructura de control para validar si la operación es viable
        if (recursoPropio == null) {
            throw new RecursoInsuficienteException(recursoDar, cantDar, 0);
        }
        if (!recursoPropio.consumir(cantDar)) {
            throw new RecursoInsuficienteException(recursoDar, cantDar, recursoPropio.getCantidad());
        }

        Recurso nuevoRecurso = new Recurso(recursoRecibir, cantRecibir, valorRecibir);
        this.inventario.agregarRecurso(nuevoRecurso);
    }

    /**
     * Valida los datos obligatorios del jugador.
     * Lanza una excepción no verificada porque recibir datos inválidos es un error de programación.
     */
    private static void validarDatos(String nombre, int edad, Ubicacion ubicacion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del jugador no puede estar vacío");
        }
        if (edad < 1 || edad > 120) {
            throw new DatoInvalidoException("La edad debe estar entre 1 y 120 (se recibió " + edad + ")");
        }
        if (ubicacion == null) {
            throw new DatoInvalidoException("El jugador debe tener una ubicación");
        }
    }

    /**
     * Cambia la ubicación actual del jugador a una nueva zona de la isla.
     * 
     * @param nueva Nueva ubicación seleccionada.
     */
    public void setUbicacion(Ubicacion nueva) {
        if (nueva != null) {
            this.ubicacion = nueva;
            System.out.println(">> Te has trasladado a: " + nueva.getNombre());
        }
    }

    /**
     * Muestra la ficha de estado general del jugador y delega la visualización del inventario.
     */
    public void mostrarEstado() {
        System.out.println("\n========================================================");
        System.out.println("               ESTADO DEL SUPERVIVIENTE                 ");
        System.out.println("========================================================");
        System.out.println("  Nombre: " + this.nombre + " | Edad: " + this.edad + " anios");
        System.out.println("  Ubicacion actual: " + this.ubicacion.getNombre());
        System.out.println("  Recurso abundante local: " + this.ubicacion.getRecursoPrincipal());
        
        // Delegación de llamada al objeto Inventario
        this.inventario.mostrar();
    }

    // --- Setters usados por el DAO y el CRUD ---

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        validarDatos(nombre, this.edad, this.ubicacion);
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        validarDatos(this.nombre, edad, this.ubicacion);
        this.edad = edad;
    }

    // --- Métodos Getters ---

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getEdad() {
        return this.edad;
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public Ubicacion getUbicacion() {
        return this.ubicacion;
    }

    @Override
    public String toString() {
        return "Jugador #" + this.id + " | " + this.nombre + " (" + this.edad + " anios) | "
                + this.ubicacion.getNombre() + " | " + this.inventario.getListaRecursos().size()
                + " recursos, valor total: " + this.inventario.calcularValorTotal();
    }
}
