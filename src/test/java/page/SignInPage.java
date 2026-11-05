package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class SignInPage extends BasePage {
    private final By registerNowLink =
            By.partialLinkText("Register Now");

    public SignInPage(WebDriver driver) {
        super(driver);
}
    public void clickRegisterNow() {
        click(registerNowLink);
    }
}
