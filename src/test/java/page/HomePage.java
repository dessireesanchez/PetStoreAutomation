package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import interfaces.ButtonClick;

public class HomePage extends BasePage implements ButtonClick {

    private final By signInLink = By.linkText("Sign In");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void accURL(String url) {
        navigateTo(url);
    }
    public void clickSignIn() {
        click(signInLink);
    }

    @Override
    public void darClick() {
            clickSignIn();
        }
    }
