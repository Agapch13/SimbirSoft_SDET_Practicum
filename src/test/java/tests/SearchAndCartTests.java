package tests;

import helpers.RandomHelper;
import models.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchAndCartTests extends BaseTest {

    @Test
    @DisplayName("Search for products, add to cart and verify total price after quantity update")
    void searchAndCartTest() {
        for (Integer id : List.of(1, 2)) {
            homePage.search("shirt");
            assertTrue(searchResultsPage.getProductNames().stream().allMatch(name -> name.contains("shirt")),
                    "Shirt keyword does not contains in search results");

            Integer productId = searchResultsPage.getProductIdFromHref(searchResultsPage.getProducts().get(id));
            int quantity = RandomHelper.getRandomIntNumberBetween(2, 6);

            searchResultsPage.sortByNameAscending();
            assertTrue(searchResultsPage.isSortedByNameAscending(), "Products are not sorted by name from A to Z");

            searchResultsPage.openProductById(productId)
                    .selectRequiredOptions()
                    .setQuantity(quantity)
                    .addToCart()
                    .returnToHomePage();
        }

        homePage.goToCart();
        assertTrue(cartPage.getCartItems().size() >= 2, "Products not added to cart");

        CartItem cheapestItem = cartPage.getCheapestItem();
        assertNotNull(cheapestItem, "The cheapest product was not found");

        cartPage.updateQuantity(cheapestItem.getName(), cheapestItem.getQuantity() * 2);
        assertEquals(cartPage.calculateExpectedTotal(),  cartPage.getTotalPrice(),
                "The total amount of the cart does not match the calculated one");
    }
}
