-- ==============================================================
-- ControlPark-IQ · API REST del proyecto
-- Script de la base de datos (MySQL 8)
-- Evidencia GA7-220501096-AA5-EV03 · Aprendiz: Carlos Eduardo Valero
--
-- No es obligatorio ejecutarlo: al iniciar el proyecto, Spring Boot
-- crea la base de datos y las tablas automáticamente
-- (createDatabaseIfNotExist=true y ddl-auto=update).
-- Se incluye como documentación del diseño y para crearla a mano
-- desde MySQL Workbench si se prefiere.
--
-- Para repetir las pruebas de Postman desde cero:
--   DROP DATABASE controlpark_iq_api;   (y volver a ejecutar el proyecto)
-- ==============================================================

CREATE DATABASE IF NOT EXISTS controlpark_iq_api
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE controlpark_iq_api;


CREATE TABLE espacio_parqueo (
    id bigint not null auto_increment,
    zona_id bigint not null,
    codigo varchar(10) not null,
    estado enum ('DISPONIBLE','INHABILITADO','OCUPADO','RESERVADO') not null,
    tipo_vehiculo enum ('CARRO','MOTO') not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE factura_visitante (
    horas_cobradas integer not null,
    pagada bit not null,
    valor_hora decimal(10,2) not null,
    valor_total decimal(10,2) not null,
    fecha_emision datetime(6) not null,
    fecha_pago datetime(6),
    id bigint not null auto_increment,
    sesion_id bigint not null,
    metodo_pago enum ('EFECTIVO','TARJETA','TRANSFERENCIA'),
    primary key (id)
) engine=InnoDB;

CREATE TABLE persona (
    id bigint not null auto_increment,
    unidad_id bigint,
    documento varchar(10) not null,
    telefono varchar(10),
    apellidos varchar(60) not null,
    nombres varchar(60) not null,
    correo varchar(100),
    tipo enum ('RESIDENTE','VISITANTE') not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE sesion_parqueo (
    espacio_id bigint not null,
    hora_entrada datetime(6) not null,
    hora_salida datetime(6),
    id bigint not null auto_increment,
    vehiculo_id bigint not null,
    estado enum ('ABIERTA','CERRADA') not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE tarifa (
    valor_hora decimal(10,2) not null,
    valor_mensualidad decimal(10,2) not null,
    id bigint not null auto_increment,
    tipo_vehiculo enum ('CARRO','MOTO') not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE unidad_residencial (
    cupos_parqueo integer not null,
    id bigint not null auto_increment,
    apartamento varchar(10) not null,
    torre varchar(10) not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE usuarios (
    activo bit not null,
    fecha_registro datetime(6) not null,
    id bigint not null auto_increment,
    ultimo_acceso datetime(6),
    nombre_usuario varchar(20) not null,
    contrasena varchar(60) not null,
    rol enum ('ADMINISTRADOR','PORTERIA','RESIDENTE') not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE vehiculo (
    placa varchar(6) not null,
    id bigint not null auto_increment,
    propietario_id bigint not null,
    color varchar(20),
    marca varchar(30),
    tipo enum ('CARRO','MOTO') not null,
    primary key (id)
) engine=InnoDB;

CREATE TABLE zona_parqueo (
    id bigint not null auto_increment,
    nombre varchar(40) not null,
    descripcion varchar(150),
    tipo enum ('RESIDENTES','VISITANTES') not null,
    primary key (id)
) engine=InnoDB;

ALTER TABLE espacio_parqueo 
   add constraint UKejqknx78cd0keva5q3rg5cpm0 unique (codigo);

ALTER TABLE factura_visitante 
   add constraint UKa2cxm4mn6tccmyeuihjub62tt unique (sesion_id);

ALTER TABLE persona 
   add constraint UKlse37b9krgqooar93rqq78hh9 unique (documento);

ALTER TABLE tarifa 
   add constraint UKlwp2n1j3ml5m85iie15y96fx8 unique (tipo_vehiculo);

ALTER TABLE unidad_residencial 
   add constraint uk_unidad_torre_apto unique (torre, apartamento);

ALTER TABLE usuarios 
   add constraint UKof5vabgukahdwmgxk4kjrbu98 unique (nombre_usuario);

ALTER TABLE vehiculo 
   add constraint UKqt70ab7dm6c22avc6b4koit9c unique (placa);

ALTER TABLE zona_parqueo 
   add constraint UK7vurdm845jexl5tcngjmofqr2 unique (nombre);

ALTER TABLE espacio_parqueo 
   add constraint FK4673vr80m5stbxpeqgqayp0n1 
   foreign key (zona_id) 
   references zona_parqueo (id);

ALTER TABLE factura_visitante 
   add constraint FK49utdmoedhar5yvdfxlkxup32 
   foreign key (sesion_id) 
   references sesion_parqueo (id);

ALTER TABLE persona 
   add constraint FKeonecxt5if9864g5fxhqqv6vh 
   foreign key (unidad_id) 
   references unidad_residencial (id);

ALTER TABLE sesion_parqueo 
   add constraint FKjtndxcedf15wn1ad0vbaeni8 
   foreign key (espacio_id) 
   references espacio_parqueo (id);

ALTER TABLE sesion_parqueo 
   add constraint FKlakdx31n8tmg3decsdaiaeacu 
   foreign key (vehiculo_id) 
   references vehiculo (id);

ALTER TABLE vehiculo 
   add constraint FKod6gqfv6y04qgw6nhdhgk8wfd 
   foreign key (propietario_id) 
   references persona (id);
