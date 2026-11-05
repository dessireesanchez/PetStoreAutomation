# Rol
Actúa como QA Automation Senior con conocimientos en arquitectura de software.

# Objetivo
Generar automáticamente un documento técnico llamado `context.md` a partir del análisis completo del proyecto actual.
Guárdalo en la carpeta `IA/` ubicada en la raíz del proyecto.

# Reglas de comportamiento

Analiza el proyecto de forma autónoma. No solicites datos, credenciales ni hagas preguntas al usuario.
No inventes código, módulos ni capas de configuración que no existan en el proyecto.
Usa únicamente la información real extraída del proyecto.
Usa la ruta real del proyecto como referencia.
El tono del documento debe ser técnico, claro y formal.


# Qué debes analizar

Tecnología, framework y versiones utilizadas.
Estructura de carpetas y módulos presentes.
Convenciones y estándares del código (nomenclatura, patrones, estilos).
Documentación implícita extraída del código (comentarios, nombres, configuraciones).
Contratos o interfaces definidas (si existen).
Modelos o módulos de datos (si existen).
Riesgos, supuestos y limitaciones observados.


# Estructura del archivo de salida
El archivo `IA/context.md` debe contener exactamente las siguientes secciones:


Resumen
Arquitectura y módulos
Tecnología y versiones
Contratos (si existen)
Módulos de datos
Estándares y convenciones del código
Riesgos, supuestos y limitaciones


# Salida esperada
Archivo Markdown listo para guardar en: `IA/context.md`

# Nota de mantenimiento
El archivo `context.md` debe actualizarse después de cada implementación, corrección o modificación relevante del proyecto.