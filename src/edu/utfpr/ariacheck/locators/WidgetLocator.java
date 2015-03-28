package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import edu.utfpr.ariacheck.cache.CacheSingleton;

public class WidgetLocator implements Locator {

    private WebDriver driver;
    private JavascriptExecutor executor;
    private Actions actions;
    private List <WebElement> invisibles = null;
    private CacheSingleton cache = null;

    private static String JS_SET_MUTATION_OBSERVER =
        "if ( ! window.observer) {" +
        "    window.setInterval = function () {};" +
        "    for (var i = 0; i < 10000; i++) { clearTimeout(i); clearInterval(i); };" +
        "    window.setTimeout = function (callback, time) { callback(); };" +
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
        this.invisibles = null;
    }

    public WidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions, CacheSingleton cache) {
        this.driver = driver;
        this.actions = actions;
        this.executor = executor;
        this.invisibles = null;
        this.cache = cache;
    }

    private List <WebElement> find_invisibles () {
        List <WebElement> child_elements = this.driver.findElements(By.cssSelector("body *"));
        List <WebElement> invisibles = new ArrayList <WebElement> ();
        for (WebElement child : child_elements) {
            if ( ! child.isDisplayed())
                invisibles.add(child);
        }
        return invisibles;
    }

    public WebElement find_widget (WebElement target) {
        if (this.cache != null && this.cache.is_there(target.getAttribute("outerHTML")))
            return null;
        List <WebElement> mutation_widgets;
        WebElement potential_widget = null;

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        if (this.invisibles == null)
            this.invisibles = this.find_invisibles();

        this.actions.moveByOffset(-1500, -1500)
                    .moveToElement(target)
                    .build()
                    .perform();

        Iterator <WebElement>iterator = this.invisibles.iterator();
        while (iterator.hasNext()) {
            WebElement inv = (WebElement) (iterator.next());
            if (inv.isDisplayed()) {
                if (potential_widget == null || potential_widget.getAttribute("outerHTML").length() < inv.getAttribute("outerHTML").length())
                    potential_widget = inv;
                iterator.remove();
            }
        }

        mutation_widgets = this.driver.findElements(By.cssSelector(".mutation_widget"));
        this.executor.executeScript(WidgetLocator.JS_CLEAN_MUTATION_RECORDS);

        for (WebElement mutation : mutation_widgets) {
            if (potential_widget == null ||
                    potential_widget.getAttribute("outerHTML").length() < mutation.getAttribute("outerHTML").length())
                potential_widget = mutation;
        }

        if (this.cache != null)
            this.cache.store(target.getAttribute("outerHTML"));

        return potential_widget;
    }

}
