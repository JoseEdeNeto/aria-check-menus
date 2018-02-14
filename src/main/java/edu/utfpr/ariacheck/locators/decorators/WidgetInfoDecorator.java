package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.locators.Locator;
import org.openqa.selenium.WebElement;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.List;

public class WidgetInfoDecorator implements Locator {
    
    private Locator locator;
    private String directory;
    private int counter;
    
    public WidgetInfoDecorator(Locator locator, String directory){
        this.locator = locator;
        this.directory = directory;
        this.counter = 1;
    }
    
    public List<WebElement> find_widget (WebElement target){
        List<WebElement> result = this.locator.find_widget(target);
        if (result == null)
            return null;
        for(int i = 0; i < result.size(); i++){
            try {
                PrintWriter writer = this.new_writer_wrapper(this.directory + (String.format("%03d", this.counter)) + "_widget_position_dimension.csv");
                int pX = result.get(i).getLocation().getX();
                int pY = result.get(i).getLocation().getY();
                int width = result.get(i).getSize().getWidth();
                int height = result.get(i).getSize().getHeight();

                String result_html = result.get(i).getAttribute("outerHTML");
                
                StringBuilder builder = new StringBuilder();
                String columnNames = "Position X,Position Y,Width,Height,Table tag,List tag,Textbox tag,Widget name";
                builder.append(columnNames+'\n');
                builder.append(pX+",");
                builder.append(pY+",");
                builder.append(width+",");
                builder.append(height+",");
                builder.append(tablePresence(result.get(i), result_html)+",");
                builder.append(listPresence(result.get(i), result_html)+",");
                builder.append(textboxPresence(result.get(i), result_html)+",");
                builder.append(widgetNamePresence(result.get(i), result_html)+",");
                builder.append('\n');

                writer.print(builder.toString());
                writer.close();
            } catch (FileNotFoundException e) {
                System.out.println("File " + this.directory + (String.format("%03d", this.counter)) +
                        "_widget_position_dimension.csv was not found or cannot be writer...");
            }
            this.counter++;
        }
        return result;
    }
    
    public PrintWriter new_writer_wrapper (String filename) throws FileNotFoundException {
        return new PrintWriter(filename);
    }
    
    public boolean tablePresence(WebElement e, String html){
        if(html.contains("table"))
            return true;
        else
            return false;
    }
    
    public boolean listPresence(WebElement e, String html){
        if(html.contains("ul") || html.contains("li") || html.contains("ol") || 
                html.contains("dl") || html.contains("dt") || html.contains("dd"))
            return true;
        else
            return false;
    }
    
    public boolean textboxPresence(WebElement e, String html){
        if(html.contains("input type =\"text\""))
            return true;
        else
            return false;
    }
    
    public boolean widgetNamePresence(WebElement e, String className){
        if (className.equalsIgnoreCase("menu")/* or other widgets*/)
            return true;
        else
            return false;
    }
    
    public boolean datePresence(WebElement e, String html){
        if(html.contains("type =\"date\""))
            return true;
        else
            return false;
    }
}
