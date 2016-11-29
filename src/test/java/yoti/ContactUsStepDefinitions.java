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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContactUsStepDefinitions {

    WebDriver webdriver;
    String baseUrl = "https://www.yoti.com/";
    String myYotiForm = "my Yoti";
    String businessForm = "business";
    int timeoutSecs = 5;

    private FirefoxProfile firefoxProfile() throws Throwable {
        return new FirefoxProfile();
    }

    @Before
    public void beforeScenario() throws Throwable {
        webdriver = new FirefoxDriver(firefoxProfile());
        webdriver.manage().timeouts().implicitlyWait(timeoutSecs, TimeUnit.SECONDS);
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
        assertTrue("Contact us element is not being displayed",contactUsValue.isDisplayed());
    }

    @When("^I click on Contact us$")
    public void iClickOnContactUs() throws Throwable {
        webdriver.findElement(By.cssSelector("#yoti-menu-sidebar .parent[href='/contact'] span")).click();
    }

    @Then("^I should be able to see following questions$")
    public void iShouldBeAbleToSeeFollowingQuestions(DataTable questions) throws Throwable {
        WebElement myYotiQuestionElem=  webdriver.findElement(By.cssSelector(".container-fluid #lets-talk-section"));
        String myYotiQuestionExp = questions.raw().get(0).get(0);
        assertEquals("Cannot find " + myYotiQuestionExp, myYotiQuestionExp, myYotiQuestionElem.getText());

        WebElement businessQuestionElem=  webdriver.findElement(By.cssSelector(".option.bg-primary-1.dark-bg.row"));
        String businessQuestionExp = questions.raw().get(1).get(0);
        assertEquals("Cannot find " + businessQuestionExp, businessQuestionExp, businessQuestionElem.getText());
     }

    @When("^I expand \"([^\"]*)\" section$")
    public void iExpandSection(String whatToExpand) throws Throwable {
        if (whatToExpand.equals("I have a question about my Yoti")) {
            WebElement elem = webdriver.findElement(By.cssSelector(".container-fluid #lets-talk-section"));
            new Actions(webdriver).moveToElement(elem).perform(); // had to move to element due to 13inch Mac book view
            elem.click();
        }
        else if (whatToExpand.equals("I have a business question")) {
            WebElement elem = webdriver.findElement(By.cssSelector(".option.bg-primary-1.dark-bg.row"));
            new Actions(webdriver).moveToElement(elem).perform();
          elem.click();
        }
    }

    @And("^I submit the \"([^\"]*)\" form$")
    public void iSubmitTheForm(String typeForm) throws Throwable {
        String locatorForm="";

        if(typeForm.equals(myYotiForm)){
            locatorForm="#contact-question-form";
        }
        else if(typeForm.equals(businessForm)){
            locatorForm="#contact-business-form";
            JavascriptExecutor jse = (JavascriptExecutor) webdriver;
            jse.executeScript("window.scrollBy(0,150)", ""); // screen view doesn't fit 13 inch Mac Book
        }
        webdriver.findElement(By.cssSelector(locatorForm +" #nameInput")).sendKeys("name:" + UUID.randomUUID().toString());
        webdriver.findElement(By.cssSelector(locatorForm +" #emailInput")).sendKeys("email."+UUID.randomUUID().toString()+"@gmail.com");
        if(typeForm.equals(businessForm)){
            webdriver.findElement(By.cssSelector(locatorForm +" #companyInput")).sendKeys("company:"+ UUID.randomUUID().toString());
        }
        webdriver.findElement(By.cssSelector(locatorForm +" #messageInput")).sendKeys("message:"+UUID.randomUUID().toString());
        WebElement sendButton = webdriver.findElement(By.cssSelector(locatorForm +" button"));
        sendButton.click();
    }

    @Then("^I should see the confirmation message for \"([^\"]*)\" form$")
    public void iShouldSeeTheConfirmationMessageForForm(String typeForm) throws Throwable {
        String locatorConfMsg="";

        if(typeForm.equals(myYotiForm)){
            locatorConfMsg="contact-question-received";
        } else if (typeForm.equals(businessForm))
            locatorConfMsg="contact-business-received";

        WebDriverWait wait = new WebDriverWait(webdriver, timeoutSecs, 200);
        WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOf(webdriver.findElement(By.id(locatorConfMsg))));
        assertTrue("Confirmation Message is not being displayed for: " + typeForm, confirmationMessage.isDisplayed());
    }
}
