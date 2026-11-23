# Resumen

Contexto actualizado el 13 de noviembre de 2026. PetStoreAutomation automatiza
pruebas web de JPetStore con Selenium y Cucumber. El entorno de trabajo es macOS
con Java 21. La raíz actual es
`/Users/dessireesanchez/Proyectos/PetStoreAutomation`.

La apertura de la URL, los clics **Sign In → Register Now** y la validación del
formulario están implementados. Dos escenarios `@userId` comprueban la
escritura directa y la generación y escritura desde la tabla, incluyendo la
sustitución por valores explícitos. La suite de Cucumber permite ejecutar los
escenarios desde Gradle. El paso del formulario recibe las doce filas,
completa y verifica sus campos, resuelve `{timestamp}` en Email y conserva
los datos por escenario. TC-SIGNUP-01 ya guarda la cuenta, comprueba la
redirección anónima a `/catalog` y autentica con el User ID y la contraseña
registrados. Los tres escenarios originales siguen pasando dentro de la
cobertura ampliada: 42 escenarios web, 16 aprobados y 26 fallidos frente a los
requisitos documentados. Las 30 pruebas unitarias del soporte pasan.

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
cierre de sesión. Esta documentación no añade escenarios nuevos implementados
ni ejecutados mediante Cucumber.

Después se realizó una inspección exploratoria por la UI real de Chrome.
Se creó una cuenta sintética, se confirmó la redirección anónima a catálogo
y se inició sesión por User ID; aparecieron `Sign Out`, `My Account` y
`Welcome Carlos!`. El acceso por email con la misma contraseña fue rechazado
con `Invalid username or password. Signon failed.`. Se volvió a entrar por
User ID y se confirmó en My Account, sin editarlo, que el email estaba
guardado correctamente. La sesión se cerró al finalizar. Se observaron dos
incumplimientos: ausencia de Remember Me (DEF-AUTH-001) y rechazo de login
por email (DEF-AUTH-002). El informe y la evidencia textual quedan en
`IA/local/inspeccion_autenticacion_2026-11-12.md`. Después se completó la
automatización del registro válido y del acceso por User ID en el escenario
existente. Después se añadieron 26 variantes de registro y 13 de Sign In y
sesión, conservando las expectativas para reproducir los incumplimientos.
La ejecución final terminó con 26 fallos funcionales agrupados en nueve
hallazgos. DEF-AUTH-003 documenta que el envío de ambos campos de login vacíos
muestra una sesión autenticada en la UI; se recomienda priorizarlo. Los
resultados, 26 capturas y el snapshot de Cucumber quedan en
`IA/local/resultados_cobertura_alta_2026-11-12.md` y `IA/local/evidencias/`.

La usuaria pidió no guardar contraseñas en el código. Los valores literales
se retiraron de Java y Gherkin: ahora se generan en memoria por escenario
con `SecureRandom`, incluidas las variantes inválidas. También pidió ejecutar
casos concretos durante el desarrollo, el bloque afectado al cambiar soporte
compartido y una general al terminar. Se documentaron los comandos con
`--tests` para seleccionar el runner web o las clases unitarias.

El 13 de noviembre se compararon las notas de la clase 12 con el proyecto,
sin ejecutar pruebas ni modificar el código. La comparación se conserva en
`IA/local/comparacion_clase_12_2026-11-13.md`. Confirma la preparación
independiente por UI, el runner, los reportes y el tratamiento de HTTP 500
como defecto. Después, por solicitud de la usuaria de preparar el commit,
se corrigieron las etiquetas: `@alta` y `@regression` incluyen las 40 variantes
funcionales; `@signup` incluye 29, de las cuales dos son `@technical` sin
guardar cuentas. TC-SIGNUP-01 queda incluido en esos filtros y `@smoke`
selecciona TC-SIGNUP-01 y TC-SIGNUP-12 conforme al plan original. Hay 18
definiciones que producen 42 ejecuciones. La selección y su justificación se
conservan en `IA/local/seleccion_casos_2026-11-13.md`. La fórmula del semáforo
no aparece en las notas; se dejó para la clase siguiente y no se inventaron
ponderaciones. Los criterios de salida del plan original no están satisfechos
por los fallos documentados.

La verificación posterior de etiquetas descubrió 42 escenarios en modo
simulación de Cucumber y comprobó las cantidades indicadas mediante los
metadatos del reporte. Las 30 pruebas unitarias del soporte pasaron en esa
ejecución. No se abrió Chrome ni se repitió la general funcional; su snapshot
real permanece en `IA/local/evidencias/cobertura_alta_final/`. Los reportes
actuales de `build/` corresponden a la simulación, no a una nueva aprobación
del comportamiento web.

# Arquitectura y módulos

El proyecto Gradle tiene un único módulo. Los tests se organizan por
responsabilidad:

- `driver/DriverManager.java`: crea un ChromeDriver por hilo y lo cierra.
- `hook/Hook.java`: prepara y cierra el driver, limpia secretos por escenario
  y adjunta evidencia redactada al fallar; omite capturas sensibles.
