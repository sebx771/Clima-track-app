---
name: explicacion-codigo
description: usa esta skill cuando el usuario pida explicar, entender, analizar o aprender código de forma pedagógica y organizada
---

# Objetivo

Explicar el código de manera clara, progresiva y pedagógica, priorizando que el usuario comprenda cómo funciona y por qué está construido de esa manera, en lugar de limitarse a describir línea por línea.

La explicación debe adaptarse al nivel del usuario y evitar asumir conocimientos que no hayan sido demostrados previamente.

# Reglas generales

1. Explicar primero el objetivo general del código antes de entrar en los detalles.

2. Mantener siempre claro qué problema resuelve el código y cómo encaja dentro del proyecto.

3. No explicar el código como una lista aislada de líneas. Primero presentar la estructura general y después profundizar en cada parte.

4. Cuando aparezca un concepto que pueda ser desconocido, explicarlo antes de utilizarlo para explicar otra parte del código.

5. No asumir conocimientos previos innecesariamente.

6. Utilizar ejemplos sencillos cuando un concepto sea abstracto.

7. Diferenciar claramente entre:
    - qué hace el código
    - por qué se hace así
    - cómo funciona internamente
    - qué ocurriría si se hiciera de otra manera

8. No introducir conceptos, tecnologías o patrones que no sean necesarios para comprender el código actual.

9. Si existe una forma más sencilla de implementar algo, mencionarla solo si ayuda a comprender la implementación actual.

10. Mantener el objetivo principal del proyecto presente durante toda la explicación y evitar desviaciones innecesarias.

# Estructura de explicación

Cuando sea posible, seguir este orden:

## 1. Contexto

Explicar:

- qué archivo estamos viendo
- qué responsabilidad tiene
- dónde se encuentra dentro del proyecto
- qué problema resuelve

## 2. Idea general

Antes del código detallado, explicar el funcionamiento general utilizando lenguaje sencillo.

Si ayuda, representar el flujo:

entrada → procesamiento → resultado

o:

usuario → ruta → servicio → modelo → base de datos

## 3. Conceptos necesarios

Identificar los conceptos que el usuario necesita conocer para entender el código.

Explicarlos de forma independiente y sencilla antes de analizar su uso.

## 4. Código completo

Mostrar el código completo cuando sea necesario para mantener el contexto.

No fragmentar excesivamente el código en pequeños ejemplos desconectados.

## 5. Explicación por bloques

Dividir el código en bloques funcionales relacionados.

Para cada bloque explicar:

### Qué hace
Describir su función.

### Cómo funciona
Explicar el comportamiento del código.

### Por qué existe
Explicar qué problema resuelve dentro del proyecto.

### Cómo se conecta
Explicar su relación con otras partes del sistema.

## 6. Flujo de ejecución

Explicar qué ocurre cuando el programa realmente ejecuta ese código.

Seguir el recorrido de los datos paso a paso.

Por ejemplo:

1. El usuario realiza una petición.
2. Flask recibe la petición.
3. La ruta identifica la operación.
4. Se llama al servicio correspondiente.
5. El servicio procesa los datos.
6. SQLAlchemy interactúa con SQLite.
7. Se obtiene el resultado.
8. Flask devuelve la respuesta.

Adaptar el flujo al código real y no inventar componentes que todavía no existen.

## 7. Ejemplo práctico

Cuando sea útil, mostrar un ejemplo concreto con datos ficticios.

Por ejemplo:

```text
Entrada:
nombre = "Carlos"
monto = 500000

↓

Procesamiento

↓

Resultado:
registro creado correctamente