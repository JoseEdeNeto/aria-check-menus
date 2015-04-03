package edu.utfpr.ariacheck;

import edu.utfpr.ariacheck.locators.Locator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class App {

    private Locator locator;
    private WebDriver driver;
    private JavascriptExecutor executor;
    private boolean log;

    private int wait_seconds = 20;

    private static String JS_SET_SLIDESHOW_MUTATION_OBSERVER =
        "window.slideshowObserver = new MutationObserver(function (mutations) {" +
        "   mutations.forEach(function (mutation) {" +
        "       if (mutation.target.parentElement)" +
        "           mutation.target.parentElement.removeChild(mutation.target);" +
        "   });" +
        "});" +
        "window.slideshowObserver.observe(document.body, {attributes: true, subtree: true});";

    private static String JS_REMOVE_SLIDESHOW_MUTATION_OBSERVER =
        "for (var i = 0; i < 100000; i++) {clearTimeout(i); clearInterval(i);}" +
        "window.slideshowObserver.disconnect();";

    public App (WebDriver driver, Locator locator, JavascriptExecutor executor) {
        this.driver = driver;
        this.locator = locator;
        this.executor = executor;
        this.log = false;
    }

    public App (WebDriver driver, Locator locator, JavascriptExecutor executor, boolean log) {
        this.driver = driver;
        this.locator = locator;
        this.executor = executor;
        this.log = true;
    }

    public List <Map <String, String>> find_all_widgets () {
        List <WebElement> elements = this.driver.findElements(By.cssSelector("body *"));
        return this.find_all_widgets(0, elements.size());
    }

    public List <Map <String, String>> find_all_widgets (int start, int end) {
        this.remove_slideshow();

        List <WebElement> elements = this.driver.findElements(By.cssSelector("body *"));
        List <Map <String, String>> results = new ArrayList <Map <String, String>> ();
        WebElement element;
        int count = 0;

        for (int i = start; (i < end && i < elements.size()); i++) {
            element = elements.get(i);
            if (this.log)
                System.out.println("Examining " + (++count) + " of " + elements.size() + " remaining elements...");
            WebElement widget = null;
            String activator_html = null;
            if (element.isDisplayed()) {
                activator_html = element.getAttribute("outerHTML");
                widget = this.locator.find_widget(element);
            } if (widget != null) {
                Map <String, String> widget_map = new HashMap <String, String> ();
                widget_map.put("activator", activator_html);
                widget_map.put("widget", widget.getAttribute("outerHTML"));
                results.add(widget_map);
            }
        }

        return results;
    }

    public void remove_slideshow () {
        this.executor.executeScript(App.JS_SET_SLIDESHOW_MUTATION_OBSERVER);
        this.sleep_wrapper();
        this.executor.executeScript(App.JS_REMOVE_SLIDESHOW_MUTATION_OBSERVER);
    }

    public void sleep_wrapper () {
        try {
            Thread.sleep(this.wait_seconds * 1000);
        } catch (InterruptedException e) {}
    }

    public void set_wait (int wait) {
        this.wait_seconds = wait;
    }
}

