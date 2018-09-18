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
                File dir = new File(this.directory + this.counter + "widgets/_");
                dir.getParentFile().mkdirs();
                File file = new File(dir + "subcomponents.csv");
                if (!file.exists()){
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter writer = new BufferedWriter(fw);
                    String columnNames = "Widget number,Position X,Position Y,Width,Height,NodeType,Number of Child Nodes,AverageWidth,AverageHeight,Average posX,Average posY\n";
                    writer.write(columnNames);
                    writer.close();
                }
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter writer = new BufferedWriter(fw);

                List<WebElement> childs = (List<WebElement>) js.executeScript("return arguments[0].children", result.get(i));
                        
                        //result.get(i).findElements(By.xpath(".//*"));
                int averageWidth = 0,
                    averageHeight = 0,
                    averagepX = 0,
                    averagepY = 0,
                    nItems = 0;
                for (int j = 0; j < childs.size(); j++){
                    int pX = childs.get(j).getLocation().getX();
                    int pY = childs.get(j).getLocation().getY();
                    int width = childs.get(j).getSize().getWidth();
                    int height = childs.get(j).getSize().getHeight();
                    String text = childs.get(j).getText();
                    String type;
                    if (text.equals("")){
                        type = "text";
                    } else {
                        type = "element";
                        averageWidth += width;
                        averageHeight += height;
                        averagepX += pX;
                        averagepY += pY;
                        nItems++;
                    }
                    Long numberChildNodes =(Long) js.executeScript("return arguments[0].childElementCount", childs.get(j));
                    
                    StringBuilder builder = new StringBuilder();
                    builder .append(String.format("%03d", i + 1)+",")
                            .append(pX + ",")
                            .append(pY + ",")
                            .append(width + ",")
                            .append(height + ",")
                            .append(type + ",")
                            .append(numberChildNodes + ",");
                    if (nItems != 0){
                        builder .append(averageWidth / nItems + ",")
                                .append(averageHeight / nItems + ",")
                                .append(averagepX / nItems + ",")
                                .append(averagepY / nItems + ",");
                    } else {
                        builder.append("," + "," + "," + ",");
                    }
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