package unit.test.ariacheck;

import ariacheck.WidgetLocator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.mockito.InOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;

import java.util.List;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class WidgetLocatorTest {

    @Test
    public void test_widget_locator_should_call_findElements_in_driver_and_return_result () {
        WebDriver driver_mock = mock(WebDriver.class);
        Actions actions_mock = mock(Actions.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        WidgetLocator locator = new WidgetLocator(driver_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(0), result);
    }

    @Test
    public void test_widget_locator_should_return_only_initially_invisible_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        Actions actions_mock = mock(Actions.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        WidgetLocator locator = new WidgetLocator(driver_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));

        when(childs_list.get(0).isDisplayed()).thenReturn(true);
        when(childs_list.get(1).isDisplayed()).thenReturn(true);
        when(childs_list.get(2).isDisplayed()).thenReturn(true);
        when(childs_list.get(3).isDisplayed()).thenReturn(true);
        when(childs_list.get(4).isDisplayed()).thenReturn(true);
        when(childs_list.get(5).isDisplayed()).thenReturn(false).thenReturn(true);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(5), result);
    }

    @Test
    public void test_widget_locator_should_return_only_initially_invisible_elements_with_became_visible () {
        WebDriver driver_mock = mock(WebDriver.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        InOrder inorder = inOrder(actions_mock, action_mock);
        WidgetLocator locator = new WidgetLocator(driver_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));

        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(3).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(4).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(5).isDisplayed()).thenReturn(false).thenReturn(false);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveByOffset(-1500, -1500)).thenReturn(actions_mock);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        inorder.verify(actions_mock).moveByOffset(-1500, -1500);
        inorder.verify(actions_mock).moveToElement(target_mock);
        inorder.verify(actions_mock).build();
        inorder.verify(action_mock).perform();

        assertEquals(childs_list.get(2), result);
    }
}
