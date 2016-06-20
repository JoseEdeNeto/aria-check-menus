package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class WidgetLocator implements Locator {

    private WebDriver driver;
    private JavascriptExecutor executor;
    private Actions actions;
    private List <WebElement> invisibles = null;
    private List <WebElement> target_cache = new ArrayList <WebElement> ();

    private int MAX_TOP = 700;
    private int MAX_WIDTH = 300;
    private int MAX_HEIGHT = 100;
    private double SIG_DIFFERENCE = 0.05;

    private static String JS_SET_MUTATION_OBSERVER =
        "if ( ! window.observer) {" +
        "    var real_setTimeout = window.setTimeout;" +
        "    for (var i = 0; i < 10000; i++) { clearTimeout(i); clearInterval(i); };" +
        "    window.observer = new MutationObserver(function (mutations) {" +
        "        mutations.forEach(function (mutation) {" +
        "            if (mutation.addedNodes && mutation.addedNodes.length > 0 &&" +
        "                mutation.addedNodes[0].nodeType === 1 &&" +
        "                mutation.addedNodes[0].parentElement &&" +
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
        "    mutation_widget[i].className = mutation_widget[i].className" +
        "                                           .replace(\"mutation_widget\",\"old_mutation\");";
    private static String JS_RECORD_INVISIBLES =
        "        window.invisibles = [];" +
        "        window.all = document.querySelectorAll('body *');" +
        "        for (var i = 0; i < window.all.length; i++) {" +
        "            if (!window.all[i].isVisible())" +
        "                window.invisibles.push(window.all[i]);" +
        "        }" +
        "        return window.invisibles;";
    private static String JS_VERIFY_VISIBILITY_CHANGES =
        "        window.potential_widget = null;" +
        "        for (var i = 0; i < window.invisibles.length; i++) {" +
        "            if (window.potential_widget == null && window.invisibles[i].isVisible()) {" +
        "                window.potential_widget = window.invisibles[i];" +
        "            } else if (window.invisibles[i].isVisible() &&" +
        "                       window.invisibles[i].outerHTML.length > window.potential_widget.outerHTML.length) {" +
        "                window.potential_widget = window.invisibles[i];" +
        "            }" +
        "        }" +
        "        return window.potential_widget;";

    public WidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions) {
        this.driver = driver;
        this.actions = actions;
        this.executor = executor;
        this.invisibles = null;
        this.executor.executeScript(this.getVisibilityJS());
    }

    private List <WebElement> find_invisibles () {
        return (List <WebElement>) this.executor.executeScript(WidgetLocator.JS_RECORD_INVISIBLES);
    }

    public WebElement find_widget (WebElement target) {
        WebElement potential_widget = null;

        if (this.target_cache.indexOf(target) >= 0)
            return null;
        this.target_cache.add(target);

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        if (target.getSize().getWidth() > this.MAX_WIDTH || target.getSize().getHeight() > this.MAX_HEIGHT)
            return null;

        this.invisibles = this.find_invisibles();

        try {
            this.actions.moveToElement(target)
                        .build()
                        .perform();
        } catch (MoveTargetOutOfBoundsException ex) {
            return null;
        } catch (StaleElementReferenceException ex) {
            return null;
        }

        potential_widget = this.getMutation();
        this.executor.executeScript(WidgetLocator.JS_CLEAN_MUTATION_RECORDS);
        if (potential_widget != null)
            return potential_widget;
        return this.getVisibilityChanges();
    }

    private WebElement getMutation () {
        List <WebElement> mutation_widgets = this.driver.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"));
        WebElement potential_widget = null;

        for (WebElement mutation : mutation_widgets) {
            try {
                if ((potential_widget == null) && (mutation.getAttribute("outerHTML") != null)||
                        potential_widget.getAttribute("outerHTML").length() <
                        mutation.getAttribute("outerHTML").length())
                    potential_widget = mutation;
            } catch (StaleElementReferenceException ex) {
                System.out.println("stale exception in mutation list");
            }
        }
        return potential_widget;
    }

    private WebElement getVisibilityChanges () {
        return (WebElement) this.executor.executeScript(WidgetLocator.JS_VERIFY_VISIBILITY_CHANGES);
    }

    private String getVisibilityJS () {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(
                        new File(getClass().getClassLoader()
                                           .getResource("visibility.js")
                                           .getPath())));
            String js_content = "", aux;
            while ((aux = br.readLine()) != null) {
                js_content += aux + "\n";
            }
            br.close();
            return js_content;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
