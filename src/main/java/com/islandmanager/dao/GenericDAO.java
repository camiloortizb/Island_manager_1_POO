package com.islandmanager.dao;

import com.islandmanager.excepciones.DAOException;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica con las operaciones CRUD básicas.
 * T es el tipo de entidad que se persiste (por ejemplo, Jugador).
 */
public interface GenericDAO<T> {

    /** CREATE: inserta la entidad y le asigna el id generado por la base. */
    void insertar(T entidad) throws DAOException;

    /** READ: busca por clave primaria. Devuelve Optional vacío si no existe. */
    Optional<T> buscarPorId(int id) throws DAOException;

    /** READ: devuelve todos los registros. */
    List<T> listarTodos() throws DAOException;

    /** UPDATE: actualiza los datos de una entidad existente. */
    void actualizar(T entidad) throws DAOException;

    /** DELETE: elimina por clave primaria. */
    void eliminar(int id) throws DAOException;
}
