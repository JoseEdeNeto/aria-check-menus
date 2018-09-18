package edu.utfpr.ariacheck.locators.decorators;

import edu.utfpr.ariacheck.Calculator;
import com.google.common.io.Files;
import edu.utfpr.ariacheck.locators.Locator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;


public class SubComponentDecorator implements Locator {
    
    private Locator locator;
    private String directory;
    private int counter;
    private JavascriptExecutor js;
    private Calculator c = new Calculator();
    
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
                    String columnNames = "Widget number,Position X,Position Y,Width,Height,Number of Child Nodes,AverageWidth,AverageHeight,Average posX,Average posY,DeviationWidth, DeviationHeight, DeviationX, DeviationY\n";
                    writer.write(columnNames);
                    writer.close();
                }
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter writer = new BufferedWriter(fw);

                List<WebElement> childs = (List<WebElement>) js.executeScript("return arguments[0].children", result.get(i));
                for (WebElement child : childs){
                    List<Double> vetorWidth = new ArrayList(),
                        vetorHeight = new ArrayList(),
                        vetorX = new ArrayList(),
                        vetorY = new ArrayList();
                    int pX = child.getLocation().getX(),
                        pY = child.getLocation().getY(),
                        width = child.getSize().getWidth(),
                        height = child.getSize().getHeight();
                    Long numberChildNodes = (Long) js.executeScript("return arguments[0].childElementCount", child);
                    List<WebElement> childsInChild = (List<WebElement>) js.executeScript("return arguments[0].children", child);
                    
                    for (WebElement secondChild : childsInChild){
                        vetorWidth.add((double)secondChild.getSize().getWidth());
                        vetorHeight.add((double)secondChild.getSize().getHeight());
                        vetorX.add((double)secondChild.getLocation().getX());
                        vetorY.add((double)secondChild.getLocation().getY());
                    }
                    
                    StringBuilder builder = new StringBuilder();
                    builder .append(String.format("%03d", i + 1)+",")
                            .append(pX + ",")
                            .append(pY + ",")
                            .append(width + ",")
                            .append(height + ",")
                            .append(numberChildNodes + ",")
                            .append(c.calculaMedia(vetorWidth) + ",")
                            .append(c.calculaMedia(vetorHeight) + ",")
                            .append(c.calculaMedia(vetorX) + ",")
                            .append(c.calculaMedia(vetorY) + ",")
                            .append(c.calculaDesvioPadrao(vetorWidth) + ",")
                            .append(c.calculaDesvioPadrao(vetorHeight) + ",")
                            .append(c.calculaDesvioPadrao(vetorX) + ",")
                            .append(c.calculaDesvioPadrao(vetorY) + ",")
                            .append('\n');
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
