package edu.utfpr.ariacheck;

import edu.utfpr.ariacheck.locators.WidgetLocator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class App {

    private WidgetLocator locator;
    private WebDriver driver;

    public App (WebDriver driver, WidgetLocator locator) {
        this.driver = driver;
        this.locator = locator;
    }

    public List <Map <String, String>> find_all_widgets () {
        List <WebElement> elements = this.driver.findElements(By.cssSelector("body *"));
        List <Map <String, String>> results = new ArrayList <Map <String, String>> ();

        for (WebElement element : elements) {
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

