package tests;

import helpers.RandomHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTests extends BaseTest {

    @Test
    @DisplayName("Verify cart integrity and price recalculation after removing even-numbered products")
    void cartTest() {
        HomePage homePage = new HomePage(driver);
        List<ProductPage> allProducts = homePage.getAllProducts();
        assertTrue(allProducts.size() >= 5, "The number of products on the main page must be at least 5");
        
        List<Integer> randomProductsIndexes = homePage.getRandomProductsIndexes(5);
        randomProductsIndexes.forEach(index -> {
            homePage.openProductByIndex(index);
            int randomIntNumber = RandomHelper.getRandomIntNumber(1, 10);
            new ProductPage(driver)
                    .setQuantity(randomIntNumber)
                    .addToCart()
                    .returnToHomePage();
        });
        
        CartPage cartPage = homePage.viewCart();
        int actualCartSize = cartPage.getCartItems()
                .size();
        assertEquals(5, actualCartSize, "There must be 5 items added to the cart");
        
        cartPage.removeEvenNumberedProducts();
        assertEquals(3, cartPage.getCartItems().size(), "After deletion there should be 3 items left");

        double expectedTotalPrice = cartPage.calculateExpectedTotal();
        double actualTotalPrice = cartPage.getTotalPrice();
        assertEquals(expectedTotalPrice, actualTotalPrice, "The total amount of the cart does not match the expected one");
    }
}