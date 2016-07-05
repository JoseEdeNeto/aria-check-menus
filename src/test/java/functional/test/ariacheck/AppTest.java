package functional.test.ariacheck;

import edu.utfpr.ariacheck.locators.decorators.ActivatorCacheDecorator;
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

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/sanity_check02.html");

        Locator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(driver, locator, (JavascriptExecutor) driver);
        app.set_wait(1);

        result_widget = app.find_all_widgets();
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
        assertEquals(3, result_widget.size());
        driver.quit();
    }

    @Test
    public void test_widget_locator_should_look_for_all_widget_instances_in_an_start_and_end_interval () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        List <Map<String, String>> result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/sanity_check02.html");

        Locator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(driver, locator, (JavascriptExecutor) driver);
        app.set_wait(1);

        result_widget = app.find_all_widgets(4, 7);
        assertEquals(1, result_widget.size());
        assertEquals(
        "<a id=\"link2\" href=\"#\">\n" +
"            Tooltip 2\n" +
"            <span id=\"useful2\" class=\"tooltip\">\n" +
"                <span class=\"arrow\"></span>\n" +
"                Useful message 2\n" +
"            </span>\n" +
"        </a>", result_widget.get(0).get("activator"));
        assertEquals(
            "<span id=\"useful2\" class=\"tooltip open\">\n" +
"                <span class=\"arrow\"></span>\n" +
"                Useful message 2\n" +
"            </span>", result_widget.get(0).get("widget"));
        driver.quit();
    }

    @Test
    public void test_widget_locator_should_ignore_carousels () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        List <Map <String, String>> result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/carousel_01.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(driver, locator, (JavascriptExecutor) driver);
        app.set_wait(1);

        result_widget = app.find_all_widgets();
        assertEquals(1, result_widget.size());
        assertEquals("<a id=\"link2\" href=\"#\">\n" +
                     "            Tooltip 3\n" +
                     "        </a>", result_widget.get(0).get("activator"));
        assertEquals("<div class=\" \"><span style=\"top: 30%;\" class=\"tooltip open \">Us" +
                     "eful 3<span class=\"arrow\"></span></span></div>",
                     result_widget.get(0).get("widget"));

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_ignore_carousels_which_restart () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        List <Map <String, String>> result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/carousel_02.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(driver, locator, (JavascriptExecutor) driver);
        app.set_wait(1);

        result_widget = app.find_all_widgets();
        assertEquals(1, result_widget.size());
        assertEquals("<a id=\"link2\" href=\"#\">\n" +
                     "            Tooltip 3\n" +
                     "        </a>", result_widget.get(0).get("activator"));
        assertEquals("<div class=\" \"><span style=\"top: 30%;\" class=\"tooltip open \">Us" +
                     "eful 3<span class=\"arrow\"></span></span></div>",
                     result_widget.get(0).get("widget"));

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_find_multi_level_menus () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        List <Map <String, String>> result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/multi-level-menu-01.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(driver,
                new ActivatorCacheDecorator(locator), (JavascriptExecutor) driver);
        app.set_wait(1);

        result_widget = app.find_all_widgets();
        assertEquals("<li class=\"red\">\n" +
"                Aloha 1\n" +
"                <ul>\n" +
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
"                </ul>\n" +
"            </li>" ,result_widget.get(0).get("activator"));
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
"                </ul>" ,result_widget.get(0).get("widget"));
        assertEquals("<li class=\"gray 2\">\n" +
"                        Aloha 2\n" +
"                        <ul>\n" +
"                            <li class=\"gray 3\">Something level 2</li>\n" +
"                            <li class=\"gray 4\">Something level 2</li>\n" +
"                            <li class=\"gray 5\">Something level 2</li>\n" +
"                        </ul>\n" +
"                    </li>" ,result_widget.get(1).get("activator"));
        assertEquals("<ul class=\" aria-check-hovered\">\n" +
"                            <li class=\"gray 3\">Something level 2</li>\n" +
"                            <li class=\"gray 4\">Something level 2</li>\n" +
"                            <li class=\"gray 5\">Something level 2</li>\n" +
"                        </ul>" ,result_widget.get(1).get("widget"));
        assertEquals("<li class=\"blue\">\n" +
"                Aloha 3\n" +
"                <ul>\n" +
"                    <li class=\"gray 10\">Nothing</li>\n" +
"                    <li class=\"gray 11\">Nothing</li>\n" +
"                </ul>\n" +
"            </li>", result_widget.get(2).get("activator"));
        assertEquals("<ul class=\" aria-check-hovered\">\n" +
"                    <li class=\"gray 10\">Nothing</li>\n" +
"                    <li class=\"gray 11\">Nothing</li>\n" +
"                </ul>", result_widget.get(2).get("widget"));

        driver.quit();
    }

    @Test
    public void test_widget_locator_should_find_multi_level_menus_with_mutations () throws IOException {
        FirefoxDriver driver = new FirefoxDriver();
        Actions actions = new Actions((WebDriver) driver);
        List <Map <String, String>> result_widget;

        driver.get("file://" + (new File(".").getCanonicalPath()) + "/fixture/multi-level-menu-02.html");

        WidgetLocator locator = new WidgetLocator((WebDriver) driver, (JavascriptExecutor) driver, actions);
        App app = new App(
            driver,
            new ActivatorCacheDecorator(locator),
            (JavascriptExecutor) driver
        );
        app.set_wait(1);
        result_widget = app.find_all_widgets();

        assertEquals("<li class=\"blue\"> Aloha 3 </li>", result_widget.get(0).get("activator"));
        assertEquals("<ul class=\" \"><li>aloha 3.1</li><li>aloha 3.2</li></ul>",
                result_widget.get(0).get("widget"));
        assertEquals("<li>aloha 3.2</li>", result_widget.get(1).get("activator"));
        assertEquals("<ul class=\" \"><li>aloha 3.2.1</li><li>aloha 3.2.2</li></ul>",
                result_widget.get(1).get("widget"));

        driver.quit();
    }

}
