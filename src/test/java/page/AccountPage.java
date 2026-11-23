package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {
    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public String getUserId() {
        return getText(By.xpath("//td[normalize-space(.)='User ID:']/following-sibling::td"))
                .strip();
    }
}
