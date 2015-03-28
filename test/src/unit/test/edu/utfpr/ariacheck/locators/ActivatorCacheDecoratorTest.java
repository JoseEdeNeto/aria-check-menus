package unit.test.edu.utfpr.ariacheck.locators;

import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.cache.CacheSingleton;

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

@RunWith(JUnit4.class)
public class ActivatorCacheDecoratorTest {

    @Test
    public void test_widget_locator_should_not_register_search_for_widgets_in_previously_searched_activation_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Locator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock, CacheSingleton.createInstance());
        CacheSingleton.createInstance().clear();
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        List <WebElement> mutations_list = new ArrayList <WebElement> ();
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(0).isDisplayed()).thenReturn(true);
        when(mutations_list.get(0).getAttribute("outerHTML")).thenReturn("<div>not so cool</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(1).isDisplayed()).thenReturn(true);
        when(mutations_list.get(1).getAttribute("outerHTML")).thenReturn("<div>nothing</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(2).isDisplayed()).thenReturn(true);
        when(mutations_list.get(2).getAttribute("outerHTML")).thenReturn("<div>the coolest and biggest widget</div>");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget"))).thenReturn(mutations_list);
        when(actions_mock.moveByOffset(-1500, -1500)).thenReturn(actions_mock);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        when(target_mock.getAttribute("outerHTML")).thenReturn("<div><span>Some activation guy</span></div>");
        result = locator.find_widget(target_mock);
        assertEquals(mutations_list.get(2), result);

        target_mock = mock(WebElement.class);
        when(target_mock.getAttribute("outerHTML")).thenReturn("<span>Some activation guy</span>");
        when(actions_mock.moveByOffset(-1500, -1500)).thenReturn(actions_mock);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);
        result = locator.find_widget(target_mock);
        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_not_store_in_cache_when_no_widget_is_found () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock, CacheSingleton.createInstance());
        CacheSingleton.createInstance().clear();
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        List <WebElement> mutations_list = new ArrayList <WebElement> ();

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget"))).thenReturn(mutations_list);
        when(actions_mock.moveByOffset(-1500, -1500)).thenReturn(actions_mock);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        when(target_mock.getAttribute("outerHTML")).thenReturn("<div><span>Some activation guy</span></div>");
        result = locator.find_widget(target_mock);
        assertEquals(null, result);

        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(0).isDisplayed()).thenReturn(true);
        when(mutations_list.get(0).getAttribute("outerHTML")).thenReturn("<div>not so cool</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(1).isDisplayed()).thenReturn(true);
        when(mutations_list.get(1).getAttribute("outerHTML")).thenReturn("<div>nothing</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(2).isDisplayed()).thenReturn(true);
        when(mutations_list.get(2).getAttribute("outerHTML")).thenReturn("<div>the coolest and biggest widget</div>");

        target_mock = mock(WebElement.class);
        when(target_mock.getAttribute("outerHTML")).thenReturn("<span>Some activation guy</span>");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget"))).thenReturn(mutations_list);
        when(actions_mock.moveByOffset(-1500, -1500)).thenReturn(actions_mock);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(mutations_list.get(2), result);
    }
}
