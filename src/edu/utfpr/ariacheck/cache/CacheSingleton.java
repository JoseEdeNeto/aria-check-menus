package edu.utfpr.ariacheck.cache;

import java.util.List;
import java.util.ArrayList;

public class CacheSingleton {

    private List <String> cache_list;

    public CacheSingleton () {
        this.cache_list = new ArrayList <String> ();
    }

    public void store (String value) {
        this.cache_list.add(value);
    }

    public boolean is_there (String value) {
        for (String cache_values : this.cache_list) {
            if (cache_values.contains(value)) return true;
        }
        return false;
    }

    private static CacheSingleton instance = null;

    public static CacheSingleton createInstance () {
        if (CacheSingleton.instance == null)
            CacheSingleton.instance = new CacheSingleton();
        return CacheSingleton.instance;
    }

}
