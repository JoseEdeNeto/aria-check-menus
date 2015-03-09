package functional.test.ariacheck;

import ariacheck.WidgetLocator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.io.File;

@RunWith(JUnit4.class)
public class WidgetLocatorTest {

    @Test
    public void test_widget_locator_should_find_css_only_tooltip () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/test/fixture/sanity_check01.html");
        target = driver.findElement(By.cssSelector("#link1"));

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        result_widget = locator.find_widget(target);

        assertEquals("Useful message 1", result_widget.getText());

        target = driver.findElement(By.cssSelector("#link2"));
        result_widget = locator.find_widget(target);
        assertEquals("Useful message 2", result_widget.getText());

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_find_widget_dynamically_inserted_in_the_DOM () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/test/fixture/sanity_check02.html");
        target = driver.findElement(By.cssSelector("#link3"));

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        result_widget = locator.find_widget(target);

        assertEquals("Useful 3", result_widget.getText());

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_deal_with_multiple_mutations () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/test/fixture/multiple_mutations01.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link1")));
        assertEquals("Useful message 1", result_widget.getText());

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link2")));
        assertEquals("Useful message 2", result_widget.getText());

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link3")));
        assertEquals("Useful 3", result_widget.getText());

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_deal_with_multiple_mutations_cleaning_mutation_previously_recorded () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/test/fixture/multiple_mutations02.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link2")));
        assertEquals("Useful 3", result_widget.getText());
        assertEquals("div", result_widget.getTagName());

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link3")));
        assertEquals("Useful 4", result_widget.getText());
        assertEquals("div", result_widget.getTagName());

        driver.quit();
    }

}
