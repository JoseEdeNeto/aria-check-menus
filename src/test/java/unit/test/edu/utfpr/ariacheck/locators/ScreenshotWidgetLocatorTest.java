package unit.test.edu.utfpr.ariacheck.locators;

import edu.utfpr.ariacheck.locators.Locator;
import edu.utfpr.ariacheck.locators.WidgetLocator;
import edu.utfpr.ariacheck.locators.decorators.ScreenshotWidgetLocatorDecorator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.mockito.InOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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

        ScreenshotWidgetLocatorDecorator locator = new ScreenshotWidgetLocatorDecorator(locator_mock, takes_mock, "captured_screens/");
        locator = spy(locator);
        inorder = inOrder(locator, locator_mock, takes_mock);
        when(locator.create_file_wrapper("captured_screens/001_before_widget.jpg")).thenReturn(new_file_stub);
        when(locator.create_file_wrapper("captured_screens/001_later_widget.jpg")).thenReturn(new_file_stub_2);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub_2, new_file_stub_2);
        doNothing().when(locator).save_element_screenshot(target_stub, screenshot_stub_2, "widget_activator");
        doNothing().when(locator).save_element_screenshot(widget_stub, screenshot_stub_2, "widget");

        result = locator.find_widget(target_stub);

        inorder.verify(takes_mock).getScreenshotAs(OutputType.FILE);
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/001_before_widget.jpg");
        inorder.verify(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        inorder.verify(takes_mock).getScreenshotAs(OutputType.FILE);
        inorder.verify(locator).create_file_wrapper("captured_screens/001_later_widget.jpg");
        inorder.verify(locator).copy_file_wrapper(screenshot_stub_2, new_file_stub_2);
        inorder.verify(locator).save_element_screenshot(target_stub, screenshot_stub_2, "widget_activator");
        inorder.verify(locator).save_element_screenshot(widget_stub, screenshot_stub_2, "widget");
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

        ScreenshotWidgetLocatorDecorator locator = new ScreenshotWidgetLocatorDecorator(locator_mock, takes_mock, "captured_screens/");
        locator = spy(locator);
        inorder = inOrder(locator, locator_mock);
        when(locator.create_file_wrapper(anyString())).thenReturn(new_file_stub);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        doNothing().when(locator).save_element_screenshot(target_stub, screenshot_stub, "widget_activator");
        doNothing().when(locator).save_element_screenshot(widget_stub, screenshot_stub, "widget");
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);
        result = locator.find_widget(target_stub);

        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/001_before_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/001_later_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/002_before_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/002_later_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/003_before_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/003_later_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/004_before_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/004_later_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/005_before_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/005_later_widget.jpg");
        inorder.verify(locator_mock).find_widget(target_stub);
        inorder.verify(locator).create_file_wrapper("captured_screens/006_before_widget.jpg");
        inorder.verify(locator).create_file_wrapper("captured_screens/006_later_widget.jpg");
        assertEquals(widget_stub, result);
    }

    @Test
    public void test_find_widget_should_call_find_widget_in_another_Locator_and_do_nothing_if_no_widget_is_found ()
                throws IOException {
        Locator locator_mock = mock(Locator.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        File screenshot_stub = mock(File.class),
             new_file_stub = mock(File.class),
             screenshot_stub_2 = mock(File.class),
             new_file_stub_2 = mock(File.class);
        WebElement target_stub = mock(WebElement.class),
                   result = null;
        when(locator_mock.find_widget(target_stub)).thenReturn(null);
        when(takes_mock.getScreenshotAs(OutputType.FILE)).thenReturn(screenshot_stub).thenReturn(screenshot_stub_2);

        ScreenshotWidgetLocatorDecorator locator = new ScreenshotWidgetLocatorDecorator(locator_mock, takes_mock, "captured_screens/");
        locator = spy(locator);
        when(locator.create_file_wrapper("captured_screens/001_before_widget.jpg")).thenReturn(new_file_stub);
        when(locator.create_file_wrapper("captured_screens/001_later_widget.jpg")).thenReturn(new_file_stub_2);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub, new_file_stub);
        doNothing().when(locator).copy_file_wrapper(screenshot_stub_2, new_file_stub_2);
        result = locator.find_widget(target_stub);

        verify(locator, never()).create_file_wrapper("captured_screens/001_before_widget.jpg");
        verify(locator, never()).copy_file_wrapper(screenshot_stub, new_file_stub);
        verify(locator_mock).find_widget(target_stub);
        verify(locator, never()).create_file_wrapper("captured_screens/001_later_widget.jpg");
        verify(locator, never()).copy_file_wrapper(screenshot_stub_2, new_file_stub_2);
        assertEquals(null, result);
    }

    @Test
    public void test_save_element_screenshot () {
        Locator locator_mock = mock(Locator.class);
        TakesScreenshot takes_mock = mock(TakesScreenshot.class);
        ScreenshotWidgetLocatorDecorator locator = spy(new ScreenshotWidgetLocatorDecorator(
                    locator_mock, takes_mock, "captured_screens/"));
        WebElement target_stub = mock(WebElement.class);
        Point target_location_stub = mock(Point.class);
        Dimension target_dimension_stub = mock(Dimension.class);
        File screenshot_stub = mock(File.class);
        BufferedImage buf_mock = mock(BufferedImage.class),
                      subimage_buf_stub = mock(BufferedImage.class);
        File file_stub = mock(File.class);

        doReturn(buf_mock).when(locator).imageio_read_wrapper(screenshot_stub);
        doReturn(target_location_stub).when(target_stub).getLocation();
        doReturn(15).when(target_location_stub).getX();
        doReturn(75).when(target_location_stub).getY();
        doReturn(target_dimension_stub).when(target_stub).getSize();
        doReturn(77).when(target_dimension_stub).getWidth();
        doReturn(98).when(target_dimension_stub).getHeight();
        doReturn(subimage_buf_stub).when(buf_mock).getSubimage(
                15, 75, 77, 98);
        doReturn(file_stub).when(locator).create_file_wrapper("captured_screens/013_widget_activator.jpg");
        locator.setCounter(13);
        doNothing().when(locator).imageio_write_wrapper(subimage_buf_stub, file_stub);

        locator.save_element_screenshot(target_stub, screenshot_stub, "widget_activator");

        verify(locator).imageio_read_wrapper(screenshot_stub);
        verify(buf_mock).getSubimage(15, 75, 77, 98);
        verify(locator).imageio_write_wrapper(subimage_buf_stub, file_stub);
    }
}
