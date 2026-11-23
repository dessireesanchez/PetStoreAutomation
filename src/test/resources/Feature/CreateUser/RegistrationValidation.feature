@web @signup @alta @regression @validacionRegistro
Feature: Validaciones del registro de una cuenta
  Como visitante de JPetStore
  Quiero que se apliquen las reglas documentadas de registro
  Para evitar cuentas duplicadas o con datos inválidos

  Background:
    Given el visitante abre un registro con datos válidos nuevos

  @negative
  Scenario Outline: TC-SIGNUP-03 - User ID duplicado <variante>
    Given existe una cuenta de referencia verificada por UI
    When prepara un registro con el User ID "<variante>" de la cuenta de referencia
    And envía el intento de registro
    Then el registro es rechazado con un mensaje visible en el formulario
    And el error de registro explica el motivo "User ID duplicado"

    Examples:
      | variante   |
      | idéntico   |
      | minúsculas |

  @negative
  Scenario: TC-SIGNUP-04 - Email duplicado
    Given existe una cuenta de referencia verificada por UI
    When prepara un registro con el email de la cuenta de referencia
    And envía el intento de registro
    Then el registro es rechazado con un mensaje visible en el formulario
    And el error de registro explica el motivo "Email duplicado"
    And no permite login con las credenciales del intento rechazado

  @negative
  Scenario: TC-SIGNUP-05 - Confirmación de contraseña diferente
    When utiliza una confirmación distinta de la contraseña de registro
    And envía el intento de registro
    Then el registro es rechazado con un mensaje visible en el formulario
    And el error de registro explica el motivo "confirmación distinta"
    And no permite login con las credenciales del intento rechazado

  @negative @boundary
  Scenario Outline: TC-SIGNUP-06 - Rechazo de contraseña: <regla>
    When utiliza una contraseña que incumple "<regla>" y la confirma
    And envía el intento de registro
    Then el registro es rechazado con un mensaje visible en el formulario
    And el error de registro explica el motivo "<regla>"
    And no permite login con las credenciales del intento rechazado

    Examples:
      | regla                 |
      | longitud 7            |
      | longitud 21           |
      | sin mayúscula         |
      | sin minúscula         |
      | sin número            |
      | sin carácter especial |

  @positive @boundary
  Scenario Outline: TC-SIGNUP-06 - Aceptación de contraseña con longitud <longitud>
    When utiliza una contraseña válida de longitud <longitud> y la confirma
    And envía el intento de registro
    Then el registro permite login por User ID con las credenciales nuevas

    Examples:
      | longitud |
      | 8        |
      | 20       |

  @negative
  Scenario Outline: TC-SIGNUP-07 - Campo obligatorio vacío: <campo>
    When deja vacío el campo de registro "<campo>"
    And envía el intento de registro
    Then el registro es rechazado con un mensaje visible en el formulario
    And el error de registro identifica el campo obligatorio "<campo>"
    And no permite login con las credenciales del intento rechazado

    Examples:
      | campo           |
      | User ID         |
      | New password    |
      | Repeat password |
      | First name      |
      | Last name       |
      | Email           |
      | Phone           |
      | Address 1       |
      | City            |
      | State           |
      | Zip             |
      | Country         |

  @negative @ux
  Scenario: TC-SIGNUP-11 - Preservar datos y vaciar contraseñas tras error
    Given existe una cuenta de referencia verificada por UI
    When prepara un registro con el email de la cuenta de referencia
    And envía el intento de registro
    Then el registro es rechazado con un mensaje visible en el formulario
    And el error de registro explica el motivo "Email duplicado"
    And conserva los datos válidos y vacía ambas contraseñas del registro

  @smoke @security @positive @session
  Scenario: TC-SIGNUP-12 - Registro sin autenticación automática
    When envía el intento de registro
    Then el registro termina en catálogo con sesión anónima
    And el registro permite login por User ID con las credenciales nuevas
