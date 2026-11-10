package steps;

import driver.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;
import page.HomePage;
import page.SignInPage;
import page.RegistrationPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateUserStep {

    private static final String GENERAR_USER_ID =
            "U + 9 caracteres alfanuméricos generados";

    private String userIdRegistro;

    HomePage homePage = new HomePage(DriverManager.getDriver());
    SignInPage signInPage =
            new SignInPage(DriverManager.getDriver());
    RegistrationPage registrationPage =
            new RegistrationPage(DriverManager.getDriver());

    @Given("ingresa en esta URL {string}")
    public void accederURL(String url) {
        homePage.accURL(url);

    }

    @And("da click en {string}")
    public void daClickEn(String boton) {
        switch (boton) {
            case "Sign In":
                homePage.darClick();
                break;

            case "Register Now":
                signInPage.darClick();
                break;

            default:
                throw new PendingException(
                        "Clic todavía no implementado: " + boton
                );
        }
    }

    @Then("ingresa a la página de registro")
    public void ingresaALaPáginaDeRegistro() {
        assertThat(registrationPage.isRegistrationFormDisplayed())
                .as("El formulario de registro debe estar visible")
                .isTrue();
    }

    @When("ingresa el User ID {string} en el formulario de registro")
    public void ingresaElUserIdEnElFormularioDeRegistro(String userId) {
        registrationPage.enterUserId(userId);
    }

    @Then("el campo User ID del registro contiene {string}")
    public void elCampoUserIdDelRegistroContiene(String userIdEsperado) {
        assertThat(registrationPage.getUserId())
                .as("El campo User ID debe contener el valor ingresado")
                .isEqualTo(userIdEsperado);
    }

    @Then("el campo User ID del registro contiene el identificador generado")
    public void elCampoUserIdContieneElIdentificadorGenerado() {
        assertThat(userIdRegistro)
                .as("El User ID generado debe ser U seguida de nueve caracteres alfanuméricos")
                .matches("U[A-Za-z0-9]{9}");
        assertThat(registrationPage.getUserId())
                .as("El formulario debe contener el User ID generado para este escenario")
                .isEqualTo(userIdRegistro);
    }

    @When("completa el formulario con los siguientes datos:")
    public void completaElFormularioConLosSiguientesDatos(DataTable tabla) {
        assertThat(tabla.cells())
                .as("La tabla debe incluir encabezados y al menos una fila de datos")
                .hasSizeGreaterThan(1);
        assertThat(tabla.row(0))
                .as("Los encabezados de la tabla deben ser Campo y Valor")
                .containsExactly("Campo", "Valor");

        List<Map<String, String>> filas =
                tabla.asMaps(String.class, String.class);
        List<String> camposPendientes = new ArrayList<>();

        for (Map<String, String> fila : filas) {
            String campo = fila.get("Campo");
            String valor = fila.get("Valor");

            assertThat(campo)
                    .as("Cada fila debe indicar el nombre del campo")
                    .isNotBlank();
            assertThat(valor)
                    .as("El campo '%s' debe tener un valor de prueba", campo)
                    .isNotBlank();

            if ("User ID".equals(campo)) {
                completarUserIdRegistro(valor);
            } else {
                camposPendientes.add(campo);
            }
        }

        if (!camposPendientes.isEmpty()) {
            throw new PendingException(
                    "Tabla leída: " + filas.size() + " campos. Pendiente completar: "
                            + String.join(", ", camposPendientes)
            );
        }
    }

    private void completarUserIdRegistro(String valor) {
        userIdRegistro = GENERAR_USER_ID.equals(valor)
                ? "U" + RandomStringUtils.randomAlphanumeric(9)
                : valor;
        registrationPage.enterUserId(userIdRegistro);
        assertThat(registrationPage.getUserId())
                .as("El formulario debe contener el User ID resuelto desde la tabla")
                .isEqualTo(userIdRegistro);
    }

    @And("deja las preferencias de perfil con sus valores por defecto")
    public void dejaLasPreferenciasDePerfilConSusValoresPorDefecto() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("el sistema redirige a la página de inicio {string}")
    public void elSistemaRedirigeALaPáginaDeInicio(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("la página de inicio muestra el texto {string}")
    public void laPáginaDeInicioMuestraElTexto(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("ingresa el User ID registrado")
    public void ingresaElUserIDRegistrado() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("ingresa la contraseña registrada")
    public void ingresaLaContraseñaRegistrada() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("envía el formulario de inicio de sesión")
    public void envíaElFormularioDeInicioDeSesión() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
