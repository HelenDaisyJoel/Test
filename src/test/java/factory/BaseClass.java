package factory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseClass {

    static WebDriver driver;
    static Properties p;
    static Logger logger;
    private static boolean isHeadless; // Add this line
    
    public static WebDriver initilizeBrowser() throws IOException {
        p = getProperties();
        String executionEnv = p.getProperty("execution_env");
        String browser = p.getProperty("browser").toLowerCase();
        String os = p.getProperty("os").toLowerCase();
        isHeadless = Boolean.parseBoolean(p.getProperty("headless")); // Set the headless mode variable

        if (executionEnv.equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            
            // OS
            switch (os) {
                case "windows":
                    capabilities.setPlatform(Platform.WINDOWS);
                    break;
                case "mac":
                    capabilities.setPlatform(Platform.MAC);
                    break;
                case "linux":
                    capabilities.setPlatform(Platform.LINUX);
                    break;
                default:
                    System.out.println("No matching OS");
                    return null;
            }
            
            // Browser
            switch (browser) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    break;
                case "edge":
                    capabilities.setBrowserName("MicrosoftEdge");
                    break;
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    break;
                default:
                    System.out.println("No matching browser");
                    return null;
            }
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
        } else if (executionEnv.equalsIgnoreCase("local")) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isHeadless) {
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--disable-gpu");
                    }
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) {
                        firefoxOptions.addArguments("--headless");
                    }
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (isHeadless) {
                        edgeOptions.addArguments("headless");
                    }
                    driver = new EdgeDriver(edgeOptions);
                    break;
                default:
                    System.out.println("No matching browser");
                    driver = null;
            }
        }
        driver.manage().deleteAllCookies(); 
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        
        return driver;
        
    }
    
    public static WebDriver getDriver() {
        return driver;
    }
    
    public static boolean isHeadless() { // Add this method
        return isHeadless;
    }

    public static Properties getProperties() throws IOException {         
        FileReader file = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties");
        p = new Properties();
        p.load(file);
        return p;
    }
    
    public static Logger getLogger() {         
        logger = LogManager.getLogger(); // Log4j
        return logger;
    }
    
    public static String randomeString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public static String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(5) + RandomStringUtils.randomNumeric(10);
    }
}
