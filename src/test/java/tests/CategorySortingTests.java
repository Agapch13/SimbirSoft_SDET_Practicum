package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CategoryPage;
import pages.HomePage;

import static enums.SortType.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategorySortingTests extends BaseTest {

    @Test
    @DisplayName("Verify product sorting functionality by name and price")
    void categorySortingTest() {
        CategoryPage categoryPage = new HomePage(driver)
                .openApparelCategory();

        assertTrue(categoryPage.getProductNames().size() >= 4, "The category must contain at least 4 products");

        categoryPage.selectSorting(NAME_ASC);
        assertTrue(categoryPage.isSortedByNameAscending(), "Products are not sorted by name from A to Z");

        categoryPage.selectSorting(NAME_DESC);
        assertTrue(categoryPage.isSortedByNameDescending(), "Products are not sorted by name from Z to A");

        categoryPage.selectSorting(PRICE_ASC);
        assertTrue(categoryPage.isSortedByPriceAscending(), "Products are not sorted by price from low to high");

        categoryPage.selectSorting(PRICE_DESC);
        assertTrue(categoryPage.isSortedByPriceDescending(), "Products are not sorted by price from high to low");
    }
}