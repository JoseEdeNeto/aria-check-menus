package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class ScreenshotWidgetLocator extends WidgetLocator {

    public ScreenshotWidgetLocator (WebDriver driver, JavascriptExecutor executor, Actions actions, String captured_folder) {
        super(driver, executor, actions);
    }

}
