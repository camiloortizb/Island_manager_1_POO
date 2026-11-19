Island Manager — Simulador de Supervivencia y Comercio
> **Proyecto Completo Cuatrimestral (PCC) — Programación Orientada a Objetos en Java**  
> **Evaluación:** Primer Parcial (Parte 1: Repositorio, Código y UML | Parte 2: Exposición en Aula)  
> **Actividad 2:** JDBC, DAO con Maven, MySQL, manejo de excepciones y colecciones (ver secciones 7 y 8)  
> **Autores:** Camilo Ortiz — Georgina Traboni  
> **Lenguaje:** Java 17 · **Build:** Maven · **Base de datos:** MySQL 8
---
1. Resumen y objetivo del proyecto
Island Manager es un simulador interactivo de supervivencia, administración y negociación de recursos, desarrollado bajo el paradigma de Programación Orientada a Objetos (POO) en Java.
Objetivo
Modelar una experiencia donde el usuario asume el rol de un superviviente recién llegado a una isla desierta. A través de un menú interactivo en consola gestionado con `Scanner`, el jugador puede:
Registrar sus datos personales (nombre y edad).
Elegir su campamento inicial: Playa Arrecife, Bosque Nuboso o Montaña Rocosa.
Gestionar un inventario dinámico de recursos (Agua, Madera, Piedra, Comida, Hierro).
Hacer expediciones de recolección eligiendo la intensidad del trabajo (más intensidad, más bonus).
Intercambiar recursos con mercaderes mediante trueques.
Cambiar de ubicación dentro de la isla.
Guardar la partida en MySQL (desde la versión 2.0).
El diseño prioriza la cohesión, el encapsulamiento, las relaciones entre clases y el uso de estructuras de control dentro de los métodos.
---
2. Estructura del repositorio
```text
Island_manager_1_POO/
│
├── pom.xml                                   # Proyecto Maven (dependencia mysql-connector-j)
├── sql/
│   └── island_manager.sql                    # Script de creación de la base y tablas
├── docs/
│   └── uml.png                               # Diagrama de clases UML (ver sección 3)
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
3. Diagrama de clases UML
![Diagrama UML](docs/uml.png)
> **Nota:** [completar: exportar el diagrama de clases como `docs/uml.png` y subirlo al repositorio. Tiene que incluir las 13 clases e interfaces de la sección 4, con atributos, métodos, visibilidades y relaciones: composición `Jugador` → `Inventario`, asociación `Jugador` → `Ubicacion`, `Inventario` → `Recurso` (1 a N), `JugadorDAO` extiende `GenericDAO<Jugador>`, `JugadorDAOImpl` implementa `JugadorDAO`, y las excepciones heredando de `Exception` / `RuntimeException`.]
---
4. Análisis de clases y conceptos de POO aplicados
El proyecto tiene 13 archivos Java: 11 clases y 2 interfaces, organizados en 5 paquetes.
Paquete	Clases / interfaces	Cantidad
`com.islandmanager`	`Main`, `MainCRUD`	2 clases
`modelo`	`Jugador`, `Inventario`, `Recurso`, `Ubicacion`	4 clases
`dao`	`GenericDAO<T>`, `JugadorDAO` (interfaces), `JugadorDAOImpl`	2 interfaces + 1 clase
`conexion`	`ConexionDB`	1 clase
`excepciones`	`DAOException`, `RecursoInsuficienteException`, `DatoInvalidoException`	3 clases
> En la versión 1 (Primer Parcial) el proyecto tenía 5 clases: las 4 de `modelo` y `Main`. En la Actividad 2 se sumaron `MainCRUD`, la capa DAO, la conexión y las excepciones propias.
4.1 Modelo
`Recurso.java`
Rol: representa un material de la isla (Agua, Madera, Hierro…) con su cantidad y su valor unitario de intercambio.
Encapsulamiento: atributos `private int id`, `private String nombre`, `private int cantidad`, `private int valor`. Solo tiene getters; la cantidad cambia únicamente con `agregar()` y `consumir()`.
Constructores: `Recurso(String nombre, int cantidad, int valor)` normaliza los datos con `Math.max(0, cantidad)` y `Math.max(1, valor)`. `Recurso(int id, String nombre, int cantidad, int valor)` lo usa el DAO al leer de la base y reutiliza al anterior con `this(...)`.
Estructuras de control: `agregar(int cant)` suma solo si `cant > 0`. `consumir(int cant)` usa `if-else` para descontar solo si la cantidad es positiva y no supera el stock; devuelve `true` o `false`.
`toString()` (`@Override`): devuelve, por ejemplo, `Madera: 45 unidades (Valor: 3 c/u)`.
`Inventario.java`
Rol: administra el conjunto de recursos del jugador.
Colección: `private ArrayList<Recurso> listaRecursos`, una lista dinámica.
Estructuras de control:
`buscarRecurso(String nombre)`: bucle `for-each` con `if (r.getNombre().equalsIgnoreCase(nombre))`; devuelve el recurso o `null`.
`agregarRecurso(Recurso nuevo)`: ignora `null` y con `if-else` decide si suma la cantidad a un recurso existente o agrega uno nuevo a la lista.
`mostrar()`: `if-else` para el inventario vacío y `for-each` con `printf` para imprimir la tabla.
`calcularValorTotal()`: recorre la lista y acumula `cantidad * valor`.
Encapsulamiento de la colección: `getListaRecursos()` devuelve `Collections.unmodifiableList(...)`, una vista de solo lectura. Si alguien intenta modificarla desde afuera, salta `UnsupportedOperationException`.
`Ubicacion.java`
Rol: modela un bioma de la isla: `id` (coincide con la tabla `ubicacion`), nombre, recurso principal y cantidad base de recolección.
Estructura de control: `obtenerBonus(int intensidad)` usa `switch-case`: 1 (Ligero) → +5, 2 (Moderado) → +15, 3 (Exhaustivo) → +30, cualquier otro valor → 0.
Solo tiene getters: una vez creada no cambia.
`Jugador.java`
Rol: la entidad central de la simulación.
Relaciones:
Composición con `Inventario`: el jugador crea su propio inventario en el constructor y este no existe sin él (en la base se refleja con `ON DELETE CASCADE`).
Asociación con `Ubicacion`: el jugador conoce la zona donde está y puede cambiarla con `setUbicacion(...)`.
Constructores:
`Jugador(String nombre, int edad, Ubicacion ubicacionInicial)`: partida nueva; arranca con 15 de Agua y 10 de Comida.
`Jugador(int id, String nombre, int edad, Ubicacion ubicacion, Inventario inventario)`: lo usa el DAO para reconstruir una partida guardada.
Validación: `validarDatos(...)` (privado y estático) lanza `DatoInvalidoException` si el nombre está vacío, la edad está fuera de 1–120 o la ubicación es `null`. La usan los dos constructores, `setNombre` y `setEdad`.
Métodos con estructuras de control:
`recolectar(int intensidad)`: calcula base de la ubicación + bonus y, con `if-else if`, asigna el valor unitario según el recurso (Madera 3, Piedra 4, Hierro 10, el resto 5). Después lo agrega al inventario.
`negociar(String recursoDar, int cantDar, String recursoRecibir, int cantRecibir, int valorRecibir)`: es `void` y declara `throws RecursoInsuficienteException`. Si el jugador no tiene el recurso o `consumir(cantDar)` devuelve `false`, lanza la excepción con el recurso, la cantidad pedida y la disponible. Si alcanza, incorpora el recurso recibido.
Delegación: `mostrarEstado()` imprime los datos del jugador y le delega el inventario a `inventario.mostrar()`.
`toString()` (`@Override`): id, nombre, edad, ubicación, cantidad de recursos y valor total del inventario.
4.2 Excepciones propias
Clase	Hereda de	Tipo	Uso
`DAOException`	`Exception`	checked	Envuelve `SQLException` (y la guarda como causa) para que el resto del programa no dependa de JDBC.
`RecursoInsuficienteException`	`Exception`	checked	La lanza `Jugador.negociar()`. Guarda `recurso`, `cantidadPedida` y `cantidadDisponible` con getters, para que `Main` informe cuánto falta.
`DatoInvalidoException`	`RuntimeException`	unchecked	La lanza `Jugador.validarDatos()` ante datos inválidos.
4.3 Conexión
`ConexionDB.java`
Clase `final` con constructor privado: es utilitaria y no se instancia.
Lee `db.properties` desde el classpath una sola vez (`private static final Properties CONFIG`) usando try-with-resources sobre el `InputStream`. Si el archivo falta o no se puede leer, lanza `IllegalStateException`.
`getConexion()` devuelve una conexión nueva con `DriverManager.getConnection(url, user, password)` y convierte la `SQLException` en `DAOException`.
4.4 Acceso a datos (DAO)
`GenericDAO<T>` (interfaz genérica): `insertar`, `buscarPorId` (devuelve `Optional<T>`), `listarTodos`, `actualizar` y `eliminar`. Todos declaran `throws DAOException`.
`JugadorDAO` (interfaz): `extends GenericDAO<Jugador>` y agrega `buscarPorNombre(String texto)`.
`JugadorDAOImpl` (clase): `implements JugadorDAO` con JDBC. Las sentencias SQL son constantes `private static final String` y todas se ejecutan con `PreparedStatement`. Al leer, `mapearJugador(...)` arma la `Ubicacion` (con `JOIN`) y `cargarInventario(...)` reconstruye el inventario. Detalles en la sección 7.4.
4.5 Programas principales
`Main.java` (el juego)
`Scanner`: pide nombre, edad, ubicación inicial y las opciones del menú. Si el nombre queda vacío, usa `"Superviviente"`.
Colección: las ubicaciones se cargan en un `Map<Integer, Ubicacion>` (`LinkedHashMap`), donde la clave es el número de opción del menú.
Bucle principal: `while (ejecutando)` con `switch (opcion)` y 6 opciones: recolectar, trueque, ver estado, cambiar de ubicación, guardar partida en MySQL y salir.
Trueque: captura `RecursoInsuficienteException` y muestra cuántas unidades faltan.
Guardar partida: `guardarPartida(JugadorDAO dao, Jugador jugador)` hace `insertar` si el id es 0 (partida nunca guardada) y `actualizar` en las veces siguientes. Si MySQL no responde, captura `DAOException`, avisa y el juego sigue.
Robustez de entrada: `leerEntero(Scanner, min, max)` lee con `nextLine()`, convierte con `Integer.parseInt`, valida el rango dentro de un `while (true)` y captura `NumberFormatException` (texto en lugar de número) y `NoSuchElementException` (entrada cerrada, por ejemplo con Ctrl+D).
`MainCRUD.java` (prueba de la Actividad 2)
Prueba la conexión (con try-with-resources y multi-catch `DAOException | SQLException`) y después ejecuta, en orden: CREATE (2 jugadores), READ (listar todos, por id y por nombre), UPDATE y DELETE. Al final fuerza 4 excepciones para mostrar que se capturan. Su salida está en la sección 6.2.
---
5. Cómo ejecutar
5.1 Requisitos
JDK 17 o superior
Maven 3
MySQL 8
5.2 Base de datos
Crear la base ejecutando el script (desde MySQL Workbench o la consola):
```bash
mysql -u root -p < sql/island_manager.sql
```
> **Ojo:** el script empieza con `DROP DATABASE IF EXISTS island_manager`. Si lo volvés a correr, se borran las partidas guardadas.
5.3 Credenciales (`db.properties`)
El archivo `src/main/resources/db.properties` trae credenciales de ejemplo (`root` / `root`) para una instalación local de prueba:
```properties
db.url=jdbc:mysql://localhost:3306/island_manager?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires&allowPublicKeyRetrieval=true
db.user=root
db.password=root
```
Para usar otro usuario o contraseña, editá `db.user` y `db.password` (y `db.url` si MySQL no está en `localhost:3306`). Volvé a compilar después del cambio, porque Maven copia el archivo a `target/classes`. No subas al repositorio contraseñas reales.
5.4 Compilar y correr
```bash
mvn compile

