# Generar y completar User ID desde la tabla

> **For agentic workers:** REQUIRED SUB-SKILL: Use
> superpowers:executing-plans to implement this plan task-by-task.

**Goal:** convertir la indicación de User ID de la tabla en `U` seguido de
nueve caracteres alfanuméricos, escribirlo y verificar su contenido.

**Architecture:** mantener los localizadores en `RegistrationPage` y la
resolución de los datos en el paso Cucumber. Reutilizar
`RandomStringUtils.randomAlphanumeric(9)` de Commons Lang, ya declarado.
Conservar el identificador en el estado del escenario para el futuro login.

**Tech Stack:** Java 21, Cucumber, Selenium, AssertJ y Commons Lang.

**Spec:** la usuaria aprobó retomar el siguiente paso propuesto: generar el
User ID indicado en la tabla y escribirlo. Pidió tener presente el registro
completo; los demás hitos se conservan pendientes y se consultarán antes de
ampliar el alcance.

## Global Constraints

Conservar los cambios locales. No agregar dependencias, guardar cuentas,
realizar commits ni completar otros campos. Un valor explícito de User ID
debe usarse tal como aparece en la tabla. La aleatoriedad reduce colisiones,
pero no garantiza que el nombre esté disponible en el servidor.

## Review Focus

Comprobar el prefijo `U`, los nueve caracteres alfanuméricos, la igualdad entre
el valor guardado en el escenario y el contenido del campo, y la sustitución
por un valor explícito. Una tabla con campos sin implementar debe seguir
marcando el paso como pendiente e indicar sus nombres.

## Task 1: conectar la generación y escritura

**Files:** `src/test/resources/Feature/CreateUser/CreateUser.feature`,
`src/test/java/steps/CreateUserStep.java`, `README.md` e `IA/context.md`.

**Interfaces:** reutilizar `RegistrationPage.enterUserId(String)` y
`getUserId()`. El paso del formulario termina correctamente cuando su tabla
solo contiene User ID; mantiene `PendingException` para los demás campos.

- [x] Agregar un escenario `@userId @userIdTabla`: generar desde la tabla,
  comprobar formato y contenido, y sustituirlo por `UQA0000003` desde otra tabla.
- [x] Ejecutarlo y comprobar que falla por el llenado todavía pendiente.
- [x] Implementar la generación, la conservación por escenario, la escritura
  y su comprobación; mantener explícitos los campos pendientes.
- [x] Ejecutar los escenarios de User ID y comprobar que pasan.
- [x] Ejecutar la suite completa y documentar el resultado y los pendientes.
- [x] Revisar el diff y conservar los cambios anteriores.

**Verification:** `JAVA_TOOL_OPTIONS=-Dcucumber.filter.tags=@userId
./gradlew test --rerun-tasks --no-daemon --console=plain` para el alcance;
`./gradlew test --rerun-tasks --no-daemon --console=plain` para la suite.

## Hitos pendientes de TC-SIGNUP-01

- [ ] Completar New password y Repeat password.
- [ ] Completar nombre, apellido, teléfono, dirección, ciudad, estado, código
  postal y país.
- [ ] Sustituir `{timestamp}` y completar Email.
- [ ] Comprobar las preferencias por defecto.
- [ ] Implementar el clic en Save Account Information.
- [ ] Validar la redirección y confirmar el resultado del registro.
- [ ] Reutilizar las credenciales en el login y comprobar la autenticación.
- [ ] Revisar si el texto `docs` del feature demuestra el resultado esperado.

## Resultado

El escenario nuevo falló inicialmente por el llenado pendiente. Tras la
implementación, las dos pruebas `@userId` pasan con Chrome. La suite completa
ejecuta tres escenarios: dos aprobados y TC-SIGNUP-01 pendiente con el mensaje
que enumera los once campos sin implementar. El User ID del flujo completo
se escribe y verifica antes de ese pendiente. No se guardaron cuentas.
