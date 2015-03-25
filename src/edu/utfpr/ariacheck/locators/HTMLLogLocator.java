package edu.utfpr.ariacheck.locators;

import org.openqa.selenium.WebElement;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class HTMLLogLocator implements Locator {

    private Locator locator;
    private String directory;
    private int counter;

    public HTMLLogLocator (Locator locator, String log_directory) {
        this.locator = locator;
        this.directory = log_directory;
        this.counter = 1;
    }

    public WebElement find_widget (WebElement target) {
        WebElement result = this.locator.find_widget(target);
        if (result == null)
            return null;
        try {
            PrintWriter writer = this.new_writer_wrapper(this.directory + (String.format("%03d", this.counter)) + "_widget_activator.txt");
            writer.print(target.getAttribute("outerHTML"));
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + this.directory + (String.format("%03d", this.counter)) +
                    "_widget_activator.txt was not found or cannot be writer...");
        }
        try {
            PrintWriter writer = this.new_writer_wrapper(this.directory + (String.format("%03d", this.counter)) + "_widget_element.txt");
            writer.print(result.getAttribute("outerHTML"));
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + this.directory + (String.format("%03d", this.counter)) +
                    "_widget_element.txt was not found or cannot be writer...");
        }
        this.counter++;
        return result;
    }

    public PrintWriter new_writer_wrapper (String filename) throws FileNotFoundException {
        return new PrintWriter(filename);
    }

}