# Prueba de conexión + CRUD (MainCRUD es la clase por defecto del pom)
mvn exec:java

# Juego por consola
mvn exec:java -Dexec.mainClass=com.islandmanager.Main
```
La clase por defecto se define en la propiedad `exec.mainClass` del `pom.xml`, y `-Dexec.mainClass=...` la reemplaza al ejecutar. También se puede abrir como proyecto Maven en IntelliJ, NetBeans o Eclipse y ejecutar cada `Main` directamente.
---
6. Demostración de flujo en consola
6.1 Juego (`Main`)
Lo que escribe el usuario aparece después de cada `:`. Para abreviar, se muestra el menú completo solo la primera vez.
```text
************************************************************
*                                                          *
*            ----- ISLAND MANAGER -----                    *
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

Seleccione una accion (1-6): 2

--- PUESTO DE COMERCIO Y TRUEQUE ---
Ofertas de mercaderes locales:
1. Entregar 20 de Madera -> Recibir 10 de Hierro
2. Entregar 15 de Piedra -> Recibir 10 de Comida
3. Entregar 10 de Agua   -> Recibir 15 de Madera
4. Cancelar y volver
Seleccione oferta (1-4): 1
>> ¡Negociacion exitosa! Intercambiaste: 20 de Madera por 10 de Hierro.

