package ui.pages;

import ui.helpers.RandomHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Step("Open \"Apparel & accessories\" category")
    public void openApparelCategory() {
        apparelCategory.click();
    }

    @Step("Open product")
    public void openProductById(Integer productId) {
        WebElement productLink = waitHelper.waitPresent(
                By.cssSelector(".prdocutname[href*='product_id=%d']".formatted(productId)));
        waitHelper.waitClickable(productLink).click();
    }

    @Step("Search products by keyword {keyword}")
    public void search(String keyword) {
        waitHelper.waitVisible(searchInput).sendKeys(keyword);
        waitHelper.waitClickable(searchButton).click();
    }

    @Step("Go to cart")
    public void goToCart() {
        By by = By.cssSelector("#main_menu_top > li:nth-child(3) > a > span");
        waitHelper.waitClickable(by).click();
    }

    @Step("Get all products")
    public List<ProductPage> getAllProducts() {
        return allProducts.stream()
                .map(product -> new ProductPage(driver))
                .collect(Collectors.toList());
    }

    @Step("Get random product ids")
    public List<Integer> getRandomProductsIds(int count) {
        Set<Integer> productIdsFromHref = getProductIdsFromHref();
        return RandomHelper.getRandomElements(productIdsFromHref, count);
    }

    private String extractProductIdFromUrl(String url) {
        Pattern pattern = Pattern.compile("product_id=(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private Set<Integer> getProductIdsFromHref() {
        Set<Integer> productIds = new HashSet<>();

        for (WebElement link : this.allProducts) {
            String href = link.getAttribute("href");
            String productId = extractProductIdFromUrl(href);
            if (productId != null) {
                int id = Integer.parseInt(productId);
                productIds.add(id);
            }
        }
        return productIds;
    }
}
