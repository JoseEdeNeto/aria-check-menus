package ariacheck;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class WidgetLocator {

    private WebDriver driver;
    private JavascriptExecutor executor;
    private Actions actions;

    private static String JS_SET_MUTATION_OBSERVER =
        "if ( ! window.observer) {" +
        "    window.setInterval = function () {};" +
        "    for (var i = 0; i < 10000; i++) { clearTimeout(i); clearInterval(i); };" +
        "    window.observer = new MutationObserver(function (mutations) {" +
        "        mutations.forEach(function (mutation) {" +
        "            if (mutation.addedNodes && mutation.addedNodes.length > 0 &&" +
        "                mutation.addedNodes[0].nodeType === 1 &&" +
        "                mutation.addedNodes[0].parentElement.getAttribute(\"role\") !== \"log\") {" +
        "                mutation.addedNodes[0].className += \" mutation_widget\";" +
        "            }" +
        "        });" +
        "    });" +
        "    window.observer.observe(document.body, {childList: true, subtree: true});" +
        "}";
    private static String JS_CLEAN_MUTATION_RECORDS =
        "var mutation_widget = document.querySelectorAll(\".mutation_widget\");" +
        "for (var i = 0; i < mutation_widget.length; i++)" +
        "    mutation_widget[i].className = mutation_widget[i].className.split(\"mutation_widget\").join(\"\");";

    public WidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions) {
        this.driver = driver;
        this.actions = actions;
        this.executor = executor;
    }

    public WebElement find_widget (WebElement target) {
        List <WebElement> child_elements = this.driver.findElements(By.cssSelector("body *"));
        List <WebElement> invisibles = new ArrayList <WebElement> ();
        List <WebElement> mutation_widgets = new ArrayList <WebElement> ();
        WebElement potential_widget = null;

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        for (WebElement child : child_elements) {
            if ( ! child.isDisplayed())
                invisibles.add(child);
        }

        this.actions.moveByOffset(-1500, -1500)
                    .moveToElement(target)
                    .build()
                    .perform();

        for (WebElement inv : invisibles) {
            if (inv.isDisplayed() && (potential_widget == null ||
                        potential_widget.getAttribute("outerHTML").length() < inv.getAttribute("outerHTML").length()))
                potential_widget = inv;
        }

        mutation_widgets = this.driver.findElements(By.cssSelector(".mutation_widget"));
        this.executor.executeScript(WidgetLocator.JS_CLEAN_MUTATION_RECORDS);

        for (WebElement mutation : mutation_widgets) {
            if (potential_widget == null ||
                    potential_widget.getAttribute("outerHTML").length() < mutation.getAttribute("outerHTML").length())
                potential_widget = mutation;
        }

        return potential_widget;
    }

    public List <Map <String, String>> find_all_widgets () {
        List <WebElement> elements = this.driver.findElements(By.cssSelector("body *"));
        List <Map <String, String>> results = new ArrayList <Map <String, String>> ();

        for (WebElement element : elements) {
            WebElement widget = this.find_widget(element);
            if (widget != null) {
                Map <String, String> widget_map = new HashMap <String, String> ();
                widget_map.put("activator", element.getAttribute("outerHTML"));
                widget_map.put("widget", widget.getAttribute("outerHTML"));
                results.add(widget_map);
            }
        }

        return results;
    }

}
