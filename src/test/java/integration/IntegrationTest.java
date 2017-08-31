package integration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import sqlcmd.controller.Main;

import java.io.PrintStream;

public class IntegrationTest {
    private static MyInputStream in;
    private static MyOutputStream out;

    @BeforeClass
    public static void setup(){
        in = new MyInputStream();
        out = new MyOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExit(){
        in.add("help");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("",out.getData());
    }
}
