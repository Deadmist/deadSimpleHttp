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
            String type = Files.probeContentType(file.toPath());
            if (type == null) {
                type = mimeTypeByExtension(file.getName());
            }
            return type;
        } catch (IOException e) {
            Logger.e("GETMIMETYPE", "Error parsing content type from file: " + file.getAbsolutePath(), e);
            return null;
        }
    }

    private static String mimeTypeByExtension(String file) {
        String ext = file.split("\\.")[file.split("\\.").length - 1];
        switch (ext) {
            case "css":     return "text/css";
            case "html":
            case "htm":
            case "shtml":   return "text/html";
            case "js":      return "text/javascript";
            case "png":     return "image/png";
            case "jpg":
            case "jpeg":    return "image/jpeg";
            case "zip":     return "application/zip";
            case "mp3":     return "audio/mpeg";
            default: return "";
        }
    }

}
