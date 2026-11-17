package com.islandmanager.dao;

import com.islandmanager.conexion.ConexionDB;
import com.islandmanager.excepciones.DAOException;
import com.islandmanager.modelo.Inventario;
import com.islandmanager.modelo.Jugador;
import com.islandmanager.modelo.Recurso;
import com.islandmanager.modelo.Ubicacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC del JugadorDAO.
 * Todas las consultas usan PreparedStatement (sentencias preparadas con "?"),
 * que evitan inyección SQL y permiten reutilizar la sentencia con distintos valores.
 * Un jugador se guarda junto con su inventario dentro de una TRANSACCIÓN:
 * o se graba todo, o no se graba nada (rollback).
 */
public class JugadorDAOImpl implements JugadorDAO {

    // --- Sentencias SQL ---
    private static final String SQL_INSERT_JUGADOR =
            "INSERT INTO jugador (nombre, edad, ubicacion_id) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BASE =
            "SELECT j.id, j.nombre, j.edad, u.id AS u_id, u.nombre AS u_nombre, "
            + "u.recurso_principal, u.cantidad_base "
            + "FROM jugador j JOIN ubicacion u ON j.ubicacion_id = u.id";
    private static final String SQL_SELECT_POR_ID = SQL_SELECT_BASE + " WHERE j.id = ?";
    private static final String SQL_SELECT_POR_NOMBRE = SQL_SELECT_BASE + " WHERE j.nombre LIKE ? ORDER BY j.id";
    private static final String SQL_SELECT_TODOS = SQL_SELECT_BASE + " ORDER BY j.id";
    private static final String SQL_UPDATE_JUGADOR =
            "UPDATE jugador SET nombre = ?, edad = ?, ubicacion_id = ? WHERE id = ?";
    private static final String SQL_DELETE_JUGADOR = "DELETE FROM jugador WHERE id = ?";

    private static final String SQL_INSERT_RECURSO =
            "INSERT INTO recurso (jugador_id, nombre, cantidad, valor) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_RECURSOS =
            "SELECT id, nombre, cantidad, valor FROM recurso WHERE jugador_id = ? ORDER BY id";
    private static final String SQL_DELETE_RECURSOS = "DELETE FROM recurso WHERE jugador_id = ?";

    // ===================== CREATE =====================
    @Override
    public void insertar(Jugador jugador) throws DAOException {
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false); // inicio de la transacción

            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_JUGADOR, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, jugador.getNombre());
                ps.setInt(2, jugador.getEdad());
                ps.setInt(3, jugador.getUbicacion().getId());
                ps.executeUpdate();

                // Recuperamos el id AUTO_INCREMENT generado por MySQL
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        jugador.setId(rs.getInt(1));
                    }
                }
            }

            insertarRecursos(con, jugador);
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            throw new DAOException("Error al insertar el jugador: " + e.getMessage(), e);
        } finally {
            cerrar(con);
        }
    }

    // ===================== READ =====================
    @Override
    public Optional<Jugador> buscarPorId(int id) throws DAOException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearJugador(con, rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar el jugador con id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Jugador> listarTodos() throws DAOException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {
            List<Jugador> jugadores = new ArrayList<>();
            while (rs.next()) {
                jugadores.add(mapearJugador(con, rs));
            }
            return jugadores;
        } catch (SQLException e) {
            throw new DAOException("Error al listar jugadores: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Jugador> buscarPorNombre(String texto) throws DAOException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_NOMBRE)) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Jugador> jugadores = new ArrayList<>();
                while (rs.next()) {
                    jugadores.add(mapearJugador(con, rs));
                }
                return jugadores;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar jugadores por nombre: " + e.getMessage(), e);
        }
    }

    // ===================== UPDATE =====================
    /**
     * Actualiza los datos del jugador y reemplaza su inventario por el actual (en una transacción).
     */
    @Override
    public void actualizar(Jugador jugador) throws DAOException {
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE_JUGADOR)) {
                ps.setString(1, jugador.getNombre());
                ps.setInt(2, jugador.getEdad());
                ps.setInt(3, jugador.getUbicacion().getId());
                ps.setInt(4, jugador.getId());
                if (ps.executeUpdate() == 0) {
                    throw new DAOException("No existe un jugador con id " + jugador.getId());
                }
            }

            try (PreparedStatement ps = con.prepareStatement(SQL_DELETE_RECURSOS)) {
                ps.setInt(1, jugador.getId());
                ps.executeUpdate();
            }
            insertarRecursos(con, jugador);

            con.commit();
        } catch (SQLException e) {
            rollback(con);
            throw new DAOException("Error al actualizar el jugador: " + e.getMessage(), e);
        } catch (DAOException e) {
            rollback(con);
            throw e;
        } finally {
            cerrar(con);
        }
    }

    // ===================== DELETE =====================
    /**
     * Elimina al jugador. Sus recursos se borran solos por el ON DELETE CASCADE de la FK.
     */
    @Override
    public void eliminar(int id) throws DAOException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_JUGADOR)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new DAOException("No existe un jugador con id " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el jugador: " + e.getMessage(), e);
        }
    }

    // ===================== Métodos auxiliares =====================

    /** Inserta todos los recursos del inventario usando batch (varias filas en un solo envío). */
    private void insertarRecursos(Connection con, Jugador jugador) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_RECURSO)) {
            for (Recurso r : jugador.getInventario().getListaRecursos()) {
                ps.setInt(1, jugador.getId());
                ps.setString(2, r.getNombre());
                ps.setInt(3, r.getCantidad());
                ps.setInt(4, r.getValor());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /** Convierte la fila actual del ResultSet en un objeto Jugador con su inventario. */
    private Jugador mapearJugador(Connection con, ResultSet rs) throws SQLException {
        Ubicacion ubicacion = new Ubicacion(
                rs.getInt("u_id"),
                rs.getString("u_nombre"),
                rs.getString("recurso_principal"),
                rs.getInt("cantidad_base"));

        int jugadorId = rs.getInt("id");
        Inventario inventario = cargarInventario(con, jugadorId);

        return new Jugador(jugadorId, rs.getString("nombre"), rs.getInt("edad"), ubicacion, inventario);
    }

    private Inventario cargarInventario(Connection con, int jugadorId) throws SQLException {
        Inventario inventario = new Inventario();
        try (PreparedStatement ps = con.prepareStatement(SQL_SELECT_RECURSOS)) {
            ps.setInt(1, jugadorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inventario.agregarRecurso(new Recurso(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getInt("valor")));
                }
            }
        }
        return inventario;
    }

    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                System.err.println("No se pudo deshacer la transacción: " + e.getMessage());
            }
        }
    }

    private void cerrar(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.err.println("No se pudo cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
