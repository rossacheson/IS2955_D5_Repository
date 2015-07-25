import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * IS2955 Software Testing Summer 2015
 * Selenium and JUnit tests of http://www.rdadesigns.net/yw-d/
 * 
 * @author Ross Acheson
 * @author Malini Santra
 *
 */

public class YWDSiteTests {

	// static WebDriver driver = new HtmlUnitDriver();
	static WebDriver foxDriver = new FirefoxDriver();

	/*
	 * As a user
	 * I want to have a shortcut to jump to the top of the page
	 * So that I don't have to scroll as much
	 * 
	 * @author Ross Acheson
	 */

	// [No given needed]
	// When I open the home page and am at the top by default
	// Then I don't want to see jump to top shortcut
	// b/c it doesn't help me when I'm already at the top
	@Test
	public void testJumpIconNotDisplayed() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/");
		WebElement jumpIcon = foxDriver.findElement(By.className("enigma_scrollup"));
		assertFalse(jumpIcon.isDisplayed());
	}

	// Given that I am on the home page
	// When I scroll down
	// Then the jump to top icon should become visible
	// and clicking it should bring me back to the top where it is no longer visible
	@Test
	public void testJumpIconDisplayed() throws InterruptedException {
		foxDriver.get("http://www.rdadesigns.net/yw-d/");
		JavascriptExecutor jse = (JavascriptExecutor) foxDriver;
		jse.executeScript("window.scrollBy(0,250)", ""); // scroll down 250 pixels
		WebElement jumpIcon = foxDriver.findElement(By.className("enigma_scrollup"));
		assertTrue(jumpIcon.isDisplayed());
		jumpIcon.click();
		Thread.sleep(1000); // the JavaScript code animates the scroll and fades the button out
							// before it gets to display: none;
							// so I have to wait for the animation to finish before checking

		// check the jump icon no longer shows
		assertFalse(jumpIcon.isDisplayed());

		// check the vertical scroll position is 0, at the top of the page
		long scrollY = (long) jse.executeScript("return window.scrollY;");
		assertEquals(scrollY, 0);
	}

}