package jga.jsonadd;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestMain
{
    private static CharStream getCharStream(String resourceName) {
        System.out.println("resourceName: " + resourceName);
        InputStream is =
                TestMain.class.getResourceAsStream(resourceName);
        if(is == null)
            throw new RuntimeException("resource not found: " + resourceName);
        InputStreamReader reader = new InputStreamReader(is);
        try {
           return CharStreams.fromReader(reader);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test public void testParseAndPrint1() {
        CharStream cs = getCharStream("/json_examples/example1.json");
        Main.parseAndPrint(cs);
    }

    @Test public void testParseAndPrint2() {
        CharStream cs = getCharStream("/json_examples/example2.json");
        Main.parseAndPrint(cs);
    }
}
