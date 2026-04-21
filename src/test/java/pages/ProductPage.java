package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage {

    @FindBy(css = ".productname")
    private WebElement nameElement;

    @FindBy(css = ".price")
    private WebElement priceElement;

    @FindBy(id = "product_quantity")
    private WebElement quantityInput;

    @FindBy(css = ".cart")
    private WebElement addToCartButton;

    @FindBy(css = "a.logo")
    private WebElement logo;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage setQuantity(int quantity) {
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
        return this;
    }

    public ProductPage addToCart() {
        addToCartButton.click();
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return this;
    }

    public void returnToHomePage() {
        logo.click();
    }
}