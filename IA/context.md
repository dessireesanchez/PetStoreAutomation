# Resumen

Contexto actualizado el 12 de noviembre de 2026. PetStoreAutomation automatiza
pruebas web de JPetStore con Selenium y Cucumber. El entorno de trabajo es macOS
con Java 21. La raíz actual es
`/Users/dessireesanchez/Proyectos/PetStoreAutomation`.

La apertura de la URL, los clics **Sign In → Register Now** y la validación del
formulario están implementados. Dos escenarios `@userId` comprueban la
escritura directa y la generación y escritura desde la tabla, incluyendo la
sustitución por valores explícitos. La suite de Cucumber permite ejecutar los
escenarios desde Gradle. El paso del formulario recibe y lee las doce filas;
completa User ID y conserva su valor por escenario. TC-SIGNUP-01 continúa
pendiente: se detiene explícitamente después de verificar User ID y enumera
los once campos restantes. No se guarda la cuenta.

El 12 de noviembre de 2026 se preparó
`IA/local/cobertura_prioridad_alta_autenticacion.md` para la cobertura de registro
y Sign In de prioridad alta, usando el plan de registro v4 y el documento
funcional de Sign In, cuyas copias se conservan en `IA/local/`. La usuaria
confirmó que se deben contrastar las reglas con JPetStore y reportar sus
incumplimientos. No hay acceso a base de datos ni backend. El login debe
aceptar tanto User ID como email. Los datos y resultados se prepararán y
comprobarán por UI.
La matriz incluye una investigación de Remember Me y la cobertura funcional
aceptada por la usuaria. Se excluye expresamente la prueba de vencimiento a
los catorce días; se mantienen las demás comprobaciones de persistencia y
cierre de sesión. No implica escenarios nuevos implementados ni nuevas
ejecuciones verificadas.

# Arquitectura y módulos

El proyecto Gradle tiene un único módulo. Los tests se organizan por
responsabilidad:

- `driver/DriverManager.java`: crea un ChromeDriver por hilo y lo cierra.
- `hook/Hook.java`: obtiene y cierra el driver antes y después del escenario.
- `page/BasePage.java`: navegación, búsqueda, interacción y esperas.
- `page/HomePage.java`: apertura de la URL y clic en **Sign In**.
- `page/SignInPage.java`: clic en **Register Now**.
- `page/RegistrationPage.java`: validación del formulario y escritura y lectura
  de User ID mediante `By.name("username")`.
- `interfaces/ButtonClick.java`: contrato `darClick()` implementado por
  `HomePage` y `SignInPage`.
- `steps/CreateUserStep.java`: definiciones de los pasos de registro y login.
- `runner/RunCucumberTest.java`: suite JUnit Platform para el motor Cucumber,
  los recursos `Feature` y el glue `steps,hook`.
- `src/test/resources/Feature/CreateUser/`: escenario Gherkin.

`src/main/java/org/example/Main.java` conserva el ejemplo inicial del IDE.
La suite compartida sustituye la dependencia de las configuraciones personales
de ejecución conservadas en `.idea/workspace.xml`.

# Tecnología y versiones

Las siguientes versiones proceden de los archivos de configuración y de la
comprobación local del entorno:

| Componente | Configuración actual |
| --- | --- |
| Java | Toolchain y release 21; Temurin 21.0.10 instalado |
| Gradle | Wrapper 9.6.0 |
| JVM del daemon de Gradle | Java 21, definido en `gradle/gradle-daemon-jvm.properties` |
| Codificación de compilación | UTF-8 |
| Cucumber | BOM 7.18.0 para Java y JUnit Platform Engine |
| Selenium | 4.52.0 |
| JUnit | BOM 5.10.2; suite y launcher de Platform 1.10.2 |
| SDKMAN | `.sdkmanrc` selecciona `21.0.10-tem` |
| IntelliJ | SDK `21`, lenguaje y bytecode 21; Gradle usa Project SDK |

`build.gradle` también declara TestNG, RestAssured, Allure, AssertJ, Hamcrest,
HttpClient, Jackson, commons-lang3, OpenCSV, Vavr, Logback y Janino. No se han
modificado sus versiones durante la preparación del entorno macOS.

Se alineó JUnit con la versión 5.10.2 empleada por el motor Cucumber 7.18.0.
Se retiró `cucumber-junit`, el adaptador JUnit 4, y las dependencias directas
de Cucumber en `implementation`. `cucumber-java` y el motor se declaran en
`testImplementation`; el motor incorpora `cucumber-core` transitivamente.

