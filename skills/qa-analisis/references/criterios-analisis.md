# Criterios para analizar y emitir el informe

Estos criterios complementan la plantilla sin sustituir sus siete secciones.
Una política de clasificación aportada y aprobada para el proyecto tiene
precedencia sobre los valores por defecto; identifica su fuente y versión.
Si hay políticas incompatibles sin precedencia confirmada, registra la
contradicción y deja pendiente la clasificación afectada.

## Evidencia y límites de la revisión

Cada hallazgo identifica una sección, regla, cita breve o ausencia concreta
en el ámbito revisado. Las observaciones de diseño o implementación incluyen
su fuente y fecha, si se conocen. Una captura evidencia lo que muestra; no
demuestra por sí sola persistencia, comportamiento interno o reglas del backend.
El código consultado no equivale a una ejecución del producto.

Prioriza las decisiones de negocio aprobadas para determinar lo esperado.
Cuando no se conoce qué versión es vigente, pregunta y documenta el conflicto;
no elijas por conveniencia entre una especificación y la UI. Una discrepancia
documentada no demuestra su causa interna ni se convierte automáticamente en
un defecto confirmado del producto.

La falta de evidencia relevante se registra como `No verificado`. Si un tipo
de evidencia no corresponde al producto, utiliza `No aplica` y explica por
qué. La ausencia de UI en una función sin interfaz visual no bloquea su
análisis. Las versiones y fechas desconocidas se declaran como tales.

## Taxonomía y trazabilidad

Las matrices distinguen el tipo de problema y su efecto:

| Tipo | Criterio | Ejemplo |
| --- | --- | --- |
| Gap | Falta una definición necesaria o existe una diferencia sustentada entre especificación y diseño. | Se exige un campo obligatorio y el diseño aportado no lo incluye. |
| Riesgo | Evento o condición que podría perjudicar el negocio, el producto o las pruebas. | Un servicio externo puede impedir completar el flujo. |
| Ambigüedad o contradicción | Un requisito admite interpretaciones distintas o contradice otra regla. | Dos plazos diferentes para la misma caducidad. |

Un riesgo formula condición o evento, consecuencia y mitigación propuesta.
Justifica probabilidad e impacto o márcalos como estimaciones pendientes; no
los presentes como hechos medidos si no lo son. Las categorías de referencia
son `Técnico`, `Negocio / UX` y `Automatización`.

Asigna IDs por funcionalidad: `GAP-01`, `RIE-01`, `AMB-01` y `P01`. Una pregunta
indica qué IDs aborda y qué validación depende de la respuesta. La recomendación
de un hallazgo puede enlazar esa pregunta sin crear un hallazgo adicional.

Una misma observación no se repite en varias matrices. Si de ella deriva un
riesgo distinto, puede registrarse y enlazarse al hallazgo original; el total
cuenta registros de análisis, no defectos independientes del producto.

## Escalas de la plantilla

Las escalas se aplican de forma cualitativa. La expresión probabilidad por
impacto no define por sí sola una fórmula numérica.

Para gaps, usa `Alta` cuando la carencia bloquea desarrollo o automatización
de un comportamiento en alcance; `Media` cuando afecta reglas secundarias sin
impedir el flujo principal; `Baja` para diferencias menores de presentación o
etiquetado. Justifica el impacto real: un texto puede ser crítico para aceptar
una operación, y no es cosmético solo por ser texto.

Para ambigüedades, usa impacto `Alto`, `Medio` o `Bajo` con la misma atención al
comportamiento que impiden definir. La severidad de un hallazgo no es su
prioridad de ejecución ni el estado del documento.

La plantilla define esta matriz de riesgo incompleta. Normaliza las variantes
gramaticales Alto/Alta, Medio/Media y Bajo/Baja al interpretarla:

| Probabilidad / Impacto | Alto | Medio | Bajo |
| --- | --- | --- | --- |
| Alta | Crítico | Alto | Pendiente de política |
| Media | Alto | Medio | Pendiente de política |
| Baja | Pendiente de política | Pendiente de política | Bajo |

