package unit.test.edu.utfpr.ariacheck.locators;

import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.ScreenshotWidgetLocator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.mockito.InOrder;
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
             new_file_stub = mock(File.class),
             screenshot_stub_2 = mock(File.class),
             new_file_stub_2 = mock(File.class);
        WebElement target_stub = mock(WebElement.class),
                   widget_stub = mock(WebElement.class),
                   result = null;
        InOrder inorder;
        when(locator_mock.find_widget(target_stub)).thenReturn(widget_stub);
        when(takes_mock.getScreenshotAs(OutputType.FILE)).thenReturn(screenshot_stub).thenReturn(screenshot_stub_2);

        ScreenshotWidgetLocator locator = new ScreenshotWidgetLocator(locator_mock, takes_mock, "captured_screens/");
        locator = spy(locator);
        inorder = inOrder(locator, locator_mock);
        when(locator.create_file_wrapper("captured_screens/001_before_widget.jpg")).thenReturn(new_file_stub);
        when(locator.create_file_wrapper("captured_screens/001_later_widget.jpg")).thenReturn(new_file_stub_2);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub_2, new_file_stub_2);
        result = locator.find_widget(target_stub);

        inorder.verify(locator).create_file_wrapper("captured_screens/001_before_widget.jpg");
        inorder.verify(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/001_later_widget.jpg");
        inorder.verify(locator).copy_file_wrapper(screenshot_stub_2, new_file_stub_2);
        assertEquals(widget_stub, result);
    }

    @Test
    public void test_should_increment_the_file_name_counter_as_more_widgets_are_found () throws IOException {
        Locator locator_mock = mock(Locator.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        File screenshot_stub = mock(File.class),
             new_file_stub = mock(File.class);
        WebElement target_stub = mock(WebElement.class),
                   widget_stub = mock(WebElement.class),
                   result = null;
        InOrder inorder;
        when(locator_mock.find_widget(target_stub)).thenReturn(widget_stub);
        when(takes_mock.getScreenshotAs(OutputType.FILE)).thenReturn(screenshot_stub);

        ScreenshotWidgetLocator locator = new ScreenshotWidgetLocator(locator_mock, takes_mock, "captured_screens/");
        locator = spy(locator);
        inorder = inOrder(locator, locator_mock);
        when(locator.create_file_wrapper(anyString())).thenReturn(new_file_stub);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);

        inorder.verify(locator).create_file_wrapper("captured_screens/001_before_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/001_later_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/002_before_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/002_later_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/003_before_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/003_later_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/004_before_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/004_later_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/005_before_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/005_later_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/006_before_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/006_later_widget.jpg");
        assertEquals(widget_stub, result);
    }

}
