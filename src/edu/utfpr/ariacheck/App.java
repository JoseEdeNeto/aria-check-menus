package edu.utfpr.ariacheck;

import edu.utfpr.ariacheck.locators.Locator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class App {

    private Locator locator;
    private WebDriver driver;
    private boolean log;

    public App (WebDriver driver, Locator locator) {
        this.driver = driver;
        this.locator = locator;
        this.log = false;
    }

    public App (WebDriver driver, Locator locator, boolean log) {
        this.driver = driver;
        this.locator = locator;
        this.log = true;
    }

    public List <Map <String, String>> find_all_widgets () {
        List <WebElement> elements = this.driver.findElements(By.cssSelector("body *"));
        return this.find_all_widgets(0, elements.size());
    }

    public List <Map <String, String>> find_all_widgets (int start, int end) {
        List <WebElement> elements = this.driver.findElements(By.cssSelector("body *"));
        List <Map <String, String>> results = new ArrayList <Map <String, String>> ();
        WebElement element;
        int count = 0;

        for (int i = start; i < end; i++) {
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

}

