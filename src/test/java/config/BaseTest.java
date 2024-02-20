package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import pages.BasePage;

import java.time.Duration;

import static java.sql.DriverManager.getDriver;

public class BaseTest {
    public static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("firefox") String browser) { /* данный метод по дефолту будет использовать браузер firefox */
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--lang=en");
            // options.addArguments("--headless");
            driverThreadLocal.set(new ChromeDriver(options));
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addPreference("intl.accept_languages", "en");
            driverThreadLocal.set(new FirefoxDriver(options));
        } else if (browser.equalsIgnoreCase("safari")) {
            SafariOptions options = new SafariOptions();
            options.setCapability("language", "en");
            driverThreadLocal.set(new SafariDriver());
        } else if (browser.equalsIgnoreCase("edge")) {
            // Настройки для Edge
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.setCapability("language", "en");
            //options.addArguments("--headless");
            driverThreadLocal.set(new EdgeDriver(options));
        } else {
            throw new IllegalArgumentException("Invalid browser" + browser);
        }
        // Этот блок кода получает веб-драйвер с помощью метода getDriver(), максимизирует окно браузера,
        // устанавливает время ожидания загрузки страницы и неявного ожидания,
        // а затем устанавливает этот драйвер для BasePage.

        WebDriver driver = getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(20000));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(20000));
        BasePage.setDriver(driver);
    }
    @AfterMethod
    public void tearDown(){
        WebDriver driver = getDriver(); // Получаем текущий экземпляр WebDriver с помощью метода getDriver().
        if (driver != null){ // Проверяем, что экземпляр драйвера не равен null.
            driver.quit(); // Если драйвер не равен null, то закрываем браузер с помощью метода quit().
            driverThreadLocal.remove(); // Удаляем текущий экземпляр WebDriver из объекта driverThreadLocal.
        }
    }
}
// Selenium Gride запускает дистанционно тесты на разных устройствах и браузерах
// Selenium ID- среда разработки позволяет записывать в текстовом режакторе тесты в
// виде текстовых сценариев
// Selenium Client Library - набор библиотек языков программирования
// Selenium Commandкоманды кот исп-ся для управлением браузером
