package de.deadmist.deadSimpleHttp.handlers.provided;

import de.deadmist.deadSimpleHttp.errors.RequestException;
import de.deadmist.deadSimpleHttp.handlers.RequestHandler;
import de.deadmist.deadSimpleHttp.structures.Request;
import de.deadmist.deadSimpleHttp.structures.Response;
import de.deadmist.deadSimpleHttp.util.StandardResponses;

/**
 * This handler is intended to provide a generic 404 Not Found error to any request<br>
 * Use it if you want to hide the 501 Not Implemented - Missing Handler errors
 *
 * @author Deadmist
 */
public class CatchAllHandler implements RequestHandler {
    @Override
    public Response processRequest(Request request) throws RequestException {
        return StandardResponses.create404();
    }
}
