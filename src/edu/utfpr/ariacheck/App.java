package edu.utfpr.ariacheck;

import edu.utfpr.ariacheck.locators.Locator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;

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

    private static String JS_CLEAR_TIMEOUTS =
        "for (var i = 0; i < 100000; i++) {clearTimeout(i); clearInterval(i);}";

    private static String JS_REMOVE_ANIMATIONS =
        "(function () {" +
        "    var images = document.querySelectorAll(\"img\")," +
        "        gifs = [];" +
        "    for (var i = 0; i < images.length; i++) {" +
        "        if (images[i].getAttribute(\"src\") &&" +
        "            images[i].getAttribute(\"src\").search(/(.+).gif(.*)/) === 0)" +
        "            images[i].setAttribute(\"src\", \"\");" +
        "    }" +
        "   var all_other = document.querySelectorAll(\"object,embed,applet\");" +
        "   for (var j = 0; j < all_other.length; j++) {" +
        "       if (all_other[j])" +
        "           all_other[j].parentElement.removeChild(all_other[j]);" +
        "   }" +
        "   var videos = document.querySelectorAll(\"video\");" +
        "   for (var h = 0; h < videos.length; h++) {" +
        "       videos[h].pause();" +
        "   }" +
        "}());";

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
        this.remove_all_animations();

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
            try {
                if (element.isDisplayed()) {
                    activator_html = element.getAttribute("outerHTML");
                    widget = this.locator.find_widget(element);
                } if (widget != null) {
                    Map <String, String> widget_map = new HashMap <String, String> ();
                    List <WebElement> possible_new_elements = widget.findElements(By.cssSelector("*"));
                    widget_map.put("activator", activator_html);
                    widget_map.put("widget", widget.getAttribute("outerHTML")
                                                   .replaceAll("old_mutation", "")
                                                   .trim());
                    results.add(widget_map);
                    int previous_size = elements.size();
                    elements.addAll(i + 1, possible_new_elements);
                    end += (elements.size() - previous_size);
                }
            } catch (StaleElementReferenceException ex) {}
        }

        return results;
    }

    public void remove_all_animations () {
        this.executor.executeScript(App.JS_REMOVE_ANIMATIONS);
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

