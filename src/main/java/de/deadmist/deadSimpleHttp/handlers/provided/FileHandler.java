package de.deadmist.deadSimpleHttp.handlers.provided;

import de.deadmist.deLog.Logger;
import de.deadmist.deadSimpleHttp.errors.RequestException;
import de.deadmist.deadSimpleHttp.handlers.RequestHandler;
import de.deadmist.deadSimpleHttp.structures.Request;
import de.deadmist.deadSimpleHttp.structures.Response;
import de.deadmist.deadSimpleHttp.util.FileUtils;
import de.deadmist.deadSimpleHttp.util.StandardResponses;

import java.io.*;

/**
 * Provides provided handling of file requests.<br>
 * It simply looks for the requested files in the base directory path and presents them to the client.<br>
 * If the file could not be found a 404 File Not Found error page is generated<br>
 * <p>
 * Supported HTTP methods are GET and POST. However, POST form data is simply discarded.<br>
 * It also supports partial downloads via Range request header and 206 Partial Content response
 *
 * @author Deadmist
 */
public class FileHandler implements RequestHandler {
    private String baseDir;

    /**
     * Constructs a new handler
     *
     * @param baseDir The base directory where the handler looks for files
     */
    public FileHandler(String baseDir) {
        this.baseDir = baseDir;
    }


    @Override
    public Response processRequest(Request request) throws RequestException {
        //Check if methods are allowed
        if (!(request.getMethod().equals("GET")
                || request.getMethod().equals("POST"))) {
            //Return Method Not Allowed error
            Logger.i("FILEHANDLER", "Method not allowed, used method: " + request.getMethod());
            return StandardResponses.create405();
        }

        //Check if file exists
        File requestedFile;
        //Catch missing directory separator in request
        if (request.getFile().startsWith("/")) {
            requestedFile = new File(baseDir + request.getFile());
        } else {
            requestedFile = new File(baseDir + File.separator + request.getFile());
        }

        //Check if file exists and return a File Not Found if it doesn't
        if (!requestedFile.exists()) {
            Logger.i("FILEHANDLER", "File not found: " + requestedFile.getPath());
            return StandardResponses.create404();
        }

        //Check if the client wants the whole file or only parts of it
        String rangeHeader = request.parseHeaders().get("Range");
        if (rangeHeader == null || rangeHeader.toLowerCase().equals("bytes=0-")) {
            //Client wants whole file
            return readFile(requestedFile);
        } else {
            //Client wants only part of a file
            return readFile(requestedFile, rangeHeader);
        }
    }

    /**
     * Reads a file partially
     *
     * @param requestedFile File to read
     * @param rangeHeader   Content of the Range header, needed to figure out which part of the file to read
     * @return Response containing the file
     */
    private Response readFile(File requestedFile, String rangeHeader) {
        //Get start and end point to read
        int startByte, endByte, contentLenght;

        //Eliminate the "bytes=" portion of the header leaving us with two numbers "start-end"
        // or "start-" if no end is given
        rangeHeader = rangeHeader.toLowerCase().replace("bytes=", "");
        String[] ranges = rangeHeader.split("-");

        if (ranges.length == 2) {
            //Both start and end are given
            startByte = Integer.parseInt(ranges[0]);
            endByte = Integer.parseInt(ranges[1]);
            if (endByte > requestedFile.length() - 1) endByte = (int) (requestedFile.length() - 1);
        } else {
            startByte = Integer.parseInt(ranges[0]);
            endByte = (int) requestedFile.length() - 1;
        }

        contentLenght = endByte - startByte + 1;

        Logger.d("FILEHANDLER", String.format("Received Range request for %s. Ranges: %d-%d",
                requestedFile.getPath(), startByte, endByte));

        try (RandomAccessFile file = new RandomAccessFile(requestedFile, "r")) {
            byte[] buffer = new byte[contentLenght];

            file.seek(startByte);
            file.readFully(buffer);

            Response r = new Response(buffer);
            String mimetype = FileUtils.getMimeType(requestedFile);
            String contentRange = String.format("bytes %d-%d/%d", startByte, endByte, requestedFile.length());

            r.setStatus("206 Partial Content");
            r.setHeader("Connection", "close");
            r.setHeader("Content-Length", String.valueOf(contentLenght));
            r.setHeader("Content-Range", contentRange);
            r.setHeader("Accept-Ranges", "bytes");

            if (mimetype != null) r.setHeader("Content-Type", mimetype);

            return r;

        } catch (FileNotFoundException e) {
            Logger.e("FILEHANLDER", "File not found, this should have been caught earlier!", e);
            return StandardResponses.create404();
        } catch (IOException e) {
            Logger.e("FILEHANDLER", "Error reading file " + requestedFile.getAbsolutePath(), e);
            return StandardResponses.create500();
        }
    }

    /**
     * Reads a whole file from disk
     *
     * @param requestedFile File to read
     * @return Contains file and headers
     */
    private Response readFile(File requestedFile) {
        try (FileInputStream stream = new FileInputStream(requestedFile)) {

            byte[] buffer = new byte[(int) requestedFile.length()];

            stream.read(buffer);

            Response r = new Response(buffer);
            String mimetype = FileUtils.getMimeType(requestedFile);

            if (mimetype != null) r.setHeader("Content-Type", mimetype);
            r.setHeader("Connection", "close");
            r.setHeader("Content-Length", String.valueOf(buffer.length));
            r.setHeader("Accept-Ranges", "bytes");

            return r;
        } catch (FileNotFoundException e) {
            Logger.e("FILEHANLDER", "File not found, this should have been caught earlier!", e);
            return StandardResponses.create404();
        } catch (IOException e) {
            //return 500 Internal Server Error
            Logger.e("FILEHANDLER", "Error reading file " + requestedFile.getAbsolutePath(), e);
            return StandardResponses.create500();
        }
    }


}
