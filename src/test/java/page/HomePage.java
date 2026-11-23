package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import interfaces.ButtonClick;

import java.util.List;

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

    public void signOut() {
        clickAndWaitForPage(By.linkText("Sign Out"));
    }

    public void openMyAccount() {
        click(By.linkText("My Account"));
    }

    public List<String> getNavigationLinks() {
        return find(By.id("MenuContent")).findElements(By.tagName("a"))
                .stream()
                .filter(link -> link.isDisplayed())
                .map(link -> link.getText().strip())
                .toList();
    }

    @Override
    public void darClick() {
        clickSignIn();
    }
}
