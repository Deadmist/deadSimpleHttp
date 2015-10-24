package de.deadmist.deadSimpleHttp.structures;

import de.deadmist.deadSimpleHttp.errors.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a HTTP request as received from a client
 *
 * @author Deadmist
 */
public class Request {
    private final String[] methods = new String[]{"GET", "POST", "HEAD", "PUT", "DELETE", "TRACE", "OPTIONS", "CONNECT"};
    private String method, url, queryString, file, httpVersion, body;
    private HashMap<String, String> headers;

    /**
     * Reads from an input stream and parses the data as a http request
     *
     * @param in Stream to read from
     * @throws IOException      Something went wrong reading the stream
     * @throws RequestException The data could not be parsed as a valid http request
     */
    public Request(InputStream in) throws IOException, RequestException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String firstline = reader.readLine();
        if (firstline == null) throw new IOException("No data received");
        this.method = firstline.split(" ")[0].toUpperCase();
        if (!Arrays.asList(methods).contains(this.method)) throw new RequestException("Method not supported");

        //Extract requested file path
        url = firstline.split(" ")[1];
        file = url.replace("%20", " ");
        this.queryString = file.split("\\?").length == 2 ? file.split("\\?")[1] : ""; //Assign the query string or "" if no query string exists
        file = file.split("\\?")[0]; //Remove the query string

        if (file.equals("/")) file = "index.html";

        this.httpVersion = firstline.split(" ")[2];
        this.httpVersion = this.httpVersion.split("/")[1];

        //Read the rest of the http headers
        this.headers = parseHeaders(reader);

        String tmp = "";
        if (headers.get("Content-Length") != null) {
            for (int i = 0; i < Integer.parseInt(headers.get("Content-Length")); i++) {
                tmp += (char) reader.read();
            }
            this.body = tmp;
        }

    }

    /**
     * Returns every header with value
     *
     * @return The headers
     */
    public HashMap<String, String> parseHeaders() {
        return headers;
    }

    /**
     * Returns the HTTP request method<br>
     * e.g. GET, HEAD
     *
     * @return HTTP request method, in all uppercase
     */
    public String getMethod() {
        return method.toUpperCase();
    }

    /**
     * Returns the requested URL, this includes the server address, port, protocol and path<br>
     * e.g. http://google.com/robot.txt
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the path of the requested file<br>
     * e.g. /robot.txt
     *
     * @return Path to requested file
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the path to the request file
     * @param file Path to requested file
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Returns the headers received with this request
     * @return Headers
     */
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    private HashMap<String, String> parseHeaders(BufferedReader reader) throws IOException {
        HashMap<String, String> header = new HashMap<>();

        String line;
        while (!(line = reader.readLine()).equals("")) {
            String[] head = line.split(":");
            header.put(head[0].trim(), head[1].trim());
        }

        return header;
    }

    /**
     * Returns the request body<br>
     * e.g. POST form data
     *
     * @return Request body
     */
    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method +
                ", url='" + url + '\'' +
                ", queryString='" + queryString + '\'' +
                ", file='" + file + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers + '\'' +
                ", body='" + body + '\'' +
                '}';

    }
}
