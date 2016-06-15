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

    private List <WebElement> find_invisibles (WebElement target) {
        List <WebElement> child_elements = target.findElements(By.cssSelector("body *"));
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

    public WebElement find_widget (WebElement target) {
        List <WebElement> mutation_widgets;
        WebElement potential_widget = null;
        File before = null,
             later = null;

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        if (target.getSize().getWidth() > this.MAX_WIDTH || target.getSize().getHeight() > this.MAX_HEIGHT)
            return null;

        if (target.getLocation().getY() > this.MAX_TOP)
            return null;

        this.invisibles = this.find_invisibles(target);

        if (this.takes != null)
            before = this.takes.getScreenshotAs(OutputType.FILE);

        try {
            this.actions.moveToElement(target)
                        .build()
                        .perform();
        } catch (MoveTargetOutOfBoundsException ex) {
            return null;
        } catch (StaleElementReferenceException ex) {
            return null;
        }

        if (this.takes != null) {
            later = this.takes.getScreenshotAs(OutputType.FILE);
            double result = this.compareImages(before, later);
            if (result < this.SIG_DIFFERENCE) {
                return null;
            }
        }

        mutation_widgets = this.driver.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"));

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
        this.executor.executeScript(WidgetLocator.JS_CLEAN_MUTATION_RECORDS);
        if (potential_widget != null)
            return potential_widget;

        potential_widget = null;
        Iterator <WebElement>iterator = this.invisibles.iterator();
        List <WebElement> inv_childs = null;
        while (iterator.hasNext()) {
            WebElement inv = (WebElement) (iterator.next());
            try {
                if (inv.isDisplayed()) {
                    if ((potential_widget == null && inv.getAttribute("outerHTML") != null) ||
                            potential_widget.getAttribute("outerHTML").length() < inv.getAttribute("outerHTML").length())
                        potential_widget = inv;
                    iterator.remove();
                } else {
                    inv_childs = inv.findElements(By.cssSelector("*"));
                    for (int i = 0; i < inv_childs.size() && iterator.hasNext(); i++) {
                        iterator.next();
                        System.out.print("S");
                    }
                }
            } catch (StaleElementReferenceException ex) {
                System.out.println("stale exception in visible list");
            }
            System.out.print(".");
        }
        System.out.println("");

        return potential_widget;
    }

    public double compareImages (File before, File after) {
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
            height2 = img2.getHeight(null);
        if ((width1 != width2) || (height1 != height2)) {
            return 100;
        }
        long diff = 0;
        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >>  8) & 0xff;
                int b1 = (rgb1      ) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >>  8) & 0xff;
                int b2 = (rgb2      ) & 0xff;
                diff += Math.abs(r1 - r2);
                diff += Math.abs(g1 - g2);
                diff += Math.abs(b1 - b2);
            }
        }
        double window_size = (this.driver.manage().window().getSize().getHeight() *
                              this.driver.manage().window().getSize().getWidth());
        //double n = width1 * height1 * 3;
        double n = window_size * 3;
        double p = diff / n / 255.0;
        System.out.println("Comparisson: " + (p * 100.0));
        return (p * 100.0);
    }

}
