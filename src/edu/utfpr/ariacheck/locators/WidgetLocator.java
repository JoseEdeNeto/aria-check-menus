package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.OutputType;

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

    private int MAX_WIDTH = 300;
    private int MAX_HEIGHT = 100;
    private double SIG_DIFFERENCE = 0.6;

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
        for (WebElement child : child_elements) {
            if ( ! child.isDisplayed())
                invisibles.add(child);
        }
        return invisibles;
    }

    public WebElement find_widget (WebElement target) {
        List <WebElement> mutation_widgets;
        WebElement potential_widget = null;
        File before = null,
             later = null;

        if (target.getSize().getWidth() > this.MAX_WIDTH || target.getSize().getHeight() > this.MAX_HEIGHT)
            return null;

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        if (this.invisibles == null)
            this.invisibles = this.find_invisibles();

        if (this.takes != null)
            before = this.takes.getScreenshotAs(OutputType.FILE);

        this.actions.moveByOffset(-1500, -1500)
                    .moveToElement(target)
                    .build()
                    .perform();

        if (this.takes != null) {
            later = this.takes.getScreenshotAs(OutputType.FILE);
            if (this.compareImages(before, later) < this.SIG_DIFFERENCE)
                return null;
        }

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

        return potential_widget;
    }

    public double compareImages (File before, File after) {
        BufferedImage img1 = null,
                      img2 = null;
        try {
            img1 = ImageIO.read(new File("001_before_widget.jpg"));
            img2 = ImageIO.read(new File("001_later_widget.jpg"));
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
        double n = width1 * height1 * 3;
        double p = diff / n / 255.0;
        return (p * 100.0);
    }

}
