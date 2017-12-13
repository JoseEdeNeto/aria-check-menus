import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import edu.utfpr.ariacheck.App;
import edu.utfpr.ariacheck.cache.CacheSingleton;
import edu.utfpr.ariacheck.locators.decorators.ScreenshotWidgetLocatorDecorator;
import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.decorators.ActivatorCacheDecorator;
import edu.utfpr.ariacheck.locators.decorators.HTMLLogLocatorDecorator;
import edu.utfpr.ariacheck.locators.decorators.WidgetInfoDecorator;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.List;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


public class Main implements Runnable {
    public static void main (String[] args) throws Exception {
        /*if (args.length == 0) {
            System.out.println("Nothing to do here...");
            return ;
        }*/
        String url = "C:\\github\\aria-check-menus\\fixture\\multi-level-menu-01.html";
        int number_of_threads;
        
        System.setProperty("webdriver.chrome.driver", "C:\\github\\aria-check-menus\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(capabilities);
        
        driver.get(url);
        int size = driver.findElements(By.cssSelector("body *")).size();
        driver.quit();

        number_of_threads = size / 300 + 1;
        number_of_threads = 1;

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
  
        System.setProperty("webdriver.chrome.driver", "C:\\github\\aria-check-menus\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(capabilities);
        
        ScreenshotWidgetLocatorDecorator screenshot_decorator = new ScreenshotWidgetLocatorDecorator(
            new WidgetInfoDecorator(
                new ActivatorCacheDecorator(
                    new WidgetLocator(
                        (WebDriver) driver,
                        (JavascriptExecutor) driver,
                        new Actions(driver),
                        (TakesScreenshot) driver
                    )
                ), "captured_widgets/" + this.start + "_"
            ),
            (TakesScreenshot) driver,
            "captured_widgets/" + this.start + "_"
        );
        if (System.getProperty("os.name").equals("Linux"))
            screenshot_decorator.set_image_filetype(".png");
        Locator locator = new HTMLLogLocatorDecorator(
                screenshot_decorator, "captured_widgets/" + this.start + "_");
        driver.get(this.url);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
        driver.get(this.url);
        driver.manage().window().maximize();

        try {
            App app = new App(
                    driver, locator, (JavascriptExecutor) driver, true);
            app.find_all_widgets(this.start, this.end);
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //code to work with firefoxDriver
    /*
    System.setProperty("webdriver.gecko.driver","C:\\github\\aria-check-menus\\geckodriver.exe");
    FirefoxOptions options = new FirefoxOptions();
    options.setBinary("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    capabilities.setCapability("moz:firefoxOptions", options);
    FirefoxDriver driver = new FirefoxDriver(capabilities);
    */

}
