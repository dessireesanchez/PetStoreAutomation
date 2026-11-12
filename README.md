# PetStoreAutomation

Proyecto de automatización web de JPetStore con Java, Selenium y Cucumber.
El entorno de trabajo utiliza macOS, Java 21 y el wrapper de Gradle 9.6.0.

## Raíz del proyecto

Abre la carpeta que contiene `build.gradle`, `settings.gradle` y `gradlew`.
En esta Mac, la ubicación actual es:

```bash
cd /Users/dessireesanchez/Proyectos/PetStoreAutomation
```

La configuración de Java y Gradle utiliza archivos del proyecto y detección
de JDK, sin fijar rutas personales. Si mueves la carpeta, ejecuta los comandos
desde su nueva ubicación.

## Preparar Java

Esta Mac tiene SDKMAN y Temurin 21.0.10 instalados. El archivo `.sdkmanrc`
selecciona esa versión para la terminal del proyecto. Desde la raíz, ejecuta:

```bash
sdk env
java -version
./gradlew --version
```

Si `sdk` no está disponible en la terminal, carga SDKMAN antes de ejecutar
los comandos anteriores:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

En otra Mac con SDKMAN, `sdk env install` instala la versión declarada si falta.
La activación con `sdk env` se repite en cada terminal nueva; no cambia el JDK
predeterminado de otros proyectos.

Después de activar el entorno, `java -version` y `Launcher JVM` deben mostrar
Java 21. `Daemon JVM` debe indicar Java 21 y el archivo
`gradle/gradle-daemon-jvm.properties` como origen de la selección.

Ese archivo exige un JDK 21 instalado y tiene prioridad sobre
`org.gradle.java.home`, incluso si tu configuración global de Gradle apunta
a Java 25. La toolchain de `build.gradle` también exige Java 21 para compilar
y ejecutar los tests. La compilación utiliza UTF-8 y genera bytecode de Java 21.

La selección del daemon sigue el mecanismo nativo de
[criterios JVM de Gradle](https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria).

## Usar Gradle

Ejecuta `./gradlew` desde la raíz. El wrapper fija Gradle 9.6.0; no hace falta
instalar ni seleccionar un Gradle global. La primera ejecución puede descargar
esa distribución y las dependencias.

Para comprobar las instalaciones Java que detecta Gradle, ejecuta:

```bash
./gradlew javaToolchains
```

`gradle/gradle-daemon-jvm.properties` se regenera con
`./gradlew updateDaemonJvm`. La configuración utiliza los JDK instalados y no
incluye URLs ni repositorios para descargar Java. En Windows, utiliza
`gradlew.bat` con un JDK 21 instalado. `.gitattributes` conserva los finales de
línea adecuados para ambos lanzadores cuando se utiliza Git.

## Abrir el proyecto en IntelliJ IDEA

Abre la raíz `PetStoreAutomation` e importa el proyecto Gradle.
Configura el JDK antes de sincronizar las dependencias:

1. Abre **File > Project Structure > Project**.
2. En **SDK**, agrega el JDK desde
   `~/.sdkman/candidates/java/21.0.10-tem` y asígnale el nombre `21`.
3. Selecciona ese SDK y el nivel de lenguaje **21**, sin opciones preview.
4. En **Settings > Build, Execution, Deployment > Build Tools > Gradle**,
   selecciona el wrapper del proyecto y **Project SDK** como **Gradle JVM**.
5. Recarga el proyecto Gradle.

Los archivos del proyecto referencian el SDK `21`. Si IntelliJ todavía no lo
tiene registrado, debes agregarlo con los pasos anteriores.

## Comprobar la compilación

Compila los fuentes principales y los de pruebas desde la raíz del proyecto:

```bash
./gradlew testClasses
```

Para reconstruir desde cero, utiliza `./gradlew clean testClasses`. Chrome debe
estar instalado para ejecutar los escenarios web; `DriverManager` utiliza
`ChromeDriver`. Selenium Manager puede resolver el driver compatible.

## Ejecutar Cucumber

