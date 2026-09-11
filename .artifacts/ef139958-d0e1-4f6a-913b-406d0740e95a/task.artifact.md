# Tareas: Mejora de Rol Cliente y Corrección de UI

- [ ] Modificar Repositorios
    - [ ] `ClienteRepository`: Añadir `getClienteByNombre`
    - [ ] `OrdenRepository`: Añadir `obtenerOrdenesPorCliente`
- [ ] Ajustar Lógica de Negocio y Seguridad
    - [ ] `AccessControl`: Permitir ver órdenes a Clientes
    - [ ] `SessionManager`: Implementar `getClienteIdAsociado`
- [ ] Actualizar Vistas (Fragments)
    - [ ] `DashboardFragment`: Ocultar menú "Clientes"
    - [ ] `EquiposFragment`: Filtrar por ID de Cliente real
    - [ ] `HistorialFragment`: Filtrar por ID de Cliente real
    - [ ] `OrdenesFragment`: Cargar órdenes propias del cliente
- [ ] Verificación Final
