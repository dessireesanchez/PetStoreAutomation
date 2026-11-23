package flow;

import data.RegistrationData;
import org.openqa.selenium.WebDriver;
import page.HomePage;
import page.AccountPage;
import page.RegistrationPage;
import page.SignInPage;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationFlow {

    private static final String BASE_URL = "https://petstore.octoperf.com";

    private final HomePage home;
    private final SignInPage signIn;
    private final RegistrationPage registration;
    private final AccountPage account;

    public RegistrationFlow(WebDriver driver) {
        home = new HomePage(driver);
        signIn = new SignInPage(driver);
        registration = new RegistrationPage(driver);
        account = new AccountPage(driver);
    }

    public void openSignIn() {
        signIn.navigateTo(BASE_URL + "/account");
        assertThat(signIn.isLoginFormDisplayed()).isTrue();
    }

    public void openAndFill(RegistrationData data) {
        registration.navigateTo(BASE_URL + "/account/new");
        assertThat(registration.isRegistrationFormDisplayed()).isTrue();
        data.values().forEach((field, value) -> {
            registration.enterField(field, value);
            assertThat(value.equals(registration.getFieldValue(field)))
                    .as("Preparación del campo '%s' por UI", field)
                    .isTrue();
        });
    }

    public void createVerifiedAccount(RegistrationData data) {
        openAndFill(data);
        registration.saveAccountInformation();
        assertAnonymousCatalog();
        login(data.value("User ID"), data.value("New password"));
        assertAuthenticated(data);
        home.signOut();
        assertAnonymousCatalog();
    }

    public void login(String identifier, String password) {
        openSignIn();
        signIn.enterUsername(identifier);
        signIn.enterPassword(password);
        assertThat(signIn.getUsername()).isEqualTo(identifier);
        assertThat(signIn.passwordMatches(password))
                .as("El formulario debe contener la contraseña del intento")
                .isTrue();
        signIn.submit();
    }

    public void assertAuthenticated(RegistrationData data) {
        assertThat(home.getValidationMessages())
                .as("Las credenciales registradas deben autenticar sin errores de validación")
                .isEmpty();
        home.waitForPath("/catalog");
        assertThat(home.getNavigationLinks())
                .contains("Sign Out", "My Account")
                .doesNotContain("Sign In");
        assertThat(home.getPageText())
                .contains("Welcome " + data.value("First name") + "!");
        home.openMyAccount();
        assertThat(account.getUserId()).as("Identidad de la cuenta autenticada")
                .isEqualTo(data.value("User ID"));
        home.navigateTo(BASE_URL + "/catalog");
    }

    public void assertAnonymousCatalog() {
        home.waitForPath("/catalog");
        assertAnonymous();
    }

    public void assertAnonymous() {
        assertThat(home.getNavigationLinks())
                .contains("Sign In")
                .doesNotContain("Sign Out", "My Account");
    }
}
