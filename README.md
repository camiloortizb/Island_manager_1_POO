Island Manager — Simulador de Supervivencia y Comercio
> **Proyecto Completo Cuatrimestral (PCC) — Programación Orientada a Objetos en Java**  
> **Evaluación:** Primer Parcial (Parte 1: Repositorio, Código y UML | Parte 2: Exposición en Aula)  
> **Actividad 2:** JDBC, DAO con Maven, MySQL, manejo de excepciones y colecciones (ver sección 7)  
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
Island_manager_1_POO/
│
├── pom.xml                                   # Proyecto Maven (dependencia mysql-connector-j)
├── sql/
│   └── island_manager.sql                    # Script de creación de la base y tablas
├── src/main/resources/
│   └── db.properties                         # URL, usuario y contraseña de MySQL
└── src/main/java/com/islandmanager/
    ├── Main.java                             # Juego por consola (Scanner + menú)
    ├── MainCRUD.java                         # Prueba de conexión y CRUD con JDBC
    ├── modelo/                               # Clases de dominio
    │   ├── Jugador.java
    │   ├── Inventario.java
    │   ├── Recurso.java
    │   └── Ubicacion.java
    ├── dao/                                  # Capa de acceso a datos (patrón DAO)
    │   ├── GenericDAO.java                   # Interfaz CRUD genérica
    │   ├── JugadorDAO.java                   # Interfaz específica de Jugador
    │   └── JugadorDAOImpl.java               # Implementación con JDBC
    ├── conexion/
    │   └── ConexionDB.java                   # Conexión a MySQL
    └── excepciones/                          # Excepciones propias
        ├── DAOException.java
        ├── RecursoInsuficienteException.java
        └── DatoInvalidoException.java
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
5. Guardar partida (MySQL)
6. Salir del simulador
--------------------------------------------------------
Seleccione una accion (1-6): 1

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
5. Guardar partida (MySQL)
6. Salir del simulador
--------------------------------------------------------
Seleccione una accion (1-6): 2

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
5. Guardar partida (MySQL)
6. Salir del simulador
--------------------------------------------------------
Seleccione una accion (1-6): 3

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

---
7. Actividad 2 — Persistencia con JDBC, DAO, Excepciones y Colecciones

### 7.1 Cómo ejecutar
1. Crear la base en MySQL ejecutando el script (desde MySQL Workbench o consola):
   ```bash
   mysql -u root -p < sql/island_manager.sql
   ```
2. Ajustar usuario y contraseña en `src/main/resources/db.properties`.
3. Compilar y correr la prueba del CRUD:
   ```bash
   mvn compile
   mvn exec:java                                          # ejecuta MainCRUD
   mvn exec:java -Dexec.mainClass=com.islandmanager.Main  # ejecuta el juego
   ```
   También se puede abrir como proyecto Maven en IntelliJ / NetBeans / Eclipse y ejecutar cada `Main` directamente.

### 7.2 Base de datos MySQL
| Tabla | Campos | Relación |
|---|---|---|
| `ubicacion` | id, nombre, recurso_principal, cantidad_base | Catálogo de biomas (se carga con 3 registros) |
| `jugador` | id, nombre, edad, ubicacion_id, fecha_alta | FK a `ubicacion` (N jugadores → 1 ubicación) |
| `recurso` | id, jugador_id, nombre, cantidad, valor | FK a `jugador` con `ON DELETE CASCADE` (1 jugador → N recursos) |

Las tablas reflejan las clases del modelo: `Jugador` se asocia a una `Ubicacion` y es dueño de su `Inventario` (lista de `Recurso`). Por eso, al borrar un jugador se borran también sus recursos.

### 7.3 Estructura DAO
- **`GenericDAO<T>` (interfaz):** define el contrato CRUD: `insertar`, `buscarPorId`, `listarTodos`, `actualizar`, `eliminar`.
- **`JugadorDAO` (interfaz):** hereda de `GenericDAO<Jugador>` y agrega `buscarPorNombre`.
- **`JugadorDAOImpl`:** implementa las interfaces con JDBC.
- **`ConexionDB`:** centraliza la conexión leyendo los datos de `db.properties`.

Así, el juego (`Main`) solo conoce la interfaz `JugadorDAO` y no sabe nada de SQL. Si mañana cambiáramos MySQL por otro motor, solo habría que escribir otra implementación.

