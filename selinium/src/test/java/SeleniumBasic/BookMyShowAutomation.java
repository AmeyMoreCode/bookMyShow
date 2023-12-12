package SeleniumBasic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public
 
class BookMyShowAutomation {

    public static void main(String[] args) throws Exception {

        String driverPath = "your_chrome_driver_path";
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriverManager.chromedriver().setup();
        
        WebDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Open BookMyShow and handle city selection
        driver.get("https://in.bookmyshow.com/explore/home/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='city']")));
        driver.findElement(By.xpath("//*[@id='city']")).sendKeys("Bengaluru");

        // Sign In and Continue with Email
        driver.findElement(By.xpath("//*[@id='main-container']/header/div[1]/div[2]/div[1]/a")).click();
        driver.findElement(By.xpath("//*[@id='loginForm']/div[1]/div[2]/a")).click();

        // Enter email and click Continue
        driver.findElement(By.xpath("//*[@id='email']")).sendKeys("selauto@yopmail.com");
        driver.findElement(By.xpath("//*[@id='loginForm']/div[2]/div/button")).click();

        // Fetch OTP from Yopmail API
        URL url = new URL("https://api.apivoid.com/v1/email/info/SELAUTO/yopmail.com");
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        		String response = reader.readLine();

        Pattern pattern = Pattern.compile("\"otp\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            String otp = matcher.group(1);

            // Enter OTP and click Submit
            driver.findElement(By.xpath("//*[@id='otp']")).sendKeys(otp);
            driver.findElement(By.xpath("//*[@id='loginForm']/div[3]/div/button")).click();

            // Validate successful login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='main-container']/header/div[1]/div[2]/div[2]/a")));
            String text = driver.findElement(By.xpath("//*[@id='main-container']/header/div[1]/div[2]/div[2]/a")).getText();
            if (text.contains("Hi, Guest")) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
        } else {
            System.out.println("Error fetching OTP from Yopmail!");
        }

        driver.quit();
    }
}