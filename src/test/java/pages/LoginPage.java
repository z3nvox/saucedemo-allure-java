package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {
    private static final String URL = "https://www.saucedemo.com/";
    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By error = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver, WebDriverWait wait) { super(driver, wait); }

    @Step("Открыть SauceDemo")
    public LoginPage open() {
        driver.get(URL);
        return this;
    }

    @Step("Выполнить вход пользователем {user}")
    public ProductsPage login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
        return new ProductsPage(driver, wait);
    }

    @Step("Попытаться войти с пользователем {user}")
    public LoginPage loginExpectingError(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
        return this;
    }

    public String getError() { return el(error).getText(); }
}
