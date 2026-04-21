package tests;

import helpers.RandomHelper;
import models.CartItem;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SearchAndCartTests extends BaseTest {

    @Test
    @DisplayName("Search for products, add to cart and verify total price after quantity update")
    void searchAndCartTest() {
        HomePage homePage = new HomePage(driver);
        SearchResultsPage resultsPage = homePage.search("shirt");
        assertTrue(resultsPage.areAllProductsContainingKeyword("shirt"), "Not all products have 'shirt' in the name");

        resultsPage.sortByNameAscending();
        assertTrue(resultsPage.isSortedByNameAscending(), "Products are not sorted by name ascending");

        ProductPage secondProduct = resultsPage.getProductByIndex(2);
        ProductPage thirdProduct = resultsPage.getProductByIndex(3);
        int quantity1 = RandomHelper.getRandomIntNumber(1, 5);
        int quantity2 = RandomHelper.getRandomIntNumber(1, 5);
        secondProduct.setQuantity(quantity1)
                .addToCart();
        thirdProduct.setQuantity(quantity2)
                .addToCart();
        CartPage cartPage = homePage.viewCart();
        assertTrue(cartPage.getCartItems().size() >= 2, "Products not added to cart");

        CartItem cheapestItem = cartPage.getCheapestItem();
        assertNotNull(cheapestItem, "The cheapest product was not found");

        int newQuantity = cheapestItem.getQuantity() * 2;
        cartPage.updateQuantity(cheapestItem.getName(), newQuantity);
        double expectedTotal = cartPage.calculateExpectedTotal();
        double actualTotal = cartPage.getTotalPrice();
        assertEquals(expectedTotal, actualTotal, 0.01, "The total amount of the cart does not match the calculated one");
    }
}
