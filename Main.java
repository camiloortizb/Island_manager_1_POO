import java.util.Scanner;

/**
 * Clase principal que inicia y coordina el simulador Island Manager.
 * Maneja la interacción por consola con el usuario mediante Scanner,
 * instanciación de objetos y flujo general de decisiones.
 * 
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Bienvenida y Portada tipo Ventana de Consola ---
        System.out.println("************************************************************");
        System.out.println("*                                                          *");
        System.out.println("*            ----- ISLAND MANAGER -----                    *");
        System.out.println("*       Supervivencia, Gestion y Comercio en la Isla       *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");

        // --- Registro de datos del Jugador ---
        System.out.print("\nIngrese su nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            nombre = "Superviviente";
        }

        System.out.print("Ingrese su edad: ");
        int edad = leerEntero(scanner, 1, 120);

        // --- Catálogo de Ubicaciones Disponibles ---
        Ubicacion playa = new Ubicacion("Playa Arrecife", "Agua", 20);
        Ubicacion bosque = new Ubicacion("Bosque Nuboso", "Madera", 35);
        Ubicacion montana = new Ubicacion("Montana Rocosa", "Piedra", 25);

        // --- Selección de Ubicación Inicial ---
        System.out.println("\nSeleccione donde desea establecer su campamento base:");
        System.out.println("1. " + playa.getNombre() + " (Recurso: " + playa.getRecursoPrincipal() + ")");
        System.out.println("2. " + bosque.getNombre() + " (Recurso: " + bosque.getRecursoPrincipal() + ")");
        System.out.println("3. " + montana.getNombre() + " (Recurso: " + montana.getRecursoPrincipal() + ")");
        System.out.print("Opcion (1-3): ");
        int opcionUbicacion = leerEntero(scanner, 1, 3);

        Ubicacion ubicacionInicial;
        if (opcionUbicacion == 1) {
            ubicacionInicial = playa;
        } else if (opcionUbicacion == 2) {
            ubicacionInicial = bosque;
        } else {
            ubicacionInicial = montana;
        }

        // --- Instanciación del Objeto Principal: Jugador ---
        Jugador jugador = new Jugador(nombre, edad, ubicacionInicial);

        System.out.println("\n>> ¡Campamento establecido con exito!");
        System.out.println(">> Bienvenido a la isla, " + jugador.getNombre() + ".");

        // --- Bucle Principal del Simulador ---
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenuPrincipal();
            System.out.print("Seleccione una accion (1-5): ");
            int opcion = leerEntero(scanner, 1, 5);

            switch (opcion) {
                case 1:
                    // --- Caso 1: Recolección ---
                    System.out.println("\n--- EXPEDICION DE RECOLECCION ---");
                    System.out.println("Ubicacion actual: " + jugador.getUbicacion().getNombre());
                    System.out.println("Elija la intensidad del trabajo:");
                    System.out.println("1. Ligero (+5 bonus)");
                    System.out.println("2. Moderado (+15 bonus)");
                    System.out.println("3. Exhaustivo (+30 bonus)");
                    System.out.print("Intensidad (1-3): ");
                    int intensidad = leerEntero(scanner, 1, 3);

                    jugador.recolectar(intensidad);
                    break;

                case 2:
                    // --- Caso 2: Negociación / Trueque ---
                    System.out.println("\n--- PUESTO DE COMERCIO Y TRUEQUE ---");
                    System.out.println("Ofertas de mercaderes locales:");
                    System.out.println("1. Entregar 20 de Madera -> Recibir 10 de Hierro");
                    System.out.println("2. Entregar 15 de Piedra -> Recibir 10 de Comida");
                    System.out.println("3. Entregar 10 de Agua   -> Recibir 15 de Madera");
                    System.out.println("4. Cancelar y volver");
                    System.out.print("Seleccione oferta (1-4): ");
                    int oferta = leerEntero(scanner, 1, 4);

                    if (oferta == 1) {
                        boolean exito = jugador.negociar("Madera", 20, "Hierro", 10, 10);
                        informarResultadoTrueque(exito, "20 de Madera por 10 de Hierro");
                    } else if (oferta == 2) {
                        boolean exito = jugador.negociar("Piedra", 15, "Comida", 10, 4);
                        informarResultadoTrueque(exito, "15 de Piedra por 10 de Comida");
                    } else if (oferta == 3) {
                        boolean exito = jugador.negociar("Agua", 10, "Madera", 15, 3);
                        informarResultadoTrueque(exito, "10 de Agua por 15 de Madera");
                    } else {
                        System.out.println(">> Operacion cancelada. Regresando al menu.");
                    }
                    break;

                case 3:
                    // --- Caso 3: Consulta de Estado e Inventario ---
                    jugador.mostrarEstado();
                    break;

                case 4:
                    // --- Caso 4: Cambiar de Ubicación ---
                    System.out.println("\n--- MAPA DE LA ISLA - VIAJAR ---");
                    System.out.println("1. Viajar a " + playa.getNombre());
                    System.out.println("2. Viajar a " + bosque.getNombre());
                    System.out.println("3. Viajar a " + montana.getNombre());
                    System.out.println("4. Permanecer en el lugar actual");
                    System.out.print("Destino (1-4): ");
                    int destino = leerEntero(scanner, 1, 4);

                    if (destino == 1) {
                        jugador.setUbicacion(playa);
                    } else if (destino == 2) {
                        jugador.setUbicacion(bosque);
                    } else if (destino == 3) {
                        jugador.setUbicacion(montana);
                    } else {
                        System.out.println(">> Permaneces en " + jugador.getUbicacion().getNombre() + ".");
                    }
                    break;

                case 5:
                    // --- Caso 5: Salir del programa ---
                    System.out.println("\n========================================================");
                    System.out.println("Gracias por jugar a Island Manager. ¡Hasta la proxima!");
                    System.out.println("========================================================");
                    ejecutando = false;
                    break;

                default:
                    System.out.println(">> Opcion no valida.");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Muestra las opciones principales del simulador.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n--------------------------------------------------------");
        System.out.println("                  MENU DE ACCIONES                      ");
        System.out.println("--------------------------------------------------------");
        System.out.println("1. Recolectar recursos en la zona actual");
        System.out.println("2. Negociar / Intercambiar recursos (Trueque)");
        System.out.println("3. Ver estado del superviviente e inventario");
        System.out.println("4. Cambiar de ubicacion");
        System.out.println("5. Salir del simulador");
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Imprime el resultado de una operación comercial.
     */
    private static void informarResultadoTrueque(boolean exito, String detalle) {
        if (exito) {
            System.out.println(">> ¡Negociacion exitosa! Intercambiaste: " + detalle + ".");
        } else {
            System.out.println(">> Error en el trueque: no dispones de los recursos requeridos.");
        }
    }

    /**
     * Lee un número entero de la consola garantizando que sea válido y se encuentre en el rango permitido.
     * Evita excepciones por tipos de datos incorrectos.
     * 
     * @param scanner Objeto Scanner para lectura.
     * @param min     Valor mínimo permitido.
     * @param max     Valor máximo permitido.
     * @return Entero validado dentro del rango [min, max].
     */
    private static int leerEntero(Scanner scanner, int min, int max) {
        int valor;
        while (true) {
            try {
                String entrada = scanner.nextLine().trim();
                valor = Integer.parseInt(entrada);
                if (valor >= min && valor <= max) {
                    return valor;
                } else {
                    System.out.print(">> Ingrese un numero entre " + min + " y " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print(">> Entrada invalida. Ingrese un numero valido: ");
            }
        }
    }
}
