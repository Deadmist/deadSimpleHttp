package de.deadmist.deadSimpleHttp.util;

import de.deadmist.deLog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Provides utilities for files
 *
 * @author Deadmist
 */
public class FileUtils {

    /**
     * Parses a file and returns the mimetype if it could be found
     *
     * @param file The file to get the mimetype of
     * @return String with the mimetype, or NULL if the mimetype could not be determined or an error occured
     */
    public static String getMimeType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            Logger.e("GETMIMETYPE", "Error parsing content type from file: " + file.getAbsolutePath(), e);
            return null;
        }
    }

}
