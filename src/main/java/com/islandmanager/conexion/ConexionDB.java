package com.islandmanager.conexion;

import com.islandmanager.excepciones.DAOException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centraliza la creación de conexiones JDBC a MySQL.
 * Los datos de acceso se leen de src/main/resources/db.properties
 * para no dejarlos "hardcodeados" en el código.
 */
public final class ConexionDB {

    private static final Properties CONFIG = cargarConfiguracion();

    private ConexionDB() {
        // Clase utilitaria: no se instancia
    }

    private static Properties cargarConfiguracion() {
        Properties props = new Properties();
        // try-with-resources: el InputStream se cierra solo aunque ocurra un error
        try (InputStream in = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró el archivo db.properties en resources");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error al leer db.properties: " + e.getMessage(), e);
        }
        return props;
    }

    /**
     * Abre una nueva conexión. Quien la pide es responsable de cerrarla
     * (idealmente con try-with-resources).
     *
     * @return conexión abierta a la base island_manager
     * @throws DAOException si MySQL no está disponible o las credenciales son incorrectas
     */
    public static Connection getConexion() throws DAOException {
        try {
            return DriverManager.getConnection(
                    CONFIG.getProperty("db.url"),
                    CONFIG.getProperty("db.user"),
                    CONFIG.getProperty("db.password"));
        } catch (SQLException e) {
            throw new DAOException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }
}
