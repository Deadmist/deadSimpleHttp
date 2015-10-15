package de.deadmist.deadSimpleHttp.handlers;

import de.deadmist.deadSimpleHttp.errors.RequestException;
import de.deadmist.deadSimpleHttp.structures.Request;
import de.deadmist.deadSimpleHttp.structures.Response;

/**
 * This handler is called when no handler matches the URL<br>
 * It sends an 501 Not Implemented error message stating the URL and that no handler has been configured.
 *
 * @author Deadmist
 */
public class HandlerMissingHandler implements RequestHandler {
    private String file;

    /**
     * Creates new handler
     *
     * @param url URL that was requested
     */
    public HandlerMissingHandler(String url) {
        this.file = url;
    }

    @Override
    public Response processRequest(Request request) throws RequestException {
        String body = "<html><head><title>501 Not Implemented</title></head><body>" +
                "<h2>501 Not Implemented - Missing Handler</h2>" +
                "<p>The server is not configured to handle this request.<br>" +
                "No handler matches the URL (" + file + ")</p></body></html>";

        Response r = new Response("501 Not Implemented", body);
        return r;
    }
}
