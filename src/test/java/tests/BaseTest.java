package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.*;

import java.time.Duration;

public abstract class BaseTest {

    protected static final String BASE_URL = "https://automationteststore.com/";

    protected WebDriver driver;
    protected HomePage homePage;
    protected ProductPage productPage;
    protected CartPage cartPage;
    protected CategoryPage categoryPage;
    protected SearchResultsPage searchResultsPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver()
                .setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        WebDriver driverInstance = new ChromeDriver(options);
        driverInstance.manage().timeouts().implicitlyWait(Duration.ZERO);
        driverInstance.manage().deleteAllCookies();
        this.driver = driverInstance;
        driverInstance.get(BASE_URL);

        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        categoryPage = new CategoryPage(driver);
        searchResultsPage = new SearchResultsPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
