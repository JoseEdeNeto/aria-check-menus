import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import edu.utfpr.ariacheck.App;
import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.HTMLLogLocator;
import edu.utfpr.ariacheck.locators.ScreenshotWidgetLocator;
import edu.utfpr.ariacheck.locators.WidgetLocator;

public class Main  {
    public static void main (String[] args) {
        FirefoxDriver driver = new FirefoxDriver();
        Locator locator = new HTMLLogLocator(
            new ScreenshotWidgetLocator(
                new WidgetLocator(
                    (WebDriver) driver, (JavascriptExecutor) driver, new Actions(driver)
                ),
                (TakesScreenshot) driver,
                "captured_widgets/"
            ),
            "captured_widgets/"
        );
        driver.get("http://jqueryui.com/resources/demos/tooltip/default.html");
        App app = new App(driver, locator);
        app.find_all_widgets();
        driver.quit();
    }

}
