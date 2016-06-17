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
        "    'use strict';" +
        "    /**" +
        "     * Checks if a DOM element is visible. Takes into" +
        "     * consideration its parents and overflow." +
        "     *" +
        "     * @param (el)      the DOM element to check if is visible" +
        "     *" +
        "     * These params are optional that are sent in recursively," +
        "     * you typically won't use these:" +
        "     *" +
        "     * @param (t)       Top corner position number" +
        "     * @param (r)       Right corner position number" +
        "     * @param (b)       Bottom corner position number" +
        "     * @param (l)       Left corner position number" +
        "     * @param (w)       Element width number" +
        "     * @param (h)       Element height number" +
        "     */" +
        "    function _isVisible(el, t, r, b, l, w, h) {" +
        "        var p = el.parentNode," +
        "                VISIBLE_PADDING = 2;" +
        "        if ( !_elementInDocument(el) ) {" +
        "            return false;" +
        "        }" +
        "        //-- Return true for document node" +
        "        if ( 9 === p.nodeType ) {" +
        "            return true;" +
        "        }" +
        "        //-- Return false if our element is invisible" +
        "        if (" +
        "             '0' === _getStyle(el, 'opacity') ||" +
        "             'none' === _getStyle(el, 'display') ||" +
        "             'hidden' === _getStyle(el, 'visibility')" +
        "        ) {" +
        "            return false;" +
        "        }" +
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
        "        //-- If we have a parent, let's continue:" +
        "        if ( p ) {" +
        "            //-- Check if the parent can hide its children." +
        "            if ( ('hidden' === _getStyle(p, 'overflow') || 'scroll' === _getStyle(p, 'overflow')) ) {" +
        "                //-- Only check if the offset is different for the parent" +
        "                if (" +
        "                    //-- If the target element is to the right of the parent elm" +
        "                    l + VISIBLE_PADDING > p.offsetWidth + p.scrollLeft ||" +
        "                    //-- If the target element is to the left of the parent elm" +
        "                    l + w - VISIBLE_PADDING < p.scrollLeft ||" +
        "                    //-- If the target element is under the parent elm" +
        "                    t + VISIBLE_PADDING > p.offsetHeight + p.scrollTop ||" +
        "                    //-- If the target element is above the parent elm" +
        "                    t + h - VISIBLE_PADDING < p.scrollTop" +
        "                ) {" +
        "                    //-- Our target element is out of bounds:" +
        "                    return false;" +
        "                }" +
        "            }" +
        "            //-- Add the offset parent's left/top coords to our element's offset:" +
        "            if ( el.offsetParent === p ) {" +
        "                l += p.offsetLeft;" +
        "                t += p.offsetTop;" +
        "            }" +
        "            //-- Let's recursively check upwards:" +
        "            return _isVisible(p, t, r, b, l, w, h);" +
        "        }" +
        "        return true;" +
        "    }" +
        "    //-- Cross browser method to get style properties:" +
        "    function _getStyle(el, property) {" +
        "        if ( window.getComputedStyle ) {" +
        "            return document.defaultView.getComputedStyle(el,null)[property];" +
        "        }" +
        "        if ( el.currentStyle ) {" +
        "            return el.currentStyle[property];" +
        "        }" +
        "    }" +
        "    function _elementInDocument(element) {" +
        "        while (element = element.parentNode) {" +
        "            if (element == document) {" +
        "                    return true;" +
        "            }" +
        "        }" +
        "        return false;" +
        "    }" +
        "    return _isVisible(this);" +
        "};";

    public WidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions) {
        this.driver = driver;
        this.actions = actions;
        this.executor = executor;
        this.invisibles = null;
        this.executor.executeScript(WidgetLocator.JS_ISVISIBLE_METHOD);
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

    public WebElement find_widget (WebElement target) {
        List <WebElement> mutation_widgets;
        WebElement potential_widget = null;

        if (this.target_cache.indexOf(target) >= 0)
            return null;
        this.target_cache.add(target);

        this.executor.executeScript(WidgetLocator.JS_SET_MUTATION_OBSERVER);

        if (target.getSize().getWidth() > this.MAX_WIDTH || target.getSize().getHeight() > this.MAX_HEIGHT)
            return null;

        if (this.invisibles == null)
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
        return this.getVisibilityChanges();
    }

    private WebElement getVisibilityChanges () {
        WebElement potential_widget = null;
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
}
