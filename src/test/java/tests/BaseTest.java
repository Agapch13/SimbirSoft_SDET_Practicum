package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public abstract class BaseTest {

    protected static final String BASE_URL = "https://automationteststore.com/";
    protected static final int IMPLICIT_WAIT_SECONDS = 10;
    protected static final int EXPLICIT_WAIT_SECONDS = 15;

    protected WebDriver driver;
    protected WebDriverWait wait;

    @RegisterExtension
    final TestExecutionExceptionHandler screenshotOnFailure = new TestExecutionExceptionHandler() {
        @Override
        public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
            attachScreenshotOnFailure();
            throw throwable;
        }
    };

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver()
                .setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        WebDriver driverInstance = new ChromeDriver(options);
        driverInstance.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        driverInstance.manage().deleteAllCookies();
        WebDriverWait waitInstance = new WebDriverWait(driverInstance, Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
        this.driver = driverInstance;
        this.wait = waitInstance;
        driverInstance.get(BASE_URL);

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void attachScreenshotOnFailure() {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return;
        }
        try {
            byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
        } catch (RuntimeException ignored) {
        }
    }
}
