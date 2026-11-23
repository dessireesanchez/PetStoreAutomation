package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class RegistrationPage extends BasePage {

    private static final Map<String, By> FIELDS = Map.ofEntries(
            Map.entry("User ID", By.name("username")),
            Map.entry("New password", By.name("password")),
            Map.entry("Repeat password", By.name("repeatedPassword")),
            Map.entry("First name", By.name("firstName")),
            Map.entry("Last name", By.name("lastName")),
            Map.entry("Email", By.name("email")),
            Map.entry("Phone", By.name("phone")),
            Map.entry("Address 1", By.name("address1")),
            Map.entry("Address 2", By.name("address2")),
            Map.entry("City", By.name("city")),
            Map.entry("State", By.name("state")),
            Map.entry("Zip", By.name("zip")),
            Map.entry("Country", By.name("country"))
    );

    private final By saveAccountButton = By.name("newAccount");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isRegistrationFormDisplayed() {
        return isDisplayed(FIELDS.get("Repeat password"))
                && isDisplayed(saveAccountButton);
    }

    public void enterField(String field, String value) {
        type(fieldLocator(field), value);
    }

    public String getFieldValue(String field) {
        return find(fieldLocator(field)).getDomProperty("value");
    }

    public String getFieldInputName(String field) {
        return find(fieldLocator(field)).getDomAttribute("name");
    }

    public void enterUserId(String userId) {
        enterField("User ID", userId);
    }

    public String getUserId() {
        return getFieldValue("User ID");
    }

    public String getLanguagePreference() {
        return find(By.name("languagePreference")).getDomProperty("value");
    }

    public String getFavouriteCategory() {
        return find(By.name("favouriteCategoryId")).getDomProperty("value");
    }

    public boolean isMyListSelected() {
        return find(By.name("listOption")).isSelected();
    }

    public boolean isMyBannerSelected() {
        return find(By.name("bannerOption")).isSelected();
    }

    public void saveAccountInformation() {
        submitForm(saveAccountButton);
    }

    private By fieldLocator(String field) {
        By locator = FIELDS.get(field);
        if (locator == null) {
            throw new IllegalArgumentException("Campo de registro desconocido: " + field);
        }
        return locator;
    }
}

