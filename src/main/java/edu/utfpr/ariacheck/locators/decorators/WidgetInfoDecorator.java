package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.locators.Locator;
import java.io.BufferedWriter;
import java.io.File;
import org.openqa.selenium.WebElement;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;

public class WidgetInfoDecorator implements Locator {
    
    private Locator locator;
    private String directory;
    private int counter;
    private JavascriptExecutor js;
    
    public WidgetInfoDecorator(Locator locator, String directory, JavascriptExecutor js){
        this.locator = locator;
        this.directory = directory;
        this.counter = 1;
        this.js = js;
    }
    
    public List<WebElement> find_widget (WebElement target){
        List<WebElement> result = this.locator.find_widget(target);
        if (result == null)
            return null;
        try {
            System.out.println(this.directory);
            File file = new File(this.directory + "widget_position_dimension.csv");
            if (!file.exists()){
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter writer = new BufferedWriter(fw);
                String columnNames = "Widget number,Position X,Position Y,Width,"
                    + "Height,Table tag,List tag,Textbox tag,Widget class,Number of Child Nodes\n";
                writer.write(columnNames);
                writer.close();
            }
            FileWriter fw = new FileWriter(file, true);

            BufferedWriter writer = new BufferedWriter(fw);
            for(int i = 0; i < result.size(); i++){
                int pX = result.get(i).getLocation().getX();
                int pY = result.get(i).getLocation().getY();
                int width = result.get(i).getSize().getWidth();
                int height = result.get(i).getSize().getHeight();
                Long numberChildNodes =(Long) js.executeScript("return arguments[0].childElementCount", result.get(i));

                String result_html = result.get(i).getAttribute("outerHTML");
                
                if (pX < 0 || pY < 0 || width < 1 || height < 1){
                    result.remove(i);
                    i--;
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append(String.format("%03d", this.counter)+",");
                    builder.append(pX+",");
                    builder.append(pY+",");
                    builder.append(width+",");
                    builder.append(height+",");
                    builder.append(tablePresence(result.get(i), result_html)+",");
                    builder.append(listPresence(result.get(i), result_html)+",");
                    builder.append(textboxPresence(result.get(i), result_html)+",");
                    builder.append(widgetNamePresence(result.get(i), result_html)+",");
                    builder.append(numberChildNodes+",");
                    builder.append('\n');
                    writer.write(builder.toString());
                }

            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + this.directory + 
                    "widget_position_dimension.csv was not found or cannot be writer...");
        } catch (IOException e){
            System.out.println("File " + this.directory + 
                    "widget_position_dimension.csv was not found or cannot be writer...");
        }
        this.counter++;
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
