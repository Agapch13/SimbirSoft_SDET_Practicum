package pages;

import io.qameta.allure.Step;
import models.CartItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = ".cart-info.product-list table tbody tr")
    private List<WebElement> cartRows;

    @FindBy(xpath = "//table[@id='totals_table']//td[contains(., 'Sub-Total')]/following-sibling::td/span")
    private WebElement totalPrice;

    @FindBy(css = ".empty-cart-message")
    private WebElement emptyCartMessage;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get products")
    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        for (WebElement row : cartRows) {
            if (!row.findElements(By.tagName("th")).isEmpty()) continue;
            String name = row.findElement(By.xpath("./td[2]")).getText();
            String priceRaw = row.findElement(By.xpath("./td[4]")).getText();
            double price = Double.parseDouble(priceRaw.replace("$", ""));
            WebElement quantityInput = row.findElement(By.xpath("./td[5]//input"));
            int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
            items.add(new CartItem(name, price, quantity));
        }
        return items;
    }

    @Step("Update product quantity")
    public void updateQuantity(String productName, int newQuantity) {
        for (WebElement row : cartRows) {
            String name = row.findElement(By.cssSelector(".product-name")).getText();
            if (name.equals(productName)) {
                WebElement quantityInput = row.findElement(By.cssSelector("input[type='number']"));
                quantityInput.clear();
                quantityInput.sendKeys(String.valueOf(newQuantity));
                row.findElement(By.cssSelector("button[title='Update']")).click();
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                break;
            }
        }
    }

    @Step("Remove even rows")
    public void removeEvenNumberedProducts() {
        int rowCount = cartRows.size() - 1;
        for (int i = rowCount; i >= 1; i--) {
            if (i % 2 == 0) {
                String rowXpath = "//table[@class='table table-striped table-bordered']//tr[" + (i + 1) + "]";
                WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(rowXpath + "//a[contains(@href, 'remove')]")));
                removeBtn.click();
                wait.until(ExpectedConditions.stalenessOf(removeBtn));
            }
        }
    }

    public CartItem getCheapestItem() {
        return getCartItems()
                .stream()
                .min(Comparator.comparingDouble(CartItem::getPrice))
                .orElse(null);
    }

    public double getTotalPrice() {
        String totalText = totalPrice.getText()
                .replace("$", "")
                .trim();
        return Double.parseDouble(totalText);
    }

    public boolean isEmpty() {
        try {
            return emptyCartMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public double calculateExpectedTotal() {
        return getCartItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}