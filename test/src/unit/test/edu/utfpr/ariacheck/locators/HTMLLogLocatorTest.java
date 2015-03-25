package unit.test.edu.utfpr.ariacheck.locators;

import edu.utfpr.ariacheck.locators.HTMLLogLocator;
import edu.utfpr.ariacheck.locators.Locator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.mockito.InOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebElement;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

@RunWith(JUnit4.class)
public class HTMLLogLocatorTest {

    @Test
    public void test_html_logger_exists () {
        Locator locator_mock = mock(Locator.class);
        Locator locator = new HTMLLogLocator(locator_mock, "logs/");
    }

    @Test
    public void test_html_logger_find_widget_implements_decorator_strategy () throws FileNotFoundException {
        Locator locator_mock = mock(Locator.class);
        HTMLLogLocator locator = spy(new HTMLLogLocator(locator_mock, "logs/"));
        WebElement target_stub = mock(WebElement.class),
                   result_stub = mock(WebElement.class),
                   result;
        PrintWriter writer_stub = mock(PrintWriter.class);

        doReturn(writer_stub).when(locator).new_writer_wrapper(anyString());
        doNothing().when(writer_stub).print(anyString());
        doNothing().when(writer_stub).close();
        when(locator_mock.find_widget(target_stub)).thenReturn(result_stub);
        result = locator.find_widget(target_stub);
        assertEquals(result_stub, result);
    }

    @Test
    public void test_html_logger_find_widget_should_save_activator_html_extract_if_a_widget_is_found () throws FileNotFoundException {
        Locator locator_mock = mock(Locator.class);
        HTMLLogLocator locator = spy(new HTMLLogLocator(locator_mock, "logs/"));
        WebElement target_stub = mock(WebElement.class),
                   result_stub = mock(WebElement.class),
                   result;
        PrintWriter new_writer_mock = mock(PrintWriter.class),
                    new_writer_stub = mock(PrintWriter.class);
        when(target_stub.getAttribute("outerHTML")).thenReturn("<a href=\"#\">Esportes</a>");
        when(locator_mock.find_widget(target_stub)).thenReturn(result_stub);
        doReturn(new_writer_mock).when(locator).new_writer_wrapper("logs/001_widget_activator.txt");
        doReturn(new_writer_stub).when(locator).new_writer_wrapper("logs/001_widget_element.txt");
        doNothing().when(new_writer_mock).print(anyString());
        doNothing().when(new_writer_mock).close();
        doNothing().when(new_writer_stub).print(anyString());
        doNothing().when(new_writer_stub).close();

        result = locator.find_widget(target_stub);

        verify(new_writer_mock).print("<a href=\"#\">Esportes</a>");
        verify(new_writer_mock).close();
    }

    @Test
    public void test_html_logger_find_widget_should_save_widget_html_extract_if_a_widget_is_found () throws FileNotFoundException {
        Locator locator_mock = mock(Locator.class);
        HTMLLogLocator locator = spy(new HTMLLogLocator(locator_mock, "something/"));
        WebElement target_stub = mock(WebElement.class),
                   result_stub = mock(WebElement.class),
                   result;
        PrintWriter new_writer_mock = mock(PrintWriter.class),
                    new_writer_stub = mock(PrintWriter.class);
        when(result_stub.getAttribute("outerHTML")).thenReturn("<ul>\n<li>Dialog</li>\n<li>Items</li>\n</ul>");
        when(locator_mock.find_widget(target_stub)).thenReturn(result_stub);
        doReturn(new_writer_mock).when(locator).new_writer_wrapper("something/001_widget_element.txt");
        doReturn(new_writer_stub).when(locator).new_writer_wrapper("something/001_widget_activator.txt");
        doNothing().when(new_writer_mock).print(anyString());
        doNothing().when(new_writer_mock).close();
        doNothing().when(new_writer_stub).print(anyString());
        doNothing().when(new_writer_stub).close();

        result = locator.find_widget(target_stub);

        verify(new_writer_mock).print("<ul>\n<li>Dialog</li>\n<li>Items</li>\n</ul>");
        verify(new_writer_mock).close();
    }

    @Test
    public void test_html_logger_find_widget_should_not_save_any_file_if_no_widget_is_found () throws FileNotFoundException {
        Locator locator_mock = mock(Locator.class);
        HTMLLogLocator locator = spy(new HTMLLogLocator(locator_mock, "anotherthing/"));
        WebElement target_stub = mock(WebElement.class),
                   result;
        PrintWriter new_writer_mock = mock(PrintWriter.class),
                    new_writer_stub = mock(PrintWriter.class);
        when(locator_mock.find_widget(target_stub)).thenReturn(null);
        doReturn(new_writer_mock).when(locator).new_writer_wrapper("anotherthing/001_widget_element.txt");
        doReturn(new_writer_stub).when(locator).new_writer_wrapper("anotherthing/001_widget_activator.txt");
        doNothing().when(new_writer_mock).print(anyString());
        doNothing().when(new_writer_mock).close();
        doNothing().when(new_writer_stub).print(anyString());
        doNothing().when(new_writer_stub).close();

        result = locator.find_widget(target_stub);

        verify(locator, never()).new_writer_wrapper("anotherthing/001_widget_element.txt");
        verify(locator, never()).new_writer_wrapper("anotherthing/001_widget_activator.txt");
        assertEquals(null, result);
    }

    @Test
    public void test_html_logger_find_widget_should_save_multiple_files_with_different_names () throws FileNotFoundException {
        Locator locator_mock = mock(Locator.class);
        HTMLLogLocator locator = spy(new HTMLLogLocator(locator_mock, "another_folder/"));
        WebElement target_mock = mock(WebElement.class),
                   result_mock = mock(WebElement.class);
        PrintWriter writer_mock = mock(PrintWriter.class);
        when(locator_mock.find_widget(target_mock)).thenReturn(result_mock);
        doNothing().when(writer_mock).print(anyString());
        doNothing().when(writer_mock).close();
        doReturn(writer_mock).when(locator).new_writer_wrapper(anyString());

        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        locator.find_widget(target_mock);
        verify(locator).new_writer_wrapper("another_folder/001_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/001_widget_activator.txt");
        verify(locator).new_writer_wrapper("another_folder/002_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/002_widget_activator.txt");
        verify(locator).new_writer_wrapper("another_folder/003_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/003_widget_activator.txt");
        verify(locator).new_writer_wrapper("another_folder/004_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/004_widget_activator.txt");
        verify(locator).new_writer_wrapper("another_folder/005_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/005_widget_activator.txt");
        verify(locator).new_writer_wrapper("another_folder/006_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/006_widget_activator.txt");
        verify(locator).new_writer_wrapper("another_folder/007_widget_element.txt");
        verify(locator).new_writer_wrapper("another_folder/007_widget_activator.txt");
    }

}
