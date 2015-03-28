package unit.test.edu.utfpr.ariacheck.cache;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.utfpr.ariacheck.cache.CacheSingleton;

@RunWith(JUnit4.class)
public class CacheSingletonTest {

    @Test
    public void test_cache_should_have_static_method_createInstance_and_implement_singleton_behavior () {
        CacheSingleton cache = CacheSingleton.createInstance(),
                       cache2 = CacheSingleton.createInstance();
        assertNotNull(cache);
        assertEquals(cache, cache2);
    }

    @Test
    public void test_cache_should_store_and_tell_if_there_was_a_cache_hit_for_strict_equal_comparisson () {
        CacheSingleton cache = CacheSingleton.createInstance();
        cache.store("abobrinha");
        cache.store("pepino");
        cache.store("limao");

        assertFalse(cache.is_there("iodsfijoasfd"));
        assertTrue(cache.is_there("limao"));
        assertFalse(cache.is_there("iiiiii"));
        assertTrue(cache.is_there("pepino"));
        assertFalse(cache.is_there("aaaaaaaa"));
        assertTrue(cache.is_there("abobrinha"));
    }

    @Test
    public void test_cache_should_store_and_tell_if_there_was_a_cache_hit_for_substring_comparisson () {
        CacheSingleton cache = CacheSingleton.createInstance();
        cache.store("abobrinha");
        cache.store("pepino");
        cache.store("limao");

        assertTrue(cache.is_there("mao"));
        assertTrue(cache.is_there("pi"));
        assertTrue(cache.is_there("bri"));
    }

    @Test
    public void test_cache_should_chear_all_stored_entries () {
        CacheSingleton cache = CacheSingleton.createInstance();
        cache.store("abobrinha");
        cache.store("pepino");
        cache.store("limao");
        cache.clear();

        assertFalse(cache.is_there("mao"));
        assertFalse(cache.is_there("pi"));
        assertFalse(cache.is_there("bri"));
    }
}
