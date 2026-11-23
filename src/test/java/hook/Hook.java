package hook;

import driver.DriverManager;
import data.ScenarioSecrets;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

public class Hook {

    @Before
    public void setup() {
        ScenarioSecrets.clear();
        DriverManager.getDriver();
    }


    @After
    public void tearDown(Scenario scenario) {
        try {
            var driver = DriverManager.driver.get();
            if (scenario.isFailed() && driver != null) {
                String visible = driver.findElement(By.tagName("body")).getText();
                boolean exposed = ScenarioSecrets.containsSecret(visible)
                        || driver.findElements(By.cssSelector("input:not([type='password'])"))
                        .stream().anyMatch(input -> ScenarioSecrets.isSecretValue(input.getDomProperty("value")));
                if (!exposed && driver instanceof TakesScreenshot screenshot) {
                    scenario.attach(screenshot.getScreenshotAs(OutputType.BYTES),
                            "image/png", "Pantalla del fallo");
                } else if (exposed) {
                    scenario.log("Captura omitida para no exponer credenciales de prueba");
                }
                scenario.attach(ScenarioSecrets.redact(driver.getCurrentUrl()), "text/plain", "URL del fallo");
                visible = ScenarioSecrets.redact(visible);
                scenario.attach(visible.substring(0, Math.min(visible.length(), 3000)),
                        "text/plain", "Texto visible del fallo");
            }
        } catch (WebDriverException exception) {
            scenario.log("No se pudo capturar evidencia: " + exception.getClass().getSimpleName());
        } finally {
            try {
                DriverManager.quitDriver();
            } finally {
                ScenarioSecrets.clear();
            }
        }
    }

}
