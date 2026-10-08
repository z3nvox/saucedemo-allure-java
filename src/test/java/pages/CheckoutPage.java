package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage extends BasePage {
    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.cssSelector(".complete-header");

    public CheckoutPage(WebDriver driver, WebDriverWait wait) { super(driver, wait); }

    @Step("Заполнить данные доставки")
    public CheckoutPage fillCustomerData(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
        return this;
    }

    @Step("Продолжить оформление заказа")
    public CheckoutPage continueCheckout() { click(continueButton); return this; }

    @Step("Завершить заказ")
    public CheckoutPage finish() { click(finishButton); return this; }

    public String getCompleteMessage() { return el(completeHeader).getText(); }
}
