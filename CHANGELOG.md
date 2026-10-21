# Bitacora de cambios del proyecto



 [Entrega 2] 21/10/2026
## Cambios
En:
/workspaces/PROYECTO-FINAL-/.devcontainer/devcontainer.json
    - Se añadió docker compose
    - Se modificó el nombre a "proyecto-final-dev

## Añadido
Se crea el CHANGELOG.md para facilitar el seguimiento del proyecto y que todos los miembros puedan estar al día. Se aceptan sugerencias sobre cómo mejorar el formato de los registros.


En:
/workspaces/PROYECTO-FINAL-/.devcontainer/docker-compose.yml
    -Se creó el archivo
    -Se añadió la linea 9 
        image: proyecto-final-app:latest
    tras problemas con el nombre original del proyecto que impedian la reconstrucción del contenedor
En:
/workspaces/PROYECTO-FINAL-/.devcontainer/Dockerfile
    -Se creo el archivo

Se creó el usuario "docky" y la base de datos "music", esta información esta considerada en

/workspaces/PROYECTO-FINAL-/demo/demo/src/main/resources/application.properties



