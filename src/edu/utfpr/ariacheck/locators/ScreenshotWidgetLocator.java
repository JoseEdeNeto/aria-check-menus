package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ScreenshotWidgetLocator implements Locator {

    private Locator decorable;
    private TakesScreenshot takes;
    private String folder;

    public ScreenshotWidgetLocator (Locator locator, TakesScreenshot takes, String folder) {
        this.decorable = locator;
        this.takes = takes;
        this.folder = folder;
    }

    public WebElement find_widget (WebElement target) {
        String new_file_path = this.folder + "001_before_widget.jpg";
        File screenshot = this.takes.getScreenshotAs(OutputType.FILE),
             new_file = this.create_file_wrapper(new_file_path);
        try {
            this.copy_file_wrapper(screenshot, new_file);
        } catch (IOException io) {
            System.out.println("Error copying screenshot file to path: " + new_file_path);
        }
        return this.decorable.find_widget(target);
    }

    public File create_file_wrapper (String name) {
        return new File(name);
    }

    public void copy_file_wrapper (File screenshot, File new_file) throws IOException {
        FileUtils.copyFile(screenshot, new_file);
    }

}
