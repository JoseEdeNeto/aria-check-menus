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
import org.openqa.selenium.TakesScreenshot;
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

        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock);
        result = locator.find_widget(target_mock);

        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_not_check_visibility_if_there_are_no_screenshot_differences () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class), result;
        Dimension dimension_mock = mock(Dimension.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        InOrder inorder = inOrder(takes_mock, actions_mock, action_mock);
        File screenshot_stub = mock(File.class),
             screenshot_stub_2 = mock(File.class);
        double comp_result = 0;
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);

        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        when(dimension_mock.getHeight()).thenReturn(90);
        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(new ArrayList());
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);
        doNothing().when(action_mock).perform();

        when(takes_mock.getScreenshotAs(OutputType.FILE)).thenReturn(screenshot_stub).thenReturn(screenshot_stub_2);

        WidgetLocator locator = spy(new WidgetLocator(driver_mock, executor_mock, actions_mock, takes_mock));
        doReturn(comp_result).when(locator).compareImages(screenshot_stub, screenshot_stub_2);

        result = locator.find_widget(target_mock);

        inorder.verify(takes_mock).getScreenshotAs(OutputType.FILE);
        inorder.verify(actions_mock).moveToElement(target_mock);
        inorder.verify(actions_mock).build();
        inorder.verify(action_mock).perform();
        inorder.verify(takes_mock).getScreenshotAs(OutputType.FILE);

        assertEquals(null, result);
    }

    @Test
    public void test_widget_locator_should_not_check_visibility_if_there_is_not_significant_screenshot_differences () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        Action action_mock = mock(Action.class);
        WebElement target_mock = mock(WebElement.class), result;
        Dimension dimension_mock = mock(Dimension.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        InOrder inorder = inOrder(takes_mock, actions_mock, action_mock);
        File screenshot_stub = mock(File.class),
             screenshot_stub_2 = mock(File.class);
        double comp_result = 0.04;
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        Point point_mock = new Point(0, 0);
        when(target_mock.getLocation()).thenReturn(point_mock);

        childs_list.add(mock(WebElement.class));
        when(childs_list.get(0).isDisplayed()).thenReturn(false).thenReturn(true);
        when(childs_list.get(0).getAttribute("outerHTML")).thenReturn("abobrinha");

        when(target_mock.getSize()).thenReturn(dimension_mock);
        when(dimension_mock.getWidth()).thenReturn(200);
        when(dimension_mock.getHeight()).thenReturn(90);
        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);
        when(actions_mock.moveToElement(target_mock)).thenReturn(actions_mock);
        when(actions_mock.build()).thenReturn(action_mock);
        doNothing().when(action_mock).perform();

        when(takes_mock.getScreenshotAs(OutputType.FILE)).thenReturn(screenshot_stub).thenReturn(screenshot_stub_2);

        WidgetLocator locator = spy(new WidgetLocator(driver_mock, executor_mock, actions_mock, takes_mock));
        doReturn(comp_result).when(locator).compareImages(screenshot_stub, screenshot_stub_2);

        result = locator.find_widget(target_mock);

        inorder.verify(takes_mock).getScreenshotAs(OutputType.FILE);
        inorder.verify(actions_mock).moveToElement(target_mock);
        inorder.verify(actions_mock).build();
        inorder.verify(action_mock).perform();
        inorder.verify(takes_mock).getScreenshotAs(OutputType.FILE);

        assertEquals(null, result);
    }

    @Test
    public void test_cache_for_target_in_widget_locator () {
        WebDriver driver_mock = mock(WebDriver.class);
        JavascriptExecutor executor_mock = mock(JavascriptExecutor.class);
        Actions actions_mock = mock(Actions.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        WebElement target_mock = mock(WebElement.class);
        Dimension dimension_mock = mock(Dimension.class);
        WidgetLocator locator = new WidgetLocator(driver_mock, executor_mock, actions_mock, takes_mock);

        doReturn(dimension_mock).when(target_mock).getSize();
        doReturn(90000).when(dimension_mock).getWidth(); // to ensure nothing will be executed later
        doReturn(null).when(executor_mock).executeScript(anyString());

        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        verify(executor_mock, times(1)).executeScript(anyString());
    }

}