### 7.4 CRUD con JDBC (sentencias preparadas)
Todas las consultas usan `PreparedStatement` con parámetros `?`:
```java
PreparedStatement ps = con.prepareStatement("UPDATE jugador SET nombre = ?, edad = ?, ubicacion_id = ? WHERE id = ?");
ps.setString(1, jugador.getNombre());
ps.setInt(2, jugador.getEdad());
```
- Evitan **inyección SQL**: los valores se envían separados del texto de la sentencia.
- La base puede **reutilizar** la sentencia ya compilada con distintos valores.
- `insertar` usa `Statement.RETURN_GENERATED_KEYS` para obtener el id `AUTO_INCREMENT`.
- El inventario se guarda con `addBatch()` / `executeBatch()` (varias filas en un solo envío).
- `insertar` y `actualizar` usan **transacciones** (`setAutoCommit(false)`, `commit()`, `rollback()`): se guarda el jugador con todo su inventario o no se guarda nada.

`MainCRUD` prueba: conexión → CREATE (2 jugadores) → READ (listar, por id, por nombre) → UPDATE → DELETE.
Además, en el juego se agregó la opción **5. Guardar partida**: la primera vez hace `insertar` y luego `actualizar`.

### 7.5 Manejo de excepciones
| Excepción | Tipo | Dónde se usa |
|---|---|---|
| `DAOException` (propia) | checked | Envuelve `SQLException` en la capa DAO. Obliga a quien guarda/lee a contemplar el error. |
| `RecursoInsuficienteException` (propia) | checked | `Jugador.negociar()` la lanza si no alcanza el recurso; `Main` la captura e informa cuánto falta. Reemplaza al `boolean` de la versión 1. |
| `DatoInvalidoException` (propia) | unchecked | Validación de nombre, edad y ubicación en el constructor de `Jugador`. |
| `NumberFormatException` | unchecked | `leerEntero()` cuando el usuario escribe texto. |
| `NoSuchElementException` | unchecked | `leerEntero()` si se cierra la entrada del Scanner. |
| `UnsupportedOperationException` | unchecked | Si se intenta modificar la lista devuelta por `Inventario.getListaRecursos()`. |
| `SQLException` / `IOException` | checked | Conexión y lectura de `db.properties`. |

También se usan **try-with-resources** (cierre automático de `Connection`, `PreparedStatement` y `ResultSet`), **multi-catch** (`catch (DAOException | SQLException e)`) y **finally** para cerrar la conexión en las transacciones.

**Checked vs unchecked:** las checked representan problemas externos que pueden pasar aunque el código esté bien (la base no responde, no alcanzan los recursos) y el compilador obliga a manejarlas. Las unchecked representan errores de programación o de datos (edad 200) y no es obligatorio capturarlas.

### 7.6 Colecciones y tipos primitivos
| Colección | Dónde | Por qué |
|---|---|---|
| `ArrayList<Recurso>` | `Inventario` | Lista dinámica: crece a medida que se recolectan recursos nuevos. |
| `List<Jugador>` | `listarTodos()`, `buscarPorNombre()` | Resultado de las consultas; no se sabe de antemano cuántas filas vuelven. |
| `Map<Integer, Ubicacion>` (`LinkedHashMap`) | `Main` | Catálogo de ubicaciones: la clave es la opción del menú. Reemplazó los `if-else` y mantiene el orden de inserción. |
| `Collections.unmodifiableList` | `Inventario.getListaRecursos()` | Devuelve la lista en modo solo lectura para proteger el encapsulamiento. |
| `Optional<Jugador>` | `buscarPorId()` | Indica explícitamente que el jugador puede no existir, en lugar de devolver `null`. |

**Colecciones vs. primitivos:** las colecciones solo guardan objetos, no tipos primitivos. Por eso el mapa usa `Integer` (clase envoltorio o *wrapper*) y no `int`. Java convierte automáticamente entre ambos (**autoboxing** al hacer `put(1, ...)` y **unboxing** al comparar). Diferencias principales:

| | Arreglo de primitivos (`int[]`) | Colección (`ArrayList<Integer>`) |
|---|---|---|
| Tamaño | Fijo al crearlo | Dinámico |
| Contenido | Valores primitivos | Objetos (wrappers) |
| Valor por defecto / nulos | `0`, no admite `null` | Admite `null` (puede causar `NullPointerException` al hacer unboxing) |
| Rendimiento | Más rápido y usa menos memoria | Algo más costoso por el boxing |
| Métodos | Solo `length` | `add`, `remove`, `contains`, `size`, etc. |

---
8. Uso de Inteligencia Artificial
<!-- Completar con nuestras propias palabras: para qué usamos la IA y qué aprendimos. -->
- **Para qué la usamos:** ...
- **Qué aprendimos:** ...
