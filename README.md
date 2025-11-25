# Logistica Austral \- Aplicacion movil

Aplicacion Android nativa para la empresa Logistica Austral dedicada al arriendo y venta de maquinaria y camiones usados. Ofrece a clientes la exploracion de la flota usada y la posibilidad de armar un carrito de compra, mientras administradores gestionan el inventario (crear, editar, eliminar y actualizar informacion de camiones).

## Objetivo
Centralizar en una sola aplicacion movil la visualizacion y gestion del parque de camiones usados, consumiendo una API REST externa configurable. Busca agilizar procesos internos y mejorar la experiencia del cliente al consultar disponibilidad y estado de unidades.

## Caracteristicas principales
\- Inicio de sesion con rol (cliente / administrador).
\- Exploracion de flota usada (lista y detalle).
\- Carrito para seleccion de camiones (cliente).
\- CRUD completo de camiones (administrador).
\- Carga de imagenes de camiones desde servidor.
\- Arquitectura simple basada en ViewModel + Repository + Room.
\- Navegacion declarativa con Jetpack Navigation Compose.

## Roles y funcionalidades
\- Cliente: autenticacion, explorar inventario usado, ver detalle, agregar al carrito.
\- Administrador: autenticacion, alta de camiones, edicion, eliminacion, actualizacion de datos e imagen (cuando corresponda).

## Stack tecnologico
\- Kotlin + Jetpack Compose (Material3).
\- AndroidX Navigation Compose.
\- Room (persistencia local de usuarios y camiones).
\- Retrofit2 + Gson (capa remota).
\- Coroutines / Flow (asincronia).
\- Coil (carga y cache de imagenes).
\- ViewModel + State (gestion de estado UI).

## Dependencias clave
\- retrofit: 2.11.0
\- room: 2.6.1
\- coil\-compose: 2.5.0
\- material3 compose: 1.3.0
\- coroutines: 1.8.0
Ver `app/build.gradle.kts` para listado completo y versiones sincronizadas.

## Requisitos locales
\- Android Studio Narwhal 2025.1.3 o superior.
\- JDK 11 configurado.
\- SDK Android con nivel minimo (minSdk) 33 y compile/target segun `build.gradle`.
\- Acceso de red a la API (misma LAN, dominio, o IP publica).
\- Emulador o dispositivo fisico API 33+.

## Estructura de carpetas
\- `data/remote/` \- configuracion de red (`NetworkConfig.kt`, servicio Retrofit, etc.).
\- `model/` \- entidades Room y base de datos (`AppDatabase`).
\- `repository/` \- capa de acceso a datos (Room + remoto).
\- `viewmodel/` \- logica de presentacion.
\- `view/` \- pantallas Compose.
\- `ui/theme/` \- tema y estilos.

## Arquitectura
Capas separadas:
\- UI (Compose) observa estado expuesto por ViewModels.
\- ViewModels coordinan repositorios y exponen State.
\- Repositorios abstraen origen de datos (Room / remoto).
\- Room persiste entidades basicas (Usuario, Camion).
Retrofit preparado para futura ampliacion de endpoints remotos.

## API Consumida
La aplicacion consume una API REST externa:
`API Repo:` https://github.com/fabetabilo/dsy1105-gestor-camiones

Endpoints esperados (ejemplos, ajustar segun backend real):
\- `POST /api/v1/auth/login`
\- `POST /api/v1/auth/register`
\- `GET /api/v1/camiones`
\- `GET /api/v1/camiones/{id}`
\- `POST /api/v1/camiones`
\- `PUT /api/v1/camiones/{id}`
\- `DELETE /api/v1/camiones/{id}`

## Configuracion de la base URL
Editar `app/src/main/java/com/example/logistica_austral/data/remote/NetworkConfig.kt`:
```kotlin
object NetworkConfig {
    const val SERVER_BASE_URL = "http://IP_O_DOMINIO"
    const val API_BASE_URL = "http://IP_O_DOMINIO/api/v1/"
}
