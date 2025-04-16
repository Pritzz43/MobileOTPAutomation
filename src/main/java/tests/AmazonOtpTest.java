package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import Pages.AmazonLoginPage;
import utils.OtpFetcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;

public class AmazonOtpTest {

    private WebDriver driver;
    private AmazonLoginPage loginPage;

    private static final String AMAZON_URL = "https://www.amazon.in";
    private static final String MOBILE_NUMBER = "9762272743";

    @BeforeClass
    public void setUp() {
        // Automatic ChromeDriver setup
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Optional implicit wait
        driver.manage().window().maximize();
        loginPage = new AmazonLoginPage(driver);
    }

    @Test(priority = 1)
    public void testAmazonOtpLogin() {
        try {
            driver.get(AMAZON_URL);
            System.out.println("Opened Amazon Homepage.");

            // ✅ Step 2: Click "Sign In" on homepage
            loginPage.clickSignInOnHomePage();
            System.out.println("Clicked Sign In button.");

            Thread.sleep(5000);
            // Step 3: Enter mobile number and continue
            loginPage.enterMobileNumber(MOBILE_NUMBER);
            loginPage.clickContinue();
            System.out.println("Mobile number submitted.");

            loginPage.clickGetOtpButton();
            System.out.println("Clicked Get OTP Button");

            // Step 4: Wait dynamically for OTP to arrive (ADB will fetch SMS when it's available)
            Thread.sleep(8000); // You may use polling in real-time for better design

            // Step 5: Fetch OTP using ADB
            String otpCode = OtpFetcher.fetchOtpFromDevice();
            Assert.assertNotNull(otpCode, "OTP could not be fetched. Test Failed!");
            System.out.println("Fetched OTP: " + otpCode);

            // Step 6: Enter OTP and submit
            loginPage.enterOtp(otpCode);
            loginPage.submitOtp();
            System.out.println("OTP submitted successfully.");

            // Optional: Verify success - this needs a valid account flow
            // Assert.assertTrue(driver.getCurrentUrl().contains("your-account"), "Login not successful!");

            Thread.sleep(8000); // Optional observation wait

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed, test finished.");
        }
    }
}
