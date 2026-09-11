---
name: commit 
description: usa esta skill cuando el usuario te pida que realizes commit/s
---

1. Inspeccionar estado y cambios del repositorio.
2. Revisar commits anteriores para respetar idioma y estilo.
3. Consultar git config --local user.email.
4. Preguntar al usuario si ese correo es el deseado.
5. Analizar los cambios y agruparlos por relación.
6. Respetar la jerarquía:
   DB → modelos → config → servicios.
7. No mezclar cambios independientes en un mismo commit.
8. Proponer cada commit:
    - archivos incluidos
    - motivo
    - mensaje
9. Esperar aprobación del usuario antes de ejecutarlo.
10. Usar Conventional Commits:
    feat, fix, refactor, test, docs, chore, etc.
11. Mantener el idioma de los commits anteriores,
    salvo indicación explícita del usuario.
12. Ejecutar el commit.
13. Verificar el resultado con git status y git log.