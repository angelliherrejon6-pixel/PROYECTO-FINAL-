# PROYECTO-FINAL-
==DESCRIPCIÓN GENERAL DEL PROYECTO==

Este proyecto es una aplicación de backend desarrollada en Spring Boot para la gestión de un servicio de música, estructurando mediante catálogos organizados por géneros musicalesprincipales (por ejemplo; Electrónica, Rock, Jazz). Para ello utilizaremos el patrón Decorator, el cual permite añadir responsabilidades y características adicionales d emanera dinámica sobre cualquier género musical base sin alterar su estructura central, logrando una alta flexibilidad y extensibilidad en las regla de negocio del sistema.

==Lista de Integrantes del equipo== Flores Carmona Iker Jair Morgado Ferreyra Dana Victoria Pérez Herrejón Angeli Nicole

==Tabla de requerimientos== REQUERIMIENTOS

REQUIRIMIENTO DESCRIPCIÓN REQ_1 La aplicación permite crear un - nuevo usuario con correo y contraseña - seguros.

REQ_2 La aplicación permite iniciar sesión - mediante credenciales válidas (correo - y contraseña).

REQ_3 La aplicación permite descargar y - consultar playlist creadas por el - usuario.

REQ_4 La aplicación permite realizar - búsquedas avanzadas de géneros, _ nombres de canciones, artistas y _ álbumes.

REQ_5 La aplicación permite al usuario _ crear, actualizar, editar y eliminar _ sus playlists personalizadas.

== Lista de Funcionalidades==

*Gestion de Base de Datos (Spring Data JPA junto con PostgreSQL) Almacenar y consultar los datos de los usarios, canciones, artistas, álbumes y playlist.

*Seguridad y Autentificación de Usuarios (Spring Security) Proteger los accesos, cifrar contraseñas de registro e inico de sesión.

*Servicios (API REST, Spring Web) Permitir la comunicación entre el backend y los clientes (búsquedas de canciones, creación de playlists, etc).

*Logs (Spring Boot Actuator y Logback) Monitorear el estado de salud fr la aplicación y resgistrar logs de actividad o errores del servidor.