- `page/BasePage.java`: navegación, interacción, lectura de mensajes y espera
  de sustitución del formulario y `document.readyState` tras el envío.
- `page/HomePage.java`: navegación, espera y lectura de la ruta actual,
  enlaces visibles de `MenuContent` y texto de la página.
- `page/SignInPage.java`: clic en **Register Now**, escritura de identificador
  y contraseña, y envío con el botón `signon` (**Login**).
- `page/RegistrationPage.java`: mapa de campos a localizadores confirmados,
  escritura y lectura de valores, lectura de preferencias y guardado.
- `page/AccountPage.java`: lectura del User ID desde My Account sin editarlo.
- `page/RegistrationErrorReason.java`: comprobación semántica del motivo de
  los mensajes de rechazo, con sus pruebas unitarias.
- `data/RegistrationData.java`: fixture inmutable de registro por escenario.
- `data/PasswordData.java`: generación de datos de contraseña válidos y de
  una sola regla inválida, con pruebas unitarias.
- `data/ScenarioSecrets.java`: registro temporal y redacción de secretos.
- `data/RegistrationValueResolver.java`: resolución de tokens de la tabla.
- `flow/RegistrationFlow.java`: preparación de cuentas por UI, verificación
  de credenciales e identidad exacta, y cierre de sesión de preparación.
- `interfaces/ButtonClick.java`: contrato `darClick()` implementado por
  `HomePage` y `SignInPage`.
- `steps/CreateUserStep.java`: definiciones de los pasos de registro y login.
- `steps/SignInStep.java`: variantes positivas, negativas y de sesión.
- `steps/RegistrationValidationStep.java`: duplicados, contraseñas,
  obligatoriedad, conservación ante error y registro anónimo.
- `runner/RunCucumberTest.java`: suite JUnit Platform para el motor Cucumber,
  los recursos `Feature` y el glue `steps,hook`.
- `src/test/resources/Feature/CreateUser/`: recorrido y validaciones de registro.
- `src/test/resources/Feature/SignIn/`: escenarios de Sign In y sesión.

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
texto. Resuelve los valores, los escribe y comprueba su contenido. El mapa
de campos rechaza nombres desconocidos mediante `IllegalArgumentException`.
Una tabla que solo contiene User ID sigue terminando correctamente. La
validación de valores con texto pertenece al contrato del paso de llenado
actual; no demuestra ni implementa reglas de obligatoriedad de JPetStore.

En la inspección del HTML público se confirmaron los nombres de inputs
`username`, `password`, `repeatedPassword`, `firstName`, `lastName`, `email`,
`phone`, `address1`, `address2`, `city`, `state`, `zip` y `country` en registro.
Las preferencias usan `languagePreference`, `favouriteCategoryId`, `listOption`
y `bannerOption`, y el botón de registro `newAccount`. En Sign In se usan
`username`, `password` y el botón `signon`, cuyo texto visible es `Login`.
Las preferencias observadas por defecto fueron `english`, `FISH` y ambas
casillas desmarcadas. Esos localizadores ya están incorporados a los Page
Objects. La sesión anónima se comprueba por `Sign In` visible sin `Sign Out`
ni `My Account`; tras login se exigen los dos últimos, ausencia de `Sign In`
y `Welcome <nombre registrado>!`. Además se lee el User ID exacto desde
My Account y se vuelve a catálogo. Las rutas se comparan por su path.
Se retiró la espera implícita global: las interacciones usan esperas explícitas.
Los rechazos exigen mensajes funcionales visibles, no una página HTTP 500;
duplicados, confirmación y complejidad comprueban también el motivo semántico.

# Módulos de datos

Los datos se describen mediante tablas o reglas en Gherkin y fixtures
inmutables de `RegistrationData`. La tabla del recorrido original se
convierte en `List<Map<String, String>>` con `asMaps(String.class, String.class)`.

Para User ID, la indicación `U + 9 caracteres alfanuméricos generados` se
resuelve con `"U" + RandomStringUtils.randomAlphanumeric(9)`, reutilizando
Commons Lang. Los valores explícitos se conservan. `datosRegistro` guarda los
valores resueltos en la instancia de pasos del escenario, incluidos User ID,
contraseña y nombre reutilizados para comprobar el login. La generación
aleatoria no garantiza que el nombre esté disponible en el servidor.

Email sustituye `{timestamp}` por el valor de `System.currentTimeMillis()`
capturado al crear la instancia de pasos. Un paso independiente comprueba
el patrón `qa_user_[0-9]+@testpetstore[.]com`. En las fixtures nuevas, el email
incorpora timestamp y User ID para reducir colisiones. El descriptor
`contraseña generada del escenario` resuelve una misma contraseña de doce
caracteres para New password y Repeat password. Las fixtures y las variantes
de frontera o complejidad también generan sus contraseñas en memoria.
Las aserciones de escritura comparan booleanos con el nombre
del campo para evitar exponer contraseñas en los mensajes de comparación.
Cada escenario `@smoke` crea su cuenta sintética; la suite completa prepara
otras cuentas por escenario. Los escenarios `@userId` no guardan cuentas.
No hay eliminación de cuentas por API
ni base de datos. Los fixtures de cuenta se crean y verifican dentro de cada
escenario; no se depende de que otro escenario haya preparado la cuenta.

