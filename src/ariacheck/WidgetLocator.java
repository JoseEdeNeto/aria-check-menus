package ariacheck;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.ArrayList;

public class WidgetLocator {

    private WebDriver driver;
    private Actions actions;

    public WidgetLocator (WebDriver driver, Actions actions) {
        this.driver = driver;
        this.actions = actions;
    }

    public WebElement find_widget (WebElement target) {
        List <WebElement> child_elements = this.driver.findElements(By.cssSelector("body *"));
        List <WebElement> invisibles = new ArrayList <WebElement> ();

        for (WebElement child : child_elements) {
            if ( ! child.isDisplayed())
                invisibles.add(child);
        }

        this.actions.moveByOffset(-1500, -1500)
                    .moveToElement(target)
                    .build()
                    .perform();

        for (WebElement inv : invisibles) {
            if (inv.isDisplayed())
                return inv;
        }

        return child_elements.get(0);
    }

}
