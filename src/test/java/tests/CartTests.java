package tests;

import helpers.RandomHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ProductPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTests extends BaseTest {

    @Test
    @DisplayName("Verify cart integrity and price recalculation after removing even-numbered products")
    void cartTest() {
        List<ProductPage> allProducts = homePage.getAllProducts();
        assertTrue(allProducts.size() >= 5, "The number of products on the main page must be at least 5");

        List<Integer> randomProductsIndexes = homePage.getRandomProductsIds(5);
        randomProductsIndexes.forEach(id -> {
            homePage.openProductById(id);
            int randomIntNumber = RandomHelper.getRandomIntNumberBetween(2, 10);
            productPage.setQuantity(randomIntNumber)
                    .selectRequiredOptions()
                    .addToCart()
                    .returnToHomePage();
        });

        homePage.goToCart();
        assertEquals(5, cartPage.getCartItems().size(), "There must be 5 items added to the cart");

        cartPage.removeEvenNumberedProducts();
        assertEquals(3, cartPage.getCartItems().size(), "After deletion there should be 3 items left");
        assertEquals(cartPage.calculateExpectedTotal(), cartPage.getTotalPrice(),
                "The total amount of the cart does not match the expected one");
    }
}
