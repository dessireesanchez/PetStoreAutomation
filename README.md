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

La suite `src/test/java/runner/RunCucumberTest.java` utiliza JUnit Platform para
buscar los escenarios bajo `src/test/resources/Feature` y sus implementaciones
en los paquetes `steps` y `hook`. Cucumber utiliza un BOM 7.18.0 compartido y
JUnit utiliza el BOM 5.10.2, la versión empleada por el motor de Cucumber
7.18.0. La configuración sigue la
[suite recomendada por Cucumber](https://github.com/cucumber/cucumber-jvm/blob/v7.18.0/cucumber-junit-platform-engine/README.md#suites-with-different-configurations).

Para ejecutar los escenarios con Chrome desde la raíz, utiliza:

```bash
./gradlew test
```

Para comprobar que Cucumber encuentra el escenario y sus definiciones de
pasos sin abrir Chrome ni ejecutar los hooks o las acciones, utiliza:

```bash
./gradlew test -PcucumberDryRun=true
```

El modo simulación no valida el registro, los resultados esperados ni el código
interno de los pasos. Tampoco comprueba que el método del formulario reciba
correctamente la tabla de datos. La ejecución normal permanece activa por
defecto; el modo simulación solo se habilita para el comando que incluye la
propiedad `cucumberDryRun=true`.

Después de cualquiera de las dos ejecuciones, el informe de Gradle está en
`build/reports/tests/test/index.html`. El informe corresponde a la última
ejecución de la tarea `test`.

## Estado de los tests

El escenario `TC-SIGNUP-01` está en
`src/test/resources/Feature/CreateUser/CreateUser.feature`. La apertura de la
URL está implementada y quedan nueve definiciones de pasos pendientes.

Gradle ya descubre el escenario mediante la suite compartida. La ejecución
real todavía requiere completar los pasos y las validaciones del registro
y del inicio de sesión. El siguiente paso del trabajo guiado es implementar
la navegación desde **Sign In** hasta el formulario de registro.
