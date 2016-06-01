package unit.test.edu.utfpr.ariacheck.cssgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.InputStream;
import java.io.BufferedReader;

import edu.utfpr.ariacheck.cssgenerator.PhantomGenerator;

@RunWith(JUnit4.class)
public class PhantomGeneratorTest {

    @Test
    public void test_generate_return_string () throws Exception {
        String url = "http://somewhere.com",
               result;
        Runtime runtime_mock = mock(Runtime.class);
        Process process_mock = mock(Process.class);
        InputStream is_mock = mock(InputStream.class);
        BufferedReader br_mock = mock(BufferedReader.class);

        doReturn(process_mock).when(runtime_mock).exec(
                "phantomjs RIA_menu_event_controller.js " + url);
        when(process_mock.exitValue()).thenReturn(0);
        doReturn(is_mock).when(process_mock).getInputStream();
        when(br_mock.readLine()).thenReturn("body > a > .something > #hover");

        PhantomGenerator generator = spy(new PhantomGenerator(runtime_mock));
        doReturn(br_mock).when(generator).getBufferedReader(is_mock);
        result = generator.generate("http://somewhere.com");

        verify(runtime_mock).exec("phantomjs RIA_menu_event_controller.js " + url);
        verify(process_mock).waitFor();
        verify(process_mock).getInputStream();
        verify(br_mock).close();
        assertEquals("body > a > .something > #hover", result);
    }

    @Test
    public void test_generate_deal_with_errors_in_shellscript () throws Exception {
        String url = "http://issued_url",
               result;
        Runtime runtime_mock = mock(Runtime.class);
        Process process_mock = mock(Process.class);
        InputStream is_mock = mock(InputStream.class);
        BufferedReader br_mock = mock(BufferedReader.class);

        doReturn(process_mock).when(runtime_mock).exec("phantomjs RIA_menu_event_controller.js " + url);
        when(process_mock.exitValue()).thenReturn(2);
        doReturn(is_mock).when(process_mock).getErrorStream();
        when(br_mock.readLine()).thenReturn("some error message related to phantomjs execution...")
                                .thenReturn(null);

        PhantomGenerator generator = spy(new PhantomGenerator(runtime_mock));
        doReturn(br_mock).when(generator).getBufferedReader(is_mock);
        try {
            generator.generate(url);
            fail("There should be an exception raised from generate method");
        } catch (Exception e) {
            verify(runtime_mock).exec("phantomjs RIA_menu_event_controller.js " + url);
            verify(process_mock).waitFor();
            verify(br_mock).close();
            assertEquals("\nsome error message related to phantomjs execution...", e.getMessage());
        }
    }

}
