package pages;

import helpers.RandomHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    @FindBy(css = ".fixed_wrapper .prdocutname")
    private List<WebElement> allProducts;

    @FindBy(css = "#categorymenu > nav > ul > li:nth-child(2) > a")
    private WebElement apparelCategory;

    @FindBy(id = "filter_keyword")
    private WebElement searchInput;

    @FindBy(css = ".button-in-search")
    private WebElement searchButton;

    @FindBy(css = ".dropdown-toggle .fa-shopping-cart")
    private WebElement cartIcon;

    @FindBy(linkText = "View Cart")
    private WebElement viewCartLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public CategoryPage openApparelCategory() {
        apparelCategory.click();
        return new CategoryPage(driver);
    }

    public SearchResultsPage search(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.sendKeys(keyword);
        searchButton.click();
        return new SearchResultsPage(driver);
    }

    public CartPage viewCart() {
        driver.get("https://automationteststore.com/index.php?rt=checkout/cart");
        return new CartPage(driver);
    }

    public List<ProductPage> getAllProducts() {
        return allProducts.stream()
                .map(product -> new ProductPage(driver))
                .collect(Collectors.toList());
    }

    public List<Integer> getRandomProductsIndexes(int count) {
        List<ProductPage> allProducts = getAllProducts();
        return RandomHelper.getRandomIndexes(allProducts, count);
    }

    public void openProductByIndex(int index) {
        allProducts.get(index).click();
    }
}