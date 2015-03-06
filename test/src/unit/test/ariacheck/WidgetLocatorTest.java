package unit.test.ariacheck;

import ariacheck.WidgetLocator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class WidgetLocatorTest {

    @Test
    public void test_widget_locator_should_find_css_only_tooltip () {
        WebDriver driver_mock = mock(WebDriver.class);
        WebElement target_mock = mock(WebElement.class),
                   result;
        WidgetLocator locator = new WidgetLocator(driver_mock);
        List <WebElement> childs_list = new ArrayList <WebElement> ();
        childs_list.add(mock(WebElement.class));

        when(driver_mock.findElements(By.cssSelector("body *"))).thenReturn(childs_list);

        result = locator.find_widget(target_mock);
        assertEquals(childs_list.get(0), result);
    }

}
