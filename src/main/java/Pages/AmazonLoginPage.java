package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AmazonLoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By signInButton = By.id("nav-link-accountList");
    private final By mobileInput = By.xpath("//input[@id='ap_email']");
    private final By continueButton = By.id("continue");
    private final By getOTPButton = By.id("continue");
    private final By otpInput = By.id("cvf-input-code");
    private final By otpSubmitButton = By.xpath("//*[@id=\"cvf-submit-otp-button\"]/span/input");

    // Constructor
    public AmazonLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Page actions
    public void clickSignInOnHomePage() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
    }

    public void enterMobileNumber(String mobileNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mobileInput)).sendKeys(mobileNumber);
    }

    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void clickGetOtpButton(){
        wait.until(ExpectedConditions.elementToBeClickable(getOTPButton)).click();
    }

    public void enterOtp(String otp) {
        WebElement otpField = wait.until(ExpectedConditions.visibilityOfElementLocated(otpInput));
        otpField.clear();
        otpField.sendKeys(otp);
    }

    public void submitOtp() {
        wait.until(ExpectedConditions.elementToBeClickable(otpSubmitButton)).click();
    }
}
