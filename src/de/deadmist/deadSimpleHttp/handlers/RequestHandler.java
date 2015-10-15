package de.deadmist.deadSimpleHttp.handlers;

import de.deadmist.deadSimpleHttp.errors.RequestException;
import de.deadmist.deadSimpleHttp.structures.Request;
import de.deadmist.deadSimpleHttp.structures.Response;

/**
 * Interface for RequestHandlers<br>
 * RequestHandler need to return a complete response
 *
 * @author Deadmist
 */
public interface RequestHandler {

    /**
     * Process the request and returns an Response<br>
     * The response is then send to the client
     *
     * @param request Request as received from the client
     * @return Response to be send to the client
     * @throws RequestException If an error occurs handling the request
     */
    Response processRequest(Request request) throws RequestException;

}
