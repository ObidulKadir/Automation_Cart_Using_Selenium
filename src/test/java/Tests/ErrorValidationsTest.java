package Tests;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import TestComponents.BaseTest;
import Pages.CartPage;
import Pages.ProductCatalogue;

public class ErrorValidationsTest extends BaseTest{
	
	
	@Test (groups = {"errorValidations"})
	public void LoginErrorValidation() throws IOException, InterruptedException {

		
		landingPage.loginApplication("anshika@gmail.com", "Iamki000");
		Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());

	}
	
	
	@Test(groups = {"errorValidations"})
	public void ProductErrorValidation() throws IOException, InterruptedException
	{

		String productName = "ZARA COAT 3";
		String emailField = "mavat33861@gianes.com";
		String passWord = "Iamking@000";
		
		ProductCatalogue productCatalogue = landingPage.loginApplication(emailField, passWord);
		List<WebElement> products = productCatalogue.getProductList();
		productCatalogue.addProductToCart(productName);
		CartPage cartPage = productCatalogue.goToCartPage();
		Boolean match = cartPage.VerifyProductDisplay("ZARA COAT 33333");
		Assert.assertFalse(match);

	}
	
}
