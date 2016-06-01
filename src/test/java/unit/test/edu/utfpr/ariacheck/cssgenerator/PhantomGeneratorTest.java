package unit.test.edu.utfpr.ariacheck.cssgenerator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;

import edu.utfpr.ariacheck.cssgenerator.PhantomGenerator;

@RunWith(JUnit4.class)
public class PhantomGeneratorTest {

    @Test
    public void test_generate_return_string () throws IOException, InterruptedException {
        String url = "http://somewhere.com",
               result;
        Runtime runtime_mock = mock(Runtime.class);
        Process process_mock = mock(Process.class);
        InputStream is_mock = mock(InputStream.class);
        BufferedReader br_mock = mock(BufferedReader.class);

        doReturn(process_mock).when(runtime_mock).exec("phantomjs cssgenerator.js " + url);
        when(process_mock.exitValue()).thenReturn(0);
        doReturn(is_mock).when(process_mock).getInputStream();
        when(br_mock.readLine()).thenReturn("body > a > .something > #hover");

        PhantomGenerator generator = spy(new PhantomGenerator(runtime_mock));
        doReturn(br_mock).when(generator).getBufferedReader(is_mock);
        result = generator.generate("http://somewhere.com");

        verify(runtime_mock).exec("phantomjs cssgenerator.js " + url);
        verify(process_mock).waitFor();
        verify(process_mock).getInputStream();
        verify(br_mock).close();
        assertEquals("body > a > .something > #hover", result);
    }

    @Test
    public void test_generate_deal_with_errors_in_shellscript () throws IOException, InterruptedException {
        String url = "http://issued_url",
               result;
        Runtime runtime_mock = mock(Runtime.class);
        Process process_mock = mock(Process.class);

        doReturn(process_mock).when(runtime_mock).exec("phantomjs cssgenerator.js " + url);
        when(process_mock.exitValue()).thenReturn(2);

        PhantomGenerator generator = spy(new PhantomGenerator(runtime_mock));
        result = generator.generate(url);

        verify(runtime_mock).exec("phantomjs cssgenerator.js " + url);
        verify(process_mock).waitFor();
        assertEquals(null, result);
    }

}
