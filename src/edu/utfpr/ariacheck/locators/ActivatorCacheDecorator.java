package edu.utfpr.ariacheck.locators;

import edu.utfpr.ariacheck.cache.CacheSingleton;

import org.openqa.selenium.WebElement;

public class ActivatorCacheDecorator implements Locator {

    private Locator locator;
    private CacheSingleton cache;

    public ActivatorCacheDecorator (Locator locator) {
        this.locator = locator;
        this.cache = CacheSingleton.createInstance();
    }

    public WebElement find_widget (WebElement target) {
        WebElement potential_widget = null;
        if (this.cache.is_there(target.getAttribute("outerHTML")))
            return null;
        potential_widget = this.locator.find_widget(target);
        if (potential_widget != null)
            this.cache.store(target.getAttribute("outerHTML"));
        return potential_widget;
    }

}