La suite [`RunCucumberTest`](src/test/java/runner/RunCucumberTest.java) es el
runner: el punto de entrada que reúne y ejecuta los escenarios. Utiliza JUnit
Platform para buscar los archivos `.feature` bajo `src/test/resources/Feature`
y sus implementaciones en los paquetes `steps` y `hook`. Cucumber utiliza el
BOM 7.18.0 y JUnit el BOM 5.10.2, la versión empleada por ese motor de
Cucumber. La configuración sigue la
[suite recomendada por Cucumber](https://github.com/cucumber/cucumber-jvm/blob/v7.18.0/cucumber-junit-platform-engine/README.md#suites-with-different-configurations).

Para ejecutar los escenarios con Chrome desde la raíz, utiliza:

```bash
./gradlew test
```

Para ejecutar únicamente los dos escenarios implementados de User ID, utiliza:

```bash
./gradlew test -PcucumberTags="@userId" --rerun-tasks
```

Puedes combinar etiquetas con `and`, `or` y `not`, por ejemplo,
`-PcucumberTags="@userId and not @userIdTabla"`. Para filtrar por el nombre
del escenario, utiliza una expresión regular:

```bash
./gradlew test -PcucumberName="Generar y completar el User ID desde la tabla"
```

En este motor, la expresión debe coincidir con el nombre completo. Para buscar
una parte, utiliza `.*`, por ejemplo, `-PcucumberName=".*desde la tabla.*"`.

Si indicas ambos filtros, el escenario debe cumplir los dos. `--rerun-tasks`
fuerza una nueva ejecución cuando Gradle considera las pruebas actualizadas.

Para comprobar que Cucumber encuentra el escenario y sus definiciones de
pasos sin abrir Chrome ni ejecutar los hooks o las acciones, utiliza:

```bash
./gradlew test -PcucumberDryRun=true
```

El modo simulación no valida el registro, los resultados esperados ni el código
interno de los pasos. Tampoco comprueba que el método del formulario reciba
correctamente la tabla de datos. La ejecución normal permanece activa por
defecto; el modo simulación solo se habilita para el comando que incluye la
propiedad `cucumberDryRun=true`. Puedes combinarla con los filtros anteriores.

Gradle reenvía estas opciones al proceso Java que ejecuta los tests:

| Opción de Gradle | Propiedad de Cucumber | Valor predeterminado |
| --- | --- | --- |
| `-PcucumberDryRun=true` | `cucumber.execution.dry-run` | `false` |
| `-PcucumberTags="@userId"` | `cucumber.filter.tags` | Sin filtro |
| `-PcucumberName="expresión"` | `cucumber.filter.name` | Sin filtro |

También puedes pasar las propiedades de Cucumber mediante `-D`, por ejemplo,
`./gradlew test -Dcucumber.filter.tags="@userId"`. Si defines la misma opción
con `-P` y `-D`, tiene prioridad `-P`.

Para ejecutar desde IntelliJ, abre `RunCucumberTest.java` y utiliza el icono
de ejecución junto a la clase. También puedes crear una configuración Gradle
con `test -PcucumberTags="@userId"`. Las opciones `-P` pertenecen a Gradle;
si ejecutas directamente la suite como JUnit, usa las propiedades `-D` en
**VM options**. Para simular allí, agrega
`-Dcucumber.execution.dry-run=true`.

### Reportes

Cada ejecución genera reportes locales, además del resumen de consola:

| Reporte | Archivo |
| --- | --- |
| Cucumber HTML, con escenarios y pasos | `build/reports/cucumber/cucumber.html` |
| Cucumber JSON, para otras herramientas | `build/reports/cucumber/cucumber.json` |
| Cucumber JUnit XML, para integración continua | `build/reports/cucumber/cucumber.xml` |
| Gradle HTML | `build/reports/tests/test/index.html` |

Los reportes corresponden a la última ejecución y sus filtros; una simulación
también los sobrescribe y no demuestra que las acciones web funcionen. Los
reportes de Gradle se generan al ejecutar desde Gradle. Al ejecutar directamente
la clase en IntelliJ, usa la raíz del proyecto como **Working directory** para
conservar las rutas de los reportes Cucumber. `./gradlew clean` elimina los
archivos generados bajo `build/`.

## Estado de los tests

El archivo `src/test/resources/Feature/CreateUser/CreateUser.feature` contiene
el escenario completo `TC-SIGNUP-01` y dos escenarios `@userId`. Uno comprueba
la escritura directa y sustitución del campo; el otro genera User ID desde la
tabla y después lo sustituye por un valor explícito. Todos utilizan la
navegación **Sign In → Register Now** y validan que el formulario esté visible.

Para ejecutar únicamente las pruebas de User ID en macOS, utiliza:

```bash
./gradlew test -PcucumberTags="@userId" --rerun-tasks
```

Este comando aplica el filtro al proceso Java de pruebas sin configurar
`JAVA_TOOL_OPTIONS` para todas las JVM. En esta suite con
JUnit Platform, `CUCUMBER_FILTER_TAGS=@userId` no filtró los escenarios durante
la verificación del 9 de noviembre de 2026.

La prueba directa escribe `UQA0000001` y lo reemplaza por `UQA0000002`,
comprobando el contenido después de cada acción. El localizador
`By.name("username")` se confirmó en el HTML de `/account/new`.

El paso `completa el formulario con los siguientes datos:` ya recibe la
`DataTable` de Gherkin. Usa `asMaps(String.class, String.class)` para leer cada
fila por sus encabezados `Campo` y `Valor`. Comprueba que exista al menos una
fila de datos, que los encabezados sean los esperados y que cada campo y valor
contengan texto. Actualmente completa y verifica User ID: la indicación
`U + 9 caracteres alfanuméricos generados` produce un identificador con ese
formato mediante Commons Lang, ya declarado en el proyecto. Si la tabla trae
un valor explícito, lo utiliza tal como está. Conserva el valor en el estado
del escenario para el futuro login.

El 10 de noviembre de 2026 las dos pruebas de User ID pasan con Chrome. La
prueba de tabla comprueba el formato generado, su igualdad con el contenido
del campo y su sustitución por `UQA0000003`.

La suite completa ejecuta tres escenarios: los dos de User ID pasan y
`TC-SIGNUP-01` queda pendiente, registrado como fallo por Gradle. En ese flujo
ya se completó y verificó User ID. El mensaje enumera los once campos que aún
faltan: New password, Repeat password, First name, Last name, Email, Phone,
Address 1, City, State, Zip y Country. No se guarda la cuenta.

Para terminar `TC-SIGNUP-01` se mantienen estos hitos pendientes, que requieren
acordar la siguiente etapa antes de ampliar la implementación:

- [ ] Completar y comprobar New password y Repeat password.
- [ ] Completar los datos personales y la dirección.
- [ ] Sustituir `{timestamp}` y completar Email.
- [ ] Comprobar las preferencias por defecto.
- [ ] Implementar el clic en Save Account Information.
- [ ] Validar la redirección y confirmar el registro de la cuenta.
- [ ] Reutilizar las credenciales para iniciar sesión y comprobar el resultado.
- [ ] Revisar si el texto `docs` demuestra el resultado esperado del registro
  y de la autenticación.
