import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import edu.utfpr.ariacheck.App;
import edu.utfpr.ariacheck.cache.CacheSingleton;
import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.HTMLLogLocator;
import edu.utfpr.ariacheck.locators.ScreenshotWidgetLocator;
import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.ActivatorCacheDecorator;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.List;

public class Main implements Runnable {
    public static void main (String[] args) throws Exception {
        String url = "http://www.google.com.br";
        int number_of_threads = 4;

        FirefoxDriver driver = new FirefoxDriver();
        driver.get(url);
        int size = driver.findElements(By.cssSelector("body *")).size();
        driver.quit();

        for (int i = 0; i < number_of_threads; i++) {
            int start = (i * (size / number_of_threads)),
                end = ((i + 1) * size / number_of_threads);
            Thread thread = new Thread(new Main(url, start, end));
            thread.start();
        }

    }

    private int start;
    private int end;
    private String url;

    public Main (String url, int start, int end) {
        this.start = start;
        this.end = end;
        this.url = url;
    }

    public void run () {
        FirefoxDriver driver = new FirefoxDriver();
        Locator locator = new HTMLLogLocator(
            new ScreenshotWidgetLocator(
                new ActivatorCacheDecorator(
                    new WidgetLocator(
                        (WebDriver) driver,
                        (JavascriptExecutor) driver,
                        new Actions(driver)
                    )
                ),
                (TakesScreenshot) driver,
                "captured_widgets/" + this.start + "_"
            ),
            "captured_widgets/" + this.start + "_"
        );
        driver.get(this.url);
        App app = new App(driver, locator, true);
        app.find_all_widgets(this.start, this.end);
        driver.quit();
    }

}
