package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.locators.Locator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;


public class SubComponentDecorator implements Locator {
    
    private Locator locator;
    private String directory;
    private int counter;
    private JavascriptExecutor js;
    
    public SubComponentDecorator(Locator locator, String directory, JavascriptExecutor js){
        this.locator = locator;
        this.directory = directory;
        this.counter = 1;
        this.js = js;
    }
    
    public List<WebElement> find_widget (WebElement target){
        List<WebElement> result = this.locator.find_widget(target);
        if (result == null)
            return null;
        for(int i = 0; i < result.size(); i++){
            try{
                File dir = new File(this.directory + this.counter + "widget_components/_");
                dir.getParentFile().mkdirs();
                File file = new File(dir + "subcomponents.csv");
                if (!file.exists()){
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter writer = new BufferedWriter(fw);
                    String columnNames = "Widget number,Position X,Position Y,Width,Height,NodeType\n";
                    writer.write(columnNames);
                    writer.close();
                }
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter writer = new BufferedWriter(fw);

                    List<WebElement> childs = result.get(i).findElements(By.xpath(".//*"));
                    for (int j = 0; j < childs.size(); j++){
                        int pX = childs.get(j).getLocation().getX();
                        int pY = childs.get(j).getLocation().getY();
                        int width = childs.get(j).getSize().getWidth();
                        int height = childs.get(j).getSize().getHeight();
                        Long tipo = (Long) js.executeScript("return arguments[0].nodeType", childs.get(j));
                        StringBuilder builder = new StringBuilder();
                        builder.append(String.format("%03d", i + 1)+",");
                        builder.append(pX+",");
                        builder.append(pY+",");
                        builder.append(width+",");
                        builder.append(height+",");
                        builder.append(tipo+",");
                        builder.append('\n');
                        writer.write(builder.toString());
                    }

                writer.close();
            } catch (FileNotFoundException e) {
                System.out.println("File " + this.directory + 
                        "widget_position_dimension.csv was not found or cannot be writer...");
            } catch (IOException e){
                System.out.println("File " + this.directory + 
                        "widget_position_dimension.csv was not found or cannot be writer...");
            }
        }
        this.counter++;
        return result;
    }

}
