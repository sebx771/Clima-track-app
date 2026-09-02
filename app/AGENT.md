# modulos esperados para la aplicacion CLIMATRACK
compara modulos actuales implementados , tienes 2 tareas
### DEFINIR PERMISOS DE LOS TRES ROLES PROPUESTOS (CLIENTE , ADMIN . TENICO )
### RESALTAR QUE MODULOS HACEN FALTA EN LA APP 
## Módulo 1. Autenticación
La aplicación deberá presentar una pantalla de inicio de sesión.
Funcionalidades
• Ingresar usuario.
• Ingresar contraseña.
• Validar credenciales almacenadas localmente.
• Mostrar mensajes de validación.
• Permitir cerrar sesión.
Ejemplo de usuario
Usuario: tecnico01
Contraseña: 123456
Rol: Técnico

## 7. Módulo 2. Panel principal
   Después del inicio de sesión, el técnico accederá a un Dashboard.
   Debe mostrar como mínimo:
   • Nombre del técnico.

• Cantidad de órdenes pendientes.
• Cantidad de órdenes en proceso.
• Cantidad de órdenes finalizadas.
• Acceso a órdenes.
• Acceso a equipos.
• Acceso a historial.
• Opción cerrar sesión.

## 8. Módulo 3. Gestión de órdenes de
   mantenimiento
   La propuesta establece la visualización de las órdenes asignadas y el registro de
   mantenimientos preventivos y correctivos.
   La aplicación deberá permitir consultar las órdenes asignadas al técnico.
   Cada orden debe contener como mínimo:
   • Número de orden.
   • Fecha.
   • Cliente.
   • Dirección.
   • Equipo.
   • Tipo de mantenimiento.
   • Descripción del servicio.
   • Estado.
   Estados sugeridos
   PENDIENTE
   EN PROCESO
   FINALIZADA
   CANCELADA
   Tipos de servicio
   La propuesta contempla mantenimiento preventivo, correctivo, asesoría e inspección.
   Por tanto:
   PREVENTIVO
   CORRECTIVO
   ASESORÍA
   INSPECCIÓN

## 9. Módulo 4. Registro del mantenimiento
   Al seleccionar una orden, el técnico podrá registrar el resultado del servicio.
   El formulario deberá incluir:
   • Fecha del mantenimiento.
   • Tipo de servicio.
   • Descripción del trabajo realizado.
   • Diagnóstico.
   • Observaciones.
   • Estado del equipo.
   • Recomendaciones.
   • Tiempo empleado.
   • Nombre del técnico.
   Validaciones
   El sistema deberá impedir guardar el mantenimiento cuando los campos obligatorios estén
   vacíos.
   Al guardar correctamente deberá mostrarse:
   Mantenimiento registrado correctamente.

## 10. Módulo 5. Gestión de equipos
    La solución deberá manejar la información básica de los equipos de climatización.
    Datos mínimos
    • ID del equipo.
    • Código del equipo.
    • Tipo de equipo.
    • Marca.
    • Modelo.
    • Número de serie.
    • Capacidad.
    • Ubicación.
    • Cliente.
    • Estado.
    Estados
    OPERATIVO

### EN MANTENIMIENTO
FUERA DE SERVICIO
 operaciones CRUD:
• Crear equipo.
• Consultar equipo.
• Actualizar equipo.
• Eliminar equipo.
• Buscar equipo.

## 11. Módulo 6. Registro de repuestos
    La propuesta contempla el registro de repuestos utilizados durante el mantenimiento.
    La aplicación deberá permitir asociar repuestos a una orden.
    Información mínima
    • Código.
    • Nombre del repuesto.
    • Cantidad.
    • Unidad.
    • Observación.
    Ejemplo:
    Repuesto: Filtro de aire
    Cantidad: 2
    Unidad: Unidad
    Observación: Cambio por deterioro

## 12. Módulo 7. Evidencias fotográficas
    La propuesta contempla la captura de evidencia mediante fotografías.
    El aprendiz deberá implementar una funcionalidad que permita:
    • Tomar una fotografía.
    • Asociarla a una orden.
    • Visualizar la evidencia.
    • Eliminar una evidencia.
    Para una primera versión académica, las fotografías podrán almacenarse como archivos
    locales y SQLite podrá conservar la ruta o URI asociada.

## 13. Módulo 8. Aprobación del cliente
    La propuesta incluye firma digital o aprobación del cliente.
    Como ejercicio académico, el aprendiz deberá implementar una pantalla de aprobación que
    permita:
    • Mostrar resumen del servicio.
    • Mostrar observaciones.
    • Registrar nombre del cliente.
    • Registrar aceptación.
    • Guardar la aprobación asociada a la orden.
    Opcionalmente, se podrá implementar una superficie de firma mediante interacción táctil.