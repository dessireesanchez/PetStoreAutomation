# Recepción y lectura de la DataTable

> **For agentic workers:** REQUIRED SUB-SKILL: Use
> superpowers:executing-plans to implement this plan task-by-task.

**Goal:** recibir y leer la tabla de datos del feature sin completar todavía
el formulario.

**Architecture:** el paso Cucumber recibe `DataTable` y usa
`asMaps(String.class, String.class)`. Verifica los encabezados `Campo` y
`Valor`, y que haya filas con nombres y valores no vacíos. Conserva una
`PendingException` explícita para el llenado que falta implementar.

**Tech Stack:** Java 21, Cucumber, AssertJ y Gradle.

**Spec:** propuesta explicada en la conversación y aprobada por la usuaria:
implementar la recepción y lectura; mantener los campos restantes pendientes.

## Global Constraints

Conservar los cambios anteriores y las acciones de User ID. No completar
otros campos, generar datos, guardar cuentas, cambiar dependencias ni hacer
commits. No imprimir los valores de la tabla, que incluyen contraseñas.

## Review Focus

La tabla tiene encabezados, doce filas y valores todavía expresados como
instrucciones de generación. Leerlos no implica generarlos ni escribirlos.
La suite completa debe seguir mostrando el flujo de registro como pendiente.

## Task 1: conectar la tabla al paso existente

**Files:** `src/test/java/steps/CreateUserStep.java`, `README.md` e
`IA/context.md`.

**Interfaces:** el método existente recibe `DataTable tabla`; no se cambian
las expresiones Gherkin ni los Page Objects.

- [x] Ejecutar la suite y confirmar el fallo previo por parámetros.
- [x] Agregar el parámetro, leer las filas y verificar su estructura.
- [x] Ejecutar la suite real: User ID debe pasar y TC-SIGNUP-01 debe llegar
  al mensaje explícito de tabla leída con doce campos y llenado pendiente.
- [x] Actualizar la documentación y revisar el diff.

**Verification:** `./gradlew test --rerun-tasks --no-daemon --console=plain`.
Se espera un escenario aprobado y uno pendiente, sin error de aridad.

## Resultado

La suite real confirma el cambio de error de aridad a `PendingException` con
el mensaje de doce campos leídos. El escenario de User ID pasa. Gradle termina
con un fallo por el flujo completo todavía pendiente. Se conserva este estado
para continuar el desarrollo sin simular que el formulario está completado.
