package page;

import data.ScenarioSecrets;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.net.URI;
import java.util.List;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(By locator) {
        find(locator).click();
    }

    public void clickAndWaitForPage(By locator) {
        WebElement link = find(locator);
        link.click();
        wait.until(ExpectedConditions.stalenessOf(link));
        wait.until(currentDriver -> "complete".equals(((JavascriptExecutor) currentDriver)
                .executeScript("return document.readyState")));
    }

    public void submitForm(By locator) {
        WebElement button = find(locator);
        WebElement form = button.findElement(By.xpath("./ancestor::form"));
        button.click();
        wait.until(currentDriver -> {
            try {
                button.isEnabled();
                return !form.findElements(By.cssSelector(":invalid")).isEmpty();
            } catch (StaleElementReferenceException navigated) {
                return "complete".equals(((JavascriptExecutor) currentDriver)
                        .executeScript("return document.readyState"));
            }
        });
    }

    public void type(By locator, String text) {
        WebElement element = find(locator);
        boolean secret = ScenarioSecrets.isSecretValue(text)
                || "password".equalsIgnoreCase(element.getDomAttribute("type"));
        element.clear();
        try {
            element.sendKeys(text);
        } catch (WebDriverException exception) {
            if (secret) {
                throw new AssertionError("No se pudo escribir un campo de contraseña: "
                        + exception.getClass().getSimpleName());
            }
            throw exception;
        }
    }

    public String getText(By locator) {
        return find(locator).getText();
    }

    public void waitForPath(String path) {
        wait.until(currentDriver -> path.equals(
                URI.create(currentDriver.getCurrentUrl()).getPath()));
    }

    public String getCurrentPath() {
        return URI.create(driver.getCurrentUrl()).getPath();
    }

    public String getPageText() {
        return ScenarioSecrets.redact(getText(By.tagName("body")));
    }

    public List<String> getValidationMessages() {
        return driver.findElements(By.cssSelector("#Content ul.messages li"))
                .stream().filter(WebElement::isDisplayed)
                .map(WebElement::getText).map(ScenarioSecrets::redact).toList();
    }

    public boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
