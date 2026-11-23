package steps;

import data.RegistrationData;
import data.PasswordData;
import driver.DriverManager;
import flow.RegistrationFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page.RegistrationPage;
import page.SignInPage;
import page.RegistrationErrorReason;

import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationValidationStep {

    private final RegistrationPage registration = new RegistrationPage(DriverManager.getDriver());
    private final SignInPage signIn = new SignInPage(DriverManager.getDriver());
    private final RegistrationFlow flow = new RegistrationFlow(DriverManager.getDriver());
    private RegistrationData candidate;
    private RegistrationData reference;

    @Given("el visitante abre un registro con datos válidos nuevos")
    public void openValidRegistration() {
        candidate = RegistrationData.valid();
        flow.openAndFill(candidate);
    }

    @Given("existe una cuenta de referencia verificada por UI")
    public void createReferenceAccount() {
        reference = RegistrationData.valid();
        reference = reference.with("User ID", reference.value("User ID").toUpperCase(Locale.ROOT));
        flow.createVerifiedAccount(reference);
        flow.openAndFill(candidate);
    }

    @When("prepara un registro con el User ID {string} de la cuenta de referencia")
    public void useExistingUserId(String variant) {
        String userId = switch (variant) {
            case "idéntico" -> reference.value("User ID");
            case "minúsculas" -> reference.value("User ID").toLowerCase(Locale.ROOT);
            default -> throw new IllegalArgumentException("Variante desconocida: " + variant);
        };
        changeField("User ID", userId);
    }

    @When("prepara un registro con el email de la cuenta de referencia")
    public void useExistingEmail() {
        changeField("Email", reference.value("Email"));
    }

    @When("cambia el campo de registro {string} por {string}")
    public void changeField(String field, String value) {
        candidate = candidate.with(field, value);
        registration.enterField(field, value);
        assertThat(value.equals(registration.getFieldValue(field)))
                .as("El intento debe contener el valor del campo '%s'", field).isTrue();
    }

    @When("utiliza una confirmación distinta de la contraseña de registro")
    public void mismatchConfirmation() {
        changeField("Repeat password", PasswordData.differentFrom(candidate.value("New password")));
    }

    @When("utiliza una contraseña que incumple {string} y la confirma")
    public void invalidPassword(String rule) {
        changePassword(PasswordData.invalid(rule));
    }

    @When("utiliza una contraseña válida de longitud {int} y la confirma")
    public void validPassword(int length) {
        changePassword(PasswordData.valid(length));
    }

    public void changePassword(String password) {
        changeField("New password", password);
        changeField("Repeat password", password);
    }

    @When("deja vacío el campo de registro {string}")
    public void clearField(String field) {
        changeField(field, "");
    }

    @When("envía el intento de registro")
    public void submitRegistration() {
        registration.saveAccountInformation();
    }

    @Then("el registro es rechazado con un mensaje visible en el formulario")
    public void assertRegistrationRejected() {
        assertThat(registration.getCurrentPath())
                .as("El registro debe rechazarse en el formulario; User ID=%s, email=%s",
                        candidate.value("User ID"), candidate.value("Email"))
                .isEqualTo("/account/new");
        assertThat(registration.getValidationMessages())
                .as("Se requiere un error funcional visible; HTTP 500 no es un rechazo válido")
                .isNotEmpty();
        assertThat(registration.isRegistrationFormDisplayed()).isTrue();
        flow.assertAnonymous();
    }

    @Then("el error de registro explica el motivo {string}")
    public void assertRejectionReason(String reason) {
        String message = String.join(" ", registration.getValidationMessages());
        assertThat(RegistrationErrorReason.matches(message, reason))
                .as("El error debe explicar '%s' y no otro motivo", reason).isTrue();
    }

    @Then("el error de registro identifica el campo obligatorio {string}")
    public void assertRequiredFieldIdentified(String field) {
        String text = String.join(" ", registration.getValidationMessages()).toLowerCase(Locale.ROOT);
        String compact = text.replaceAll("[^a-z]", "");
        String htmlName = registration.getFieldInputName(field).toLowerCase(Locale.ROOT);
        boolean labelMentioned = Arrays.stream(field.toLowerCase(Locale.ROOT).split(" "))
                .allMatch(text::contains);
        boolean confirmationMentioned = "Repeat password".equals(field)
                && text.contains("password") && (text.contains("repeat") || text.contains("confirm"));
        assertThat(labelMentioned || compact.contains(htmlName) || confirmationMentioned)
                .as("El error debe identificar el campo obligatorio '%s'", field).isTrue();
    }

    @Then("no permite login con las credenciales del intento rechazado")
    public void assertRejectedAccountCannotLogin() {
        flow.login(candidate.value("User ID"), candidate.value("New password"));
        assertThat(signIn.getValidationMessages())
                .as("Las credenciales del intento rechazado no deben habilitar acceso")
                .isNotEmpty();
        flow.assertAnonymous();
    }

    @Then("conserva los datos válidos y vacía ambas contraseñas del registro")
    public void assertPreservedFields() {
        for (String field : new String[]{"User ID", "First name", "Last name", "Address 1", "Phone"}) {
            assertThat(registration.getFieldValue(field)).as("Conservación de '%s'", field)
                    .isEqualTo(candidate.value(field));
        }
        assertThat(registration.getFieldValue("New password").isEmpty()).isTrue();
        assertThat(registration.getFieldValue("Repeat password").isEmpty()).isTrue();
    }

    @Then("el registro permite login por User ID con las credenciales nuevas")
    public void assertRegistrationEnablesLogin() {
        flow.assertAnonymousCatalog();
        flow.login(candidate.value("User ID"), candidate.value("New password"));
        flow.assertAuthenticated(candidate);
    }

    @Then("el registro termina en catálogo con sesión anónima")
    public void assertAnonymousAfterRegistration() {
        flow.assertAnonymousCatalog();
    }
}
