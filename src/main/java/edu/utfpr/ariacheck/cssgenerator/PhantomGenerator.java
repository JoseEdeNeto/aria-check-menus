package edu.utfpr.ariacheck.cssgenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class PhantomGenerator {
    private Runtime runtime = null;

    private static String PHANTOM_COMMAND = "phantomjs cssgenerator.js ";

    public PhantomGenerator (Runtime runtime) {
        this.runtime = runtime;
    }

    public String generate (String url) throws IOException, InterruptedException {
        String selector = "";
        Process p = this.runtime.exec(PhantomGenerator.PHANTOM_COMMAND + url);
        p.waitFor();

        if (p.exitValue() > 0)
            return null;

        BufferedReader br = this.getBufferedReader(p.getInputStream());
        selector = br.readLine();
        br.close();
        return selector;
    }

    public BufferedReader getBufferedReader (InputStream is) {
        return new BufferedReader(
                new InputStreamReader(is));
    }
}
