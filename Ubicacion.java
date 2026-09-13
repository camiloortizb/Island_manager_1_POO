/**
 * Representa una zona o bioma geográfico dentro de la isla.
 * Define qué tipo de recurso abunda y calcula bonificaciones según el esfuerzo aplicado.
 * @version 1.0
 */
public class Ubicacion {

    // --- Atributos privados ---
    private String nombre;
    private String recursoPrincipal;
    private int cantidadBase;

    /**
     * Constructor para inicializar una ubicación de la isla.
     * 
     * @param nombre           Nombre de la zona (ej. "Bosque", "Playa", "Montana").
     * @param recursoPrincipal Tipo de recurso característico de este lugar.
     * @param cantidadBase     Cantidad base obtenida al recolectar en condiciones normales.
     
    */
    public Ubicacion(String nombre, String recursoPrincipal, int cantidadBase) {
        this.nombre = nombre;
        this.recursoPrincipal = recursoPrincipal;
        this.cantidadBase = cantidadBase;
    }

    /**
     * Calcula una cantidad adicional de recursos según la intensidad de la expedición.
     * Estructura de control: sentencia switch para determinar el bonus por intensidad.
     * 
     * @param intensidad Nivel de esfuerzo (1: Ligero, 2: Moderado, 3: Exhaustivo).
     * @return Cantidad extra de recursos generada.
     */
    public int obtenerBonus(int intensidad) {
        int bonus;
        // --- estructura de control: switch para determinar el bonus según la intensidad    
        switch (intensidad) {
            case 1:
                bonus = 5;
                break;
            case 2:
                bonus = 15;
                break;
            case 3:
                bonus = 30;
                break;
            default:
                bonus = 0;
                break;
        }

        return bonus;
    }

    // --- Getters ---

    public String getNombre() {
        return this.nombre;
    }

    public String getRecursoPrincipal() {
        return this.recursoPrincipal;
    }

    public int getCantidadBase() {
        return this.cantidadBase;
    }

   /* toString() no lo inventamos nosotros: lo hereda toda clase de Object. 
   Lo que hicimos fue sobreescribirlo (@Override) para que en vez de devolver el nombre de la clase
    con un hash(codigo identificador) raro, devuelva un texto legible con los datos del recurso (nombre, cantidad, valor). */ 
   
   @Override
    public String toString() {
        return this.nombre + " [Recurso principal: " + this.recursoPrincipal + ", Base: " + this.cantidadBase + " unidades]";
    }
}
