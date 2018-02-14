package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class WidgetLocator implements Locator {

    private WebDriver driver;
    private JavascriptExecutor executor;
    private Actions actions;
    private List <WebElement> invisibles = null;
    private TakesScreenshot takes = null;
    private List <WebElement> target_cache = new ArrayList <WebElement> ();

    private int MAX_WIDTH = 300;
    private int MAX_HEIGHT = 100;
    private double SIG_DIFFERENCE = 1.2;

    private static String JS_SET_MUTATION_OBSERVER =
        "if ( ! window.observer) {" +
        "    var real_setTimeout = window.setTimeout;" +
        "    for (var i = 0; i < 10000; i++) { clearTimeout(i); clearInterval(i); };" +
        "    window.observer = new MutationObserver(function (mutations) {" +
        "        mutations.forEach(function (mutation) {" +
        "            if (mutation.target.className.includes(\"mutation_widget\") == false &&" +
        "                mutation.target.className.includes(\"old_mutation\") == false) {" +
        "                mutation.target.className += \" mutation_widget\";" +
        "            }" +
        "        });" +
        "    });" +
        "    window.observer.observe(document.body, {childList: true, subtree: true, attributes: true, attributeOldValue: true});" +
        "    document.body.addEventListener('mouseover', function (ev) { " +
        "      var aux = ev.target; " +
        "      while(aux != null) { " +
        "        if (aux.className.search('aria-check-hovered') === -1) " +
        "          aux.className += ' aria-check-hovered'; " +
        "        aux = aux.parentElement; " +
        "      } " +
        "    }, true); " +
        "}";
    private static String JS_CLEAN_MUTATION_RECORDS =
        "var mutation_widget = document.querySelectorAll(\".mutation_widget\");" +
        "for (var i = 0; i < mutation_widget.length; i++)" +
        "    mutation_widget[i].className = mutation_widget[i].className" +
        "                                           .replace(\"mutation_widget\",\"old_mutation\");";

    public WidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions) {
        this.driver = driver;
        this.actions = actions;
        this.executor = executor;
        this.invisibles = null;
    }

    public WidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions, TakesScreenshot takes) {
        this.driver = driver;
        this.actions = actions;
        this.executor = executor;
        this.invisibles = null;
        this.takes = takes;
    }

    private List <WebElement> find_invisibles () {
        List <WebElement> child_elements = this.driver.findElements(By.cssSelector("body *"));
        List <WebElement> invisibles = new ArrayList <WebElement> ();
        List <WebElement> inv_childs;
        WebElement child;

        for (int i = 0; i < child_elements.size(); i++) {
            child = child_elements.get(i);
            if ( ! child.isDisplayed()) {
                invisibles.add(child);
                inv_childs = child.findElements(By.cssSelector("*"));
                invisibles.addAll(inv_childs);
                i += inv_childs.size();
            }
        }
        return invisibles;
    }

    public List<WebElement> find_widget (WebElement target) {
        List <WebElement> mutation_widgets;
            WebElement potential_widget = null;
        File before = null,
             later = null;

        if (this.target_cache.indexOf(target) >= 0)
            return null;
        this.target_cache.add(target);

        if (target.getAttribute("className") != null && target.getAttribute("className").contains("aria-check-hovered"))
            return null;

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        if (target.getSize().getWidth() > this.MAX_WIDTH || target.getSize().getHeight() > this.MAX_HEIGHT ||
                target.getSize().getWidth() == 0 || target.getSize().getHeight() == 0)
            return null;

        if (this.takes != null)
            before = this.takes.getScreenshotAs(OutputType.FILE);

        try {
            this.actions.moveToElement(target)
                        .build()
                        .perform();
        } catch (MoveTargetOutOfBoundsException ex) {
            ex.printStackTrace();
            return null;
        } catch (StaleElementReferenceException ex) {
            ex.printStackTrace();
            return null;
        }

        if (this.takes != null) {
            later = this.takes.getScreenshotAs(OutputType.FILE);
            long result = this.compareImages(before, later);
            System.out.println("Comparisson with target: " + (result / (target.getSize().getHeight() * target.getSize().getWidth())));
            if ((result / (target.getSize().getHeight() * target.getSize().getWidth())) < this.SIG_DIFFERENCE) {
                return null;
            }
        }

        mutation_widgets = this.driver.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"));

        this.executor.executeScript(WidgetLocator.JS_CLEAN_MUTATION_RECORDS);
        if (mutation_widgets != null)
            return mutation_widgets;

        return mutation_widgets;
    }

    public long compareImages (File before, File after) {
        BufferedImage img1 = null,
                      img2 = null;
        try {
            img1 = ImageIO.read(before);
            img2 = ImageIO.read(after);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width1 = img1.getWidth(null),
            width2 = img2.getWidth(null),
            height1 = img1.getHeight(null),
            height2 = img2.getHeight(null),
            diff1_x = -1, diff2_x = -1, diff1_y = -1, diff2_y = -1;
        if ((width1 != width2) || (height1 != height2)) {
            return 100;
        }
        long diff = 0;
        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                int rgb1 = img1.getRGB(x, y),
                    rgb2 = img2.getRGB(x, y);
                if (rgb1 != rgb2) { diff++; }
            }
        }
        return diff;
    }
}
