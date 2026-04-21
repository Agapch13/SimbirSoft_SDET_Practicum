package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsPage extends BasePage {

    @FindBy(id = "sort")
    private WebElement sortDropdown;

    @FindBy(css = ".fixed_wrapper .prdocutname")
    private List<WebElement> productElements;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public void sortByNameAscending() {
        Select dropdown = new Select(sortDropdown);
        dropdown.selectByVisibleText("Name A - Z");
    }

    public List<ProductPage> getProducts() {
        return productElements.stream()
                .map(element -> new ProductPage(driver))
                .collect(Collectors.toList());
    }

    public List<String> getProductNames() {
        return this.productElements.stream()
                .map(WebElement::getText)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public ProductPage getProductByIndex(int index) {
        return new ProductPage(driver);
    }

    public boolean areAllProductsContainingKeyword(String keyword) {
        return getProductNames().stream()
                .allMatch(s -> {
                    System.out.println(s);
                    return s.contains(keyword);
                });
    }

    public boolean isSortedByNameAscending() {
        List<String> names = getProductNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Comparator.naturalOrder());
        return names.equals(sorted);
    }
}