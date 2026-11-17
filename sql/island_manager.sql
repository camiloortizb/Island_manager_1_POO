-- =====================================================
-- Island Manager - Script de creación de base de datos
-- Motor: MySQL 8
-- =====================================================

DROP DATABASE IF EXISTS island_manager;
CREATE DATABASE island_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE island_manager;

-- Biomas / zonas de la isla
CREATE TABLE ubicacion (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(50) NOT NULL UNIQUE,
    recurso_principal VARCHAR(30) NOT NULL,
    cantidad_base     INT         NOT NULL CHECK (cantidad_base > 0)
);

-- Supervivientes (partidas guardadas)
CREATE TABLE jugador (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(60) NOT NULL,
    edad         INT         NOT NULL CHECK (edad BETWEEN 1 AND 120),
    ubicacion_id INT         NOT NULL,
    fecha_alta   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_jugador_ubicacion FOREIGN KEY (ubicacion_id) REFERENCES ubicacion(id)
);

-- Recursos del inventario de cada jugador
CREATE TABLE recurso (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    jugador_id INT         NOT NULL,
    nombre     VARCHAR(30) NOT NULL,
    cantidad   INT         NOT NULL CHECK (cantidad >= 0),
    valor      INT         NOT NULL CHECK (valor >= 1),
    CONSTRAINT fk_recurso_jugador FOREIGN KEY (jugador_id) REFERENCES jugador(id) ON DELETE CASCADE,
    CONSTRAINT uq_recurso_jugador UNIQUE (jugador_id, nombre)
);

-- Datos iniciales: mismas ubicaciones que usa el juego
INSERT INTO ubicacion (id, nombre, recurso_principal, cantidad_base) VALUES
    (1, 'Playa Arrecife', 'Agua',   20),
    (2, 'Bosque Nuboso',  'Madera', 35),
    (3, 'Montana Rocosa', 'Piedra', 25);