Si una pareja no tiene nivel definido, conserva sus valores, marca el nivel
`Pendiente de política` y formula la pregunta correspondiente. Si no se puede
justificar alguno de los valores, deja también ese valor pendiente. Continúa
el análisis sin completar la matriz con criterios propios. Una matriz completa
propuesta para el futuro se presenta como propuesta que requiere decisión.

## Métricas y versiones

Calcula el resumen desde los hallazgos vigentes de las matrices. Aplica esta
correspondencia para las columnas de severidad de la plantilla:

| Fila | Alta / Crítica | Media | Baja |
| --- | --- | --- | --- |
| Gaps | Alta | Media | Baja |
| Riesgos | Crítico y Alto | Medio | Bajo |
| Ambigüedades | Alto | Medio | Bajo |

No introduzcas una severidad `Crítica` para gaps: la plantilla no la define.
El total de cada fila incluye todos sus hallazgos vigentes, también aquellos
pendientes de clasificación. Si los hay, añade debajo del resumen su conteo
por categoría y explica la conciliación: total = clasificados + pendientes.
Una categoría sin hallazgos tiene cero; una categoría no revisada se declara
no evaluada, en vez de presentar el cero como ausencia de problemas.

En una revisión, toma del informe anterior los IDs de la misma funcionalidad.
Un hallazgo solo se resuelve si una respuesta confirmada, una nueva definición
o una evidencia pertinente atiende su causa. Documenta la referencia anterior,
los cambios y las resoluciones en las secciones existentes del informe. Los
resueltos quedan en una nota de seguimiento, fuera de las matrices y métricas
de hallazgos vigentes. Si persiste parte del problema, conserva el ID y
explica qué queda abierto. La nueva versión no prueba que el producto cambió.

## Dictamen de QA

Evalúa la preparación documental para el alcance solicitado. Usa exactamente
uno de los tres dictámenes de la plantilla y explica sus razones:

| Dictamen | Condición |
| --- | --- |
| Bloqueado por Gaps Críticos | Hay una carencia o contradicción sustentada que impide definir el resultado esperado de un flujo principal en alcance. Indica qué validaciones bloquea y qué decisión lo desbloquea. |
| Requiere Aclaración del Negocio | Quedan decisiones relevantes sobre reglas, alcance, fuentes o clasificación de riesgos que impiden cerrar el análisis; identifica las partes que sí permiten diseñar pruebas. |
| Listo para Desarrollo | Las fuentes revisadas permiten definir resultados verificables en el alcance y no quedan dudas bloqueantes. Declara observaciones no bloqueantes y límites de evidencia. |

Aplica la condición de bloqueo antes de las otras. `Gaps Críticos` es el nombre
del dictamen de la plantilla, no una cuarta severidad de gaps. No bloquees una
funcionalidad por riesgos genéricos ajenos al alcance ni la apruebes solo por
tener pocos hallazgos. La ausencia de evidencia requerida para una validación
concreta limita esa conclusión; no prueba ausencia de defectos.

Conserva el estado documentado de la fuente en los metadatos. Si la plantilla
no distingue el estado de la fuente del estado del informe, acláralos en la
misma sección. Registra el informe como `Propuesto` y reserva las declaraciones
de aprobación para decisiones humanas con evidencia. El dictamen no certifica
el producto ni autoriza automatizar, desplegar o modificarlo.

## Recomendaciones y errores frecuentes

Formula acciones concretas, proporcionales al hallazgo, diferenciando la
propuesta de la decisión vigente. Las recomendaciones de BDD, UI, API, datos
y ambiente se completan solo cuando aplican al alcance y a las capacidades
conocidas; en los demás casos explica `No aplica` o la dependencia pendiente.

Evita sustituir una duda de negocio por una regla inventada, equiparar una URL
a una inspección, confundir un dato sintético con unicidad verificada, copiar
ejemplos de la plantilla como resultados o trasladar políticas de otro proyecto.
