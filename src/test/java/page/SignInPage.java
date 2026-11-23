package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import interfaces.ButtonClick;

import java.util.Locale;

public class SignInPage extends BasePage implements ButtonClick {
    private final By registerNowLink =
            By.partialLinkText("Register Now");

    public SignInPage(WebDriver driver) {
        super(driver);
    }

    public void clickRegisterNow() {
        click(registerNowLink);
    }

    public void enterUsername(String username) {
        type(By.name("username"), username);
    }

    public void enterPassword(String password) {
        type(By.name("password"), password);
    }

    public void submit() {
        submitForm(By.name("signon"));
    }

    public boolean isLoginFormDisplayed() {
        return driver.findElements(By.name("signon")).stream().anyMatch(button -> button.isDisplayed());
    }

    public String getUsername() {
        return find(By.name("username")).getDomProperty("value");
    }

    public boolean passwordMatches(String expected) {
        return expected.equals(find(By.name("password")).getDomProperty("value"));
    }

    public String getPasswordInputType() {
        return find(By.name("password")).getDomAttribute("type");
    }

    public boolean hasRememberMeOption() {
        var form = find(By.cssSelector("form[action='/account/signon']"));
        return form.getText().toLowerCase(Locale.ROOT).contains("remember me")
                && form.findElements(By.cssSelector("input[type='checkbox']"))
                .stream().anyMatch(checkbox -> checkbox.isDisplayed());
    }

    @Override
    public void darClick() {
        clickRegisterNow();
    }
}
