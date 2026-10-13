-- ==============================================================
-- ControlPark-IQ · Servicio web de autenticación
-- Script de la base de datos (MySQL 8)
--
-- No es obligatorio ejecutarlo: al iniciar el proyecto, Spring Boot
-- crea la base de datos y la tabla automáticamente
-- (createDatabaseIfNotExist=true y ddl-auto=update).
-- Se incluye como documentación del diseño y para crearla a mano
-- desde MySQL Workbench si se prefiere.
-- ==============================================================

CREATE DATABASE IF NOT EXISTS controlpark_auth
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE controlpark_auth;

-- Tabla de usuarios del sistema (equivale a la entidad Usuario.java)
CREATE TABLE IF NOT EXISTS usuarios (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_usuario  VARCHAR(20)  NOT NULL,                -- en minúsculas
    contrasena      VARCHAR(60)  NOT NULL,                -- hash BCrypt, nunca texto plano
    rol             VARCHAR(15)  NOT NULL DEFAULT 'RESIDENTE',
    activo          BIT(1)       NOT NULL DEFAULT b'1',
    fecha_registro  DATETIME(6)  NOT NULL,
    ultimo_acceso   DATETIME(6)  NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_nombre_usuario UNIQUE (nombre_usuario),
    CONSTRAINT ck_usuarios_rol CHECK (rol IN ('ADMINISTRADOR', 'PORTERIA', 'RESIDENTE'))
) ENGINE = InnoDB;

-- Consulta útil para la EV02: ver que la contraseña quedó protegida
-- SELECT id, nombre_usuario, contrasena, rol, activo, fecha_registro, ultimo_acceso FROM usuarios;
