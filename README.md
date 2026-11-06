# PROYECTO-FINAL-
Servicio de música
Proyecto final de Implementación de Sistemas: API REST desarrollada con Spring Boot para gestionar usuarios, sus listas de reproducción y el catálogo de medios (canciones) de un servicio de música.

Descripción general

Este proyecto es una aplicación de backend desarrollada en Spring Boot que expone una API REST para administrar los usuarios de un servicio de música, sus listas de reproducción y el catálogo de medios. Los datos se almacenan en PostgreSQL mediante Spring Data JPA.

El catálogo se organiza por géneros musicales principales (por ejemplo, Electrónica, Rock y Jazz). Para extenderlo se aplicará el patrón de diseño Decorator, que permite añadir responsabilidades y características adicionales a un género base de forma dinámica, sin alterar su estructura central. Con esto se busca que las reglas de negocio del sistema sean flexibles y fáciles de ampliar.

Estado actual: el proyecto cuenta con el modelo de datos, las relaciones entre entidades y el CRUD de usuarios, listas de reproducción y medios. La autenticación, las búsquedas avanzadas, el patrón Decorator y el monitoreo con Actuator están pendientes.

==Lista de Integrantes del equipo== 
Flores Carmona Iker Jair
Morgado Ferreyra Dana Victoria 
Pérez Herrejón Angeli Nicole

==Tabla de requerimientos== 

REQUIRIMIENTO DESCRIPCIÓN 
Requerimiento	Descripción	Estado	Notas
REQ_1	La aplicación permite registrar un nuevo usuario con correo y contraseña seguros.	🟡 Parcial	POST /api/usuarios crea usuarios y la contraseña nunca se devuelve en las respuestas. Falta el campo correo y cifrar la contraseña, que hoy se guarda sin cifrar.
REQ_2	La aplicación permite iniciar sesión mediante credenciales válidas (correo y contraseña).	⏳ Pendiente	Requiere agregar Spring Security.
REQ_3	La aplicación permite consultar y descargar las listas de reproducción creadas por el usuario.	🟡 Parcial	Se consultan con GET /api/listas y GET /api/usuarios/{id}, que incluye las listas del usuario. Falta la descarga y filtrar por el usuario con sesión iniciada (depende de REQ_2).
REQ_4	La aplicación permite realizar búsquedas avanzadas de géneros, nombres de canciones, artistas y álbumes.	⏳ Pendiente	Hoy solo hay listados y consulta por identificador. Artista, álbum y género son texto dentro de Medio.
REQ_5	La aplicación permite al usuario crear, consultar, actualizar y eliminar sus listas de reproducción personalizadas.	🟡 Parcial	El CRUD de listas está en /api/listas. Falta agregar y quitar medios de una lista ya creada, porque la actualización no modifica su contenido.


#== Lista de Funcionalidades==

Funcionalidad	Tecnología	Descripción	Estado
Gestión de la base de datos	Spring Data JPA y PostgreSQL	Almacena y consulta usuarios, listas de reproducción y medios. El esquema se genera a partir de las entidades (ddl-auto=update).	✅ Implementada
Relaciones entre entidades	JPA (@OneToMany y @ManyToOne)	Un usuario tiene muchas listas, y cada lista contiene medios mediante la entidad intermedia ListaReproduccionMedio. Se documentan en Modelo de datos y relaciones.	✅ Implementada
Servicios REST	Spring Web	CRUD completo de usuarios, listas de reproducción y medios, con respuestas en JSON.	✅ Implementada
Seguridad y autenticación	Spring Security	Proteger el acceso, cifrar las contraseñas y permitir el inicio de sesión.	⏳ Pendiente
Búsquedas avanzadas	Spring Data JPA	Buscar por género, nombre de canción, artista y álbum.	⏳ Pendiente
Catálogo por géneros con Decorator	Patrón de diseño	Añadir características a un género base sin modificar su estructura.	⏳ Pendiente
Monitoreo y logs	Spring Boot Actuator y Logback	Consultar el estado de salud de la aplicación y registrar actividad y errores. Logback ya viene incluido en Spring Boot y escribe en consola; falta agregar Actuator.	⏳ Pendiente

Avance del proyecto
Fase	Contenido	Estado
Fase 1	Entorno de desarrollo (Dev Container), proyecto base con Spring Initializr y primer controlador de prueba (/hello).	✅ Completada
Fase 2	Persistencia con Spring Data JPA y PostgreSQL (laboratorio de Spring JPA).	✅ Completada
Fase 3	Modelos Usuario, ListaReproduccion y Medio, con sus repositorios y controladores CRUD.	✅ Completada
Fase 4	Relaciones 1 a muchos, colecciones anidadas, corrección del ciclo infinito en JSON y documentación con diagramas Mermaid.	✅ Completada

