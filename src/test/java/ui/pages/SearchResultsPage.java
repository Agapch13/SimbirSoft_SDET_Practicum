package ui.pages;

import ui.enums.SortType;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchResultsPage extends BasePage {

    @FindBy(id = "sort")
    private WebElement sortDropdown;

    @Getter
    @FindBy(css = ".fixed_wrapper .prdocutname")
    private List<WebElement> products;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public SearchResultsPage sortByNameAscending() {
        Select dropdown = new Select(sortDropdown);
        dropdown.selectByVisibleText(SortType.NAME_ASC.getDisplayText());
        return this;
    }

    public List<String> getProductNames() {
        return products.stream()
                .map(WebElement::getText)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public Integer getProductIdFromHref(WebElement link) {
        String href = link.getAttribute("href");
        String productId = extractProductIdFromUrl(href);
        return Integer.parseInt(productId);
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

    public boolean isSortedByNameAscending() {
        List<String> names = getProductNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Comparator.naturalOrder());
        return names.equals(sorted);
    }

    public ProductPage openProductById(Integer productId) {
        WebElement productLink = waitHelper.waitPresent(
                By.cssSelector(".prdocutname[href*='product_id=%d']".formatted(productId)));
        waitHelper.waitClickable(productLink).click();
        return new ProductPage(driver);
    }
}
