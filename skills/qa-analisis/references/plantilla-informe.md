# Plantilla Estándar: Informe de Análisis de Gaps, Riesgos y Ambigüedades

> **Guía de uso:** Esta plantilla define la estructura estándar para los informes de análisis estático de requerimientos funcionales. Es independiente de proyecto, dominio, lenguaje y herramienta. Completa los campos entre corchetes `[ ... ]` al analizar cualquier funcionalidad. Los ejemplos entre paréntesis son orientativos y deben reemplazarse por datos reales del análisis.

---

# Informe de Calidad de Requerimientos: \[NOMBRE DE LA FUNCIONALIDAD\]

## 1\. Metadatos del Análisis

| Atributo | Detalle |
| :---- | :---- |
| **Proyecto** | \[Nombre del proyecto\] |
| **Funcionalidad** | \[Nombre de la funcionalidad analizada\] |
| **Documento Analizado** | \[Nombre y ruta del archivo del documento funcional\] |
| **Versión del Documento** | \[Versión, ej. v1.0\] |
| **Fecha de Análisis** | \[AAAA-MM-DD\] |
| **Analista QA** | \[Nombre o rol del analista\] |
| **Fuente de Verdad de Diseño/UI** | \[URL de la UI, ruta de wireframes, capturas de pantalla o diseño de referencia\] |
| **Estado del Documento** | \[Propuesto / En Revisión / Aprobado con Observaciones\] |

---

## 2\. Resumen Ejecutivo

### 2.1. Métricas de Calidad de la Especificación

| Categoría | Alta / Crítica | Media | Baja | Total |
| :---- | :---: | :---: | :---: | :---: |
| **Gaps Funcionales y de Diseño** | \[0\] | \[0\] | \[0\] | **\[0\]** |
| **Riesgos Identificados** | \[0\] | \[0\] | \[0\] | **\[0\]** |
| **Ambigüedades y Contradicciones** | \[0\] | \[0\] | \[0\] | **\[0\]** |
| **Total Hallazgos** | **\[0\]** | **\[0\]** | **\[0\]** | **\[0\]** |

### 2.2. Veredicto de QA

> **Dictamen:** \[Listo para Desarrollo / Requiere Aclaración del Negocio / Bloqueado por Gaps Críticos\]

> **Síntesis:** \[Párrafo breve que resuma el estado general del documento, los principales puntos de atención y el nivel de preparación para iniciar la automatización de pruebas.\]

---

## 3\. Matriz de Gaps (Brechas Funcionales y de Diseño)

> **Criterios de Severidad:**

> * **Alta:** Bloquea el desarrollo o la automatización; discrepancia directa en campos obligatorios, mecanismo de envío o flujos principales.  
> * **Media:** Discrepancia en reglas de formato secundarias, valores por defecto, textos de ayuda o controles visuales que no impiden el flujo principal.  
> * **Baja:** Diferencias cosméticas menores, inconsistencias de etiquetado o mejoras sugeridas de usabilidad.

| ID | Sección / Elemento | Especificación Funcional | Diseño / UI Real Observado | Severidad | Recomendación de QA |
| :---: | :---- | :---- | :---- | :---: | :---- |
| `GAP-01` | \[Sección del documento y elemento, ej. §X.X / Campo\] | \[Lo que describe el documento\] | \[Lo que realmente muestra la UI o el diseño\] | `Alta` | \[Acción correctiva propuesta\] |
| `GAP-02` | \[Sección y elemento\] | \[Lo que describe el documento\] | \[Lo que realmente muestra la UI o el diseño\] | `Media` | \[Acción correctiva propuesta\] |
| `GAP-03` | \[Sección y elemento\] | \[Lo que describe el documento\] | \[Lo que realmente muestra la UI o el diseño\] | `Baja` | \[Acción correctiva propuesta\] |

---

## 4\. Matriz de Riesgos

> **Nivel de Riesgo (Probabilidad x Impacto):**

> * **Crítico:** Probabilidad Alta / Impacto Alto (riesgo inminente de fallo en producción o bloqueo total de pruebas).  
> * **Alto:** Probabilidad Alta / Impacto Medio, o Probabilidad Media / Impacto Alto.  
> * **Medio:** Probabilidad Media / Impacto Medio.  
> * **Bajo:** Probabilidad Baja / Impacto Bajo.

| ID | Categoría | Descripción del Riesgo | Probabilidad | Impacto | Nivel de Riesgo | Estrategia de Mitigación / Contingencia |
| :---: | :---: | :---- | :---: | :---: | :---: | :---- |
| `RIE-01` | `Técnico` | \[Descripción técnica del riesgo, ej. fallo de integración o de persistencia\] | Media | Alta | `Alto` | \[Medida preventiva o de mitigación técnica\] |
| `RIE-02` | `Negocio / UX` | \[Descripción del riesgo de negocio, ej. abandono del usuario o pérdida de datos\] | Alta | Media | `Alto` | \[Medida preventiva o ajuste de UX\] |
| `RIE-03` | `Automatización` | \[Riesgo para la automatización, ej. selectores inestables o respuestas no previstas\] | Alta | Alta | `Crítico` | \[Estrategia para el framework de pruebas\] |

---

## 5\. Matriz de Ambigüedades y Contradicciones

| ID | Sección | Requisito o Texto Analizado | Conflicto / Ambigüedad Detectada | Impacto Funcional | Propuesta de Redacción / Clarificación |
| :---: | :---: | :---- | :---- | :---: | :---- |
| `AMB-01` | \[Ej. §X vs §Y\] | \[Cita textual del documento\] | \[Explicación de la contradicción o vaguedad\] | `Alto` | \[Texto sugerido sin ambigüedad\] |
| `AMB-02` | \[Ej. §X.X\] | \[Cita textual del documento\] | \[Explicación de la contradicción o vaguedad\] | `Medio` | \[Texto sugerido sin ambigüedad\] |

---

## 6\. Preguntas Abiertas para Product Owner / Negocio

Preguntas prioritarias para desbloquear el diseño definitivo de pruebas y escenarios BDD:

1. **\[P01\] \[Tema de la pregunta\]:** \[Descripción de la duda, con opciones concretas de decisión\].  
   * *Impacto en pruebas:* \[Escenarios o validaciones bloqueados hasta resolver la duda\].  
2. **\[P02\] \[Tema de la pregunta\]:** \[Descripción de la duda, con opciones concretas de decisión\].  
   * *Impacto en pruebas:* \[Escenarios o validaciones bloqueados hasta resolver la duda\].

---

## 7\. Recomendaciones para el Framework de Automatización

Lineamientos independientes de lenguaje y herramienta:

* **Estrategia BDD / de escenarios:** \[Definición de features y escenarios basados en criterios de aceptación estables y verificables\].  
* **Automatización de interfaz (UI):** \[Mapeo de componentes o páginas y criterios de localizadores estables, ej. atributos de identificación únicos\].  
* **Automatización de API / HTTP:** \[Manejo de autenticación, cabeceras, formato de respuestas y validación de códigos de estado y contratos\].  
* **Gestión de datos de prueba:** \[Aislamiento de datos entre pruebas, limpieza de estado y generación de datos sintéticos\].  
* **Dependencias de ambiente:** \[Disponibilidad de ambientes, servicios de terceros y datos de referencia necesarios\].