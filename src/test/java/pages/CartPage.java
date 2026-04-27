package pages;

import io.qameta.allure.Step;
import models.CartItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CartPage extends BasePage {

    private static final By CART_ROW = By.cssSelector(".cart-info.product-list table tbody tr");
    private static final By TOTAL_PRICE = By.xpath("//table[@id='totals_table']//td[contains(., 'Sub-Total')]/following-sibling::td/span");

    @FindBy(css = ".cart-info.product-list table tbody tr")
    private List<WebElement> cartRow;

    @FindBy(xpath = "//table[@id='totals_table']//td[contains(., 'Sub-Total')]/following-sibling::td/span")
    private WebElement totalPrice;

    @FindBy(css = ".empty-cart-message")
    private WebElement emptyCartMessage;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get all added products to cart")
    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        for (WebElement row : waitHelper.waitVisibleAll(CART_ROW)) {
            if (!row.findElements(By.tagName("td")).isEmpty()) {
                WebElement nameCell = row.findElement(By.cssSelector("td:nth-child(2)"));
                WebElement nameLink = nameCell.findElement(By.tagName("a"));
                String name = nameLink.getText();
                String priceRaw = row.findElement(By.cssSelector("td:nth-child(4)")).getText();
                double price = Double.parseDouble(priceRaw.replace("$", ""));
                WebElement quantityInput = row.findElement(By.cssSelector("td:nth-child(5) input"));
                int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
                items.add(new CartItem(name, price, quantity));
            }
        }
        return items;
    }

    @Step("Update product quantity for: {productName}")
    public void updateQuantity(String productName, int newQuantity) {
        By rowLocator = By.xpath("//table[@class='table table-striped table-bordered']//tr[.//td[2]//a[contains(text(), '%s')]]"
                .formatted(productName));
        WebElement row = waitHelper.waitVisible(rowLocator);
        String oldTotal = getTotalPriceText();
        WebElement quantityInput = row.findElement(By.cssSelector("td:nth-child(5) input[type='text']"));
        quantityInput.click();
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(newQuantity));
        WebElement updateButton = driver.findElement(By.xpath("//button[@title='Update' or contains(text(), 'Update')]"));
        updateButton.click();
        waitHelper.waitUntil(Duration.ofSeconds(10), driver -> !getTotalPriceText().equals(oldTotal));
    }

    @Step("Remove even rows from cart")
    public void removeEvenNumberedProducts() {
        int rowCount = waitHelper.waitVisibleAll(CART_ROW).size() - 1;
        for (int i = rowCount; i >= 1; i--) {
            if (i % 2 == 0) {
                String rowXpath = "//table[@class='table table-striped table-bordered']//tr[" + (i + 1) + "]";
                WebElement removeBtn = waitHelper.waitClickable(By.xpath(rowXpath + "//a[contains(@href, 'remove')]"));
                removeBtn.click();
                waitHelper.waitStaleness(removeBtn);
            }
        }
    }

    @Step("Get cheapest item from the cart")
    public CartItem getCheapestItem() {
        return getCartItems()
                .stream()
                .min(Comparator.comparingDouble(CartItem::getPrice))
                .orElse(null);
    }


    @Step("Get products total price from the cart")
    public double getTotalPrice() {
        String totalText = getTotalPriceText();
        return Double.parseDouble(totalText);
    }

    @Step("Calculate total of products prices")
    public double calculateExpectedTotal() {
        return getCartItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private String getTotalPriceText() {
        return waitHelper.waitVisible(TOTAL_PRICE).getText()
                .replace("$", "")
                .replace(",", "")
                .trim();
    }
}
