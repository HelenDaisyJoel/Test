package hooks;

import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.ReportManager;
import utilities.ScreenRecorderUtil;

public class Hooks {

    WebDriver driver;
    Properties p;
    boolean isHeadless;

    // Initialize the driver for both regression and sanity tests
    @Before
    public void setup(Scenario scenario) throws Exception {
        driver = BaseClass.initilizeBrowser();
        p = BaseClass.getProperties();
        driver.get(p.getProperty("appURL"));
        driver.manage().window().maximize();
        
        // Check if headless mode is used
        isHeadless = BaseClass.isHeadless(); // Implement this method as needed
    }

 // Start screen recording for sanity tests if not in headless mode
    @Before("@sanity")
    public void startScreenRecording(Scenario scenario) throws Exception {
        if (!isHeadless) {
            ScreenRecorderUtil.startRecord(scenario.getName());
        }
    }

    // Stop screen recording for sanity tests if not in headless mode
    @After("@sanity")
    public void stopScreenRecording() throws Exception {
        if (!isHeadless) {
            ScreenRecorderUtil.stopRecord();
        }
    }

    // Take screenshot if a scenario fails
    @AfterStep
    public void addScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
    }

    // Quit the driver after each scenario
    @After
    public void tearDown() {
    	ReportManager.cleanOldReports(10); 
        driver.quit();
    }
}
