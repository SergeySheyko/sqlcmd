package integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ser on 31.08.2017.
 */
public class MyOutputStream extends OutputStream{
    private String log;

    @Override
    public void write(int b) throws IOException {
        log += String.valueOf((char) b);
    }

    public String getData() {
        return log;
    }
}
