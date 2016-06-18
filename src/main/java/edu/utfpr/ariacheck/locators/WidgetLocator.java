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
    private static String JS_ISVISIBLE_METHOD =
        "/**" +
        " * Author: Jason Farrell" +
        " * Author URI: http://useallfive.com/" +
        " *" +
        " * Description: Checks if a DOM element is truly visible." +
        " * Package URL: https://github.com/UseAllFive/true-visibility" +
        " */" +
        "Element.prototype.isVisible = function() {" +
        "    function _isVisible(el, t, r, b, l, w, h) {" +
        "        var p = el.parentNode," +
        "                VISIBLE_PADDING = 2;" +
        "        if ( !_elementInDocument(el) ) {" +
        "            return false;" +
        "        }" +
        "        if ( 9 === p.nodeType ) {" +
        "            return true;" +
        "        }" +
        "        if (" +
        "             '0' === _getStyle(el, 'opacity') ||" +
        "             'none' === _getStyle(el, 'display') ||" +
        "             'hidden' === _getStyle(el, 'visibility')" +
        "        ) {" +
        "            return false;" +
        "        }" +
        "" +
        "        if (" +
        "            'undefined' === typeof(t) ||" +
        "            'undefined' === typeof(r) ||" +
        "            'undefined' === typeof(b) ||" +
        "            'undefined' === typeof(l) ||" +
        "            'undefined' === typeof(w) ||" +
        "            'undefined' === typeof(h)" +
        "        ) {" +
        "            t = el.offsetTop;" +
        "            l = el.offsetLeft;" +
        "            b = t + el.offsetHeight;" +
        "            r = l + el.offsetWidth;" +
        "            w = el.offsetWidth;" +
        "            h = el.offsetHeight;" +
        "        }" +
        "        if ( p ) {" +
        "            if ( ('hidden' === _getStyle(p, 'overflow') || 'scroll' === _getStyle(p, 'overflow')) ) {" +
        "                if (" +
        "                    l + VISIBLE_PADDING > p.offsetWidth + p.scrollLeft ||" +
        "                    l + w - VISIBLE_PADDING < p.scrollLeft ||" +
        "                    t + VISIBLE_PADDING > p.offsetHeight + p.scrollTop ||" +
        "                    t + h - VISIBLE_PADDING < p.scrollTop" +
        "                ) {" +
        "                    return false;" +
        "                }" +
        "            }" +
        "            if ( el.offsetParent === p ) {" +
        "                l += p.offsetLeft;" +
        "                t += p.offsetTop;" +
        "            }" +
        "            return _isVisible(p, t, r, b, l, w, h);" +
        "        }" +
        "        return true;" +
        "    }" +
        "" +
        "    function _getStyle(el, property) {" +
        "        if ( window.getComputedStyle ) {" +
        "            return document.defaultView.getComputedStyle(el,null)[property];" +
        "        }" +
        "        if ( el.currentStyle ) {" +
        "            return el.currentStyle[property];" +
        "        }" +
        "    }" +
        "" +
        "    function _elementInDocument(element) {" +
        "        while (element = element.parentNode) {" +
        "            if (element == document) {" +
        "                    return true;" +
        "            }" +
        "        }" +
        "        return false;" +
        "    }" +
        "" +
        "    return _isVisible(this);" +
        "" +
        "};";

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
        this.executor.executeScript(WidgetLocator.JS_ISVISIBLE_METHOD);
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
}