### 1. Relaciones 1 a muchos identificadas

| # | Relación | Lado "uno" | Lado "muchos" | Llave foránea | Obligatoria |
|---|----------|------------|---------------|---------------|-------------|
| 1 | Un usuario tiene muchas listas de reproducción | `Usuario` | `ListaReproduccion` | `lista_reproduccion.usuario_id` | Sí |
| 2 | Una lista contiene muchas entradas de lista | `ListaReproduccion` | `ListaReproduccionMedio` | `lista_reproduccion_medio.lista_id` | Sí |
| 3 | Un medio aparece en muchas entradas de lista | `Medio` | `ListaReproduccionMedio` | `lista_reproduccion_medio.medio_id` | Sí |

Una lista puede tener muchos medios y un medio puede estar en muchas listas (relación muchos a muchos). Para modelarla solo con relaciones 1 a muchos se usa la entidad intermedia `ListaReproduccionMedio`, que además guarda la `posicion` del medio dentro de la lista y la `fechaAgregado`. Una restricción única sobre `(lista_id, medio_id)` evita repetir el mismo medio en una lista.

Relaciones que también existen en el dominio, pero que hoy están guardadas como texto dentro de `Medio` (`artista`, `album` y `genero`). Quedan pendientes hasta que existan sus entidades propias:

- Un `Artista` tiene muchos `Album`.
- Un `Album` tiene muchos `Medio`.
- Un `Genero` tiene muchos `Medio`.

Una lista puede terne rmuchos medios y un medio puede estar en muchas listas (relación muchos a mucos). Para modelarla solo con relaciones 1 a muchos se usa la entidad intermedia ListaReproduccionMedio, que ademas guarda la posicion del medio dentro de la lista y la fechaAgregado. Una restricción única sobre (lista_id, medio_id) evita reperir el mismo medio en una lista. 

Las demas relaciones que contendrian Medio (artista, album y genero). Quedan pendienres hasa que existan sus entidades propias:
    *Un ARTISTA tiene mcuhos Album 
    *Un ALBUM tiene muchos Medio.
    *Un GENERO tiene muchos Medio. 

# DIAGRAMA ENTIDAD- RELACION 
```mermaid
erDiagram
    USUARIO ||--o{ LISTA_REPRODUCCION : "tiene"
    LISTA_REPRODUCCION ||--o{ LISTA_REPRODUCCION_MEDIO : "contiene"
    MEDIO ||--o{ LISTA_REPRODUCCION_MEDIO : "aparece en"

    USUARIO {
        bigint id PK
        string nombre
        string fecha_nacimiento
        string contrasena
    }
    LISTA_REPRODUCCION {
        bigint id PK
        string nombre
        int duracion_minutos
        string url
        bigint usuario_id FK
    }
    MEDIO {
        bigint id PK
        string nombre
        string genero
        string artista
        string album
        int duracion_minutos
        int anio_lanzamiento
    }
    LISTA_REPRODUCCION_MEDIO {
        bigint id PK
        bigint lista_id FK
        bigint medio_id FK
        int posicion
        date fecha_agregado
    }
```

En la base de datos las columnas son `contraseña` y `año_lanzamiento`; en el diagrama se escriben sin tilde ni eñe solo para que Mermaid pueda dibujarlas.

### 3. Diagrama de clases

Cada lado "uno" guarda una colección (`List`) de objetos del lado "muchos", y cada lado "muchos" guarda la referencia a su padre.

```mermaid
classDiagram
    class Usuario {
        -Long id
        -String nombre
        -String fechaNacimiento
        -String contrasena
        -List~ListaReproduccion~ listas
    }
    class ListaReproduccion {
        -Long id
        -String nombre
        -Integer duracionMinutos
        -String URL
        -Usuario usuario
        -List~ListaReproduccionMedio~ medios
    }
    class ListaReproduccionMedio {
        -Long id
        -ListaReproduccion listaReproduccion
        -Medio medio
        -Integer posicion
        -LocalDate fechaAgregado
    }
    class Medio {
        -Long id
        -String nombre
        -String genero
        -String artista
        -String album
        -Integer duracionMinutos
        -Integer anioLanzamiento
    }

    Usuario "1" *-- "0..*" ListaReproduccion : listas
    ListaReproduccion "1" *-- "0..*" ListaReproduccionMedio : medios
    ListaReproduccionMedio "0..*" --> "1" Medio : medio
```

Las dos primeras relaciones se dibujan como composición (rombo relleno) porque se configuró `cascade = ALL` y `orphanRemoval = true`: al eliminar un usuario se eliminan sus listas, y al eliminar una lista se eliminan sus entradas, pero nunca los `Medio`, que siguen existiendo en el catálogo.
