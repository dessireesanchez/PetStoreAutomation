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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateUserStep {

    private static final String GENERAR_USER_ID =
            "U + 9 caracteres alfanuméricos generados";

    private final Map<String, String> datosRegistro = new HashMap<>();
    private final String timestamp = Long.toString(System.currentTimeMillis());

    private final HomePage homePage = new HomePage(DriverManager.getDriver());
    private final SignInPage signInPage =
            new SignInPage(DriverManager.getDriver());
    private final RegistrationPage registrationPage =
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

            case "Save Account Information":
                registrationPage.saveAccountInformation();
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
        assertThat(datosRegistro.get("User ID"))
                .as("El User ID generado debe ser U seguida de nueve caracteres alfanuméricos")
                .matches("U[A-Za-z0-9]{9}");
        assertThat(registrationPage.getUserId())
                .as("El formulario debe contener el User ID generado para este escenario")
                .isEqualTo(datosRegistro.get("User ID"));
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

        for (Map<String, String> fila : filas) {
            String campo = fila.get("Campo");
            String valor = fila.get("Valor");

            assertThat(campo)
                    .as("Cada fila debe indicar el nombre del campo")
                    .isNotBlank();
            assertThat(valor)
                    .as("El campo '%s' debe tener un valor de prueba", campo)
                    .isNotBlank();

            String resuelto = resolverValor(campo, valor);
            registrationPage.enterField(campo, resuelto);
            assertThat(resuelto.equals(registrationPage.getFieldValue(campo)))
                    .as("El campo '%s' debe contener el valor resuelto", campo)
                    .isTrue();
            datosRegistro.put(campo, resuelto);
        }
    }

    private String resolverValor(String campo, String valor) {
        if ("User ID".equals(campo) && GENERAR_USER_ID.equals(valor)) {
            return "U" + RandomStringUtils.randomAlphanumeric(9);
        }
        if ("Email".equals(campo)) {
            return valor.replace("{timestamp}", timestamp);
        }
        return valor;
    }

    @Then("el campo Email del registro contiene un timestamp generado")
    public void elEmailContieneUnTimestampGenerado() {
        assertThat(registrationPage.getFieldValue("Email"))
                .matches("qa_user_[0-9]+@testpetstore[.]com");
    }

    @Then("las preferencias de perfil tienen sus valores por defecto")
    public void lasPreferenciasDePerfilTienenSusValoresPorDefecto() {
        assertThat(registrationPage.getLanguagePreference()).isEqualTo("english");
        assertThat(registrationPage.getFavouriteCategory()).isEqualTo("FISH");
        assertThat(registrationPage.isMyListSelected()).isFalse();
        assertThat(registrationPage.isMyBannerSelected()).isFalse();
    }

    @Then("el sistema redirige a la página de inicio {string}")
    public void elSistemaRedirigeALaPáginaDeInicio(String path) {
        homePage.waitForPath(path);
        assertThat(homePage.getCurrentPath()).isEqualTo(path);
    }

    @Then("la página de inicio está en sesión anónima")
    public void laPaginaDeInicioEstaEnSesionAnonima() {
        assertThat(homePage.getNavigationLinks())
                .contains("Sign In")
                .doesNotContain("Sign Out", "My Account");
    }

    @Then("la página de inicio muestra al usuario registrado autenticado")
    public void laPaginaMuestraAlUsuarioRegistradoAutenticado() {
        homePage.waitForPath("/catalog");
        assertThat(homePage.getNavigationLinks())
                .contains("Sign Out", "My Account")
                .doesNotContain("Sign In");
        assertThat(homePage.getPageText())
                .contains("Welcome " + datoRegistrado("First name") + "!");
    }

    @And("ingresa el User ID registrado")
    public void ingresaElUserIDRegistrado() {
        signInPage.enterUsername(datoRegistrado("User ID"));
    }

    @And("ingresa la contraseña registrada")
    public void ingresaLaContraseñaRegistrada() {
        signInPage.enterPassword(datoRegistrado("New password"));
    }

    @And("envía el formulario de inicio de sesión")
    public void envíaElFormularioDeInicioDeSesión() {
        signInPage.submit();
    }

    private String datoRegistrado(String campo) {
        String valor = datosRegistro.get(campo);
        assertThat(valor).as("Debe existir el dato registrado '%s'", campo).isNotBlank();
        return valor;
    }
}
