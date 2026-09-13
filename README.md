Island Manager — Simulador de Supervivencia y Comercio
> **Proyecto Completo Cuatrimestral (PCC) — Programación Orientada a Objetos en Java**  
> **Evaluación:** Primer Parcial (Parte 1: Repositorio, Código y UML | Parte 2: Exposición en Aula)  
> **Autores:** Camilo Ortiz - Georgina Traboni
> **Lenguaje:** Java  

1. Resumen y Objetivo Global del Proyecto
Island Manager es un simulador interactivo de supervivencia, administración y negociación de recursos desarrollado bajo el paradigma de Programación Orientada a Objetos (POO) en Java.
Objetivo Global
El objetivo del proyecto es modelar una experiencia donde el usuario asume el rol de un superviviente recién llegado a una isla desierta. A través de un menú interactivo en consola gestionado con `Scanner`, el jugador debe:
Registrar sus datos personales (nombre y edad).
Seleccionar estratégicamente su bioma o campamento inicial (Playa, Bosque o Montaña).
Gestionar un inventario dinámico de recursos esenciales (Agua, Madera, Piedra, Comida, Hierro).
Realizar expediciones de recolección ajustando la intensidad del trabajo (obteniendo bonificaciones según la zona).
Negociar e intercambiar recursos mediante operaciones de trueque con mercaderes locales para progresar.
El diseño prioriza la cohesión, el encapsulamiento, las relaciones estructurales entre clases y el uso riguroso de estructuras de control dentro de la lógica interna de los métodos.
---
2. Estructura del Repositorio
```text
Island admin/
│
├── src/                               # Código fuente Java
│   ├── Main.java                      # Punto de entrada, interacción con Scanner y menú
│   ├── Jugador.java                   # Entidad principal: estado, recolección y trueque
│   ├── Ubicacion.java                 # Biomas de la isla y cálculo de rendimientos
│   ├── Inventario.java                # Administrador de la colección de recursos
│   └── Recurso.java                   # Modelo individual de un bien o insumo
│
├── uml/                               # Modelado del sistema
│   ├── diagrama_clases.png            # Imagen PNG en alta resolución del diagrama UML
│   └── diagrama_clases.svg            # Archivo vectorial escalable del diagrama
│
├── PRESENTACION.md                    # Guion y diapositivas para la defensa oral del Parcial
└── README.md                          # Portada y documentación integral del proyecto
```
---
3. Diagrama de Clases UML
Imagen del Diagrama
A continuación se visualiza el modelado del sistema con sus atributos, métodos, visibilidades y relaciones:

