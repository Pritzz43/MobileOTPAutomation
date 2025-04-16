package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public class otpautomation {

        // Configurations
        private static final String OTP_REGEX = "\\b\\d{4,6}\\b"; // Change if OTP format differs
        private static final String WEB_URL = "https://example.com/otp"; // Target URL
        private static final String OTP_INPUT_XPATH = "//input[@name='otp']"; // XPath to OTP input field

        // Path to ChromeDriver (Optional if already in PATH)
        private static final String CHROME_DRIVER_PATH = "/usr/local/bin/chromedriver"; // Adjust as needed

        public static void main(String[] args) {
            try {
                // 1. Fetch OTP via ADB
                String otpCode = fetchOtpFromDevice();

                if (otpCode != null) {
                    System.out.println("Fetched OTP: " + otpCode);

                    // 2. Use Selenium to auto-fill OTP
                    fillOtpInWebForm(otpCode);

                } else {
                    System.out.println("OTP could not be fetched. Exiting.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Fetch latest OTP from SMS using ADB command.
         */
        private static String fetchOtpFromDevice() {
            String otpCode = null;
            try {
                // ADB command to pull latest SMS
                String[] command = {
                        "adb", "shell", "content", "query",
                        "--uri", "content://sms/inbox",
                        "--projection", "address,body,date",
                        "--sort", "date DESC",
                        "--limit", "5"
                };

                // Execute ADB command
                Process process = new ProcessBuilder(command).start();

                // Read command output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder smsContent = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    smsContent.append(line).append("\n");
                }
                process.waitFor();

                // Display fetched SMS
                System.out.println("Fetched SMS:\n" + smsContent);

                // Parse OTP using regex
                Pattern pattern = Pattern.compile(OTP_REGEX);
                Matcher matcher = pattern.matcher(smsContent.toString());

                if (matcher.find()) {
                    otpCode = matcher.group(0); // Get first OTP match
                }

            } catch (Exception e) {
                System.out.println("Error fetching OTP via ADB: " + e.getMessage());
            }
            return otpCode;
        }

        /**
         * Use Selenium to auto-fill OTP into a web form.
         */
        private static void fillOtpInWebForm(String otpCode) {
            // Set ChromeDriver system property
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

            // Initialize WebDriver
            WebDriver driver = new ChromeDriver();

            try {
                // Open web page
                driver.get(WEB_URL);
                System.out.println("Opened Web URL: " + WEB_URL);

                // Find OTP input field
                WebElement otpField = driver.findElement(By.xpath(OTP_INPUT_XPATH));

                // Fill OTP
                otpField.clear();
                otpField.sendKeys(otpCode);
                System.out.println("Entered OTP successfully.");

                // Optionally submit form (simulate Enter key)
                otpField.submit();
                System.out.println("OTP submitted.");

                // Keep browser open for observation (Optional: Thread.sleep to pause)
                Thread.sleep(10000); // Pause 10 sec

            } catch (Exception e) {
                System.out.println("Error in Selenium automation: " + e.getMessage());
            } finally {
                // Close browser
                driver.quit();
            }
        }
    }

