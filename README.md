# ControlPark-IQ · API REST del proyecto

**Evidencia:** GA7-220501096-AA5-EV03 · Diseño y desarrollo de servicios web – proyecto  
**Programa:** Tecnólogo en Análisis y Desarrollo de Software · SENA · Ficha 3235898  
**Aprendiz:** Carlos Eduardo Valero  
**Instructora:** Adriana Milena Rangel Márquez  
**Proyecto:** ControlPark-IQ, sistema de gestión de parqueaderos para conjuntos residenciales

Servicios web REST que necesita el software ControlPark-IQ para sus tres roles (administración, portería y residentes): unidades residenciales, personas, vehículos, zonas y espacios de parqueo, tarifas, entrada y salida de vehículos y cobro a visitantes. Parte del servicio de registro e inicio de sesión construido en la evidencia EV01 y lo amplía a todo el proyecto.

---

## Contenido

1. [De las historias de usuario a los servicios](#1-de-las-historias-de-usuario-a-los-servicios)
2. [Tabla de endpoints](#2-tabla-de-endpoints)
3. [Formato de las respuestas y códigos de estado](#3-formato-de-las-respuestas-y-códigos-de-estado)
4. [Documentación de cada servicio](#4-documentación-de-cada-servicio)
5. [Validaciones y reglas del negocio](#5-validaciones-y-reglas-del-negocio)
6. [Arquitectura y estructura del proyecto](#6-arquitectura-y-estructura-del-proyecto)
7. [Cómo ejecutarlo](#7-cómo-ejecutarlo)
8. [Pruebas con Postman](#8-pruebas-con-postman)
9. [Control de versiones](#9-control-de-versiones)

## 1. De las historias de usuario a los servicios

Los servicios salen de las historias de usuario, de las pantallas del prototipo de ControlPark-IQ y del modelo de datos (diagrama de clases) del proyecto.

| HU | Historia de usuario | Entidad | Recurso de la API |
|---|---|---|---|
| HU-01 | Como usuario quiero registrarme e iniciar sesión para entrar al sistema según mi rol | Usuario | `/api/auth` |
| HU-02 | Como administrador quiero gestionar usuarios, residentes y visitantes | Usuario · Persona · Rol | `/api/usuarios` · `/api/personas` · `/api/roles` |
| HU-03 | Como administrador quiero gestionar las unidades residenciales del conjunto | UnidadResidencial | `/api/unidades` |
| HU-04 | Como residente quiero registrar mis vehículos | Vehiculo | `/api/vehiculos` |
| HU-05 | Como administrador quiero gestionar las zonas y los espacios de parqueo | ZonaParqueo · EspacioParqueo | `/api/zonas` · `/api/espacios` |
| HU-06 | Como guarda de portería quiero ver los espacios disponibles para asignar uno | EspacioParqueo | `GET /api/espacios?estado=DISPONIBLE` |
| HU-07 | Como administrador quiero gestionar las tarifas por tipo de vehículo | Tarifa | `/api/tarifas` |
| HU-08 | Como guarda de portería quiero registrar la entrada y la salida de los vehículos | SesionParqueo | `/api/sesiones` |
| HU-09 | Como guarda de portería quiero cobrarle al visitante al salir | FacturaVisitante | `/api/facturas` |
| HU-10 | Como guarda de portería quiero consultar el historial de sesiones del día | SesionParqueo | `GET /api/sesiones?fecha=AAAA-MM-DD` |

Los roles (`ADMINISTRADOR`, `PORTERIA`, `RESIDENTE`) son fijos, por eso se manejan como un `enum` y solo se consultan.

## 2. Tabla de endpoints

URL base: `http://localhost:8080` · 48 endpoints agrupados por recurso. Los datos viajan en JSON.

### Autenticación · `/api/auth` (HU-01)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Registrar usuario | `POST /api/auth/registro` | **201** | **400** · **409** |
| Iniciar sesión | `POST /api/auth/login` | **200** | **400** · **401** |

### Usuarios · `/api/usuarios` (HU-02)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar usuarios | `GET /api/usuarios` | **200** | — |
| Consultar usuario | `GET /api/usuarios/{id}` | **200** | **404** |
| Crear usuario | `POST /api/usuarios` | **201** | **400** · **409** |
| Actualizar usuario | `PUT /api/usuarios/{id}` | **200** | **400** · **404** · **409** |
| Eliminar usuario | `DELETE /api/usuarios/{id}` | **204** | **404** · **409** |

### Roles · `/api/roles` (HU-02)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar roles | `GET /api/roles` | **200** | — |

### Unidades residenciales · `/api/unidades` (HU-03)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar unidades | `GET /api/unidades` | **200** | — |
| Consultar unidad | `GET /api/unidades/{id}` | **200** | **404** |
| Crear unidad | `POST /api/unidades` | **201** | **400** · **409** |
| Actualizar unidad | `PUT /api/unidades/{id}` | **200** | **400** · **404** · **409** |
| Eliminar unidad | `DELETE /api/unidades/{id}` | **204** | **404** · **409** |

### Personas · `/api/personas` (HU-02)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar personas | `GET /api/personas` | **200** | **400** |
| Consultar persona | `GET /api/personas/{id}` | **200** | **404** |
| Crear persona | `POST /api/personas` | **201** | **400** · **404** · **409** |
| Actualizar persona | `PUT /api/personas/{id}` | **200** | **400** · **404** · **409** |
| Eliminar persona | `DELETE /api/personas/{id}` | **204** | **404** · **409** |

### Vehículos · `/api/vehiculos` (HU-04)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar vehículos | `GET /api/vehiculos` | **200** | — |
| Consultar vehículo | `GET /api/vehiculos/{id}` | **200** | **404** |
| Registrar vehículo | `POST /api/vehiculos` | **201** | **400** · **404** · **409** |
| Actualizar vehículo | `PUT /api/vehiculos/{id}` | **200** | **400** · **404** · **409** |
| Eliminar vehículo | `DELETE /api/vehiculos/{id}` | **204** | **404** · **409** |

### Zonas de parqueo · `/api/zonas` (HU-05)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar zonas | `GET /api/zonas` | **200** | — |
| Consultar zona | `GET /api/zonas/{id}` | **200** | **404** |
| Crear zona | `POST /api/zonas` | **201** | **400** · **409** |
| Actualizar zona | `PUT /api/zonas/{id}` | **200** | **400** · **404** · **409** |
| Eliminar zona | `DELETE /api/zonas/{id}` | **204** | **404** · **409** |

### Espacios de parqueo · `/api/espacios` (HU-05 · HU-06)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar espacios | `GET /api/espacios` | **200** | **400** |
| Consultar espacio | `GET /api/espacios/{id}` | **200** | **404** |
| Crear espacio | `POST /api/espacios` | **201** | **400** · **404** · **409** |
| Actualizar espacio | `PUT /api/espacios/{id}` | **200** | **400** · **404** · **409** |
| Eliminar espacio | `DELETE /api/espacios/{id}` | **204** | **404** · **409** |

### Tarifas · `/api/tarifas` (HU-07)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar tarifas | `GET /api/tarifas` | **200** | — |
| Consultar tarifa | `GET /api/tarifas/{id}` | **200** | **404** |
| Crear tarifa | `POST /api/tarifas` | **201** | **400** · **409** |
| Actualizar tarifa | `PUT /api/tarifas/{id}` | **200** | **400** · **404** · **409** |
| Eliminar tarifa | `DELETE /api/tarifas/{id}` | **204** | **404** |

### Sesiones de parqueo · `/api/sesiones` (HU-08 · HU-10)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar sesiones | `GET /api/sesiones` | **200** | **400** |
| Consultar sesión | `GET /api/sesiones/{id}` | **200** | **404** |
| Registrar entrada | `POST /api/sesiones` | **201** | **400** · **404** · **409** |
| Registrar salida | `PUT /api/sesiones/{id}` | **200** | **400** · **404** · **409** |
| Eliminar sesión | `DELETE /api/sesiones/{id}` | **204** | **404** · **409** |

### Facturas de visitantes · `/api/facturas` (HU-09)

| Acción | Método y ruta | Si todo sale bien | Si algo falla |
|---|---|---|---|
| Listar facturas | `GET /api/facturas` | **200** | — |
| Consultar factura | `GET /api/facturas/{id}` | **200** | **404** |
| Generar factura | `POST /api/facturas` | **201** | **400** · **404** · **409** |
| Pagar factura | `PUT /api/facturas/{id}` | **200** | **400** · **404** · **409** |
| Anular factura | `DELETE /api/facturas/{id}` | **204** | **404** · **409** |

## 3. Formato de las respuestas y códigos de estado

- **Cuando todo sale bien**, la API devuelve el registro (o la lista) en JSON. `DELETE` responde **204** sin cuerpo.
- **Cuando algo falla**, todos los servicios responden con el mismo formato:

```json
{
    "exito": false,
    "mensaje": "Datos inválidos",
    "errores": ["La placa es obligatoria", "El tipo es obligatorio: CARRO o MOTO"]
}
```

La lista `errores` solo aparece en los errores de validación de campos.

| Código | Significado | Cuándo lo responde ControlPark-IQ |
|---|---|---|
| **200** | OK | consulta, actualización o login correctos |
| **201** | Created | registro creado |
| **204** | No Content | registro eliminado |
| **400** | Bad Request | datos vacíos o con formato inválido, JSON mal escrito o regla del negocio no cumplida |
| **401** | Unauthorized | usuario o contraseña incorrectos (login) |
| **404** | Not Found | el registro buscado no existe |
| **409** | Conflict | dato repetido, espacio ocupado o registro que no se puede borrar porque otros datos dependen de él |
| **415** | Unsupported Media Type | el cuerpo no se envió como JSON |

## 4. Documentación de cada servicio

Hay dos formas de consultar la documentación:

1. **Esta sección**, con la ficha de cada servicio: descripción, método y URL, datos que recibe y respuestas posibles.
2. **Swagger (OpenAPI)**, generada automáticamente por `springdoc-openapi`: con el proyecto en ejecución, abrir <http://localhost:8080/swagger-ui.html>. Desde ahí también se puede probar cada servicio.

### 4.1 Autenticación (HU-01)

Registro e inicio de sesión de los usuarios (reutilizado de la EV01). La contraseña se guarda con BCrypt y el error de login es genérico.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `usuario` | texto | Sí | 4 a 20 caracteres: letras, números, punto o guion bajo; único (no distingue mayúsculas) |
| `contrasena` | texto | Sí | 8 a 64 caracteres con al menos una letra y un número |
| `rol` | ADMINISTRADOR · PORTERIA · RESIDENTE | No | Por defecto RESIDENTE |

#### Registrar usuario

- **Descripción:** Crea una cuenta nueva.
- **Método y URL:** `POST http://localhost:8080/api/auth/registro`
- **Body (JSON):**

```json
{
    "usuario": "admin.torres",
    "contrasena": "Parque2026",
    "rol": "ADMINISTRADOR"
}
```

- **Respuestas:**
  - **201 Created** → usuario registrado
  - **400 Bad Request** → datos vacíos o con formato inválido
  - **409 Conflict** → el usuario ya existe

#### Iniciar sesión

- **Descripción:** Verifica usuario y contraseña. Responde "Autenticación satisfactoria" o "Error en la autenticación".
- **Método y URL:** `POST http://localhost:8080/api/auth/login`
- **Body (JSON):**

```json
{
    "usuario": "admin.torres",
    "contrasena": "Parque2026"
}
```

- **Respuestas:**
  - **200 OK** → autenticación satisfactoria
  - **400 Bad Request** → datos vacíos
  - **401 Unauthorized** → usuario o contraseña incorrectos, o usuario inactivo

### 4.2 Usuarios (HU-02)

El administrador gestiona las cuentas del sistema. Las respuestas nunca incluyen la contraseña. El sistema no puede quedar sin administrador.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `usuario / contrasena / rol` | — | Sí (al crear) | Mismas reglas del registro |
| `rol` | ADMINISTRADOR · PORTERIA · RESIDENTE | Sí (al actualizar) | — |
| `activo` | true · false | Sí (al actualizar) | Un usuario inactivo no puede iniciar sesión |

#### Listar usuarios

- **Descripción:** Devuelve todos los usuarios.
- **Método y URL:** `GET http://localhost:8080/api/usuarios`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista de usuarios

#### Consultar usuario

- **Descripción:** Devuelve un usuario.
- **Método y URL:** `GET http://localhost:8080/api/usuarios/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → el usuario
  - **404 Not Found** → no existe

#### Crear usuario

- **Descripción:** Crea una cuenta con las reglas del registro y devuelve el usuario creado.
- **Método y URL:** `POST http://localhost:8080/api/usuarios`
- **Body (JSON):**

```json
{
    "usuario": "porteria.torre1",
    "contrasena": "Garita2026",
    "rol": "PORTERIA"
}
```

- **Respuestas:**
  - **201 Created** → usuario creado
  - **400 Bad Request** → datos inválidos
  - **409 Conflict** → el usuario ya existe

#### Actualizar usuario

- **Descripción:** Cambia el rol o activa / inactiva la cuenta.
- **Método y URL:** `PUT http://localhost:8080/api/usuarios/{id}`
- **Body (JSON):**

```json
{
    "rol": "PORTERIA",
    "activo": false
}
```

- **Respuestas:**
  - **200 OK** → usuario actualizado
  - **400 Bad Request** → rol inválido o campos vacíos
  - **404 Not Found** → no existe
  - **409 Conflict** → dejaría el sistema sin administrador

#### Eliminar usuario

- **Descripción:** Borra la cuenta.
- **Método y URL:** `DELETE http://localhost:8080/api/usuarios/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminado
  - **404 Not Found** → no existe
  - **409 Conflict** → es el último administrador

### 4.3 Roles (HU-02)

Los roles son fijos (enum Rol), por eso solo se consultan.

#### Listar roles

- **Descripción:** Devuelve los roles del sistema.
- **Método y URL:** `GET http://localhost:8080/api/roles`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → ["ADMINISTRADOR", "PORTERIA", "RESIDENTE"]

### 4.4 Unidades residenciales (HU-03)

Apartamentos o casas del conjunto. La combinación torre + apartamento no se repite.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `torre` | texto | Sí | Máximo 10 caracteres |
| `apartamento` | texto | Sí | Máximo 10 caracteres; único junto con la torre |
| `cuposParqueo` | número | Sí | Entre 0 y 4 |

#### Listar unidades

- **Descripción:** Devuelve todas las unidades.
- **Método y URL:** `GET http://localhost:8080/api/unidades`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista

#### Consultar unidad

- **Descripción:** Devuelve una unidad.
- **Método y URL:** `GET http://localhost:8080/api/unidades/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → la unidad
  - **404 Not Found** → no existe

#### Crear unidad

- **Descripción:** Registra un apartamento.
- **Método y URL:** `POST http://localhost:8080/api/unidades`
- **Body (JSON):**

```json
{
    "torre": "1",
    "apartamento": "302",
    "cuposParqueo": 1
}
```

- **Respuestas:**
  - **201 Created** → unidad creada
  - **400 Bad Request** → datos inválidos
  - **409 Conflict** → la torre + apartamento ya existe

#### Actualizar unidad

- **Descripción:** Modifica una unidad.
- **Método y URL:** `PUT http://localhost:8080/api/unidades/{id}`
- **Body (JSON):**

```json
{
    "torre": "1",
    "apartamento": "302",
    "cuposParqueo": 2
}
```

- **Respuestas:**
  - **200 OK** → unidad actualizada
  - **400 Bad Request** → datos inválidos
  - **404 Not Found** → no existe
  - **409 Conflict** → torre + apartamento repetidos

#### Eliminar unidad

- **Descripción:** Borra una unidad.
- **Método y URL:** `DELETE http://localhost:8080/api/unidades/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminada
  - **404 Not Found** → no existe
  - **409 Conflict** → tiene residentes asignados

### 4.5 Personas (HU-02)

Residentes y visitantes, dueños de los vehículos. Un residente debe pertenecer a una unidad; un visitante no.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `documento` | texto | Sí | 6 a 10 dígitos; único |
| `nombres / apellidos` | texto | Sí | 2 a 60 caracteres |
| `telefono` | texto | No | Celular de 10 dígitos que empieza por 3 |
| `correo` | texto | No | Formato de correo válido |
| `tipo` | RESIDENTE · VISITANTE | Sí | — |
| `unidadId` | número | Si es RESIDENTE | La unidad debe existir |

#### Listar personas

- **Descripción:** Devuelve todas las personas. Filtro opcional: ?tipo=RESIDENTE o ?tipo=VISITANTE.
- **Método y URL:** `GET http://localhost:8080/api/personas`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista
  - **400 Bad Request** → tipo inválido

#### Consultar persona

- **Descripción:** Devuelve una persona con su unidad.
- **Método y URL:** `GET http://localhost:8080/api/personas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → la persona
  - **404 Not Found** → no existe

#### Crear persona

- **Descripción:** Registra un residente o un visitante.
- **Método y URL:** `POST http://localhost:8080/api/personas`
- **Body (JSON):**

```json
{
    "documento": "1012345678",
    "nombres": "Laura",
    "apellidos": "Gómez Ruiz",
    "telefono": "3001234567",
    "correo": "laura.gomez@correo.com",
    "tipo": "RESIDENTE",
    "unidadId": 1
}
```

- **Respuestas:**
  - **201 Created** → persona creada
  - **400 Bad Request** → datos inválidos o residente sin unidad
  - **404 Not Found** → la unidad no existe
  - **409 Conflict** → documento repetido

#### Actualizar persona

- **Descripción:** Modifica los datos de una persona (mismo body que crear).
- **Método y URL:** `PUT http://localhost:8080/api/personas/{id}`
- **Respuestas:**
  - **200 OK** → persona actualizada
  - **400 Bad Request** → datos inválidos
  - **404 Not Found** → persona o unidad no existe
  - **409 Conflict** → documento repetido

#### Eliminar persona

- **Descripción:** Borra una persona.
- **Método y URL:** `DELETE http://localhost:8080/api/personas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminada
  - **404 Not Found** → no existe
  - **409 Conflict** → tiene vehículos registrados

### 4.6 Vehículos (HU-04)

Carros y motos de residentes y visitantes. La placa se guarda en mayúsculas y debe corresponder al tipo.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `placa` | texto | Sí | Carro ABC123 · Moto ABC12D; única |
| `tipo` | CARRO · MOTO | Sí | — |
| `marca` | texto | No | Máximo 30 caracteres |
| `color` | texto | No | Máximo 20 caracteres |
| `propietarioId` | número | Sí | La persona debe existir |

#### Listar vehículos

- **Descripción:** Devuelve todos los vehículos. Filtro opcional: ?propietarioId=1.
- **Método y URL:** `GET http://localhost:8080/api/vehiculos`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista

#### Consultar vehículo

- **Descripción:** Devuelve un vehículo con su propietario.
- **Método y URL:** `GET http://localhost:8080/api/vehiculos/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → el vehículo
  - **404 Not Found** → no existe

#### Registrar vehículo

- **Descripción:** Registra un carro o una moto.
- **Método y URL:** `POST http://localhost:8080/api/vehiculos`
- **Body (JSON):**

```json
{
    "placa": "KLM482",
    "tipo": "CARRO",
    "marca": "Mazda",
    "color": "Gris",
    "propietarioId": 1
}
```

- **Respuestas:**
  - **201 Created** → vehículo creado
  - **400 Bad Request** → placa inválida o no corresponde al tipo
  - **404 Not Found** → el propietario no existe
  - **409 Conflict** → placa repetida

#### Actualizar vehículo

- **Descripción:** Modifica un vehículo (mismo body que registrar).
- **Método y URL:** `PUT http://localhost:8080/api/vehiculos/{id}`
- **Respuestas:**
  - **200 OK** → vehículo actualizado
  - **400 Bad Request** → datos inválidos
  - **404 Not Found** → vehículo o propietario no existe
  - **409 Conflict** → placa repetida

#### Eliminar vehículo

- **Descripción:** Borra un vehículo.
- **Método y URL:** `DELETE http://localhost:8080/api/vehiculos/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminado
  - **404 Not Found** → no existe
  - **409 Conflict** → tiene sesiones de parqueo registradas

### 4.7 Zonas de parqueo (HU-05)

Sectores del parqueadero, para residentes o para visitantes.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `nombre` | texto | Sí | 3 a 40 caracteres; único (no distingue mayúsculas) |
| `tipo` | RESIDENTES · VISITANTES | Sí | — |
| `descripcion` | texto | No | Máximo 150 caracteres |

#### Listar zonas

- **Descripción:** Devuelve todas las zonas.
- **Método y URL:** `GET http://localhost:8080/api/zonas`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista

#### Consultar zona

- **Descripción:** Devuelve una zona.
- **Método y URL:** `GET http://localhost:8080/api/zonas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → la zona
  - **404 Not Found** → no existe

#### Crear zona

- **Descripción:** Registra una zona.
- **Método y URL:** `POST http://localhost:8080/api/zonas`
- **Body (JSON):**

```json
{
    "nombre": "Sótano 1",
    "tipo": "RESIDENTES",
    "descripcion": "Torres 1 y 2"
}
```

- **Respuestas:**
  - **201 Created** → zona creada
  - **400 Bad Request** → datos inválidos
  - **409 Conflict** → nombre repetido

#### Actualizar zona

- **Descripción:** Modifica una zona (mismo body que crear).
- **Método y URL:** `PUT http://localhost:8080/api/zonas/{id}`
- **Respuestas:**
  - **200 OK** → zona actualizada
  - **400 Bad Request** → datos inválidos
  - **404 Not Found** → no existe
  - **409 Conflict** → nombre repetido

#### Eliminar zona

- **Descripción:** Borra una zona.
- **Método y URL:** `DELETE http://localhost:8080/api/zonas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminada
  - **404 Not Found** → no existe
  - **409 Conflict** → tiene espacios

### 4.8 Espacios de parqueo (HU-05 · HU-06)

Cupos físicos del parqueadero. Portería consulta los disponibles para asignar uno. El estado OCUPADO solo lo pone el sistema al registrar una entrada.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `codigo` | texto | Sí | Formato LETRAS-NÚMERO (R-01, V-03); único |
| `tipoVehiculo` | CARRO · MOTO | Sí | — |
| `estado` | DISPONIBLE · RESERVADO · INHABILITADO | No | Por defecto DISPONIBLE; OCUPADO no se permite a mano |
| `zonaId` | número | Sí | La zona debe existir |

#### Listar espacios

- **Descripción:** Devuelve los espacios. Filtros opcionales: ?estado=DISPONIBLE y ?tipoVehiculo=CARRO.
- **Método y URL:** `GET http://localhost:8080/api/espacios`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista
  - **400 Bad Request** → estado o tipo inválido

#### Consultar espacio

- **Descripción:** Devuelve un espacio con su zona y estado.
- **Método y URL:** `GET http://localhost:8080/api/espacios/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → el espacio
  - **404 Not Found** → no existe

#### Crear espacio

- **Descripción:** Registra un cupo.
- **Método y URL:** `POST http://localhost:8080/api/espacios`
- **Body (JSON):**

```json
{
    "codigo": "R-01",
    "tipoVehiculo": "CARRO",
    "zonaId": 1
}
```

- **Respuestas:**
  - **201 Created** → espacio creado
  - **400 Bad Request** → datos inválidos o estado OCUPADO
  - **404 Not Found** → la zona no existe
  - **409 Conflict** → código repetido

#### Actualizar espacio

- **Descripción:** Modifica un espacio (por ejemplo, inhabilitarlo).
- **Método y URL:** `PUT http://localhost:8080/api/espacios/{id}`
- **Body (JSON):**

```json
{
    "codigo": "R-02",
    "tipoVehiculo": "MOTO",
    "estado": "INHABILITADO",
    "zonaId": 1
}
```

- **Respuestas:**
  - **200 OK** → espacio actualizado
  - **400 Bad Request** → datos inválidos
  - **404 Not Found** → espacio o zona no existe
  - **409 Conflict** → código repetido o espacio ocupado

#### Eliminar espacio

- **Descripción:** Borra un espacio.
- **Método y URL:** `DELETE http://localhost:8080/api/espacios/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminado
  - **404 Not Found** → no existe
  - **409 Conflict** → está ocupado o tiene historial de parqueo

### 4.9 Tarifas (HU-07)

Una tarifa por tipo de vehículo: valor por hora para visitantes y mensualidad para residentes. Valores DECIMAL(10,2) en pesos.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `tipoVehiculo` | CARRO · MOTO | Sí | Único |
| `valorHora` | número | Sí | Mayor que 0; máximo 8 enteros y 2 decimales |
| `valorMensualidad` | número | Sí | Mayor que 0; máximo 8 enteros y 2 decimales |

#### Listar tarifas

- **Descripción:** Devuelve todas las tarifas.
- **Método y URL:** `GET http://localhost:8080/api/tarifas`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista

#### Consultar tarifa

- **Descripción:** Devuelve una tarifa.
- **Método y URL:** `GET http://localhost:8080/api/tarifas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → la tarifa
  - **404 Not Found** → no existe

#### Crear tarifa

- **Descripción:** Registra la tarifa de un tipo de vehículo.
- **Método y URL:** `POST http://localhost:8080/api/tarifas`
- **Body (JSON):**

```json
{
    "tipoVehiculo": "CARRO",
    "valorHora": 3000,
    "valorMensualidad": 80000
}
```

- **Respuestas:**
  - **201 Created** → tarifa creada
  - **400 Bad Request** → valores en 0, negativos o tipo inválido
  - **409 Conflict** → ya existe tarifa para ese tipo

#### Actualizar tarifa

- **Descripción:** Cambia los valores.
- **Método y URL:** `PUT http://localhost:8080/api/tarifas/{id}`
- **Body (JSON):**

```json
{
    "tipoVehiculo": "CARRO",
    "valorHora": 3500,
    "valorMensualidad": 85000
}
```

- **Respuestas:**
  - **200 OK** → tarifa actualizada
  - **400 Bad Request** → datos inválidos
  - **404 Not Found** → no existe
  - **409 Conflict** → tipo repetido

#### Eliminar tarifa

- **Descripción:** Borra una tarifa.
- **Método y URL:** `DELETE http://localhost:8080/api/tarifas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminada
  - **404 Not Found** → no existe

### 4.10 Sesiones de parqueo (HU-08 · HU-10)

Entrada (POST) y salida (PUT) de vehículos. Al entrar el espacio queda OCUPADO; al salir vuelve a DISPONIBLE. Residentes parquean en zonas de residentes y visitantes en zonas de visitantes.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `vehiculoId` | número | Sí (entrada) | El vehículo debe existir y no estar ya adentro |
| `espacioId` | número | Sí (entrada) | Debe existir, estar DISPONIBLE, ser del mismo tipo y de la zona correcta |
| `horaEntrada` | fecha y hora | No | Por defecto la hora actual; no puede ser futura |
| `horaSalida` | fecha y hora | No (salida) | Por defecto la hora actual; no puede ser futura ni anterior a la entrada |

#### Listar sesiones

- **Descripción:** Historial. Filtros opcionales: ?estado=ABIERTA (vehículos adentro) y ?fecha=2026-10-20 (entradas del día).
- **Método y URL:** `GET http://localhost:8080/api/sesiones`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista
  - **400 Bad Request** → estado o fecha inválidos

#### Consultar sesión

- **Descripción:** Devuelve una sesión con vehículo y espacio.
- **Método y URL:** `GET http://localhost:8080/api/sesiones/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → la sesión
  - **404 Not Found** → no existe

#### Registrar entrada

- **Descripción:** Abre la sesión y ocupa el espacio.
- **Método y URL:** `POST http://localhost:8080/api/sesiones`
- **Body (JSON):**

```json
{
    "vehiculoId": 2,
    "espacioId": 2,
    "horaEntrada": "2026-10-20T08:00:00"
}
```

- **Respuestas:**
  - **201 Created** → sesión ABIERTA
  - **400 Bad Request** → faltan datos, fecha futura, tipo o zona incorrectos
  - **404 Not Found** → vehículo o espacio no existe
  - **409 Conflict** → espacio no disponible o vehículo ya adentro

#### Registrar salida

- **Descripción:** Cierra la sesión y libera el espacio. Body opcional ({} = hora actual).
- **Método y URL:** `PUT http://localhost:8080/api/sesiones/{id}`
- **Body (JSON):**

```json
{
    "horaSalida": "2026-10-20T10:30:00"
}
```

- **Respuestas:**
  - **200 OK** → sesión CERRADA
  - **400 Bad Request** → salida anterior a la entrada o futura
  - **404 Not Found** → no existe
  - **409 Conflict** → la sesión ya estaba cerrada

#### Eliminar sesión

- **Descripción:** Borra una sesión registrada por error (si estaba abierta, libera el espacio).
- **Método y URL:** `DELETE http://localhost:8080/api/sesiones/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → eliminada
  - **404 Not Found** → no existe
  - **409 Conflict** → ya tiene factura

### 4.11 Facturas de visitantes (HU-09)

Cobro al visitante. El sistema calcula: horas cobradas (toda fracción es hora completa, mínimo 1) × valor por hora de la tarifa. Ejemplo: 08:00 a 10:30 = 3 horas × $3.500 = $10.500.

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `sesionId` | número | Sí (generar) | Sesión CERRADA de un visitante, sin factura previa |
| `metodoPago` | EFECTIVO · TARJETA · TRANSFERENCIA | Sí (pagar) | — |

#### Listar facturas

- **Descripción:** Devuelve las facturas. Filtro opcional: ?pagada=false (pendientes).
- **Método y URL:** `GET http://localhost:8080/api/facturas`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → lista

#### Consultar factura

- **Descripción:** Devuelve una factura con su sesión.
- **Método y URL:** `GET http://localhost:8080/api/facturas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **200 OK** → la factura
  - **404 Not Found** → no existe

#### Generar factura

- **Descripción:** Calcula y registra el cobro de una sesión de visitante.
- **Método y URL:** `POST http://localhost:8080/api/facturas`
- **Body (JSON):**

```json
{
    "sesionId": 2
}
```

- **Respuestas:**
  - **201 Created** → factura pendiente de pago
  - **400 Bad Request** → sesión abierta, es de residente o no hay tarifa
  - **404 Not Found** → la sesión no existe
  - **409 Conflict** → la sesión ya tiene factura

#### Pagar factura

- **Descripción:** Registra el pago.
- **Método y URL:** `PUT http://localhost:8080/api/facturas/{id}`
- **Body (JSON):**

```json
{
    "metodoPago": "EFECTIVO"
}
```

- **Respuestas:**
  - **200 OK** → factura pagada
  - **400 Bad Request** → método de pago vacío o inválido
  - **404 Not Found** → no existe
  - **409 Conflict** → ya estaba pagada

#### Anular factura

- **Descripción:** Borra una factura pendiente.
- **Método y URL:** `DELETE http://localhost:8080/api/facturas/{id}`
- **Body:** no lleva
- **Respuestas:**
  - **204 No Content** → anulada
  - **404 Not Found** → no existe
  - **409 Conflict** → ya fue pagada

## 5. Validaciones y reglas del negocio

Las validaciones de formato se hacen en los DTO con anotaciones (`@NotBlank`, `@Size`, `@Pattern`, `@Email`, `@Positive`, `@Digits`, `@PastOrPresent`) y responden **400**. Las reglas del negocio se programan en los servicios (`service/`).

| Tipo de validación | Ejemplo en ControlPark-IQ | Dónde se hace | Responde |
|---|---|---|---|
| Campo obligatorio | La placa, el documento o el código del espacio no pueden ir vacíos | `@NotBlank` / `@NotNull` en el DTO | **400** |
| Formato | Correo válido, celular de 10 dígitos, placa ABC123, código R-01 | `@Email` · `@Pattern` | **400** |
| Longitud o rango | Contraseña de 8 a 64 caracteres, tarifa mayor que 0, máximo 4 cupos por unidad | `@Size` · `@Positive` · `@Max` | **400** |
| Valor permitido | Tipo de vehículo CARRO o MOTO, estado DISPONIBLE, RESERVADO o INHABILITADO | `enum` + `ManejadorErrores` | **400** |
| Que exista | Consultar, modificar o eliminar un id que no existe; propietario o zona inexistente | `findById` en el servicio | **404** |
| Que no se repita | Placa, documento, usuario, código de espacio, nombre de zona, torre + apartamento, tarifa por tipo | `existsBy...()` en el repositorio | **409** |
| Regla del negocio | La placa debe corresponder al tipo (carro / moto) | `VehiculoService` | **400** |
| Regla del negocio | Un residente debe tener unidad; un visitante no | `PersonaService` | **400** |
| Regla del negocio | El espacio debe estar DISPONIBLE y el vehículo no puede estar ya adentro | `SesionParqueoService` | **409** |
| Regla del negocio | El espacio debe ser del mismo tipo que el vehículo y de la zona correcta (residentes / visitantes) | `SesionParqueoService` | **400** |
| Regla del negocio | La salida no puede ser anterior a la entrada ni una fecha futura | `SesionParqueoService` · `@PastOrPresent` | **400** |
| Regla del negocio | Solo se factura a visitantes, con la sesión cerrada, una vez por sesión | `FacturaVisitanteService` | **400 · 409** |
| Regla del negocio | No se elimina un registro del que dependen otros (unidad con residentes, zona con espacios, factura pagada...) | Servicio de cada recurso | **409** |
| Regla del negocio | El sistema no puede quedar sin administrador | `UsuarioService` | **409** |

## 6. Arquitectura y estructura del proyecto

```
Cliente (Postman / React) → Controller → Service → Repository → MySQL
```

Para cada recurso se repiten los mismos cuatro pasos de la EV01:

| Capa | Ejemplo (vehículos) | Responsabilidad |
|---|---|---|
| 1. Entidad + Repositorio | `model/Vehiculo` · `repository/VehiculoRepository` | La tabla en MySQL y quien busca y guarda en ella |
| 2. DTO con validaciones | `dto/VehiculoDTO` | Los datos que llegan, con `@NotBlank`, `@Pattern`... |
| 3. Servicio | `service/VehiculoService` | La lógica: reglas del negocio, que exista, que no se repita |
| 4. Controlador | `controller/VehiculoController` | Los endpoints REST con su código de estado |

Además: `exception/ManejadorErrores` convierte todos los errores en respuestas JSON claras, `config/SeguridadConfig` crea el codificador BCrypt y `config/CorsConfig` permite las peticiones del front-end React (`http://localhost:5173`).

```
ControlPark-IQ-API/
├── pom.xml
├── README.md
├── database/
│   └── controlpark_iq_api.sql        # script de la BD (opcional: Spring la crea sola)
├── postman/
│   └── ControlPark-IQ-API.postman_collection.json
└── src/
    ├── main/
    │   ├── java/co/edu/sena/controlparkiq/
    │   │   ├── ControlParkApplication.java
    │   │   ├── config/        SeguridadConfig · CorsConfig
    │   │   ├── controller/    Auth · Usuario · Rol · UnidadResidencial · Persona · Vehiculo
    │   │   │                  ZonaParqueo · EspacioParqueo · Tarifa · SesionParqueo · FacturaVisitante
    │   │   ├── dto/           datos que entran, con sus validaciones
    │   │   ├── exception/     ManejadorErrores · RecursoNoEncontrado (404) · Conflicto (409) · ReglaNegocio (400)
    │   │   ├── model/         entidades JPA y enums (Rol, TipoVehiculo, EstadoEspacio...)
    │   │   ├── repository/    acceso a cada tabla
    │   │   └── service/       lógica y reglas del negocio
    │   └── resources/application.properties
    └── test/
        ├── java/co/edu/sena/controlparkiq/AuthControllerTest.java
        └── resources/application.properties   # H2 en memoria solo para pruebas
```

| Tecnología | Uso |
|---|---|
| Java 17+ · Spring Boot 3.3.5 | Proyecto base y servidor en el puerto 8080 |
| Spring Web | Controladores REST y JSON |
| Spring Data JPA + MySQL 8 | 9 tablas en la base `controlpark_iq_api` |
| Validation (Jakarta) | Validaciones en los DTO con `@Valid` |
| spring-security-crypto | Contraseñas protegidas con BCrypt |
| springdoc-openapi 2.6.0 | Documentación Swagger en `/swagger-ui.html` |
| JUnit 5 + MockMvc + H2 | Pruebas automáticas |
| Postman | Pruebas de los servicios (colección incluida) |
| Git + GitHub | Control de versiones |

## 7. Cómo ejecutarlo

1. Tener instalado **Java 17 o superior**, **MySQL 8** (con MySQL Workbench) e **IntelliJ IDEA** o VS Code.
2. Clonar el repositorio:
   ```bash
   git clone https://github.com/CarlosEvalero82/ControlPark-IQ-API.git
   ```
3. Abrir la carpeta en IntelliJ (File → Open → `pom.xml` → Open as Project) y esperar a que Maven descargue las dependencias.
4. Configurar el usuario de MySQL. Si su usuario `root` **no tiene contraseña**, no hay que hacer nada. Si **tiene contraseña**, defínala como variable de entorno `DB_PASSWORD` en la configuración de ejecución (Run → Edit Configurations → Environment variables: `DB_PASSWORD=su_clave`). El repositorio no guarda contraseñas reales.
5. Ejecutar `ControlParkApplication` (botón **Run**) o `mvn spring-boot:run`. En consola debe aparecer `Tomcat started on port 8080` y `Started ControlParkApplication`. La base `controlpark_iq_api` y sus tablas se crean solas.
6. Abrir la documentación en <http://localhost:8080/swagger-ui.html> o probar con Postman (siguiente sección).

## 8. Pruebas con Postman

En Postman: **Import** → `postman/ControlPark-IQ-API.postman_collection.json`. La colección tiene una carpeta por recurso y, en cada una, el orden **crear → consultar → actualizar → eliminar**, con al menos un caso exitoso y uno de error por endpoint. Usa la variable `{{baseUrl}}` (`http://localhost:8080`) y guarda sola los ids que se van creando. Cada petición trae sus pruebas (pestaña *Tests*) que verifican el código y la respuesta.

Para correrla completa: clic derecho en la colección → **Run collection** → **Run**. Debe ejecutarse con la base de datos vacía; para repetirla desde cero, en MySQL Workbench ejecutar `DROP DATABASE controlpark_iq_api;` y volver a dar **Run** al proyecto.

Pruebas automáticas del registro e inicio de sesión (sin MySQL, con H2 en memoria):

```bash
mvn test
```

## 9. Control de versiones

El proyecto continúa el historial de Git de la EV01: cada avance quedó en un commit con un mensaje claro (`feat:`, `fix:`, `docs:`, `test:`), recurso por recurso. El archivo `.gitignore` excluye la carpeta `target/`. Para ver el historial:

```bash
git log --oneline
```