4. Análisis de Clases y Conceptos de POO Aplicados
El proyecto implementa 5 clases (4 clases de dominio + 1 clase ejecutora con `Main`), cumpliendo estrictamente el rango solicitado de entre 4 y 10 clases:
1. `Recurso.java`
Rol: Modela de manera atómica cualquier material tangible en la isla (ej. Agua, Madera, Hierro).
Encapsulamiento: Atributos `private String nombre`, `private int cantidad`, `private int valor`.
Estructura de control en método:
En `consumir(int cant)`: utiliza un condicional `if-else` para validar que la cantidad solicitada sea positiva y no exceda el stock actual, protegiendo la integridad de los datos.
2. `Inventario.java`
Rol: Administra el conjunto de recursos que pertenecen al jugador.
** Uso de Colecciones:** Utiliza `ArrayList<Recurso>` para un manejo dinámico del almacenamiento.
** Estructura de control en método:**
En `buscarRecurso(String nombre)`: implementa un bucle `for-each` y un condicional `if (r.getNombre().equalsIgnoreCase(nombre))` para realizar búsquedas insensibles a mayúsculas.
En `agregarRecurso(Recurso nuevo)`: utiliza `if-else` para verificar si el recurso ya existía (acumulando la cantidad) o si debe agregarse como una nueva entrada.
3. `Ubicacion.java`
Rol: Modela los diferentes biomas o regiones de la isla (Playa, Bosque, Montaña), sus recursos característicos y su rendimiento base.
Estructura de control en método:
En `obtenerBonus(int intensidad)`: utiliza una estructura `switch-case` que evalúa el nivel de esfuerzo (1: Ligero, 2: Moderado, 3: Exhaustivo) y retorna bonificaciones numéricas de recolección.
4. `Jugador.java`
Rol: Representa la entidad central que interactúa en la simulación.
Relaciones POO:
Composición (`*--`) con `Inventario`: El jugador es dueño de su inventario; este se crea en su constructor.
Asociación (`-->`) con `Ubicacion`: El jugador conoce la zona donde está asentado y puede cambiar de ubicación durante la partida.
Estructura de control en método:
En `negociar(...)`: valida con `if-else` si el jugador posee en su inventario el recurso exigido y si cuenta con la cantidad suficiente mediante `recursoPropio.consumir(cantDar)`. Si la condición se cumple, se incorpora el nuevo recurso y retorna `true`; en caso contrario, rechaza la transacción y retorna `false`.
5. `Main.java`
Rol: Coordinador de la aplicación y flujo de interacción con el usuario.
Uso de `Scanner`: Solicita interactivamente el nombre, edad, ubicación inicial y opciones de menú.
Robustez de Entrada: Método auxiliar `leerEntero(Scanner, min, max)` con bloque `try-catch (NumberFormatException)` y bucle `while` para evitar caídas del programa ante ingresos erróneos de texto o números fuera de rango.
---
6. Demostración de Flujo en Consola
```text
************************************************************
*                                                          *
*            ISLAND MANAGER - SIMULADOR POO                *
*       Supervivencia, Gestion y Comercio en la Isla       *
*                                                          *
************************************************************

Ingrese su nombre: Pepito Lopez
Ingrese su edad: 22

Seleccione donde desea establecer su campamento base:
1. Playa Arrecife (Recurso: Agua)
2. Bosque Nuboso (Recurso: Madera)
3. Montana Rocosa (Recurso: Piedra)
Opcion (1-3): 2

>> ¡Campamento establecido con exito!
>> Bienvenido a la isla, Pepito Lopez.

--------------------------------------------------------
                  MENU DE ACCIONES                      
--------------------------------------------------------
1. Recolectar recursos en la zona actual
2. Negociar / Intercambiar recursos (Trueque)
3. Ver estado del superviviente e inventario
4. Cambiar de ubicacion
5. Salir del simulador
--------------------------------------------------------
Seleccione una accion (1-5): 1

--- EXPEDICION DE RECOLECCION ---
Ubicacion actual: Bosque Nuboso
Elija la intensidad del trabajo:
1. Ligero (+5 bonus)
2. Moderado (+15 bonus)
3. Exhaustivo (+30 bonus)
Intensidad (1-3): 3

>> ¡Recoleccion exitosa en Bosque Nuboso!
>> Has obtenido: 65 unidades de Madera (Base: 35 + Bonus: 30).

--------------------------------------------------------
                  MENU DE ACCIONES                      
--------------------------------------------------------
1. Recolectar recursos en la zona actual
2. Negociar / Intercambiar recursos (Trueque)
3. Ver estado del superviviente e inventario
4. Cambiar de ubicacion
5. Salir del simulador
--------------------------------------------------------
Seleccione una accion (1-5): 2

--- PUESTO DE COMERCIO Y TRUEQUE ---
Ofertas de mercaderes locales:
1. Entregar 20 de Madera -> Recibir 10 de Hierro
2. Entregar 15 de Piedra -> Recibir 10 de Comida
3. Entregar 10 de Agua   -> Recibir 15 de Madera
4. Cancelar y volver
Seleccione oferta (1-4): 1
>> ¡Negociacion exitosa! Intercambiaste: 20 de Madera por 10 de Hierro.

--------------------------------------------------------
                  MENU DE ACCIONES                      
--------------------------------------------------------
1. Recolectar recursos en la zona actual
2. Negociar / Intercambiar recursos (Trueque)
3. Ver estado del superviviente e inventario
4. Cambiar de ubicacion
5. Salir del simulador
--------------------------------------------------------
Seleccione una accion (1-5): 3

========================================================
               ESTADO DEL SUPERVIVIENTE                 
========================================================
  Nombre: Camilo | Edad: 24 anios
  Ubicacion actual: Bosque Nuboso
  Recurso abundante local: Madera

+------------------------------------------------------+
|               INVENTARIO DE RECURSOS                 |
+------------------------------------------------------+
|  • Agua         :   15 unidades  (Valor:   2 c/u) |
|  • Comida       :   10 unidades  (Valor:   3 c/u) |
|  • Madera       :   45 unidades  (Valor:   3 c/u) |
|  • Hierro       :   10 unidades  (Valor:  10 c/u) |
+------------------------------------------------------+
```
---