Seleccione una accion (1-6): 3

========================================================
               ESTADO DEL SUPERVIVIENTE                 
========================================================
  Nombre: Pepito Lopez | Edad: 22 anios
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

Seleccione una accion (1-6): 5
>> Partida guardada. Tu numero de partida es #1.

Seleccione una accion (1-6): 6

========================================================
Gracias por jugar a Island Manager. ¡Hasta la proxima!
========================================================
```
Si en el trueque no alcanza el recurso, el juego no se corta y muestra, por ejemplo:
```text
>> Error en el trueque: Recurso insuficiente: se necesitan 20 de Madera y solo hay 0.
>> Te faltan 20 unidades de Madera.
```
6.2 Prueba de CRUD (`MainCRUD`)
Salida con la base recién creada por el script:
```text
=== PRUEBA DE CONEXION Y CRUD - ISLAND MANAGER ===

[OK] Conectado a: jdbc:mysql://localhost:3306/island_manager?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires&allowPublicKeyRetrieval=true
     Motor: MySQL 8.4.3

--- CREATE: insertar jugadores ---

>> ¡Recoleccion exitosa en Bosque Nuboso!
>> Has obtenido: 65 unidades de Madera (Base: 35 + Bonus: 30).
Insertado -> Jugador #1 | Camilo (24 anios) | Bosque Nuboso | 4 recursos, valor total: 295
Insertado -> Jugador #2 | Georgina (30 anios) | Playa Arrecife | 2 recursos, valor total: 60

