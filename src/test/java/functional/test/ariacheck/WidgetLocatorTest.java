package functional.test.ariacheck;

import edu.utfpr.ariacheck.locators.WidgetLocator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class WidgetLocatorTest {

    @Test
    public void test_widget_locator_should_find_css_only_tooltip () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/sanity_check01.html");
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
    public void test_widget_locator_should_find_className_change_only_tooltip () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/sanity_check03.html");
        target = driver.findElement(By.cssSelector("#link1"));

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        result_widget = locator.find_widget(target);

        assertEquals("Useful message 1", result_widget.getText());
        driver.quit();
    }

    @Test
    public void test_widget_locator_should_find_widget_dynamically_inserted_in_the_DOM () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/sanity_check02.html");
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

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/multiple_mutations01.html");

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

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/multiple_mutations02.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link2")));
        assertEquals("Useful 3", result_widget.getText());
        assertEquals("div", result_widget.getTagName());

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link3")));
        assertEquals("Useful 4", result_widget.getText());
        assertEquals("div", result_widget.getTagName());

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_ignore_mutations_in_log_elements () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/multiple_mutations03.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);

        result_widget = locator.find_widget(driver.findElement(By.cssSelector("#link2")));
        assertEquals("Super Useful 3", result_widget.getText());
        assertEquals("div", result_widget.getTagName());

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_not_report_false_positive_for_centered_widget_container () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/centered_false_positive.html");
        target = driver.findElement(By.cssSelector("#false_positive"));

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        result_widget = locator.find_widget(target);

        assertEquals(null, result_widget);

        driver.quit();
    }

    @Test
    public void a_test_widget_locator_should_find_multilevel_menu_widgets () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/multi-level-menu-01.html");
        target = driver.findElement(By.cssSelector(".red"));

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        result_widget = locator.find_widget(target);

        assertEquals("<ul class=\" aria-check-hovered\">\n" +
"                    <li class=\"gray 1\">Nothing here</li>\n" +
"                    <li class=\"gray 2\">\n" +
"                        Aloha 2\n" +
"                        <ul>\n" +
"                            <li class=\"gray 3\">Something level 2</li>\n" +
"                            <li class=\"gray 4\">Something level 2</li>\n" +
"                            <li class=\"gray 5\">Something level 2</li>\n" +
"                        </ul>\n" +
"                    </li>\n" +
"                    <li class=\"gray 9\">Nothing here</li>\n" +
"                </ul>", result_widget.getAttribute("outerHTML"));

        driver.quit();
    }

    @Test
    public void Widget_locator_should_identify_widgets_based_on_visibility_and_viewport () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        WebElement target, result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/viewport_visibility01.html");
        target = driver.findElement(By.cssSelector("#menu01"));

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        result_widget = locator.find_widget(target);

        assertNotNull(result_widget);
        assertEquals("menu", result_widget.getAttribute("className"));

        driver.quit();
    }
}
