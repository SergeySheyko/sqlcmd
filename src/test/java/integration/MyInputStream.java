package integration;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ser on 31.08.2017.
 */
public class MyInputStream extends InputStream {
    private String line;

    @Override
    public int read() throws IOException {
        if (line.length() == 0) {
            return -1;
        }
        char ch = line.charAt(0);
        line = line.substring(1);
        return (int) ch;
    }

    public void add(String line) {
        if (this.line == null) {
            this.line = line;
        } else {
            this.line += "\n"+line;
        }
    }
}