`ScenarioSecrets` usa un ThreadLocal limpiado antes y después del escenario.
Redacta los valores generados en los mensajes y el texto visible. Las
excepciones al enviar contraseñas no incluyen el valor enviado. Las capturas
se omiten si se detecta una credencial expuesta. Las pruebas de datos evitan
imprimir el contenido de las contraseñas en sus aserciones.

# Estándares y convenciones del código

Los paquetes se separan en `driver`, `hook`, `page`, `steps`, `runner`,
`data` y `flow`.
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

Los filtros de Cucumber no filtran las pruebas unitarias de Jupiter. Para
ejecutar solo una variante web se usa, por ejemplo, `./gradlew test --tests
runner.RunCucumberTest '-PcucumberName=TC-SIGNIN-02.*User ID' --rerun-tasks`.
Para datos o interpretación de errores se seleccionan `data.PasswordDataTest`
o `page.RegistrationErrorReasonTest` con `--tests`. Gradle puede listar como
`SKIPPED` los escenarios filtrados sin ejecutarlos ni abrir Chrome para ellos.

La suite tiene el nombre visible `Pruebas de JPetStore` y usa los plugins
`pretty,summary,html,json,junit`. Los reportes Cucumber se escriben en
`build/reports/cucumber/cucumber.html`, `cucumber.json` y `cucumber.xml`.
Gradle registra ese directorio como salida de la tarea `test` y también genera
`build/reports/tests/test/index.html`. Los reportes reflejan la última
ejecución, incluidos sus filtros y el modo simulación.

# Riesgos, supuestos y limitaciones

Ejecución final de cobertura alta, 12 de noviembre de 2026:

- Comando: `./gradlew test --rerun-tasks --no-daemon --console=plain
  --warning-mode=fail`. Resultado: `BUILD FAILED`, 72 tests, 26 fallidos.
- Cucumber: 42 escenarios, 16 aprobados, 26 fallidos; sin omisiones ni hooks
  fallidos. Jupiter: 30 pruebas unitarias, todas aprobadas.
- Los fallos se agrupan en nueve requisitos incumplidos: Remember Me ausente,
  login por email rechazado, sesión al enviar ambos campos vacíos, duplicado
  de User ID idéntico con HTTP 500, cambio de mayúsculas sin rechazo, email
  duplicado sin rechazo, confirmación distinta, complejidad y obligatoriedad.
- TC-SIGNUP-11 no llegó a comprobar conservación/vaciado porque no se provoca
  el error por email duplicado. Sus comprobaciones quedan bloqueadas por ese
  incumplimiento. Los negativos por email aprobados no acreditan el soporte
  del login por email mientras su caso positivo falla.
- En una general intermedia se detectó una carrera al leer errores antes de
  terminar el envío. Se corrigió la sincronización del formulario y se
  verificó solo TC-SIGNIN-02 mediante User ID; pasó y también pasó en la
  general final. Ese fallo de automatización no se reportó como defecto web.
- La revisión independiente encontró dos falsos positivos potenciales:
  saludo compartido por todas las cuentas y aceptación de cualquier mensaje.
  Se corrigieron con identidad exacta y motivos semánticos; la revisión de
  los ajustes no encontró problemas materiales adicionales.
- Se inspeccionaron las capturas del HTTP 500 por duplicado y de la sesión
  mostrada con campos vacíos. No se infieren datos de DB ni causas internas.

Verificación del registro y login por User ID el 12 de noviembre de 2026:

- RED: `./gradlew test -PcucumberTags=@smoke --rerun-tasks --no-daemon
  --console=plain` falló por `PendingException`, enumerando los once campos
  sin implementar antes de escribir el código de llenado.
- GREEN: el mismo comando aprobó TC-SIGNUP-01 tras implementar el recorrido.
- La ejecución real completa con `./gradlew test --rerun-tasks --no-daemon
  --console=plain --warning-mode=fail` aprobó los tres escenarios. El registro
  comprobó el email resuelto, las preferencias, el estado anónimo y el acceso
  con credenciales del escenario. Se generan los reportes Cucumber habituales.
- No se modificaron dependencias ni el driver. Permanece la advertencia del
  launcher de Gradle sobre acceso nativo, ya documentada en el entorno.
- El código se trabaja en la rama `feat/registro-login-user-id`. El plan y la
  evidencia del ciclo RED/GREEN están en `IA/local/`.

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

Quedan bloqueadas las pruebas de persistencia que requieren activar Remember
Me porque el control no existe en la UI. Bloqueo de cuenta, duración de sesión
ordinaria y destinos protegidos siguen sin definición suficiente. No se
inventaron reglas para esos puntos ni se añadieron casos Should/Could.
La actualización local del contexto con la inspección previa se conservó.

`IA/local/context_analisis_2026-11-03.md` conserva el análisis histórico del
entorno Windows. Sus rutas son históricas; Java 21 vuelve a ser la versión
del proyecto.
