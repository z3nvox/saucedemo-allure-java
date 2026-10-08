package tests;

import io.qameta.allure.Allure;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.AllureAttachments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void prepareDrivers() {
        // Загружаем/кешируем драйверы последовательно до параллельного запуска.
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.edgedriver().setup();
    }
    protected WebDriver driver;
    protected WebDriverWait wait;

    @Parameters({"browser", "headless"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless) {
        driver = createDriver(browser, Boolean.parseBoolean(headless));
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        writeEnvironment(browser);
        Allure.parameter("browser", browser);
        Allure.parameter("headless", headless);
    }

    private WebDriver createDriver(String browser, boolean headless) {
        return switch (browser.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) options.addArguments("--headless=new");
                yield new EdgeDriver(options);
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) options.addArguments("--headless=new");
                yield new ChromeDriver(options);
            }
        };
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null && !result.isSuccess()) {
            AllureAttachments.screenshot(driver, "Failure screenshot");
            AllureAttachments.pageSource(driver, "Page source on failure");
        }
        if (driver != null) driver.quit();
    }

    private void writeEnvironment(String browser) {
        File dir = new File("target/allure-results");
        if (!dir.exists()) dir.mkdirs();
        Properties p = new Properties();
        p.setProperty("os.name", System.getProperty("os.name"));
        p.setProperty("os.version", System.getProperty("os.version"));
        p.setProperty("jdk.version", System.getProperty("java.version"));
        p.setProperty("selenium", "4.50.0");
        p.setProperty("testng", "7.10.2");
        p.setProperty("browser", browser);
        p.setProperty("driver.manager", "WebDriverManager 6.4.0");
        try (FileWriter writer = new FileWriter(new File(dir, "environment.properties"))) {
            p.store(writer, "SauceDemo test environment");
        } catch (IOException ignored) { }
    }
}
