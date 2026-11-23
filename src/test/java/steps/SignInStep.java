package steps;

import data.RegistrationData;
import data.PasswordData;
import driver.DriverManager;
import flow.RegistrationFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page.HomePage;
import page.SignInPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SignInStep {

    private final SignInPage signIn = new SignInPage(DriverManager.getDriver());
    private final HomePage home = new HomePage(DriverManager.getDriver());
    private final RegistrationFlow flow = new RegistrationFlow(DriverManager.getDriver());
    private RegistrationData account;

    @Given("el visitante abre la pantalla de Sign In")
    public void openSignIn() {
        flow.openSignIn();
    }

    @Given("dispone de una cuenta nueva verificada por UI")
    public void prepareAccount() {
        account = RegistrationData.valid();
        flow.createVerifiedAccount(account);
    }

    @When("inicia sesión mediante {string} con la contraseña registrada")
    public void loginWithRegisteredPassword(String identifier) {
        flow.login(identifier(identifier), account.value("New password"));
    }

    @When("inicia sesión mediante {string} con una contraseña incorrecta")
    public void loginWithWrongPassword(String identifier) {
        flow.login(identifier(identifier), PasswordData.differentFrom(account.value("New password")));
    }

    @When("intenta iniciar sesión con un {string} nuevo sin cuenta preparada")
    public void loginWithoutPreparedAccount(String identifier) {
        account = RegistrationData.valid();
        flow.login(identifier(identifier), account.value("New password"));
    }

    @When("envía el login con la variante de campos vacíos {string}")
    public void loginWithMissingFields(String variant) {
        switch (variant) {
            case "identificador vacío" -> flow.login("", account.value("New password"));
            case "contraseña vacía con User ID" -> flow.login(account.value("User ID"), "");
            case "contraseña vacía con Email" -> flow.login(account.value("Email"), "");
            case "ambos vacíos" -> flow.login("", "");
            default -> throw new IllegalArgumentException("Variante desconocida: " + variant);
        }
    }

    @Then("se rechaza el login con un error visible y sin autenticación")
    public void assertLoginRejected() {
        assertThat(signIn.getValidationMessages())
                .as("El rechazo debe mostrar un error funcional en la UI")
                .isNotEmpty();
        assertNotAuthenticated();
    }

    @Then("no se autentica y continúa disponible el formulario de login")
    public void assertNotAuthenticated() {
        flow.assertAnonymous();
        assertThat(signIn.isLoginFormDisplayed()).isTrue();
    }

    @Then("accede a catálogo como el usuario de esa cuenta")
    public void assertAccountAuthenticated() {
        flow.assertAuthenticated(account);
    }

    @When("escribe una contraseña de prueba en Sign In")
    public void enterPassword() {
        String password = PasswordData.valid(12);
        signIn.enterPassword(password);
        assertThat(signIn.passwordMatches(password)).isTrue();
    }

    @Then("el campo de contraseña utiliza presentación oculta")
    public void assertPasswordMasked() {
        assertThat(signIn.getPasswordInputType()).isEqualTo("password");
    }

    @Then("el formulario de login ofrece la opción Remember Me")
    public void assertRememberMePresent() {
        assertThat(signIn.hasRememberMeOption())
                .as("DEF-AUTH-001: se requiere una opción Remember Me visible en Sign In")
                .isTrue();
    }

    @When("cierra esa sesión mediante Sign Out")
    public void signOut() {
        home.signOut();
    }

    @Then("queda en catálogo sin autenticación")
    public void assertSignedOut() {
        flow.assertAnonymousCatalog();
    }

    private String identifier(String type) {
        if (!"User ID".equals(type) && !"Email".equals(type)) {
            throw new IllegalArgumentException("Identificador desconocido: " + type);
        }
        return account.value(type);
    }
}
