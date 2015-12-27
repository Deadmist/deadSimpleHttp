package de.deadmist.deadSimpleHttp.structures;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Represents a HTTP response that can be send to a client<br>
 * Every response has the date and server headers set automatically.
 *
 * @author Deadmist
 */
public class Response {

    private static String serverName = "deadSimpleHTTP";

    private String status, httpVersion;
    private byte[] body;
    private HashMap<String, String> headers;

    /**
     * Creates an empty response without status code or version<br>
     * Sets date header and server header (default "deadSimpleHTTP")
     */
    public Response() {
        this.headers = new HashMap<>();
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        //Set data header
        setHeader("Date", format.format(d));

        //Set connection header to close on provided
        setHeader("Connection", "close");

        //Set server header
        setHeader("Server", serverName);
    }

    /**
     * Creates a response with status code 200 OK and includes the body
     *
     * @param binaryBody Binary data to include as the response body
     */
    public Response(byte[] binaryBody) {
        this();
        this.status = "200 OK";
        setHttpVersion(1.1f);
        this.body = binaryBody;
    }

    /**
     * Creates a response with status code 200 OK and includes the body
     *
     * @param body String that represents the body
     */
    public Response(String body) {
        this();
        this.status = "200 OK";
        setHttpVersion(1.1f);
        this.body = body.getBytes();
    }

    /**
     * Creates a response
     *
     * @param status Status code <b>and</b> message, e.g. "200 OK"
     * @param body   String body to include
     */
    public Response(String status, String body) {
        this();
        setHttpVersion(1.1f);
        this.status = status;
        this.body = body.getBytes();
    }

    /**
     * Creates a response
     *
     * @param status      Status code <b>and</b> message, e.g. "200 OK"
     * @param body        String body to include
     * @param httpVersion sets the http version (1.0 or 1.1)
     */
    public Response(String status, String body, float httpVersion) {
        this.status = status;
        setHttpVersion(httpVersion);
        this.body = body.getBytes();
    }

    /**
     * Sets the server name that gets send with every response
     * default is "deadSimpleHTTP"
     *
     * @param serverName the servers name
     */
    public static void setServerName(String serverName) {
        Response.serverName = serverName;
    }

    /**
     * Gets this response as a byte array<br>
     * Use this for sending data to a client, otherwise binary data (images etc.) will get corrupted
     *
     * @return Byte representation of the response
     */
    public byte[] getBytes() {
        String tmp = this.httpVersion + " " + this.status + "\r\n";
        for (String header : headers.keySet()) {
            tmp += header + ": " + headers.get(header) + "\r\n";
        }
        //Add length header if not set
        if (getHeader("Content-Length") == null) {
            setHeader("Content-Length", String.valueOf(body.length));
        }

        tmp += "\r\n";

        byte[] head = tmp.getBytes();
        byte[] total = new byte[head.length + body.length];

        System.arraycopy(head, 0, total, 0, head.length);
        System.arraycopy(body, 0, total, head.length, body.length);

        return total;

    }

    /**
     * Returns a string representation of the HTTP response including header and body<br>
     * <p>
     * <b>DO NOT USE THIS TO SEND DATA</b><br>
     * The conversion to a string will break any binary data that may be included in the body. Only use this method for
     * debugging. See {@link #getBytes()} for sending data to a client.
     *
     * @return String representation of this response
     */
    public String toString() {
        String m = this.httpVersion + " " + this.status + "\r\n";
        for (String header : headers.keySet()) {
            m += header + ": " + headers.get(header) + "\r\n";
        }
        if (getHeader("Content-Length") == null) {
            setHeader("Content-Length", String.valueOf(body.length));
        }

        m += "\r\n";
        m += new String(body);

        return m;
    }

    /**
     * Set status code and message (e.g. "404 Not Found")
     *
     * @param status status code and message
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the HTTP version (1.0 or 1.1)
     *
     * @param httpVersion the HTTP version
     */
    public void setHttpVersion(float httpVersion) {
        this.httpVersion = "HTTP/" + new Float(httpVersion);
    }

    /**
     * Sets the body as a byte array
     *
     * @param body the body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * Sets a headers value
     *
     * @param header Header to set (e.g. "Content-Length")
     * @param value  Value of the header field
     */
    public void setHeader(String header, String value) {
        this.headers.put(header, value);
    }

    private String getHeader(String header) {
        return this.headers.get(header);
    }
}
