# SauceDemo UI Tests — Java + Selenium + TestNG + Allure

Проект выполнен по требованиям практического занятия №14–16: Maven, Java 17+, Page Object, Selenium WebDriver, TestNG, Allure, скриншоты при падении, environment.properties и кроссбраузерный запуск Chrome/Firefox/Edge.

## Требования
- JDK 17+
- Maven 3.9+
- Chrome / Firefox / Edge
- Selenium Manager автоматически подберёт драйверы.

## Запуск всех браузеров
```bash
mvn clean test
```

## Один браузер
```bash
mvn -Dtest=tests.SauceDemoTest -Dbrowser=chrome -Dheadless=false test
```

Для headless:
```bash
mvn -Dtest=tests.SauceDemoTest -Dbrowser=chrome -Dheadless=true test
```

## Allure
```bash
mvn allure:serve
```
или
```bash
mvn allure:report
```

Результаты: `target/allure-results`.

## Структура
- `pages/BasePage.java` — ожидания и общие действия
- `pages/LoginPage.java` — авторизация
- `pages/ProductsPage.java` — каталог
- `pages/CartPage.java` — корзина
- `pages/CheckoutPage.java` — оформление заказа
- `tests/BaseTest.java` — драйвер, cross-browser, teardown, вложения
- `tests/SauceDemoTest.java` — 6 тестовых сценариев
- `utils/AllureAttachments.java` — screenshot/page source
- `src/test/resources/testng.xml` — параллельный Chrome/Firefox/Edge


### Если запускаете на macOS

Для кроссбраузерного прогона должны быть установлены сами браузеры. Драйверы проект скачивает и кеширует через WebDriverManager.

Если Firefox и Edge ещё не установлены:

```bash
brew install --cask firefox
brew install --cask microsoft-edge
```

Chrome можно оставить установленным как есть. После установки выполните:

```bash
mvn clean test
```

Для проверки только Chrome:

```bash
mvn -Dbrowser=chrome -Dheadless=false test
```