Se actualizó `selenium-java` de 4.20.0 a 4.52.0 para incorporar soporte CDP
para Chrome 156. La versión 4.52.0 incluye CDP 154, 155 y 156 según su
[registro oficial de cambios](https://github.com/SeleniumHQ/selenium/blob/selenium-4.52.0/java/CHANGELOG).

Las seis declaraciones de dependencias que usaban notación de múltiples
argumentos ahora usan `grupo:artefacto:versión`. Esto elimina las advertencias
de obsolescencia de Gradle para esas declaraciones.

# Contratos (si existen)

El feature describe el comportamiento esperado. La interfaz `ButtonClick`
define `void darClick()`. No existen contratos de API. El método del paso que
completa el formulario recibe `DataTable tabla` y utiliza los encabezados
`Campo` y `Valor`. Requiere al menos una fila de datos y nombres y valores con
texto. Completa User ID y comprueba su contenido. Si quedan campos sin
implementar, lanza una `PendingException` con sus nombres. Una tabla que solo
contiene User ID termina correctamente.

# Módulos de datos

Los datos se describen en el feature. No existen modelos ni archivos externos
de datos. La tabla se convierte en `List<Map<String, String>>` con
`asMaps(String.class, String.class)`.

Para User ID, la indicación `U + 9 caracteres alfanuméricos generados` se
resuelve con `"U" + RandomStringUtils.randomAlphanumeric(9)`, reutilizando
Commons Lang. Los valores explícitos se conservan. `userIdRegistro` guarda el
identificador en la instancia de pasos del escenario; su reutilización en el
login todavía está pendiente. La generación aleatoria no garantiza que el
nombre esté disponible en el servidor.

Los demás valores todavía se leen como texto. La sustitución de `{timestamp}`
del correo y la conservación y reutilización de la contraseña están
pendientes. No se imprimen los valores de las filas, que incluyen contraseñas
de prueba.

# Estándares y convenciones del código

Los paquetes se separan en `driver`, `hook`, `page`, `steps` y `runner`.
Las clases usan PascalCase y los métodos camelCase. Los pasos están escritos
en español y las palabras clave de Gherkin en inglés. Se utiliza el patrón
Page Object y un `ThreadLocal` para el driver.

`README.md` documenta la preparación en macOS. Los cambios de código o
configuración relevantes deben reflejarse en este contexto.

Por acuerdo con la usuaria, los únicos documentos de `IA/` que se mantienen
versionados son `IA/context.md` y `IA/prompt_context.md`. Los documentos
funcionales, planes, matrices e informes históricos se guardan en `IA/local/`,
excluido localmente mediante `/IA/local/` en `.git/info/exclude`. Este archivo
de exclusión no se versiona. El contexto conserva los acuerdos necesarios
para trabajar; los documentos locales pueden no existir en otro clon.

La raíz contiene `.sdkmanrc` para seleccionar Java en la terminal y el wrapper
para fijar Gradle. `.gitattributes` establece LF para `gradlew` y CRLF para los
archivos `.bat` cuando se utiliza Git. La configuración ejecutable no incluye
rutas absolutas a los JDK de esta Mac.

Para preparar el control de versiones, `.gitignore` excluye `.idea/`, las
cachés y compilaciones, y los directorios generados `allure-results/` y
`allure-report/`. Se conserva el JAR del wrapper de Gradle entre los archivos
que deben versionarse.

`./gradlew test` ejecuta la suite real. La opción `-PcucumberDryRun=true`
habilita explícitamente `cucumber.execution.dry-run` para comprobar el
descubrimiento sin ejecutar hooks ni acciones. El valor predeterminado es
`false`. `-PcucumberTags` y `-PcucumberName` filtran por expresión de etiquetas
y expresión regular del nombre completo; para coincidencias parciales se usa
`.*`. Si se combinan, se aplican ambos filtros. Gradle
reenvía estas opciones a la JVM de pruebas y admite las propiedades equivalentes
`-Dcucumber.execution.dry-run`, `-Dcucumber.filter.tags` y
`-Dcucumber.filter.name`. Las opciones `-P` tienen prioridad sobre `-D`.

La suite tiene el nombre visible `Pruebas de JPetStore` y usa los plugins
`pretty,summary,html,json,junit`. Los reportes Cucumber se escriben en
`build/reports/cucumber/cucumber.html`, `cucumber.json` y `cucumber.xml`.
Gradle registra ese directorio como salida de la tarea `test` y también genera
`build/reports/tests/test/index.html`. Los reportes reflejan la última
ejecución, incluidos sus filtros y el modo simulación.

# Riesgos, supuestos y limitaciones

Verificaciones realizadas el 12 de noviembre de 2026 antes de preparar el
commit de filtros, reportes y documentación:

- La simulación completa con `-PcucumberDryRun=true` descubre tres escenarios
  únicos y genera los reportes HTML, JSON y XML de Cucumber.
- La simulación mediante `-Dcucumber.execution.dry-run=true`, combinando
  `-PcucumberTags=@userId` y `-PcucumberName=.*desde la tabla`, selecciona
  únicamente el escenario de tabla. Las opciones `-P` prevalecen sobre los
  filtros `-D` contradictorios pasados en el mismo comando.
- La ejecución real con `-Dcucumber.filter.tags=@userId` aprueba los dos
  escenarios implementados y omite el registro completo por el filtro.
  No se guarda ninguna cuenta.
- Las tres ejecuciones terminan con `BUILD SUCCESSFUL`, usando
  `--rerun-tasks --no-daemon --console=plain --warning-mode=fail`.
- En el índice de Git, `IA/` contiene únicamente `context.md` y
  `prompt_context.md`. Los documentos de `IA/local/` quedan excluidos.

Verificaciones realizadas el 11 de noviembre de 2026:

- La simulación completa descubre tres escenarios únicos y genera reportes
  HTML, JSON y JUnit XML válidos.
- El filtro `@userId and not @userIdTabla` selecciona únicamente el escenario
  de escritura directa. La combinación de filtros de nombre y etiquetas
  selecciona el escenario de tabla y confirma la prioridad de `-P` sobre `-D`.
- La expresión del nombre debe coincidir con el nombre completo; una expresión
  parcial sin `.*` deja los escenarios omitidos en este motor.
- La ejecución real con `-Dcucumber.filter.tags=@userId` pasa los dos escenarios
  implementados. La ejecución completa con `./gradlew test --no-daemon
  --console=plain --warning-mode=fail` ejecuta tres escenarios: dos pasan y
  `TC-SIGNUP-01` falla con `PendingException`, enumerando los once campos del
  formulario todavía sin implementar. No se guarda ninguna cuenta.

Verificaciones realizadas el 10 de noviembre de 2026:

- El escenario nuevo de User ID desde tabla falla antes de implementar el
  llenado, con el mensaje previo de tabla recibida y llenado pendiente.
- Tras implementar la generación y escritura, el comando
  `JAVA_TOOL_OPTIONS=-Dcucumber.filter.tags=@userId ./gradlew test
  --rerun-tasks --no-daemon --console=plain` termina con `BUILD SUCCESSFUL`:
  dos escenarios aprobados y TC-SIGNUP-01 omitido por el filtro.
- La prueba nueva valida `U[A-Za-z0-9]{9}`, compara el valor conservado en el
  escenario con el campo y después escribe el valor explícito `UQA0000003`
  mediante otra tabla.
- `./gradlew test --rerun-tasks --no-daemon --console=plain` ejecuta tres
  escenarios: dos aprobados y TC-SIGNUP-01 pendiente, registrado como fallo
  por Gradle. El escenario completo escribe y verifica User ID; después
  enumera los once campos sin implementar. No se guarda ninguna cuenta.
- Quedan pendientes las contraseñas, los datos de contacto, el correo
  dinámico, las preferencias, el guardado y las validaciones de registro y
  login. También debe revisarse si el texto `docs` del feature confirma
  realmente los resultados esperados.

Verificaciones realizadas el 9 de noviembre de 2026:

- Se confirmó `name="username"` en el HTML público de `/account/new`.
- Antes de conectar la validación, el escenario falla con `PendingException`
  en `ingresa a la página de registro`.
- Tras conectar la validación, los cuatro pasos del Background pasan y el
  escenario focalizado falla por los nuevos pasos de User ID sin definir.
- Con las acciones implementadas, `JAVA_TOOL_OPTIONS=-Dcucumber.filter.tags=@userId
  ./gradlew test --rerun-tasks --no-daemon --console=plain` termina con
  `BUILD SUCCESSFUL`: el escenario de User ID pasa y TC-SIGNUP-01 se omite.
- Antes de conectar la tabla, `./gradlew test --rerun-tasks --no-daemon
  --console=plain` ejecuta dos escenarios: User ID pasa y TC-SIGNUP-01 falla
  por la incompatibilidad de parámetros del paso con tabla.
- Tras agregar `DataTable tabla` y la lectura de filas, el mismo comando
  vuelve a ejecutar ambos escenarios con Chrome. User ID pasa y TC-SIGNUP-01
  queda pendiente con `Tabla recibida y leída: 12 campos. Pendiente: completar
  el formulario con estos datos.` Gradle registra ese pendiente como fallo.
  Desaparece el error de parámetros. No se completaron otros campos ni se
  envió el formulario.
- `CUCUMBER_FILTER_TAGS=@userId` no filtró esta suite con JUnit Platform;
  el filtro mediante propiedad Java sí funcionó.
- El launcher Java emite una advertencia de acceso nativo restringido de
  Gradle. No impide ejecutar las pruebas y no se cambió la configuración Java.

Las verificaciones del 4 de noviembre que aparecen a continuación son
históricas y describen el estado anterior a la implementación de los clics.

La preparación de macOS establece Java 21 en Gradle e IntelliJ y conserva el
permiso de ejecución de `gradlew`. El SDK debe registrarse en IntelliJ con el
nombre `21` si todavía no está disponible allí. `sdk env` activa Java 21 en la
terminal del proyecto; la activación no es automática en esta Mac.

La terminal global usa Temurin 25.0.4 y
`~/.gradle/gradle.properties` contiene `org.gradle.java.home` apuntando a
Temurin 25.0.2. El archivo `gradle/gradle-daemon-jvm.properties` tiene prioridad
sobre esa propiedad y exige Java 21 para el daemon de este proyecto. No se
modificó la configuración global. `build.gradle` configura `updateDaemonJvm`
para regenerar el criterio 21 sin URLs de descarga; se necesita un JDK 21
instalado y detectable por Gradle.

Verificaciones realizadas el 4 de noviembre de 2026:

- `sdk env` selecciona Temurin 21.0.10 en la terminal.
- `./gradlew updateDaemonJvm` genera el archivo con `toolchainVersion=21`.
- `./gradlew --version` confirma Gradle 9.6.0 y el criterio Java 21 del daemon,
  incluso cuando el launcher utiliza el Java 25 global.
- `./gradlew clean testClasses javaToolchains --offline --no-daemon
  --console=plain --warning-mode=fail` finaliza con `BUILD SUCCESSFUL`.
  Gradle detecta Temurin 21.0.10 como JVM actual y recompila los fuentes
  principales y de pruebas. No aparecen advertencias de obsolescencia.
- Antes de crear la suite, `./gradlew test` fallaba por no descubrir pruebas.
- `./gradlew test -PcucumberDryRun=true --no-daemon --console=plain
  --warning-mode=fail` compila la suite y finaliza con `BUILD SUCCESSFUL`;
  descubre el escenario TC-SIGNUP-01. El feature contiene 14 pasos y sus
  expresiones están definidas. El modo simulación no valida el código interno
  ni la firma del paso que recibe una tabla.
- `./gradlew test --no-daemon --console=plain --warning-mode=fail` ejecuta un
  escenario real: Chrome abre la URL y el primer `Given` pasa. La ejecución
  falla en `CreateUserStep.java:25`, con `PendingException` en el clic
  **Sign In**; los pasos siguientes se omiten. Se mantiene el fallo visible.
- Chrome y ChromeDriver 156.0.8078.80 están instalados. Antes de actualizar
  Selenium, el acceso inicial con 4.20.0 funcionaba bajo Gradle, pero el log
  avisaba de que no había una implementación CDP para Chrome 156.
- Con Selenium 4.52.0, `./gradlew testClasses --no-daemon --console=plain
  --warning-mode=fail` compila correctamente los fuentes principales y de
  pruebas.
- La ejecución real con Selenium 4.52.0 vuelve a pasar el acceso a JPetStore
  y se detiene únicamente en el `PendingException` del clic **Sign In**.
  El informe XML registra un escenario fallido y `system-err` vacío, sin las
  advertencias de incompatibilidad CDP que aparecían con 4.20.0.
- `dependencyInsight` sobre `testRuntimeClasspath` confirma que
  `selenium-java:4.52.0` incorpora `selenium-devtools-v156:4.52.0`
  transitivamente. No se añadió un módulo DevTools manual.

El comando combinado que carga SDKMAN y después arranca el wrapper encontró
una restricción de escritura del archivo de bloqueo bajo `~/.gradle` en esta
sesión. Las invocaciones directas de `./gradlew` sí pudieron completar la
compilación descrita. Esa limitación no se atribuye al código del proyecto.

Continúan pendientes los demás pasos, las comprobaciones del resultado del registro
y del login, la revisión de las demás dependencias y la combinación de esperas
implícitas y explícitas. Esta copia tiene un repositorio Git y contiene cambios
locales anteriores a esta implementación, que se conservaron.

`IA/local/context_analisis_2026-11-03.md` conserva el análisis histórico del
entorno Windows. Sus rutas son históricas; Java 21 vuelve a ser la versión
del proyecto.
