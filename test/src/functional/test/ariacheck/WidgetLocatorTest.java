package functional.test.ariacheck;

import ariacheck.WidgetLocator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WidgetLocatorTest {

    @Test
    public void test_widget_locator_should_find_css_only_tooltip () {
        WidgetLocator locator = new WidgetLocator();
    }

}