--- READ: listar todos ---
  Jugador #1 | Camilo (24 anios) | Bosque Nuboso | 4 recursos, valor total: 295
  Jugador #2 | Georgina (30 anios) | Playa Arrecife | 2 recursos, valor total: 60

--- READ: buscar por id 1 ---
  Jugador #1 | Camilo (24 anios) | Bosque Nuboso | 4 recursos, valor total: 295

+------------------------------------------------------+
|               INVENTARIO DE RECURSOS                 |
+------------------------------------------------------+
|  • Agua         :   15 unidades  (Valor:   2 c/u) |
|  • Comida       :   10 unidades  (Valor:   3 c/u) |
|  • Madera       :   45 unidades  (Valor:   3 c/u) |
|  • Hierro       :   10 unidades  (Valor:  10 c/u) |
+------------------------------------------------------+

--- READ: buscar por nombre que contenga 'geo' ---
  Jugador #2 | Georgina (30 anios) | Playa Arrecife | 2 recursos, valor total: 60

--- UPDATE: cambiar edad, ubicación e inventario de Georgina ---
>> Te has trasladado a: Bosque Nuboso

>> ¡Recoleccion exitosa en Bosque Nuboso!
>> Has obtenido: 50 unidades de Madera (Base: 35 + Bonus: 15).
Actualizado -> Jugador #2 | Georgina (31 anios) | Bosque Nuboso | 3 recursos, valor total: 210

