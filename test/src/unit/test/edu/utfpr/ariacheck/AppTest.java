package unit.test.edu.utfpr.ariacheck;

import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.App;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.mockito.InOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@RunWith(JUnit4.class)
public class AppTest {

    @Test
    public void test_find_all_widgets_should_do_multiple_calls_to_find_widget () {
        WebDriver driver_mock = mock(WebDriver.class);
        Locator spy = mock(Locator.class);
        App app = new App(driver_mock, spy);

        List <Map<String, String>> result_widget;
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        List <WebElement> widget_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        widget_list.add(mock(WebElement.class));
        widget_list.add(mock(WebElement.class));
        widget_list.add(mock(WebElement.class));

        when(childs_list.get(0).isDisplayed()).thenReturn(true);
        when(childs_list.get(1).isDisplayed()).thenReturn(true);
        when(childs_list.get(2).isDisplayed()).thenReturn(true);
        when(childs_list.get(3).isDisplayed()).thenReturn(true);
        when(childs_list.get(4).isDisplayed()).thenReturn(true);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        doReturn(null).when(spy).find_widget(childs_list.get(0));
        doReturn(widget_list.get(0)).when(spy).find_widget(childs_list.get(1));
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("<a href=\"#\">activator 1</a>");
        when(widget_list.get(0).getAttribute("outerHTML")).thenReturn("<span>widget 1</span>");
        doReturn(null).when(spy).find_widget(childs_list.get(2));
        doReturn(widget_list.get(1)).when(spy).find_widget(childs_list.get(3));
        when(childs_list.get(3).getAttribute("outerHTML")).thenReturn("<a href=\"#\">activator 2</a>");
        when(widget_list.get(1).getAttribute("outerHTML")).thenReturn("<span>widget 2</span>");
        doReturn(widget_list.get(2)).when(spy).find_widget(childs_list.get(4));
        when(childs_list.get(4).getAttribute("outerHTML")).thenReturn("<a href=\"#\">activator 3</a>");
        when(widget_list.get(2).getAttribute("outerHTML")).thenReturn("<span>widget 3</span>");

        result_widget = app.find_all_widgets();
        assertEquals(3, result_widget.size());
        assertEquals("<a href=\"#\">activator 1</a>", result_widget.get(0).get("activator"));
        assertEquals("<span>widget 1</span>", result_widget.get(0).get("widget"));
        assertEquals("<a href=\"#\">activator 2</a>", result_widget.get(1).get("activator"));
        assertEquals("<span>widget 2</span>", result_widget.get(1).get("widget"));
        assertEquals("<a href=\"#\">activator 3</a>", result_widget.get(2).get("activator"));
        assertEquals("<span>widget 3</span>", result_widget.get(2).get("widget"));
    }

    @Test
    public void test_find_all_widgets_should_do_multiple_calls_to_find_widget_only_in_visible_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        Locator spy = mock(Locator.class);
        App app = new App(driver_mock, spy);

        List <Map<String, String>> result_widget;
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        List <WebElement> widget_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        widget_list.add(mock(WebElement.class));

        when(childs_list.get(0).isDisplayed()).thenReturn(false);
        when(childs_list.get(1).isDisplayed()).thenReturn(false);
        when(childs_list.get(2).isDisplayed()).thenReturn(false);
        when(childs_list.get(3).isDisplayed()).thenReturn(false);
        when(childs_list.get(4).isDisplayed()).thenReturn(true);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        doReturn(widget_list.get(0)).when(spy).find_widget(childs_list.get(4));
        when(childs_list.get(4).getAttribute("outerHTML")).thenReturn("<a href=\"#\">activator 1</a>");
        when(widget_list.get(0).getAttribute("outerHTML")).thenReturn("<span>widget 1</span>");

        result_widget = app.find_all_widgets();
        assertEquals(1, result_widget.size());
        assertEquals("<a href=\"#\">activator 1</a>", result_widget.get(0).get("activator"));
        assertEquals("<span>widget 1</span>", result_widget.get(0).get("widget"));
        verify(spy, never()).find_widget(childs_list.get(0));
        verify(spy, never()).find_widget(childs_list.get(1));
        verify(spy, never()).find_widget(childs_list.get(2));
        verify(spy, never()).find_widget(childs_list.get(3));
    }
}
