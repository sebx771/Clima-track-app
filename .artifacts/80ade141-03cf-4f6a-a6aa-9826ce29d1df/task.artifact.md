# Tareas: Muestreo Académico (DB y Mapa Barranquilla)

- [x] Actualizar `DatabaseHelper.kt`
  - [x] Incrementar `DATABASE_VERSION` a 12.
  - [x] Añadir 2 técnicos (`tecnico02`, `tecnico03`).
  - [x] Añadir 2 clientes ("Hospital Norte Barranquilla", "Centro Comercial Buenavista").
  - [x] Añadir 2 equipos asignados a estos nuevos clientes.
- [x] Actualizar `LocationUtils.kt`
  - [x] Cambiar ubicación `fallback` al centro de Barranquilla, Atlántico (Lat: 10.9639, Lon: -74.7964).
- [x] Actualizar `GeolocalizacionActivity.kt`
  - [x] Crear método `agregarTecnicosSimulados()` que dibuje pines alrededor de Barranquilla.
  - [x] Ajustar nivel de zoom inicial para ver la dispersión de los técnicos en la ciudad.
- [ ] Compilar y verificar.
