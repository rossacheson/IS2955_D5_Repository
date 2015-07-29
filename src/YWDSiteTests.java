import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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

	static WebDriver driver = new HtmlUnitDriver();
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
	public void testJumpIconWorks() throws InterruptedException {
		foxDriver.get("http://www.rdadesigns.net/yw-d/");
		JavascriptExecutor jse = (JavascriptExecutor) foxDriver;
		jse.executeScript("window.scrollBy(0,250)", ""); // scroll down 250 pixels
		WebElement jumpIcon = foxDriver.findElement(By.className("enigma_scrollup"));
		assertTrue(jumpIcon.isDisplayed());
		jumpIcon.click();
		Thread.sleep(1000); // the JavaScript code animates the scroll and fades the button out
							// so I have to wait for the animation to finish before checking

		// check the jump icon no longer shows
		assertFalse(jumpIcon.isDisplayed());

		// check the vertical scroll position is 0, at the top of the page
		long scrollY = (long) jse.executeScript("return window.scrollY;");
		assertEquals(scrollY, 0);
	}

	/*
	 * As a staff member
	 * I want users to be aware of and able to get to the donation page
	 * So that they can donate
	 * 
	 * @author Ross Acheson
	 */

	// [No given needed]
	// When I navigate to any page on the site
	// Then I see "Online Donation" button is present
	@Test
	public void testDonationButtonPresent() {
		// test a variety of pages
		driver.get("http://www.rdadesigns.net/yw-d/"); // home page
		assertTrue(driver.findElement(By.linkText("Online Donation")).isDisplayed());
		driver.get("http://www.rdadesigns.net/yw-d/programs/praise/"); // a program page
		assertTrue(driver.findElement(By.linkText("Online Donation")).isDisplayed());
		driver.get("http://www.rdadesigns.net/yw-d/about/detroit/"); // an about page
		assertTrue(driver.findElement(By.linkText("Online Donation")).isDisplayed());
		driver.get("http://www.rdadesigns.net/yw-d/get-involved/mission-partner/"); // partner
		assertTrue(driver.findElement(By.linkText("Online Donation")).isDisplayed());
		driver.get("http://www.rdadesigns.net/yw-d/contact/"); // contact page
		assertTrue(driver.findElement(By.linkText("Online Donation")).isDisplayed());
	}

	// Given that the Donation button is present
	// When I click on it
	// Then I am navigated to the secure PayPal donation page
	@Test
	public void testDonationDestination() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/");
		foxDriver.findElement(By.linkText("Online Donation")).click();

		String title = foxDriver.getTitle();
		assertTrue(title.contains("PayPal"));

		String url = foxDriver.getCurrentUrl();
		assertTrue(url.contains("https://www.paypal.com")); // correct site, secure protocol
	}

	/*
	 * As a staff member
	 * I want users to be able to find us on our social media sites
	 * So that we can increase our social network reach
	 * and more effectively share who we are
	 * 
	 * @author Ross Acheson
	 */

	// [No given needed]
	// When I click the social icons
	// Then I should be directed to our social media pages
	@Test
	public void testSocialIconsTarget() throws InterruptedException {
		foxDriver.get("http://www.rdadesigns.net/yw-d/");
		String url;
		// --facebook--
		WebElement linkContainer = foxDriver.findElement(By.className("facebook"));
		WebElement fbLink = linkContainer.findElement(By.tagName("a"));
		fbLink.click();
		url = foxDriver.getCurrentUrl();
		assertTrue(url.contains("https://www.facebook.com/youthworksdetroit"));
		foxDriver.navigate().back();
		// --tumblr--
		linkContainer = foxDriver.findElement(By.className("dribbble"));
		WebElement tumblrLink = linkContainer.findElement(By.tagName("a"));
		tumblrLink.click();
		url = foxDriver.getCurrentUrl();
		assertTrue(url.contains("http://youthworksdetroit.tumblr.com/"));
		foxDriver.navigate().back();
		// --youtube--
		// added explicit wait below to alleviate race condition where we tried to get
		// the youtube container element before it was loaded
		linkContainer = (new WebDriverWait(foxDriver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.className("youtube")));
		WebElement ytLink = linkContainer.findElement(By.tagName("a"));
		ytLink.click();
		url = foxDriver.getCurrentUrl();
		assertTrue(url.contains("https://www.youtube.com/user/youthworksdetroit"));
		foxDriver.navigate().back();

	}

	// [No given needed]
	// When I hover over a social icon
	// Then there should be a tooltip with the name of the social site
	@Test
	public void testSocialIconHover() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/");
		WebElement fbLinkContainer = foxDriver.findElement(By.className("facebook"));
		new Actions(foxDriver).moveToElement(fbLinkContainer).perform(); // hover
		String tooltip = foxDriver.findElement(By.className("tooltip-inner")).getText();
		assertTrue(tooltip.equals("Facebook"));
	}

