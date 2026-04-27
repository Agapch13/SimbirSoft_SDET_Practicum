package ui.pages;

import ui.enums.SortType;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryPage extends BasePage {

    @FindBy(id = "sort")
    private WebElement sortDropdown;

    @FindBy(css = ".fixed_wrapper .prdocutname")
    private List<WebElement> productNames;

    @FindBy(css = ".price .oneprice")
    private List<WebElement> productPrices;

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select sorting by {sortType}")
    public void selectSorting(SortType sortType) {
        Select dropdown = new Select(sortDropdown);
        dropdown.selectByVisibleText(sortType.getDisplayText());
    }

    @Step("Get product names")
    public List<String> getProductNames() {
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Get product prices")
    public List<Double> getProductPrices() {
        return productPrices.stream()
                .map(el -> el.getText().replace("$", "").trim())
                .filter(s -> !s.isEmpty())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }


    public boolean isSortedByNameAscending() {
        List<String> names = getProductNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(String::compareTo);
        return names.equals(sorted);
    }

    public boolean isSortedByNameDescending() {
        List<String> names = getProductNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Comparator.reverseOrder());
        return names.equals(sorted);
    }

    public boolean isSortedByPriceAscending() {
        List<Double> prices = getProductPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Double::compareTo);
        return prices.equals(sorted);
    }

    public boolean isSortedByPriceDescending() {
        List<Double> prices = getProductPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.reverseOrder());
        return prices.equals(sorted);
    }
}
