package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.locators.Locator;
import java.awt.Color;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class ScreenshotWidgetLocatorDecorator implements Locator {

    private Locator decorable;
    private TakesScreenshot takes;
    private String folder;
    private int counter = 1;
    private String image_filetype = ".jpg";

    public void setCounter (int counter) { this.counter = counter; }

    public ScreenshotWidgetLocatorDecorator (Locator locator, TakesScreenshot takes, String folder) {
        this.decorable = locator;
        this.takes = takes;
        this.folder = folder;
    }

    public List<WebElement> find_widget (WebElement target) {
        File screenshot = this.takes.getScreenshotAs(OutputType.FILE),
             new_file;
        List<WebElement> widget = null;
        widget = this.decorable.find_widget(target);
        if (widget == null)
            return widget;
        for(int i = 0; i < widget.size(); i++){
            new_file = this.create_file_wrapper(this.folder + this.counter + "widgets/_" + i +
                                                "_before_widget" + this.image_filetype);
            try {
                this.copy_file_wrapper(screenshot, new_file);
            } catch (IOException io) {
                System.out.println("Error copying screenshot file to path: before widget");
            }
            screenshot = this.takes.getScreenshotAs(OutputType.FILE);
            new_file = this.create_file_wrapper(this.folder + this.counter + "widgets/_" + i +
                                                "_later_widget" + this.image_filetype);
            try {
                this.copy_file_wrapper(screenshot, new_file);
            } catch (IOException io) {
                System.out.println("Error copying screenshot file to path: later widget");
            }
            this.save_element_screenshot(target, screenshot, "widget_activator"+ i);
            this.save_element_screenshot(widget.get(i), screenshot, "widget" + i);
        }
        this.counter++;
        return widget;
    }

    public File create_file_wrapper (String name) {
        return new File(name);
    }

    public void copy_file_wrapper (File screenshot, File new_file) throws IOException {
        FileUtils.copyFile(screenshot, new_file);
    }

    public void set_image_filetype (String filetype) {
        this.image_filetype = filetype;
    }

    public void save_element_screenshot (WebElement target, File screenshot, String filename) {
        BufferedImage full_image = this.imageio_read_wrapper(screenshot),
                      sub_image = null;
        int left = target.getLocation().getX(),
            top = target.getLocation().getY(),
            height = target.getSize().getHeight(),
            width = target.getSize().getWidth();
        System.out.println(left);
        System.out.println(top);
        System.out.println(height);
        System.out.println(width);
        if (top < 0) {
            height = height + top;
            top = 0;
        }
        if (left < 0) {
            width = width + left;
            left = 0;
        }
        if (top >= full_image.getHeight())
            top = full_image.getHeight() - 2;
        if (left >= full_image.getWidth())
            left = full_image.getWidth() - 2;
        if (top + height >= full_image.getHeight())
            height = full_image.getHeight() - top - 1;
        if (left + width >= full_image.getWidth())
            width = full_image.getWidth() - left - 1;
        
        System.out.println(left);
        System.out.println(top);
        System.out.println(height);
        System.out.println(width);
        
        BufferedImage targetScreenshot = full_image.getSubimage(
                left,
                top,
                (width == 0 ? 1 : width),
                (height == 0 ? 1 : height));

        File file = this.create_file_wrapper(this.folder + this.counter + "widgets/_" +
                                            "_" + filename + this.image_filetype);
        this.imageio_write_wrapper(targetScreenshot, file);
    }

    public BufferedImage imageio_read_wrapper (File screenshot) {
        try {
            return ImageIO.read(screenshot);
        } catch (IOException exception) {
            System.out.println("Could not convert screenshot to BufferedImage...");
            exception.printStackTrace();
            return null;
        }
    }

    public void imageio_write_wrapper (BufferedImage buf_image, File file) {
        try {
            ImageIO.write(buf_image, "png", file);
        } catch (IOException ex) {
            System.out.println("Could not save buffered image...");
            ex.printStackTrace();
        }
    }

}
