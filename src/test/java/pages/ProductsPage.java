package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductsPage extends BasePage {
    private final By title = By.cssSelector(".title");
    private final By cart = By.cssSelector(".shopping_cart_link");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By sort = By.cssSelector("[data-test='product-sort-container']");
    private final By backpackAdd = By.id("add-to-cart-sauce-labs-backpack");
    private final By bikeLightAdd = By.id("add-to-cart-sauce-labs-bike-light");

    public ProductsPage(WebDriver driver, WebDriverWait wait) { super(driver, wait); }

    public boolean isOpened() { return el(title).getText().equals("Products"); }

    @Step("Добавить рюкзак в корзину")
    public ProductsPage addBackpack() { click(backpackAdd); return this; }

    @Step("Добавить велосипедный фонарь в корзину")
    public ProductsPage addBikeLight() { click(bikeLightAdd); return this; }

    @Step("Открыть корзину")
    public CartPage openCart() { click(cart); return new CartPage(driver, wait); }

    public int cartCount() {
        return driver.findElements(cartBadge).isEmpty() ? 0 : Integer.parseInt(el(cartBadge).getText());
    }

    @Step("Отсортировать товары по цене: от низкой к высокой")
    public ProductsPage sortByPriceLowToHigh() {
        new Select(el(sort)).selectByValue("lohi");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".inventory_item_price")));
        return this;
    }

    public double firstProductPrice() {
        return Double.parseDouble(el(By.cssSelector(".inventory_item_price")).getText().replace("$", ""));
    }
}
