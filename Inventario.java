import java.util.ArrayList;

/**
 * Administra la colección de recursos que posee el jugador o la base de la isla.
 * Demuestra la relación de agregación/composición con la clase Recurso y el uso de colecciones.
 * 
 * @version 1.0
 */


public class Inventario {

    // --- Atributo ---
    private ArrayList<Recurso> listaRecursos;

    /**
     * Constructor que inicializa la lista dinámica de recursos.
     */
    public Inventario() {
        this.listaRecursos = new ArrayList<>();
    }

    /**
     * Busca un recurso por su nombre dentro del inventario.
     * Estructuras de control: bucle for-each y condicional if.
     * 
     * @param nombre Nombre del recurso a buscar.
     * @return El objeto Recurso encontrado, o null si no existe en el inventario.
     */
    public Recurso buscarRecurso(String nombre) {
        for (Recurso r : this.listaRecursos) {
            if (r.getNombre().equalsIgnoreCase(nombre)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Agrega un recurso al inventario. Si el recurso ya existía, suma la cantidad;
     * si no existía, lo añade a la lista.
     * Estructura de control: if-else para verificar existencia previa.
     * 
     * @param nuevo Objeto Recurso que se desea agregar.
     */
    public void agregarRecurso(Recurso nuevo) {
        if (nuevo == null) {
            return;
        }

        Recurso existente = buscarRecurso(nuevo.getNombre());
        if (existente != null) {
            existente.agregar(nuevo.getCantidad());
        } else {
            this.listaRecursos.add(nuevo);
        }
    }

    /**
     * Imprime en pantalla el listado formateado de todos los recursos almacenados.
     * Estructuras de control: if-else para verificar si está vacío y for-each para iterar.
     */
    public void mostrar() {
        System.out.println("\n+------------------------------------------------------+");
        System.out.println("|               INVENTARIO DE RECURSOS                 |");
        System.out.println("+------------------------------------------------------+");

        if (this.listaRecursos.isEmpty()) {
            System.out.println("|  (Inventario vacio. No posees recursos aun)          |");
        } else {
            for (Recurso r : this.listaRecursos) {
                System.out.printf("|  • %-12s : %4d unidades  (Valor: %3d c/u) |\n",
                        r.getNombre(), r.getCantidad(), r.getValor());
            }
        }
        System.out.println("+------------------------------------------------------+");
    }

    // --- Getter de la lista ---
    public ArrayList<Recurso> getListaRecursos() {
        return this.listaRecursos;
    }
}
