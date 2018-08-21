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
import org.openqa.selenium.By;
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
                    + "Height,Table tag,List tag,Textbox tag,Widget class,Number of Elements,Number of Child Nodes,numberOfWordsTextNodes,numberOfTextNodes,links80percent\n";
                writer.write(columnNames);
                writer.close();
            }
            FileWriter fw = new FileWriter(file, true);

            BufferedWriter writer = new BufferedWriter(fw);
            for(int i = 0; i < result.size(); i++){
                int pX = result.get(i).getLocation().getX(),
                    pY = result.get(i).getLocation().getY(),
                    width = result.get(i).getSize().getWidth(),
                    height = result.get(i).getSize().getHeight(),
                    numberElements = result.get(i).findElements(By.cssSelector("*")).size(),
                    tablePresent = result.get(i).findElements(By.cssSelector("table")).size() > 0 ? 1: 0,
                    listPresent = result.get(i).findElements(By.cssSelector("ul")).size() > 0 ? 1: 0,
                    inputPresent = result.get(i).findElements(By.cssSelector("input")).size() > 0 ? 1: 0,
                    classNamePresent = widgetNamePresence(result.get(i)).intValue(),
                    numberOfWordsTextNodes = getWordsTextNodes(result.get(i)).intValue(),
                    numberOfTextNodes = getNumberOfTextNodes(result.get(i)).intValue(),
                    links80percent = tableUl80Percent(result.get(i)).intValue();
                Long numberChildNodes =(Long) js.executeScript("return arguments[0].childElementCount", result.get(i));

                StringBuilder builder = new StringBuilder();
                builder .append(String.format("%03d", this.counter) + ",")
                        .append(pX + ",")
                        .append(pY + ",")
                        .append(width + ",")
                        .append(height + ",")
                        .append(tablePresent + ",")
                        .append(listPresent + ",")
                        .append(inputPresent + ",")
                        .append(classNamePresent + ",")
                        .append(numberElements + ",")
                        .append(numberChildNodes + ",")
                        .append(numberOfWordsTextNodes + ",")
                        .append(numberOfTextNodes + ",")
                        .append(links80percent)
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
        this.counter++;
        return result;
    }
    
    public PrintWriter new_writer_wrapper (String filename) throws FileNotFoundException {
        return new PrintWriter(filename);
    }
    
    public Long widgetNamePresence(WebElement e){
        String script =  "var target = arguments[0]," +
                            "    childElements = target.querySelectorAll(\"*\")," +
                            "    classNames = \"\"," +
                            "    widget_name = (\"dropdown,drop-down,menu,tooltip,dialog,popup,pop-up\").split(\",\");" +
                            "for (var i = 0; i < childElements.length; i++) {" +
                            "    classNames += \" \" + childElements[i].className;" +
                            "};" +
                            "for (var j = 0; j < widget_name.length; j++) {" +
                            "    if(classNames.search(widget_name[j]) >= 0)" +
                            "        return 1;" +
                            "};" +
                            "return 0;";
        return (Long) js.executeScript(script, e);
    }

    private Long getWordsTextNodes(WebElement e) {
        String script = "var target = arguments[0]," +
                        "    aux = null, elements = []," +
                        "    number_of_words = 0;" +
                        "elements.push(target);" +
                        "while (elements.length != 0) {" +
                        "    aux = elements.pop();" +
                        "    for (var i = 0; i < aux.childNodes.length; i++) {" +
                        "        if (aux.childNodes[i].nodeType === 3) {" +
                        "            number_of_words += aux.childNodes[i].nodeValue.split(\" \").length;" +
                        "        }" +
                        "        if (aux.childNodes[i].nodeType === 1)" +
                        "            elements.push(aux.childNodes[i]);" +
                        "    }" +
                        "}" +
                        "return number_of_words;";
        return (Long) js.executeScript(script, e);
    }

    private Long getNumberOfTextNodes(WebElement e) {
        String script = "var target = arguments[0]," +
                        "    aux = null, elements = []," +
                        "    number_of_child_nodes = 0;" +
                        "elements.push(target);" +
                        "while (elements.length != 0) {" +
                        "    aux = elements.pop();" +
                        "    for (var i = 0; i < aux.childNodes.length; i++) {" +
                        "        if (aux.childNodes[i].nodeType === 3)" +
                        "            number_of_child_nodes++;" +
                        "        if (aux.childNodes[i].nodeType === 1)" +
                        "            elements.push(aux.childNodes[i]);" +
                        "    }" +
                        "}" +
                        "return number_of_child_nodes;";
        return (Long) js.executeScript(script, e);
    }

    private Long tableUl80Percent(WebElement e) {
        String script = "var target = arguments[0]," +
                        "    table = target.querySelector(\"table\")," +
                        "    ul = target.querySelector(\"ul\")," +
                        "    childs, childs2;" +
                        "if (table) {" +
                        "    childs = table.querySelectorAll(\"*\").length;" +
                        "    childs2 = table.querySelectorAll(\"a\").length;" +
                        "    if ((childs2/childs) > 0.8)" +
                        "        return 1;" +
                        "    else" +
                        "        return 0;" +
                        "}" +
                        "if (ul) {" +
                        "    childs = ul.querySelectorAll(\"*\").length;" +
                        "    childs2 = ul.querySelectorAll(\"a\").length;" +
                        "    if ((childs2/childs) > 0.8)" +
                        "        return 1;" +
                        "    else" +
                        "        return 0;" +
                        "}" +
                        "return 0;";
        return (Long) js.executeScript(script, e);
    }

    private Long proportionOfTextNodesNumber(WebElement e) {
        String script = "var target = arguments[0],\n" +
                        "    aux = null, elements = [],\n" +
                        "    number_of_numbers = 0;\n" +
                        "\n" +
                        "elements.push(target);\n" +
                        "while (elements.length != 0) {\n" +
                        "    aux = elements.pop();\n" +
                        "    for (var i = 0; i < aux.childNodes.length; i++) {\n" +
                        "        if (aux.childNodes[i].nodeType === 3) {\n" +
                        "            if (!isNaN(parseInt(aux.childNodes[i].nodeValue)))\n" +
                        "                number_of_numbers++;\n" +
                        "        }\n" +
                        "        if (aux.childNodes[i].nodeType === 1)\n" +
                        "            elements.push(aux.childNodes[i]);\n" +
                        "\n" +
                        "    }\n" +
                        "}\n" +
                        "return number_of_numbers;";
        return (Long) js.executeScript(script, e);
    }
}
