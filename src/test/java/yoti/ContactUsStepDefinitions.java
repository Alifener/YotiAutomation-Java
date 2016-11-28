package yoti;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContactUsStepDefinitions {

    WebDriver webdriver;

    String baseUrl = "https://www.yoti.com/";

    private FirefoxProfile firefoxProfile() throws Throwable {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        return firefoxProfile;
    }

    @Before
    public void beforeScenario() throws Throwable {

        webdriver = new FirefoxDriver(firefoxProfile());
        webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void afterScenario() {
        webdriver.close();
    }

    @Given("^I go to Yoti web site$")
    public void iGoToYotiWebSite() throws Throwable {
        webdriver.get(baseUrl);
    }

    @When("^I click on the menu$")
    public void iClickOnTheMenu() throws Throwable {
        WebElement menuButton = webdriver.findElement(By.cssSelector(".hamburger"));
        menuButton.click();
    }

    @Then("^I should be able to see Contact us in the opened menu$")
    public void iShouldBeAbleToSeeContactUsInTheOpenedMenu() throws Throwable {
        WebElement contactUsValue = webdriver.findElement(By.cssSelector("#yoti-menu-sidebar .parent[href='/contact']"));
        assertTrue("Verification Failed: Contact us element is not being displayed",contactUsValue.isDisplayed());
    }

    @When("^I click on Contact us$")
    public void iClickOnContactUs() throws Throwable {
        webdriver.findElement(By.cssSelector("#yoti-menu-sidebar .parent[href='/contact'] span")).click();
    }

    @Then("^I should be able to see following questions$")
    public void iShouldBeAbleToSeeFollowingQuestions(DataTable questions) throws Throwable {
      WebElement questionUs=  webdriver.findElement(By.cssSelector(".container-fluid #lets-talk-section"));
        assertEquals("Bulamadim", questions.raw().get(0).get(0), questionUs.getText());
        WebElement businessQ=  webdriver.findElement(By.cssSelector(".option.bg-primary-1.dark-bg.row"));
        assertEquals("Bulamadim2", questions.raw().get(1).get(0), businessQ.getText());
     }

    @When("^I expand \"([^\"]*)\" section$")
    public void iExpandSection(String whatToExpand) throws Throwable {
        if (whatToExpand.equals( "I have a question about my Yoti")) {
            WebElement elem = webdriver.findElement(By.cssSelector(".container-fluid #lets-talk-section"));
            new Actions(webdriver).moveToElement(elem).perform();
            elem.click();
        }
        else if (whatToExpand.equals( "I have a business question")) {
            WebElement elem = webdriver.findElement(By.cssSelector(".option.bg-primary-1.dark-bg.row"));
            new Actions(webdriver).moveToElement(elem).perform();
          elem.click();
        }
    }

    @And("^I submit the my Yoti form$")
    public void iSubmitTheForm() throws Throwable {

        webdriver.findElement(By.cssSelector("#contact-question-form #nameInput")).sendKeys("name:" +UUID.randomUUID().toString());
        webdriver.findElement(By.cssSelector("#contact-question-form #emailInput")).sendKeys("email."+UUID.randomUUID().toString()+"@gmail.com");
        webdriver.findElement(By.cssSelector("#contact-question-form #messageInput")).sendKeys("message:"+UUID.randomUUID().toString());
        WebElement sendButton = webdriver.findElement(By.cssSelector("#contact-question-form button"));
        sendButton.click();
        Thread.sleep(1000);
    }

    @And("^I submit the business form$")
    public void iSubmitTheBusinessForm() throws Throwable {

        webdriver.findElement(By.cssSelector("#contact-business-form #nameInput")).sendKeys("name:" +UUID.randomUUID().toString());
        webdriver.findElement(By.cssSelector("#contact-business-form #emailInput")).sendKeys("email."+UUID.randomUUID().toString()+"@gmail.com");
        webdriver.findElement(By.cssSelector("#contact-business-form #companyInput")).sendKeys("company:"+UUID.randomUUID().toString());
        webdriver.findElement(By.cssSelector("#contact-business-form #messageInput")).sendKeys("message:"+UUID.randomUUID().toString());
        WebElement sendButton = webdriver.findElement(By.cssSelector("#contact-business-form button"));
        JavascriptExecutor jse = (JavascriptExecutor)webdriver;
        jse.executeScript("window.scrollBy(0,250)", "");
        sendButton.click();
        Thread.sleep(1000);
    }

    @Then("^I should see the confirmation message$")
    public void iShouldSeeTheConfirmationMessage() throws Throwable {
        WebElement confirmationMessage = webdriver.findElement(By.id("contact-question-received"));
        assertTrue("Verification Failed: Confirmation Message is not being displayed",confirmationMessage.isDisplayed());
    }
    @Then("^I should see the business confirmation message$")
    public void iShouldSeeTheBusinessConfirmationMessage() throws Throwable {
        WebElement confirmationMessage = webdriver.findElement(By.id("contact-business-received"));
        assertTrue("Verification Failed: Confirmation Message is not being displayed",confirmationMessage.isDisplayed());
    }
}
