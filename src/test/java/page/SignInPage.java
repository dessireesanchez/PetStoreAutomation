package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import interfaces.ButtonClick;

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
        click(By.name("signon"));
    }

    @Override
    public void darClick() {
        clickRegisterNow();
    }
}
