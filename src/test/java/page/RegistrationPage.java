package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class RegistrationPage extends BasePage {

    private final By userIdField = By.name("username");

    private final By repeatPasswordField =
            By.name("repeatedPassword");

    private final By saveAccountButton =
            By.name("newAccount");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }
    public boolean isRegistrationFormDisplayed() {
        return isDisplayed(repeatPasswordField)
                && isDisplayed(saveAccountButton);
    }

    public void enterUserId(String userId) {
        type(userIdField, userId);
    }

    public String getUserId() {
        return find(userIdField).getDomProperty("value");
    }
}

