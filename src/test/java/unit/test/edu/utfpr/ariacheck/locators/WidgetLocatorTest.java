package unit.test.edu.utfpr.ariacheck.locators;

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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.io.File;

@RunWith(JUnit4.class)
public class WidgetLocatorTest {

    @Test
    public void test_widget_locator_should_call_findElements_in_driver_and_return_result () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("some html");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(0), result);
    }

    @Test
    public void test_widget_locator_should_return_null_if_move_target_out_of_bound_exception_is_thrown_on_mouse_move () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock))
            .thenThrow(mock(MoveTargetOutOfBoundsException.class));
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_return_null_if_stale_exception_is_thrown_on_mouse_move () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock))
            .thenThrow(mock(StaleElementReferenceException.class));
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_return_only_initially_invisible_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
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
        when(childs_list.get(5).getAttribute("outerHTML")).thenReturn("some html");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(5), result);
    }

    @Test
    public void test_widget_locator_should_return_only_initially_invisible_elements_which_became_visible () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        InOrder inorder = inOrder(actions_mock, action_mock);
        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
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
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("some html");
        when(childs_list.get(3).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(4).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(5).isDisplayed()).thenReturn(false).thenReturn(false);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        inorder.verify(actions_mock).moveToElement(target_mock);
        inorder.verify(actions_mock).build();
        inorder.verify(action_mock).perform();

        assertEquals(childs_list.get(2), result);
    }

    @Test
    public void test_widget_locator_should_return_return_newly_visible_element_without_looking_in_child_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));

        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(0).findElements(By.cssSelector("*"))).thenReturn(childs_list.subList(1, 2));
        //when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("some html");
        when(childs_list.get(2).findElements(By.cssSelector("*"))).thenReturn(new ArrayList <WebElement> ());
        when(childs_list.get(3).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(3).findElements(By.cssSelector("*"))).thenReturn(childs_list.subList(4, 6));
        //when(childs_list.get(4).isDisplayed()).thenReturn(false).thenReturn(false);
        //when(childs_list.get(5).isDisplayed()).thenReturn(false).thenReturn(false);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        verify(childs_list.get(1), never()).isDisplayed();
        verify(childs_list.get(4), never()).isDisplayed();
        verify(childs_list.get(5), never()).isDisplayed();

        assertEquals(childs_list.get(2), result);
    }

    @Test
    public void test_widget_locator_should_return_null_if_no_invisible_elements_became_visible () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));
        childs_list.add(mock(WebElement.class));

        when(childs_list.get(0).isDisplayed()).thenReturn(false);
        when(childs_list.get(1).isDisplayed()).thenReturn(false);
        when(childs_list.get(2).isDisplayed()).thenReturn(false);
        when(childs_list.get(3).isDisplayed()).thenReturn(false);
        when(childs_list.get(4).isDisplayed()).thenReturn(false);
        when(childs_list.get(5).isDisplayed()).thenReturn(false);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_return_null_if_there_are_no_inv_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor, actions_mock);
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
        when(childs_list.get(5).isDisplayed()).thenReturn(true);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_return_mutation_elements () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        List <WebElement> mutations_list = new ArrayList <WebElement> ();
        mutations_list.add(mutation_widget);
        when(mutation_widget.getAttribute("outerHTML")).thenReturn("some element");
        String javascript_code =
            "if ( ! window.observer) {" +
            "    var real_setTimeout = window.setTimeout;" +
            "    for (var i = 0; i < 10000; i++) { clearTimeout(i); clearInterval(i); };" +
            "    window.observer = new MutationObserver(function (mutations) {" +
            "        mutations.forEach(function (mutation) {" +
            "            if (mutation.addedNodes && mutation.addedNodes.length > 0 &&" +
            "                mutation.addedNodes[0].nodeType === 1 &&" +
            "                mutation.addedNodes[0].parentElement &&" +
            "                mutation.addedNodes[0].parentElement.getAttribute(\"role\") !== \"log\") {" +
            "                mutation.addedNodes[0].className += \" mutation_widget\";" +
            "            }" +
            "        });" +
            "    });" +
            "    window.observer.observe(document.body, {childList: true, subtree: true});" +
            "}";


        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);
        when(executor_mock.executeScript(javascript_code)).thenReturn(null);

        result = locator.find_widget(target_mock);

        verify(executor_mock).executeScript(javascript_code);
        assertEquals(mutation_widget, result);
    }

    @Test
    public void test_widget_locator_should_return_mutation_elements_and_clean_previous_mutation_records () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        List <WebElement> mutations_list = new ArrayList <WebElement> ();
        InOrder inorder = inOrder(driver_mock, executor_mock);

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        mutations_list.add(mutation_widget);
        when(mutation_widget.getAttribute("outerHTML")).thenReturn("some element");
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)")))
                        .thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(mutation_widget, result);
        inorder.verify(driver_mock).findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"));
        inorder.verify(executor_mock).executeScript(
        "var mutation_widget = document.querySelectorAll(\".mutation_widget\");" +
        "for (var i = 0; i < mutation_widget.length; i++)" +
        "    mutation_widget[i].className = mutation_widget[i].className" +
        "                                           .replace(\"mutation_widget\",\"old_mutation\");"
        );
    }
    @Test
    public void test_widget_locator_should_return_only_the_biggest_changed_element_priorizing_mutations () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("<div><div>Some very cool thing</div></div>");
        List <WebElement> mutations_list = new ArrayList <WebElement> ();
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(0).isDisplayed()).thenReturn(true);
        when(mutations_list.get(0).getAttribute("outerHTML")).thenReturn("<div>nothing</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(1).isDisplayed()).thenReturn(true);
        when(mutations_list.get(1).getAttribute("outerHTML")).thenReturn("<div>small</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(2).isDisplayed()).thenReturn(true);
        when(mutations_list.get(2).getAttribute("outerHTML")).thenReturn("<div>not so cool</div>");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(mutations_list.get(2), result);
    }

    @Test
    public void test_widget_locator_should_return_only_the_biggest_changed_element_from_the_visibles () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("<div>Some cool thing</div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("<div><div>Some cool thing</div></div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("<div><div>Some very cool thing</div></div>");
        List <WebElement> mutations_list = new ArrayList <WebElement> ();

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(childs_list.get(1), result);
    }

    @Test
    public void test_widget_locator_should_return_only_the_biggest_changed_element_from_the_mutations () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("<div>Some cool thing</div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("<div><div>Some cool thing</div></div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("<div><div>Some very cool thing</div></div>");
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
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(mutations_list.get(2), result);
    }

    @Test
    public void test_widget_locator_should_deal_with_stale_exception_in_mutations_list () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("<div>Some cool thing</div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("<div><div>Some cool thing</div></div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("<div><div>Some very cool thing</div></div>");
        List <WebElement> mutations_list = new ArrayList <WebElement> ();
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(0).isDisplayed()).thenReturn(true);
        when(mutations_list.get(0).getAttribute("outerHTML"))
                                  .thenThrow(new StaleElementReferenceException("oops"));
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(1).isDisplayed()).thenReturn(true);
        when(mutations_list.get(1).getAttribute("outerHTML")).thenReturn("<div>nothing</div>");
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(2).isDisplayed()).thenReturn(true);
        when(mutations_list.get(2).getAttribute("outerHTML")).thenReturn("<div>the coolest and biggest widget</div>");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(mutations_list.get(2).getAttribute("outerHTML"), result.getAttribute("outerHTML"));
    }

    @Test
    public void test_widget_locator_should_deal_with_stale_exception_as_the_other_widgets_are_searched_into () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenThrow(new StaleElementReferenceException("oops"));
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("<div><div>Some cool thing</div></div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("insignificant content");
        List <WebElement> mutations_list = new ArrayList <WebElement> ();
        mutations_list.add(mock(WebElement.class));
        when(mutations_list.get(0).isDisplayed()).thenReturn(true);
        when(mutations_list.get(0).getAttribute("outerHTML"))
                                  .thenThrow(new StaleElementReferenceException("oops 2"));

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(childs_list.get(1), result);
    }

    @Test
    public void test_widget_locator_should_deal_with_stale_exception_in_invisibles_list () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenThrow(new StaleElementReferenceException("should not come here"));
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("<div>Some cool thing</div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(false);
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("<div><div>Some cool thing</div></div>");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("<div><div>Some very cool thing</div></div>");
        List <WebElement> mutations_list = new ArrayList <WebElement> ();

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(mutations_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);

        assertEquals(childs_list.get(2).getAttribute("outerHTML"), result.getAttribute("outerHTML"));
    }

    @Test
    public void test_widget_locator_should_cache_invisibles_list_in_multiple_calls_to_find_widget () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(false).thenReturn(false).thenThrow(new RuntimeException());
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("abobrinha");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true).thenReturn(false).thenThrow(new RuntimeException());
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("abo");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(new ArrayList <WebElement> ());

        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(1), result);
        result = locator.find_widget(target_mock);
        assertEquals(null, result);

        verify(driver_mock, times(1)).findElements(By.cssSelector("body *"));
    }

    @Test
    public void test_widget_locator_should_remove_previously_potential_widgets_from_cache_list () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(false).thenReturn(false).thenThrow(new RuntimeException());
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("abobrinha");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true)
            .thenThrow(new RuntimeException("The second element should be removed from cache list..."));
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("abo");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(new ArrayList <WebElement> ());

        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(1), result);
        result = locator.find_widget(target_mock);
        assertEquals(null, result);

        verify(driver_mock, times(1)).findElements(By.cssSelector("body *"));
    }

    @Test
    public void test_widget_locator_should_remove_multiple_potential_widgets_from_cache_list () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class),
                   mutation_widget = mock(WebElement.class),
                   result;
        Dimension dimension_mock = mock(Dimension.class);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(false).thenReturn(false).thenThrow(new RuntimeException());
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("abobrinha");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(1).isDisplayed()).thenReturn(false).thenReturn(true)
            .thenThrow(new RuntimeException("The second element should be removed from cache list..."));
        when(childs_list.get(1).getAttribute("outerHTML")).thenReturn("abo");
        childs_list.add(mock(WebElement.class));
        when(childs_list.get(2).isDisplayed()).thenReturn(false).thenReturn(true)
            .thenThrow(new RuntimeException("The second element should be removed from cache list..."));
        when(childs_list.get(2).getAttribute("outerHTML")).thenReturn("<a href=\"#\">abo</a>");

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(driver_mock.findElements(By.cssSelector(".mutation_widget:not(.old_mutation)"))).thenReturn(new ArrayList <WebElement> ());

        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(2), result);
        result = locator.find_widget(target_mock);
        assertEquals(null, result);

        verify(driver_mock, times(1)).findElements(By.cssSelector("body *"));
    }

    @Test
    public void test_widget_locator_should_only_inspect_elements_with_a_limited_width () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        WebElement target_mock = mock(WebElement.class), result;
        Dimension dimension_mock = mock(Dimension.class);

        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(500);
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        result = locator.find_widget(target_mock);

        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_only_inspect_elements_with_a_limited_height () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        WebElement target_mock = mock(WebElement.class), result;
        Dimension dimension_mock = mock(Dimension.class);
        Point point_mock = new Point(0, 0);

        when(target_mock.getLocation()).thenReturn(point_mock);
        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        when(dimension_mock.getHeight()).thenReturn(200);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        result = locator.find_widget(target_mock);

        assertEquals(null, result);
    }

    @Test
    public void test_cache_for_target_in_widget_locator () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        WebElement target_mock = mock(WebElement.class);
        Dimension dimension_mock = mock(Dimension.class);
        doReturn(null).when(executor_mock).executeScript(anyString());

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);

        doReturn(dimension_mock).when(target_mock).getSize();
        doReturn(90000).when(dimension_mock).getWidth(); // to ensure nothing will be executed later

        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        verify(executor_mock, times(2)).executeScript(anyString());
    }

    @Test
    public void test_isVisible_js_function_called_in_constructor () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);

        doReturn(null).when(executor_mock).executeScript(
        "/**" +
        " * Author: Jason Farrell" +
        " * Author URI: http://useallfive.com/" +
        " *" +
        " * Description: Checks if a DOM element is truly visible." +
        " * Package URL: https://github.com/UseAllFive/true-visibility" +
        " */" +
        "Element.prototype.isVisible = function() {" +
        "    'use strict';" +
        "    /**" +
        "     * Checks if a DOM element is visible. Takes into" +
        "     * consideration its parents and overflow." +
        "     *" +
        "     * @param (el)      the DOM element to check if is visible" +
        "     *" +
        "     * These params are optional that are sent in recursively," +
        "     * you typically won't use these:" +
        "     *" +
        "     * @param (t)       Top corner position number" +
        "     * @param (r)       Right corner position number" +
        "     * @param (b)       Bottom corner position number" +
        "     * @param (l)       Left corner position number" +
        "     * @param (w)       Element width number" +
        "     * @param (h)       Element height number" +
        "     */" +
        "    function _isVisible(el, t, r, b, l, w, h) {" +
        "        var p = el.parentNode," +
        "                VISIBLE_PADDING = 2;" +
        "        if ( !_elementInDocument(el) ) {" +
        "            return false;" +
        "        }" +
        "        //-- Return true for document node" +
        "        if ( 9 === p.nodeType ) {" +
        "            return true;" +
        "        }" +
        "        //-- Return false if our element is invisible" +
        "        if (" +
        "             '0' === _getStyle(el, 'opacity') ||" +
        "             'none' === _getStyle(el, 'display') ||" +
        "             'hidden' === _getStyle(el, 'visibility')" +
        "        ) {" +
        "            return false;" +
        "        }" +
        "        if (" +
        "            'undefined' === typeof(t) ||" +
        "            'undefined' === typeof(r) ||" +
        "            'undefined' === typeof(b) ||" +
        "            'undefined' === typeof(l) ||" +
        "            'undefined' === typeof(w) ||" +
        "            'undefined' === typeof(h)" +
        "        ) {" +
        "            t = el.offsetTop;" +
        "            l = el.offsetLeft;" +
        "            b = t + el.offsetHeight;" +
        "            r = l + el.offsetWidth;" +
        "            w = el.offsetWidth;" +
        "            h = el.offsetHeight;" +
        "        }" +
        "        //-- If we have a parent, let's continue:" +
        "        if ( p ) {" +
        "            //-- Check if the parent can hide its children." +
        "            if ( ('hidden' === _getStyle(p, 'overflow') || 'scroll' === _getStyle(p, 'overflow')) ) {" +
        "                //-- Only check if the offset is different for the parent" +
        "                if (" +
        "                    //-- If the target element is to the right of the parent elm" +
        "                    l + VISIBLE_PADDING > p.offsetWidth + p.scrollLeft ||" +
        "                    //-- If the target element is to the left of the parent elm" +
        "                    l + w - VISIBLE_PADDING < p.scrollLeft ||" +
        "                    //-- If the target element is under the parent elm" +
        "                    t + VISIBLE_PADDING > p.offsetHeight + p.scrollTop ||" +
        "                    //-- If the target element is above the parent elm" +
        "                    t + h - VISIBLE_PADDING < p.scrollTop" +
        "                ) {" +
        "                    //-- Our target element is out of bounds:" +
        "                    return false;" +
        "                }" +
        "            }" +
        "            //-- Add the offset parent's left/top coords to our element's offset:" +
        "            if ( el.offsetParent === p ) {" +
        "                l += p.offsetLeft;" +
        "                t += p.offsetTop;" +
        "            }" +
        "            //-- Let's recursively check upwards:" +
        "            return _isVisible(p, t, r, b, l, w, h);" +
        "        }" +
        "        return true;" +
        "    }" +
        "    //-- Cross browser method to get style properties:" +
        "    function _getStyle(el, property) {" +
        "        if ( window.getComputedStyle ) {" +
        "            return document.defaultView.getComputedStyle(el,null)[property];" +
        "        }" +
        "        if ( el.currentStyle ) {" +
        "            return el.currentStyle[property];" +
        "        }" +
        "    }" +
        "    function _elementInDocument(element) {" +
        "        while (element = element.parentNode) {" +
        "            if (element == document) {" +
        "                    return true;" +
        "            }" +
        "        }" +
        "        return false;" +
        "    }" +
        "    return _isVisible(this);" +
        "};");
        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);

        verify(executor_mock).executeScript(
        "/**" +
        " * Author: Jason Farrell" +
        " * Author URI: http://useallfive.com/" +
        " *" +
        " * Description: Checks if a DOM element is truly visible." +
        " * Package URL: https://github.com/UseAllFive/true-visibility" +
        " */" +
        "Element.prototype.isVisible = function() {" +
        "    'use strict';" +
        "    /**" +
        "     * Checks if a DOM element is visible. Takes into" +
        "     * consideration its parents and overflow." +
        "     *" +
        "     * @param (el)      the DOM element to check if is visible" +
        "     *" +
        "     * These params are optional that are sent in recursively," +
        "     * you typically won't use these:" +
        "     *" +
        "     * @param (t)       Top corner position number" +
        "     * @param (r)       Right corner position number" +
        "     * @param (b)       Bottom corner position number" +
        "     * @param (l)       Left corner position number" +
        "     * @param (w)       Element width number" +
        "     * @param (h)       Element height number" +
        "     */" +
        "    function _isVisible(el, t, r, b, l, w, h) {" +
        "        var p = el.parentNode," +
        "                VISIBLE_PADDING = 2;" +
        "        if ( !_elementInDocument(el) ) {" +
        "            return false;" +
        "        }" +
        "        //-- Return true for document node" +
        "        if ( 9 === p.nodeType ) {" +
        "            return true;" +
        "        }" +
        "        //-- Return false if our element is invisible" +
        "        if (" +
        "             '0' === _getStyle(el, 'opacity') ||" +
        "             'none' === _getStyle(el, 'display') ||" +
        "             'hidden' === _getStyle(el, 'visibility')" +
        "        ) {" +
        "            return false;" +
        "        }" +
        "        if (" +
        "            'undefined' === typeof(t) ||" +
        "            'undefined' === typeof(r) ||" +
        "            'undefined' === typeof(b) ||" +
        "            'undefined' === typeof(l) ||" +
        "            'undefined' === typeof(w) ||" +
        "            'undefined' === typeof(h)" +
        "        ) {" +
        "            t = el.offsetTop;" +
        "            l = el.offsetLeft;" +
        "            b = t + el.offsetHeight;" +
        "            r = l + el.offsetWidth;" +
        "            w = el.offsetWidth;" +
        "            h = el.offsetHeight;" +
        "        }" +
        "        //-- If we have a parent, let's continue:" +
        "        if ( p ) {" +
        "            //-- Check if the parent can hide its children." +
        "            if ( ('hidden' === _getStyle(p, 'overflow') || 'scroll' === _getStyle(p, 'overflow')) ) {" +
        "                //-- Only check if the offset is different for the parent" +
        "                if (" +
        "                    //-- If the target element is to the right of the parent elm" +
        "                    l + VISIBLE_PADDING > p.offsetWidth + p.scrollLeft ||" +
        "                    //-- If the target element is to the left of the parent elm" +
        "                    l + w - VISIBLE_PADDING < p.scrollLeft ||" +
        "                    //-- If the target element is under the parent elm" +
        "                    t + VISIBLE_PADDING > p.offsetHeight + p.scrollTop ||" +
        "                    //-- If the target element is above the parent elm" +
        "                    t + h - VISIBLE_PADDING < p.scrollTop" +
        "                ) {" +
        "                    //-- Our target element is out of bounds:" +
        "                    return false;" +
        "                }" +
        "            }" +
        "            //-- Add the offset parent's left/top coords to our element's offset:" +
        "            if ( el.offsetParent === p ) {" +
        "                l += p.offsetLeft;" +
        "                t += p.offsetTop;" +
        "            }" +
        "            //-- Let's recursively check upwards:" +
        "            return _isVisible(p, t, r, b, l, w, h);" +
        "        }" +
        "        return true;" +
        "    }" +
        "    //-- Cross browser method to get style properties:" +
        "    function _getStyle(el, property) {" +
        "        if ( window.getComputedStyle ) {" +
        "            return document.defaultView.getComputedStyle(el,null)[property];" +
        "        }" +
        "        if ( el.currentStyle ) {" +
        "            return el.currentStyle[property];" +
        "        }" +
        "    }" +
        "    function _elementInDocument(element) {" +
        "        while (element = element.parentNode) {" +
        "            if (element == document) {" +
        "                    return true;" +
        "            }" +
        "        }" +
        "        return false;" +
        "    }" +
        "    return _isVisible(this);" +
        "};");
    }

}
