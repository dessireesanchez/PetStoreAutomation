@web @signin @alta @regression
Feature: Inicio de sesión de cuentas registradas
  Como usuario registrado de JPetStore
  Quiero iniciar y cerrar sesión mediante los identificadores admitidos
  Para acceder a mi cuenta y finalizar mi acceso

  Background:
    Given el visitante abre la pantalla de Sign In

  @positive
  Scenario Outline: TC-SIGNIN-01 - Login válido mediante <identificador>
    Given dispone de una cuenta nueva verificada por UI
    When inicia sesión mediante "<identificador>" con la contraseña registrada
    Then accede a catálogo como el usuario de esa cuenta

    Examples:
      | identificador |
      | User ID       |
      | Email         |

  @negative
  Scenario Outline: TC-SIGNIN-02 - Contraseña incorrecta mediante <identificador>
    Given dispone de una cuenta nueva verificada por UI
    When inicia sesión mediante "<identificador>" con una contraseña incorrecta
    Then se rechaza el login con un error visible y sin autenticación

    Examples:
      | identificador |
      | User ID       |
      | Email         |

  @negative
  Scenario Outline: TC-SIGNIN-03 - Rechazo de <identificador> sin cuenta preparada
    When intenta iniciar sesión con un "<identificador>" nuevo sin cuenta preparada
    Then se rechaza el login con un error visible y sin autenticación

    Examples:
      | identificador |
      | User ID       |
      | Email         |

  @negative
  Scenario Outline: TC-SIGNIN-04 - Campos vacíos en login: <variante>
    Given dispone de una cuenta nueva verificada por UI
    When envía el login con la variante de campos vacíos "<variante>"
    Then no se autentica y continúa disponible el formulario de login

    Examples:
      | variante                     |
      | identificador vacío          |
      | contraseña vacía con User ID |
      | contraseña vacía con Email   |
      | ambos vacíos                 |

  @positive
  Scenario: TC-SIGNIN-05 - Contraseña oculta en el formulario
    When escribe una contraseña de prueba en Sign In
    Then el campo de contraseña utiliza presentación oculta

  @rememberMe @DEF-AUTH-001
  Scenario: TC-REMEMBER-01 - El formulario ofrece Remember Me
    Then el formulario de login ofrece la opción Remember Me

  @positive @session
  Scenario: TC-SESSION-01 - Cerrar una sesión iniciada
    Given dispone de una cuenta nueva verificada por UI
    When inicia sesión mediante "User ID" con la contraseña registrada
    Then accede a catálogo como el usuario de esa cuenta
    When cierra esa sesión mediante Sign Out
    Then queda en catálogo sin autenticación
