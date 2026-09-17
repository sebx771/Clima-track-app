# Corrección de Crash en APK (Geolocalización)

El usuario reporta que al presionar el botón de geolocalización en el APK (dispositivo físico), la aplicación se cierra inesperadamente (Crash). Esto sugiere un error en tiempo de ejecución que no se manifiesta en el emulador, probablemente relacionado con la inicialización del motor gráfico o el manejo de recursos.

## User Review Required

> [!IMPORTANT]
> Se aplicarán medidas de robustez extrema para evitar el cierre de la app. Si el mapa falla en cargar por hardware, la app mostrará un mensaje de error en lugar de cerrarse.

## Proposed Changes

### 1. Robustez en la Carga de Estilo

#### [MODIFY] [GeolocalizacionActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/activities/GeolocalizacionActivity.kt)
- **Validación de JSON**: Asegurar que el JSON de estilo nunca sea nulo o vacío (`{}`). Si la lectura del archivo falla, usaremos una URL de respaldo o un estilo mínimo válido predefinido para evitar que el motor de MapLibre lance una excepción fatal.
- **Manejo de Ciclo de Vida**: Envolver las llamadas a `binding.mapView` en bloques `try-catch` y verificar si el binding está inicializado.
- **Google Play Services**: Añadir una verificación de disponibilidad de los servicios de Google antes de intentar obtener la ubicación.
- **Evitar isDestroyed**: Aunque es válido en API 25+, usaremos una bandera local más segura para el hilo de `Geocoder`.

### 2. Optimización de Assets

- Verificar que el archivo `map_style.json` sea parseable.

## Verification Plan

### Manual Verification
1.  **APK Test**: Generar un nuevo APK y probar en el dispositivo físico.
2.  **Modo Avión**: Probar la entrada al mapa sin conexión para asegurar que la capa de background y el fallback del estilo eviten el crash.
3.  **Denegar Permisos**: Asegurar que si el usuario deniega el GPS, la app no se cierre y use la ubicación de Barranquilla (emulada).
