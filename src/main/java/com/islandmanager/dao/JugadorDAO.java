package com.islandmanager.dao;

import com.islandmanager.excepciones.DAOException;
import com.islandmanager.modelo.Jugador;

import java.util.List;

/**
 * Operaciones de persistencia específicas del Jugador.
 * Hereda el CRUD genérico y agrega búsquedas propias del dominio.
 */
public interface JugadorDAO extends GenericDAO<Jugador> {

    /** Busca jugadores cuyo nombre contenga el texto indicado. */
    List<Jugador> buscarPorNombre(String texto) throws DAOException;
}
