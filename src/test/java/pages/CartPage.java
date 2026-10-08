package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage extends BasePage {
    private final By checkout = By.id("checkout");
    private final By backpackItem = By.id("item_4_title_link");
    private final By bikeLightItem = By.id("item_0_title_link");
    private final By backpackRemove = By.id("remove-sauce-labs-backpack");

    public CartPage(WebDriver driver, WebDriverWait wait) { super(driver, wait); }

    public boolean hasBackpack() { return !driver.findElements(backpackItem).isEmpty(); }
    public boolean hasBikeLight() { return !driver.findElements(bikeLightItem).isEmpty(); }

    @Step("Удалить рюкзак из корзины")
    public CartPage removeBackpack() { click(backpackRemove); return this; }

    @Step("Перейти к оформлению заказа")
    public CheckoutPage checkout() { click(checkout); return new CheckoutPage(driver, wait); }
}
