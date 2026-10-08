package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;

@Epic("SauceDemo UI")
@Owner("Student")
public class SauceDemoTest extends BaseTest {
    private static final String USER = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    @Test(description = "Позитив: стандартный пользователь успешно входит")
    @Feature("Авторизация")
    @Story("Успешный вход")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldLoginSuccessfully() {
        ProductsPage products = new LoginPage(driver, wait).open().login(USER, PASSWORD);
        Assert.assertTrue(products.isOpened(), "Должна открыться страница Products");
    }

    @Test(description = "Негатив: заблокированный пользователь не может войти")
    @Feature("Авторизация")
    @Story("Заблокированный пользователь")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldRejectLockedUser() {
        LoginPage login = new LoginPage(driver, wait).open();
        login.loginExpectingError("locked_out_user", PASSWORD);
        Assert.assertTrue(login.getError().toLowerCase().contains("locked"), "Должно отображаться сообщение о блокировке");
    }

    @Test(description = "Добавление двух товаров в корзину")
    @Feature("Корзина")
    @Story("Добавление товаров")
    @Severity(SeverityLevel.NORMAL)
    public void shouldAddTwoProductsToCart() {
        ProductsPage products = new LoginPage(driver, wait).open().login(USER, PASSWORD);
        products.addBackpack().addBikeLight();
        Assert.assertEquals(products.cartCount(), 2, "В корзине должно быть 2 товара");
    }

    @Test(description = "Удаление товара из корзины")
    @Feature("Корзина")
    @Story("Удаление товара")
    @Severity(SeverityLevel.NORMAL)
    public void shouldRemoveProductFromCart() {
        ProductsPage products = new LoginPage(driver, wait).open().login(USER, PASSWORD);
        CartPage cart = products.addBackpack().addBikeLight().openCart();
        Assert.assertTrue(cart.hasBackpack() && cart.hasBikeLight());
        cart.removeBackpack();
        Assert.assertFalse(cart.hasBackpack(), "Рюкзак должен исчезнуть из корзины");
        Assert.assertTrue(cart.hasBikeLight(), "Второй товар должен остаться");
    }

    @Test(description = "Полное оформление заказа")
    @Feature("Checkout")
    @Story("Успешное оформление")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldCompleteCheckout() {
        ProductsPage products = new LoginPage(driver, wait).open().login(USER, PASSWORD);
        CheckoutPage checkout = products.addBackpack().openCart().checkout();
        checkout.fillCustomerData("Ivan", "Petrov", "123456").continueCheckout().finish();
        Assert.assertEquals(checkout.getCompleteMessage(), "Thank you for your order!");
    }

    @Test(description = "Сортировка товаров по возрастанию цены")
    @Feature("Каталог")
    @Story("Сортировка")
    @Severity(SeverityLevel.MINOR)
    public void shouldSortProductsByPriceAscending() {
        ProductsPage products = new LoginPage(driver, wait).open().login(USER, PASSWORD);
        products.sortByPriceLowToHigh();
        Assert.assertEquals(products.firstProductPrice(), 7.99, 0.001, "Первым должен быть самый дешёвый товар");
    }

    @Test(description = "Негатив: неверный пароль отклоняется")
    @Feature("Авторизация")
    @Story("Неверные учетные данные")
    @Severity(SeverityLevel.NORMAL)
    public void shouldRejectInvalidPassword() {
        LoginPage login = new LoginPage(driver, wait).open();
        login.loginExpectingError(USER, "wrong_password");
        Assert.assertTrue(login.getError().toLowerCase().contains("do not match"), "Должна быть ошибка неверных данных");
    }
}
