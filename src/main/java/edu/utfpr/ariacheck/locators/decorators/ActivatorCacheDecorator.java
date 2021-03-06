package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.cache.CacheSingleton;
import java.util.List;

import org.openqa.selenium.WebElement;

public class ActivatorCacheDecorator implements Locator {

    private Locator locator;
    private CacheSingleton cache;

    public ActivatorCacheDecorator (Locator locator) {
        this.locator = locator;
        this.cache = CacheSingleton.createInstance();
    }

    public List<WebElement> find_widget (WebElement target) {
        List<WebElement> potential_widget = null;
        if (this.cache.is_there(target.getAttribute("outerHTML")))
            return null;
        potential_widget = this.locator.find_widget(target);
        if (potential_widget != null)
            this.cache.store(target.getAttribute("outerHTML"));
        return potential_widget;
    }

}
