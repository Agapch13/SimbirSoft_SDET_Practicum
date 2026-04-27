package ui.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WaitHelper {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private final WebDriver driver;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitVisible(By locator) {
        return await().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitVisible(WebElement element) {
        return await().until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitClickable(By locator) {
        return await().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitClickable(WebElement element) {
        return await().until(ExpectedConditions.elementToBeClickable(element));
    }

    public List<WebElement> waitVisibleAll(By locator) {
        return await().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public WebElement waitPresent(By locator) {
        return await().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean waitStaleness(WebElement element) {
        return await().until(ExpectedConditions.stalenessOf(element));
    }

    public <T> T waitUntil(Function<WebDriver, T> condition) {
        return await().until(condition);
    }

    public <T> T waitUntil(Duration timeout, Function<WebDriver, T> condition) {
        return await(timeout).until(condition);
    }

    public WebDriverWait await() {
        return await(DEFAULT_TIMEOUT);
    }

    private WebDriverWait await(Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }
}
