package Tests;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.ConfirmationPage;
import Pages.LandingPage;
import Pages.ProductCatalogue;
import TestComponents.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import Pages.OrderPage;

public class AutomationCartTest extends BaseTest {
	WebDriver driver;

//	String emailField = "anshika@gmail.com";
//	String passWord = "Iamking@000";
	String productName = "ZARA COAT 3";
	String countryName = "Bangladesh";

	@Test(dataProvider = "getData", groups = { "purchase" })
	public void submitOrder( HashMap<String, String> input) throws InterruptedException {

		ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));
		List<WebElement> productList = productCatalogue.getProductList();
		productCatalogue.addProductToCart(input.get("product"));
		CartPage cartPage = productCatalogue.goToCartPage();

		Boolean match = cartPage.VerifyProductDisplay(input.get("product"));
		Assert.assertTrue(match);

		CheckoutPage checkOutPage = cartPage.goToCheckout();
		checkOutPage.selectCountry(countryName);
		ConfirmationPage confirmationPage = checkOutPage.submitOrder();

		// verify the text
		String confirmMessage = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
	}

	@Test(dependsOnMethods = { "submitOrder" })
	public void verifyOrderHistory() {
		ProductCatalogue productCatalogue = landingPage.loginApplication("anshika@gmail.com", "Iamking@000");
		OrderPage ordersPage = productCatalogue.goToOrdersPage();
		Assert.assertTrue(ordersPage.VerifyOrderDisplay(productName));
	}
	
	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String,String>> data = getJsonDataToMap(System.getProperty("user.dir")+"\\src\\test\\java\\Data\\purchaseOrderJson.json");
		return new Object[][]  {{data.get(0)}, {data.get(1) } };
	}

//	// data provider using 2d object and hashMap
//	@DataProvider
//	public Object[][] getData() {
//		HashMap<String, String> data1 = new HashMap<String, String>();
//		data1.put("emailField", "anshika@gmail.com");
//		data1.put("passWord", "Iamking@000");
//		data1.put("productName", "ZARA COAT 3");
//		
//		HashMap<String, String> data2 = new HashMap<String, String>();
//		data2.put("emailField", "shetty@gmail.com");
//		data2.put("passWord", "Iamking@000");
//		data2.put("productName", "ADIDAS ORIGINAL");
//		
//		return new Object[][] {{ data1 },
//				{ data2 } };
//	}

//	// data provider using 2d object
//	@DataProvider
//	public Object[][] getData() {
//		return new Object [][] {{"anshika@gmail.com","Iamking@000", "ZARA COAT 3"},{"shetty@gmail.com","Iamking@000","ADIDAS ORIGINAL" }};
//	}
}
