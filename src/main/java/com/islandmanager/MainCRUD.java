package com.islandmanager;

import com.islandmanager.conexion.ConexionDB;
import com.islandmanager.dao.JugadorDAO;
import com.islandmanager.dao.JugadorDAOImpl;
import com.islandmanager.excepciones.DAOException;
import com.islandmanager.excepciones.DatoInvalidoException;
import com.islandmanager.excepciones.RecursoInsuficienteException;
import com.islandmanager.modelo.Jugador;
import com.islandmanager.modelo.Ubicacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Programa de prueba de la capa de persistencia (Actividad 2).
 * Prueba la conexión a MySQL y ejecuta las 4 operaciones del CRUD con JDBC
 * sobre la tabla "jugador" (y su inventario en la tabla "recurso").
 *
 * Requisito previo: haber ejecutado sql/island_manager.sql en MySQL.
 */
public class MainCRUD {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXION Y CRUD - ISLAND MANAGER ===\n");

        // ---------- 1. Prueba de conexión ----------
        // try-with-resources: la conexión se cierra automáticamente al terminar el bloque
        try (Connection con = ConexionDB.getConexion()) {
            System.out.println("[OK] Conectado a: " + con.getMetaData().getURL());
            System.out.println("     Motor: " + con.getMetaData().getDatabaseProductName()
                    + " " + con.getMetaData().getDatabaseProductVersion() + "\n");
        } catch (DAOException | SQLException e) { // multi-catch
            System.out.println("[ERROR] " + e.getMessage());
            System.out.println("Revisar que MySQL esté iniciado y los datos de db.properties.");
            return;
        }

        JugadorDAO dao = new JugadorDAOImpl();
        Ubicacion playa = new Ubicacion(1, "Playa Arrecife", "Agua", 20);
        Ubicacion bosque = new Ubicacion(2, "Bosque Nuboso", "Madera", 35);

        try {
            // ---------- 2. CREATE ----------
            System.out.println("--- CREATE: insertar jugadores ---");
            Jugador camilo = new Jugador("Camilo", 24, bosque);
            camilo.recolectar(3);                         // suma Madera al inventario
            camilo.negociar("Madera", 20, "Hierro", 10, 10); // trueque válido
            dao.insertar(camilo);
            System.out.println("Insertado -> " + camilo);

            Jugador georgina = new Jugador("Georgina", 30, playa);
            dao.insertar(georgina);
            System.out.println("Insertado -> " + georgina);

            // ---------- 3. READ ----------
            System.out.println("\n--- READ: listar todos ---");
            List<Jugador> jugadores = dao.listarTodos();
            for (Jugador j : jugadores) {
                System.out.println("  " + j);
            }

            System.out.println("\n--- READ: buscar por id " + camilo.getId() + " ---");
            Optional<Jugador> encontrado = dao.buscarPorId(camilo.getId());
            encontrado.ifPresent(j -> {
                System.out.println("  " + j);
                j.getInventario().mostrar();
            });

            System.out.println("\n--- READ: buscar por nombre que contenga 'geo' ---");
            dao.buscarPorNombre("geo").forEach(j -> System.out.println("  " + j));

            // ---------- 4. UPDATE ----------
            System.out.println("\n--- UPDATE: cambiar edad, ubicación e inventario de Georgina ---");
            georgina.setEdad(31);
            georgina.setUbicacion(bosque);
            georgina.recolectar(2);
            dao.actualizar(georgina);
            System.out.println("Actualizado -> " + dao.buscarPorId(georgina.getId()).orElseThrow());

            // ---------- 5. DELETE ----------
            System.out.println("\n--- DELETE: eliminar a Camilo (id " + camilo.getId() + ") ---");
            dao.eliminar(camilo.getId());
            boolean existe = dao.buscarPorId(camilo.getId()).isPresent();
            System.out.println("¿Sigue existiendo? " + existe);
            System.out.println("Registros restantes: " + dao.listarTodos().size());

        } catch (DAOException e) {
            System.out.println("[ERROR DE BASE DE DATOS] " + e.getMessage());
        } catch (RecursoInsuficienteException e) {
            System.out.println("[ERROR DE JUEGO] " + e.getMessage());
        }

        // ---------- 6. Pruebas de manejo de excepciones ----------
        System.out.println("\n--- PRUEBAS DE EXCEPCIONES ---");

        // a) Excepción checked propia de la capa DAO: eliminar un id que no existe
        try {
            dao.eliminar(99999);
        } catch (DAOException e) {
            System.out.println("[DAOException capturada] " + e.getMessage());
        }

        // b) Excepción checked propia del juego: trueque sin recursos suficientes
        try {
            Jugador pobre = new Jugador("Sin recursos", 20, playa);
            pobre.negociar("Hierro", 5, "Comida", 5, 3);
        } catch (RecursoInsuficienteException e) {
            System.out.println("[RecursoInsuficienteException capturada] " + e.getMessage());
        }

        // c) Excepción unchecked propia: datos inválidos en el constructor
        try {
            new Jugador("", 200, playa);
        } catch (DatoInvalidoException e) {
            System.out.println("[DatoInvalidoException capturada] " + e.getMessage());
        }

        // d) Excepción de la colección: la lista del inventario es de solo lectura desde afuera
        try {
            Jugador prueba = new Jugador("Prueba", 20, playa);
            prueba.getInventario().getListaRecursos().clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("[UnsupportedOperationException capturada] El inventario no se puede modificar por fuera de la clase Inventario.");
        }

        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }
}
