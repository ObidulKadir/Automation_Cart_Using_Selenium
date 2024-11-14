package Tests;

import java.time.Duration;
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

	@Test (dataProvider = "getData", groups = {"purchase"})
	public void submitOrder(String emailField, String passWord, String productName) throws InterruptedException {

		ProductCatalogue productCatalogue = landingPage.loginApplication(emailField, passWord);
		List<WebElement> productList = productCatalogue.getProductList();
		productCatalogue.addProductToCart(productName);
		CartPage cartPage = productCatalogue.goToCartPage();
		
		Boolean match = cartPage.VerifyProductDisplay(productName);
		Assert.assertTrue(match);

		CheckoutPage checkOutPage = cartPage.goToCheckout();
		checkOutPage.selectCountry(countryName);
		ConfirmationPage confirmationPage = checkOutPage.submitOrder();


		// verify the text
		String confirmMessage = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
	}
	
	@Test(dependsOnMethods = {"submitOrder"})
	public void verifyOrderHistory() {
		ProductCatalogue productCatalogue = landingPage.loginApplication("anshika@gmail.com", "Iamking@000");
		OrderPage ordersPage= productCatalogue.goToOrdersPage();
		Assert.assertTrue(ordersPage.VerifyOrderDisplay(productName));
	}
	
	@DataProvider
	public Object[][] getData() {
		return new Object [][] {{"anshika@gmail.com","Iamking@000", "ZARA COAT 3"},{"shetty@gmail.com","Iamking@000","ADIDAS ORIGINAL" }};
	}
}
