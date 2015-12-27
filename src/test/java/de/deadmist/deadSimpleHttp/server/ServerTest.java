package de.deadmist.deadSimpleHttp.server;

import de.deadmist.deLog.Logger;
import de.deadmist.deadSimpleHttp.handlers.provided.FileHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Deadmist on 27/12/15.
 */
public class ServerTest {

    static HTTPServer s;

    @BeforeClass
    public static void startServer() throws IOException {
        Logger.setErrorEnabled(true);
        Logger.setLogLevel("INFO");
        s = new HTTPServer(9999, 20, new HashMap<>());

        s.addHandler(".*", new FileHandler("www"));

        s.start();
    }

    @Test
    public void testReadingFile() throws IOException {
        URLConnection con = new URL("http://localhost:9999/index.html").openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String in = reader.readLine();

        assertEquals(in, "YAY!!!");
    }

    @Test
    public void testIndexing() throws IOException {
        URLConnection con = new URL("http://localhost:9999/").openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String in = reader.readLine();

        assertEquals(in, "YAY!!!");
    }

    @AfterClass
    public static void stopServer() throws InterruptedException {
        s.interrupt();
        s.join();
    }
}