--- DELETE: eliminar a Camilo (id 1) ---
¿Sigue existiendo? false
Registros restantes: 1

--- PRUEBAS DE EXCEPCIONES ---
[DAOException capturada] No existe un jugador con id 99999
[RecursoInsuficienteException capturada] Recurso insuficiente: se necesitan 5 de Hierro y solo hay 0
[DatoInvalidoException capturada] El nombre del jugador no puede estar vacío
[UnsupportedOperationException capturada] El inventario no se puede modificar por fuera de la clase Inventario.

=== FIN DE LA PRUEBA ===
```
> Si MySQL no está iniciado, `MainCRUD` muestra `[ERROR] No se pudo conectar a la base de datos: ...` y termina sin ejecutar el resto.
---
7. Actividad 2 — Persistencia con JDBC, DAO, excepciones y colecciones
7.1 Consignas y dónde se cumplen
Consigna	Dónde está
a. Manejo de excepciones (varias)	`excepciones/` (3 propias). Se lanzan en `Jugador.negociar()`, `Jugador.validarDatos()`, `ConexionDB.getConexion()` y `JugadorDAOImpl`, y se capturan en `Main` (trueque, `guardarPartida`, `leerEntero`) y en `MainCRUD`. Ver 7.5.
b. Al menos una colección	`ArrayList<Recurso>` en `Inventario`, `Map<Integer, Ubicacion>` (`LinkedHashMap`) en `Main`, `List<Jugador>` en `JugadorDAOImpl`. Ver 7.6.
c. Estructura DAO con Maven	`pom.xml` (dependencia `mysql-connector-j` y `exec-maven-plugin`) + paquete `dao/`: `GenericDAO`, `JugadorDAO`, `JugadorDAOImpl`. Ver 7.3.
d. Base MySQL con tablas del proyecto	`sql/island_manager.sql`: tablas `ubicacion`, `jugador` y `recurso`. Ver 7.2.
e. Conexión desde Java probada desde un Main	`conexion/ConexionDB.java` + bloque 1 de `MainCRUD.main()` (muestra URL y versión del motor).
f. Al menos una interfaz	`dao/GenericDAO.java` y `dao/JugadorDAO.java`.
g. CRUD con JDBC probado desde Main (mínimo 2 instrucciones)	`JugadorDAOImpl` + `MainCRUD` (insertar, listar, buscar por id y por nombre, actualizar, eliminar). Además, la opción 5 de `Main` usa `insertar` y `actualizar`.
7.2 Base de datos MySQL
Tabla	Campos	Relación
`ubicacion`	id, nombre, recurso_principal, cantidad_base	Catálogo de biomas (se carga con 3 registros)
`jugador`	id, nombre, edad, ubicacion_id, fecha_alta	FK a `ubicacion` (N jugadores → 1 ubicación)
`recurso`	id, jugador_id, nombre, cantidad, valor	FK a `jugador` con `ON DELETE CASCADE` (1 jugador → N recursos)
Las tablas reflejan las clases del modelo: `Jugador` se asocia a una `Ubicacion` y es dueño de su `Inventario` (lista de `Recurso`). Por eso, al borrar un jugador se borran también sus recursos. Además, los `CHECK` repiten en la base las reglas de Java (edad entre 1 y 120, cantidad ≥ 0, valor ≥ 1), y `UNIQUE (jugador_id, nombre)` impide que un jugador tenga dos filas del mismo recurso.
7.3 Estructura DAO
`GenericDAO<T>` (interfaz): define el contrato CRUD: `insertar`, `buscarPorId`, `listarTodos`, `actualizar`, `eliminar`.
`JugadorDAO` (interfaz): hereda de `GenericDAO<Jugador>` y agrega `buscarPorNombre`.
`JugadorDAOImpl`: implementa las interfaces con JDBC.
`ConexionDB`: centraliza la conexión leyendo los datos de `db.properties`.
Así, el juego (`Main`) solo conoce la interfaz `JugadorDAO` (`JugadorDAO jugadorDAO = new JugadorDAOImpl();`) y no sabe nada de SQL. Si mañana cambiáramos MySQL por otro motor, solo habría que escribir otra implementación.
7.4 CRUD con JDBC (sentencias preparadas)
Todas las consultas usan `PreparedStatement` con parámetros `?`:
```java
PreparedStatement ps = con.prepareStatement("UPDATE jugador SET nombre = ?, edad = ?, ubicacion_id = ? WHERE id = ?");
ps.setString(1, jugador.getNombre());
ps.setInt(2, jugador.getEdad());
```
Evitan inyección SQL: los valores se envían separados del texto de la sentencia.
La base puede reutilizar la sentencia ya compilada con distintos valores.
`executeUpdate()` se usa para INSERT, UPDATE y DELETE y devuelve las filas afectadas: si `actualizar` o `eliminar` no afectan ninguna fila, se lanza `DAOException("No existe un jugador con id ...")`. `executeQuery()` se usa para los SELECT y devuelve un `ResultSet`.
`insertar` usa `Statement.RETURN_GENERATED_KEYS` para obtener el id `AUTO_INCREMENT`.
El inventario se guarda con `addBatch()` / `executeBatch()` (varias filas en un solo envío).
`insertar` y `actualizar` usan transacciones (`setAutoCommit(false)`, `commit()`, `rollback()`): se guarda el jugador con todo su inventario o no se guarda nada. `actualizar` reemplaza el inventario: borra los recursos del jugador y los vuelve a insertar.
`MainCRUD` prueba: conexión → CREATE (2 jugadores) → READ (listar, por id, por nombre) → UPDATE → DELETE.
Además, en el juego se agregó la opción 5. Guardar partida: la primera vez hace `insertar` y luego `actualizar`.
7.5 Manejo de excepciones
Excepción	Tipo	Dónde se usa
`DAOException` (propia)	checked	Envuelve `SQLException` en la capa DAO. Obliga a quien guarda o lee a contemplar el error.
`RecursoInsuficienteException` (propia)	checked	`Jugador.negociar()` la lanza si no alcanza el recurso; `Main` la captura e informa cuánto falta. Reemplaza al `boolean` que devolvía `negociar` en la versión 1.
`DatoInvalidoException` (propia)	unchecked	Validación de nombre, edad y ubicación en los constructores y setters de `Jugador`.
`NumberFormatException`	unchecked	`leerEntero()` cuando el usuario escribe texto.

`NoSuchElementException`	unchecked	`leerEntero()` si se cierra la entrada del Scanner.
`UnsupportedOperationException`	unchecked	Si se intenta modificar la lista devuelta por `Inventario.getListaRecursos()`.
`SQLException` / `IOException`	checked	Conexión y lectura de `db.properties`.
`IllegalStateException`	unchecked	`ConexionDB` la lanza si no encuentra o no puede leer `db.properties`.
También se usan try-with-resources (cierre automático de `Connection`, `PreparedStatement`, `ResultSet` e `InputStream`), multi-catch (`catch (DAOException | SQLException e)`) y finally para cerrar la conexión en las transacciones.
Checked vs unchecked: las checked representan problemas externos que pueden pasar aunque el código esté bien (la base no responde, no alcanzan los recursos) y el compilador obliga a manejarlas. Las unchecked representan errores de programación o de datos (edad 200) y no es obligatorio capturarlas.
7.6 Colecciones y tipos primitivos
Colección	Dónde	Por qué
`ArrayList<Recurso>`	`Inventario`	Lista dinámica: crece a medida que se recolectan recursos nuevos.
`List<Jugador>`	`listarTodos()`, `buscarPorNombre()`	Resultado de las consultas; no se sabe de antemano cuántas filas vuelven.
`Map<Integer, Ubicacion>` (`LinkedHashMap`)	`Main`	Catálogo de ubicaciones: la clave es la opción del menú. Reemplazó los `if-else` y mantiene el orden de inserción.
`Collections.unmodifiableList`	`Inventario.getListaRecursos()`	Devuelve la lista en modo solo lectura para proteger el encapsulamiento.
`Optional<Jugador>`	`buscarPorId()`	Indica explícitamente que el jugador puede no existir, en lugar de devolver `null`. (No es una colección, pero cumple un rol parecido: un contenedor de 0 o 1 elemento.)
Colecciones vs. primitivos: las colecciones solo guardan objetos, no tipos primitivos. Por eso el mapa usa `Integer` (clase envoltorio o wrapper) y no `int`. Java convierte automáticamente entre ambos: hay autoboxing al hacer `ubicaciones.put(1, ...)` o `ubicaciones.get(opcionUbicacion)` con un `int`, y unboxing cuando un `Integer` se asigna a un `int`.
En los atributos del modelo (`edad`, `cantidad`, `valor`, `id`) se usan primitivos `int`: siempre tienen valor y las cuentas no necesitan conversiones. Como un `int` no puede ser `null`, el valor por defecto `0` en `Jugador.id` se usa para indicar "partida todavía no guardada".
	Arreglo de primitivos (`int[]`)	Colección (`ArrayList<Integer>`)
Tamaño	Fijo al crearlo	Dinámico
Contenido	Valores primitivos	Objetos (wrappers)
Valor por defecto / nulos	`0`, no admite `null`	Admite `null` (puede causar `NullPointerException` al hacer unboxing)
Rendimiento	Más rápido y usa menos memoria	Algo más costoso por el boxing
Métodos	Solo `length`	`add`, `remove`, `contains`, `size`, etc.
---
8. Uso de Inteligencia Artificial
Herramienta utilizada: [completar: nombre de la herramienta de IA].
Para qué la usamos: Usamos la IA como apoyo para comprender mejor el código que ya teníamos armado de la Actividad 2. Le pedimos que nos explique clase por clase y método por método qué hace cada parte (modelo, DAO, excepciones, conexión, etc.), las mejoras respecto a la versión anterior del proyecto, y que profundice en el patrón DAO. También nos ayudó a armar el paso a paso para hacer el commit y subir la nueva versión al repositorio de forma ordenada. [completar si corresponde: si también la usaron para revisar el README o detectar errores, por ejemplo el comando de `exec:java`, aclararlo acá.]
Qué aprendimos: Aprendimos a leer y explicar con más claridad una estructura DAO con interfaces genéricas, implementaciones JDBC, transacciones y manejo de excepciones propias. También entendimos mejor cómo se separa la lógica de negocio del acceso a datos, y practicamos el flujo de Git (status, add, commit y push) sobre un repositorio ya existente. La IA nos sirvió para consolidar lo que ya habíamos investigado y programado, no para generar el código desde cero. [completar si corresponde: algún concepto puntual que hayan aprendido, por ejemplo la diferencia entre `Statement` y `PreparedStatement`.]
