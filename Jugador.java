/**
 * Representa al usuario / superviviente que administra los recursos en la isla.
 * Demuestra relaciones de composición con Inventario y asociación con Ubicacion.
 * 
 * @version 1.0
 */
public class Jugador {

    // --- Atributos privados ---
    private String nombre;
    private int edad;
    private Inventario inventario; // Relación de Composición (un Jugador posee un Inventario)
    private Ubicacion ubicacion;   // Relación de Asociación (un Jugador está en una Ubicacion)

    /**
     * Constructor que crea al jugador, inicializa su inventario y le asigna su ubicación inicial.
     * 
     * @param nombre           Nombre del superviviente.
     * @param edad             Edad del jugador.
     * @param ubicacionInicial Ubicación inicial donde establece su campamento.
     */
    public Jugador(String nombre, int edad, Ubicacion ubicacionInicial) {
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
     * @return true si la negociación fue concretada; false si no contaba con los recursos.
     */
    public boolean negociar(String recursoDar, int cantDar, String recursoRecibir, int cantRecibir, int valorRecibir) {
        Recurso recursoPropio = this.inventario.buscarRecurso(recursoDar);

        // Estructura de control para validar si la operación es viable
        if (recursoPropio != null && recursoPropio.consumir(cantDar)) {
            Recurso nuevoRecurso = new Recurso(recursoRecibir, cantRecibir, valorRecibir);
            this.inventario.agregarRecurso(nuevoRecurso);
            return true;
        } else {
            return false;
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

    // --- Métodos Getters ---

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
}
