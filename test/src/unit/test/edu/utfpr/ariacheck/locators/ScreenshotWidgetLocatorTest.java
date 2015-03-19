package unit.test.edu.utfpr.ariacheck.locators;

import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.ScreenshotWidgetLocator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
public class ScreenshotWidgetLocatorTest {

    @Test
    public void test_find_widget_should_call_find_widget_in_another_Locator_and_take_two_screenshots () throws IOException {
        Locator locator_mock = mock(Locator.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        File screenshot_stub = mock(File.class),
             new_file_stub = mock(File.class);
        WebElement target_stub = mock(WebElement.class),
                   widget_stub = mock(WebElement.class),
                   result = null;
        when(locator_mock.find_widget(target_stub)).thenReturn(widget_stub);
        when(takes_mock.getScreenshotAs(OutputType.FILE)).thenReturn(screenshot_stub);

        ScreenshotWidgetLocator locator = new ScreenshotWidgetLocator(locator_mock, takes_mock, "captured_screens/");
        locator = spy(locator);
        when(locator.create_file_wrapper("captured_screens/001_before_widget.jpg")).thenReturn(new_file_stub);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        result = locator.find_widget(target_stub);

        verify(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        assertEquals(widget_stub, result);
    }

}