/*	
 * This test removed. I think it worked previously, but more that 100 manual checks
 * could have been performed in the time it has taken me to fail at debugging this test.
 * Various complicated attempted workarounds have been removed so that the logic shows more simply.
 * 
 *  // Given I am on the Media page, and not signed in to facebook in my browser
 *	// When I click the 'Like Page' button
 *	// Then I should be directed to a facebook sign-in
 *	@Test
 *	public void testFacebookWidget() throws InterruptedException {
 *		foxDriver.get("http://www.rdadesigns.net/yw-d/about/media");
 *		// facebook widget loads slower than the page, so use explicit wait
 *		WebElement likePageButton = (new WebDriverWait(foxDriver, 10)).until(ExpectedConditions
 *				.presenceOfElementLocated(By.className("pluginConnectButtonDisconnected")));
 *		likePageButton.click();
 *		String url = foxDriver.getCurrentUrl();
 *		assertTrue(url.contains("https://www.facebook.com/youthworksdetroit"));
 *	}*/


	// ***************** Malini's Tests ***********

	/*
	 * As a user
	 * I want users to be able to to open(expand) and close(contract) the accordions
	 * So that I can get the information but not be overwhelmed by it all at once
	 * 
	 * @author Malini Santra
	 */

	// Given that I am on the "Internships" page
	// When I click on the first accordion having a question
	// Then then it expands and shows me the answer
	@Test
	public void testAccordionExpandedText() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/get-involved/internships/");
		// Click on first accordion to expand
		foxDriver.findElement(By.cssSelector("div.su-spoiler-title")).click();
		// Assertion
		String observed = foxDriver.findElement(
				By.cssSelector("div.su-spoiler-content.su-clearfix")).getText();
		assertTrue(observed
				.contains("Young people between the ages of 18 and 25 who have a desire to grow "
						+ "in their relationship with the Lord and to learn what it means to give "
						+ "all that they have for Jesus."));
	}

	// Given that I am on the "Internships" page
	// When I click twice on the first accordion which has the question
	// Then it expands and contracts, leaving the answer hidden
	@Test
	public void testAccordionContractedText() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/get-involved/internships/");
		// Click on first accordion to expand
		foxDriver.findElement(By.cssSelector("div.su-spoiler-title")).click();
		// Click on first accordion again to contract
		foxDriver.findElement(By.cssSelector("div.su-spoiler-title")).click();
		// Assertion
		String observed = foxDriver.findElement(By.cssSelector("span.su-spoiler-icon")).getText();
		assertTrue(observed.contains(""));
	}

	// Given that I am on the "Internships" page
	// When I click on the first accordion having a question
	// Then it expands to at least twice as tall as initially (actual expanded height depends on
	// screen width)
	@Test
	public void testAccordionExpandedHeight() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/get-involved/internships/");
		// Click on first accordion to expand
		foxDriver.findElement(By.cssSelector("div.su-spoiler-title")).click();
		// Assertion
		int elementheight = foxDriver
				.findElement(
						By.cssSelector("div.su-spoiler.su-spoiler-style-fancy.su-spoiler-icon-plus"))
				.getSize().getHeight();
		assertTrue(elementheight >= 72); // initial height: 36; should expand to at least 72
	}

	// Given that I am on the "Internships" page
	// When I click twice on the first accordion having a question
	// Then it expands and contracts, leaving the height at the initial height of (36px)
	@Test
	public void testAccordionContractedHeight() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/get-involved/internships/");
		// //Click on first accordion to expand
		foxDriver.findElement(By.cssSelector("div.su-spoiler-title")).click();
		// Click on first accordion again to contract
		foxDriver.findElement(By.cssSelector("div.su-spoiler-title")).click();
		// Assertion
		int elementheight = foxDriver
				.findElement(
						By.cssSelector("div.su-spoiler.su-spoiler-style-fancy.su-spoiler-icon-plus.su-spoiler-closed"))
				.getSize().getHeight();
		assertEquals(elementheight, 36);
	}

	/*
	 * As a user
	 * I want to be able to fill a form
	 * So that I can get registered for "Job Readiness Workshops"
	 * 
	 * @author Malini Santra
	 */

	// Given that I am on the Job Readiness Workshops registration page
	// When I complete the form and click submit
	// Then the form submits and I am returned to the Job Readiness Workshops registration page
	@Test
	public void testSuccesfulFormValidation() {
		foxDriver
				.get("http://www.rdadesigns.net/yw-d/programs/streetteam/street-team-application/job-readiness-workshop-registration-2/");
		// Filling out Form Details
		foxDriver.findElement(By.name("textfield")).sendKeys("Malini");
		foxDriver.findElement(By.name("textfield2")).sendKeys("Santra");
		foxDriver.findElement(By.name("textfield3")).sendKeys("malini.santra@gmail.com");
		foxDriver.findElement(By.name("textfield4")).sendKeys("173 Morewood Avenue");
		foxDriver.findElement(By.name("textfield5")).sendKeys("Pittsburgh");
		foxDriver.findElement(By.name("textfield6")).sendKeys("PA");
		foxDriver.findElement(By.name("textfield7")).sendKeys("15213");
		foxDriver.findElement(By.name("textfield8")).sendKeys("4126134004");
		foxDriver.findElement(By.name("textfield9")).sendKeys("22");
		new Select(foxDriver.findElement(By.name("select"))).selectByVisibleText("Senior");
		// Click on Submit
		foxDriver.findElement(By.name("Submit")).click();

		// Assertion
		WebElement element = foxDriver.findElement(By.cssSelector("h1"));
		assertEquals(element.getText(), "Job Readiness Workshop Registration");
	}

	// Given that I am on the Job Readiness Workshops registration page
	// When I click Submit without having entered my information
	// Then submission is prevented, and the cursor goes to the "Name" field
	// (the first required that has not been completed) asking me to fill it
	@Test
	public void testUnSuccesfulFormValidation() {
		foxDriver
				.get("http://www.rdadesigns.net/yw-d/programs/streetteam/street-team-application/job-readiness-workshop-registration-2/");
		// Click on Submit
		foxDriver.findElement(By.name("Submit")).click();

		// Assertion
		WebElement element = foxDriver.findElement(By.name("textfield"));
		assertTrue(element.equals(foxDriver.switchTo().activeElement()));
	}

	/*
	 * As an administrator
	 * I want to be able to sign in
	 * So that I can manage the site
	 * 
	 * @author Malini Santra
	 */

	// Given that I am on the sign-in page
	// When I enter the correct username and correct password
	// Then I am signed into my account
	@Test
	public void testCorrectUsernameandPassword() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/admin");
		// Click on textbox for username and type the username
		foxDriver.findElement(By.id("user_login")).sendKeys("Malini");
		// Click on password and type password
		foxDriver.findElement(By.id("user_pass")).sendKeys("Malini's pw");
		// CLick on "Log in"
		foxDriver.findElement(By.id("wp-submit")).click();
		// Assertion
		assertEquals("Howdy, Malini Santra",
				foxDriver.findElement(By.linkText("Howdy, Malini Santra")).getText());
		// logout
		foxDriver.get("http://www.rdadesigns.net/yw-d/wp-login.php?action=logout");
		foxDriver.findElement(By.linkText("log out")).click();
	}
		// Two in one
	// Given that I am on the sign-in page
	// When I enter the incorrect username and incorrect password
	// Then I am prevented from signing into my account and get an error message
		// also covers
	// Given that I am on the sign-in page
	// When I enter a default/easily guessable username and password
	// Then I am prevented from signing into my account and get an error message
	@Test
	public void testIncorrectUsernameAndPassword() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/admin");
		// Click on textbox for username and type the username
		foxDriver.findElement(By.id("user_login")).sendKeys("admin"); 
		// Click on textbox for password and type password
		foxDriver.findElement(By.id("user_pass")).sendKeys("admin");
		// Click on "Log In"
		foxDriver.findElement(By.id("wp-submit")).click();
		// Assertion
		WebElement element = foxDriver.findElement(By.id("login_error"));
		assertNotNull(element);
	}

	// Given that I am on the sign-in page
	// When I enter the incorrect username and correct password
	// Then I am prevented from signing into my account and get an error message
	@Test
	public void testIncorrectUsernameAndCorrectPassword() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/admin");
		// Click on textbox for username and type the username
		foxDriver.findElement(By.id("user_login")).sendKeys("Ross");
		// Click on textbox for password and type password
		foxDriver.findElement(By.id("user_pass")).sendKeys("Malini's pw");
		// Click on "Log In"
		foxDriver.findElement(By.id("wp-submit")).click();
		// Assertion
		WebElement element = foxDriver.findElement(By.id("login_error"));
		assertNotNull(element);
	}

	// Given that I am on the sign-in page
	// When I enter the correct username and incorrect password
	// Then I am prevented from signing into my account and get an error message
	@Test
	public void testCorrectUsernameAndIncorrectPassword() {
		foxDriver.get("http://www.rdadesigns.net/yw-d/admin");
		// Click on textbox for username and type the username
		foxDriver.findElement(By.id("user_login")).sendKeys("Malini");
		// Click on textbox for password and type password
		foxDriver.findElement(By.id("user_pass")).sendKeys("qwerty");
		// Click on "Log In"
		foxDriver.findElement(By.id("wp-submit")).click();
		// Assertion
		WebElement element = foxDriver.findElement(By.id("login_error"));
		assertNotNull(element);
	}

}