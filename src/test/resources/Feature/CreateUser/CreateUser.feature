@web @signup
Feature: Registro de Nueva Cuenta de Usuario (Sign Up)
  Como un usuario nuevo de JPetStore
  Quiero registrar mis credenciales, datos de contacto y preferencias
  Para disponer de una cuenta activa y poder comprar en la tienda

  Background:
    Given ingresa en esta URL "https://petstore.octoperf.com/catalog"
    And da click en "Sign In"
    And da click en "Register Now"
    Then ingresa a la página de registro

  @technical @userId
  Scenario: Completar y reemplazar el User ID sin guardar la cuenta
    When ingresa el User ID "UQA0000001" en el formulario de registro
    Then el campo User ID del registro contiene "UQA0000001"
    When ingresa el User ID "UQA0000002" en el formulario de registro
    Then el campo User ID del registro contiene "UQA0000002"

  @technical @userId @userIdTabla
  Scenario: Generar y completar el User ID desde la tabla
    When completa el formulario con los siguientes datos:
      | Campo   | Valor                                  |
      | User ID | U + 9 caracteres alfanuméricos generados |
    Then el campo User ID del registro contiene el identificador generado
    When completa el formulario con los siguientes datos:
      | Campo   | Valor      |
      | User ID | UQA0000003 |
    Then el campo User ID del registro contiene "UQA0000003"

  # ---------------------------------------------------------------------------
  # FLUJOS POSITIVOS (HAPPY PATH)
  # ---------------------------------------------------------------------------

  @alta @regression @smoke @positive
  Scenario: TC-SIGNUP-01 - Registro exitoso con valores por defecto en preferencias de perfil
    When completa el formulario con los siguientes datos:
      | Campo           | Valor                                      |
      | User ID         | U + 9 caracteres alfanuméricos generados   |
      | New password    | contraseña generada del escenario          |
      | Repeat password | contraseña generada del escenario          |
      | First name      | Carlos                                     |
      | Last name       | Tester                                     |
      | Email           | qa_user_{timestamp}@testpetstore.com       |
      | Phone           | +123456789                                 |
      | Address 1       | Av. Principal 123                          |
      | City            | Bogotá                                     |
      | State           | Cundinamarca                               |
      | Zip             | 110111                                     |
      | Country         | Colombia                                   |
    Then el campo Email del registro contiene un timestamp generado
    And las preferencias de perfil tienen sus valores por defecto
    When da click en "Save Account Information"
    Then el sistema redirige a la página de inicio "/catalog"
    And la página de inicio está en sesión anónima
    When da click en "Sign In"
    And ingresa el User ID registrado
    And ingresa la contraseña registrada
    And envía el formulario de inicio de sesión
    Then la página de inicio muestra al usuario registrado autenticado
