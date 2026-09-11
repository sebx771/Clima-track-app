# Plan de Implementación: Mejoras en el Rol Cliente y Corrección de UI

Este plan detalla las correcciones necesarias para que el rol de `Cliente` pueda ver su historial, sus equipos, sus órdenes asociadas y resolver el problema visual de paneles inaccesibles.

## El Problema Actual (Diagnóstico)
Actualmente el rol Cliente sufre de 2 problemas principales:
1. **Secciones vacías (Equipos/Historial):** Las vistas están buscando los equipos y mantenimientos asociados al `userId` (ID de sesión del usuario, por ejemplo `3`), pero en la base de datos estos están asociados al `cliente_id` (ID de la empresa, por ejemplo `1`). Al no coincidir, las listas aparecen vacías.
2. **Falta de acceso a Órdenes:** El control de acceso bloquea la vista de "Órdenes" para los clientes.
3. **Panel "Clientes" visible:** En el menú lateral/popup (el menú de 3 puntitos o menú de opciones), se programó ocultar "Usuarios" a los Técnicos y Clientes, pero se olvidó ocultar el menú de "Clientes", exponiendo una opción no autorizada y provocando mala experiencia UI.

## Propuesta de Solución

Para solucionarlo de forma elegante sin tener que reconstruir la base de datos, vamos a crear un puente en `SessionManager` que, a partir del nombre de la empresa del cliente (`empresaCliente`), busque automáticamente su verdadero `cliente_id` usando el repositorio, y se lo pase a las vistas.

### [ClienteRepository]
#### [MODIFY] [ClienteRepository.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/repository/ClienteRepository.kt)
- Añadir método `getClienteByNombre(nombre: String): Cliente?` para poder buscar el `cliente_id` sabiendo el nombre de la empresa del cliente logueado (ej. "ACME S.A.S").

### [OrdenRepository]
#### [MODIFY] [OrdenRepository.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/repository/OrdenRepository.kt)
- Añadir método `obtenerOrdenesPorCliente(clienteId: Int): List<Orden>` para traer exclusivamente las órdenes asociadas a su empresa.

### [Seguridad y Sesión]
#### [MODIFY] [AccessControl.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/security/AccessControl.kt)
- Modificar `canViewOrders` para que retorne `true` también si el rol es `Role.CLIENTE`. (De esta forma ya no se bloqueará el fragmento).

#### [MODIFY] [SessionManager.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/security/SessionManager.kt)
- Añadir método `getClienteIdAsociado(context: Context): Int`. Este buscará en `ClienteRepository` la empresa vinculada a la sesión y retornará su verdadero `ID` de base de datos. Si no se encuentra, retornará -1.

### [Vistas y UI]
#### [MODIFY] [DashboardFragment.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/fragments/DashboardFragment.kt)
- En la lógica del `PopupMenu` (ivMenuOpciones), añadir la línea para ocultar `nav_clientes` además de `nav_usuarios` cuando el rol no sea Admin.

#### [MODIFY] [EquiposFragment.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/fragments/EquiposFragment.kt)
- Cambiar la lógica para que cuando el rol sea `CLIENTE`, busque los equipos por `sessionManager.getClienteIdAsociado(requireContext())` en lugar del ID del usuario.

#### [MODIFY] [HistorialFragment.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/fragments/HistorialFragment.kt)
- Cambiar la lógica de búsqueda de manera similar a EquiposFragment, usando `getClienteIdAsociado()`.

#### [MODIFY] [OrdenesFragment.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/fragments/OrdenesFragment.kt)
- Añadir un bloque condicional que, cuando el usuario sea `CLIENTE`, cargue la lista filtrando únicamente sus propias órdenes mediante `obtenerOrdenesPorCliente()`.

## Verificación
1. **Ejecutar la app e iniciar sesión como cliente01.**
2. **Validar menú superior:** Al abrir las opciones en el Dashboard, no se debe ver el panel de "Clientes".
3. **Validar datos del Cliente:** Ingresar a "Equipos" y constatar que se lista al menos 1 equipo asignado a "ACME S.A.S" (Ej: 'WindFree 24K').
4. **Validar "Órdenes":** Asegurarse de que el botón de órdenes esté disponible y que al ingresar se listé su orden de mantenimiento (Ej: 'OT-00025').