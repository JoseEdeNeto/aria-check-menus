package functional.test.ariacheck;

import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.App;

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

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class AppTest {

    @Test
    public void test_widget_locator_should_look_for_all_widget_instances_in_a_webpage () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        List <Map<String, String>> result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/test/fixture/sanity_check02.html");

        Locator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(driver, locator);

        result_widget = app.find_all_widgets();
        assertEquals(3, result_widget.size());
        assertEquals(
        "<a id=\"link1\" class=\"tooltip_link\" href=\"#\">\n" +
"            Tooltip 1\n" +
"            <span class=\"tooltip\">\n" +
"                <span class=\"arrow\"></span>\n" +
"                    Useful message 1\n" +
"            </span>\n" +
"        </a>", result_widget.get(0).get("activator"));
        assertEquals(
            "<span class=\"tooltip\">\n" +
"                <span class=\"arrow\"></span>\n" +
"                    Useful message 1\n" +
"            </span>", result_widget.get(0).get("widget"));
        assertEquals(
        "<a id=\"link2\" href=\"#\">\n" +
"            Tooltip 2\n" +
"            <span id=\"useful2\" class=\"tooltip\">\n" +
"                <span class=\"arrow\"></span>\n" +
"                Useful message 2\n" +
"            </span>\n" +
"        </a>", result_widget.get(1).get("activator"));
        assertEquals(
            "<span id=\"useful2\" class=\"tooltip open\">\n" +
"                <span class=\"arrow\"></span>\n" +
"                Useful message 2\n" +
"            </span>", result_widget.get(1).get("widget"));
        assertEquals(
        "<a id=\"link3\" href=\"#\">\n" +
"            Tooltip 3\n" +
"        </a>",
                result_widget.get(2).get("activator"));
        assertEquals("<span class=\"tooltip open \">Useful 3<span class=\"arrow\"></span></span>",
                result_widget.get(2).get("widget"));
        driver.quit();
    }

}
