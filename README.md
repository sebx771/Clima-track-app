# ❄️ ClimaTrack - Gestión de Mantenimiento

Aplicación móvil Android (Kotlin) para la gestión operativa en campo de servicios de mantenimiento de climatización, con capacidad de trabajo **100% offline** mediante persistencia local en SQLite.

---

## 🏗️ Arquitectura de Paquetes

```text
com.example.mantenimiento
 ├── activities/    # Controladores de pantalla (UI)
 ├── adapters/      # Adaptadores para RecyclerView
 ├── models/        # Data Classes de entidades
 ├── database/      # SQLiteOpenHelper y DAOs
 ├── repositories/  # Abstracción de datos
 └── utils/         # Constantes y validaciones
```

---

## 🌿 Propósito de las Ramas (Git Flow)

### `main`

* **Producción:** Rama principal con código estable e integrado.
* Solo recibe cambios aprobados mediante **Pull Request**.

### `feature/diseno-ui`

* **Responsable:** Diseñador UI/UX (Integrante 1)
* **Propósito:** Creación de archivos XML de diseño (`res/layout`), temas, paleta de colores, íconos y componentes visuales reutilizables.

### `feature/core-autenticacion`

* **Responsable:** Líder de Proyecto (Integrante 2)
* **Propósito:**

    * Configuración de la base de datos mediante `SQLiteOpenHelper`.
    * **Módulo 1:** Splash/Login.
    * **Módulo 2:** Dashboard con métricas.
    * **Módulo 3:** Listado de Órdenes.

### `feature/gestion-equipos`

* **Responsable:** Desarrollador Kotlin (Integrante 3)
* **Propósito:**

    * **Módulo 4:** Registro de Mantenimiento.
    * **Módulo 5:** CRUD de Equipos.
    * **Módulo 6:** Repuestos.
    * **Módulo 10:** Historial de Mantenimientos.

### `feature/hardware-servicios`

* **Responsable:** Desarrollador Kotlin (Integrante 4)
* **Propósito:**

    * **Módulo 7:** Captura de Fotos/Evidencias.
    * **Módulo 8:** Aprobación y Firma en Canvas táctil.
    * **Módulo 9:** Geolocalización GPS.

---

## 🚀 Flujo de Trabajo

1. Trabajar exclusivamente en la rama `feature/` asignada.
2. Actualizar la rama diariamente con:

```bash
git pull origin main
```

3. Realizar los cambios correspondientes a la funcionalidad asignada.
4. Probar y verificar el funcionamiento antes de realizar la integración.
5. Crear un **Pull Request** hacia `main` para integrar el código finalizado.
