package de.deadmist.deadSimpleHttp.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by Deadmist on 28/12/15.
 */
public class FileUtilsTest {

    @Test
    public void testGetMimeType() throws Exception {
        assertEquals("text/css", FileUtils.getMimeType(new File("test.css")));
        assertEquals("text/html", FileUtils.getMimeType(new File("htmlTest.html")));
        //assertEquals("audio/mpeg", FileUtils.getMimeType(new File("test.mp3")));
    }
}