package Tests;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.ConfirmationPage;
import Pages.LandingPage;
import Pages.ProductCatalogue;
import Pages.OrderPage;
import TestComponents.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AutomationCartTest extends BaseTest {

    // Test parameters
    String productName = "ZARA COAT 3";
    String countryName = "Bangladesh";

    @Test(dataProvider = "getData", groups = {"Purchase"})
    public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {
        // Login and navigate to product catalog
        ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));

        // Add product to cart
        productCatalogue.addProductToCart(input.get("product"));
        CartPage cartPage = productCatalogue.goToCartPage();

        // Verify product is in the cart
        Boolean match = cartPage.VerifyProductDisplay(input.get("product"));
        Assert.assertTrue(match);

        // Proceed to checkout and place order
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.selectCountry(countryName);
        ConfirmationPage confirmationPage = checkoutPage.submitOrder();

        // Verify confirmation message
        String confirmMessage = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
    }

    @Test(dependsOnMethods = {"submitOrder"})
    public void OrderHistoryTest() {
        // Login and navigate to order history
        ProductCatalogue productCatalogue = landingPage.loginApplication("anshika@gmail.com", "Iamking@000");
        OrderPage ordersPage = productCatalogue.goToOrdersPage();

        // Verify the order is displayed
        Assert.assertTrue(ordersPage.VerifyOrderDisplay(productName));
    }

    @DataProvider
    public Object[][] getData() throws IOException {
        // Fetch data from JSON file
        List<HashMap<String, String>> data = getJsonDataToMap(
                System.getProperty("user.dir") + "\\src\\test\\java\\Data\\purchaseOrderJson.json");
        return new Object[][]{{data.get(0)}, {data.get(1)}};
    }
}
