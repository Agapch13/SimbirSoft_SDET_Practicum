package pages;

import helpers.RandomHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Locale;

public class ProductPage extends BasePage {

    private static final By PRODUCT_OPTION_GROUP = By.cssSelector("#product .form-group");
    private static final By QUANTITY_INPUT = By.id("product_quantity");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector(".cart");

    @FindBy(css = ".productname")
    private WebElement nameElement;

    @FindBy(css = ".price")
    private WebElement priceElement;

    @FindBy(id = "product_quantity")
    private WebElement quantityInput;

    @FindBy(css = ".cart")
    private WebElement addToCartButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage selectRequiredOptions() {
        waitHelper.waitVisible(QUANTITY_INPUT);
        List<WebElement> groups = driver.findElements(PRODUCT_OPTION_GROUP);

        for (WebElement group : groups) {
            List<WebElement> inputs = group.findElements(By.cssSelector("input[type='radio'], select"));

            if (inputs.isEmpty()) {
                continue;
            }

            WebElement firstInput = inputs.get(0);
            String tagName = firstInput.getTagName();

            if (tagName.equals("input")) {
                List<WebElement> labels = group.findElements(By.tagName("label"));

                List<WebElement> validLabels = labels.stream()
                        .filter(label -> !label.getText().toLowerCase(Locale.ROOT).contains("out of stock"))
                        .filter(l -> {
                            List<WebElement> rd = l.findElements(By.tagName("input"));
                            return !rd.isEmpty() && rd.get(0).isEnabled();
                        })
                        .toList();

                if (!validLabels.isEmpty()) {
                    WebElement randomLabel = RandomHelper.getRandomElement(validLabels);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomLabel);
                }

            } else if (tagName.equals("select")) {
                Select select = new Select(firstInput);
                List<WebElement> validOptions = select.getOptions().stream()
                        .filter(option -> {
                            String optionText = option.getText().toLowerCase();
                            String optionValue = option.getAttribute("value");
                            return optionValue != null
                                   && !optionValue.isEmpty()
                                   && !optionText.contains("select")
                                   && !optionText.contains("out of stock");
                        })
                        .toList();

                if (!validOptions.isEmpty()) {
                    select.selectByValue(RandomHelper.getRandomElement(validOptions).getAttribute("value"));
                }
            }
        }
        return this;
    }

    public ProductPage setQuantity(int quantity) {
        WebElement input = waitHelper.waitVisible(QUANTITY_INPUT);
        input.clear();
        input.sendKeys(String.valueOf(quantity));
        return this;
    }

    public ProductPage addToCart() {
        waitHelper.waitClickable(ADD_TO_CART_BUTTON).click();
        return this;
    }

    public void returnToHomePage() {
        waitHelper.waitClickable(By.linkText("Home")).click();
    }
}
