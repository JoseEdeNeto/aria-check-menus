package ariacheck;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;

public class WidgetLocator {

    private WebDriver driver;

    public WidgetLocator (WebDriver driver) {
        this.driver = driver;
    }

    public WebElement find_widget (WebElement target) {
        List <WebElement> child_elements = this.driver.findElements(By.cssSelector("body *"));
        return child_elements.get(0);
    }

}
