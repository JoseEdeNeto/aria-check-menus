package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.locators.Locator;
import org.openqa.selenium.WebElement;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class WidgetInfoDecorator implements Locator {
    
    private Locator locator;
    private String directory;
    private int counter;
    
    public WidgetInfoDecorator(Locator locator, String directory){
        this.locator = locator;
        this.directory = directory;
        this.counter = 1;
    }
    
    public WebElement find_widget (WebElement target){
        WebElement result = this.locator.find_widget(target);
        if (result == null)
            return null;
        try {
            PrintWriter writer = this.new_writer_wrapper(this.directory + (String.format("%03d", this.counter)) + "_widget_position_dimension.csv");
            int pX = result.getLocation().getX();
            int pY = result.getLocation().getY();
            int width = result.getSize().getWidth();
            int height = result.getSize().getHeight();
            
            StringBuilder builder = new StringBuilder();
            String columnNames = "Position X,Position Y,Width,Height";
            builder.append(columnNames+'\n');
            builder.append(pX+",");
            builder.append(pY+",");
            builder.append(width+",");
            builder.append(height+",");
            builder.append('\n');
            
            writer.print(builder.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + this.directory + (String.format("%03d", this.counter)) +
                    "_widget_position_dimension.csv was not found or cannot be writer...");
        }
        this.counter++;
        return result;
    }
    
    public PrintWriter new_writer_wrapper (String filename) throws FileNotFoundException {
        return new PrintWriter(filename);
    }
}
