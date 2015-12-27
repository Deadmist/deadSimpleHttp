package de.deadmist.deadSimpleHttp.util;

import de.deadmist.deadSimpleHttp.structures.Response;

/**
 * This class provides standard responses for different status codes and errors
 *
 * @author Deadmist
 */
public class StandardResponses {

    /**
     * Creates a 301 Moved Permanently response directing to the new location
     *
     * @param newLocation new location of the resource
     * @return Response with the status message
     */
    public static Response create301(String newLocation) {
        String body = "<html><head><title>301 Moved Permanently</title></head>" +
                "<body><h2>301 Moved Permanently</h2>" +
                "<p>This resource has been moved permanently to " + newLocation + " </p></body></html>";

        Response r = new Response("301 Moved Permanently", body);
        r.setHeader("Location", newLocation);

        return r;
    }

    /**
     * Create a 302 Found response
     *
     * @param newLocation new location of the resource
     * @return Response with the status message
     */
    public static Response create302(String newLocation) {
        String body = "<html><head><title>302 Found</title></head>" +
                "<body><h2>302 Found</h2>" +
                "<p>This resource can be found at " + newLocation + " </p></body></html>";

        Response r = new Response("302 Found", body);
        r.setHeader("Location", newLocation);

        return r;
    }

    /**
     * Create a 303 See Other response
     *
     * @param newLocation new location of the resource
     * @return Response with the status message
     */
    public static Response create303(String newLocation) {
        String body = "<html><head><title>303 See Other response</title></head>" +
                "<body><h2>303 See Other response</h2>" +
                "<p>This resource can be found at " + newLocation + " </p></body></html>";

        Response r = new Response("303 See Other response", body);
        r.setHeader("Location", newLocation);

        return r;
    }

    /**
     * Create a 400 Bad Request
     *
     * @param reason Reason for rejecting the request
     * @return Response with the status message
     */
    public static Response create400(String reason) {
        String body = "<html><head><title>400 Bad Request</title></head>" +
                "<body><h2>400 Bad Request</h2>" +
                "<p>Additional Reason: " + reason + "</p></body></html>";

        Response r = new Response("400 Bad Request", body);

        return r;
    }

    /**
     * Create a 401 Unauthorized
     *
     * @param challenge The challenge presented to the client through a WWW-Authenticate header
     * @return Response with the status message
     */
    public static Response create401(String challenge) {
        String body = "<html><head><title>401 Unauthorized</title></head>" +
                "<body><h2>401 Unauthorized</h2>" +
                "<p>Your are not authorized to access this page</p></body></html>";

        Response r = new Response("401 Unauthorized", body);
        if (!challenge.isEmpty()) {
            r.setHeader("WWW-Authenticate", challenge);
        }

        return r;
    }

    /**
     * Create a 403 Forbidden
     *
     * @param reason Reason the user is forbidden from this page
     * @return Response with the status message
     */
    public static Response create403(String reason) {
        String body = "<html><head><title>403 Forbidden</title></head>" +
                "<body><h2>403 Forbidden</h2>" +
                "<p>Your are forbidden from accessing this page</p></body></html>";

        Response r = new Response("403 Forbidden", body);

        return r;
    }

    /**
     * Create a 404 Not Found
     *
     * @return Response with the status message
     */
    public static Response create404() {
        String body = "<html><head><title>404 Not Found</title></head>" +
                "<body><h2>404 Not Found</h2>" +
                "<p>The page you were trying to access was not found</p></body></html>";

        Response r = new Response("404 Not Found", body);

        return r;
    }

    /**
     * Create a 405 Method Not Allowed
     *
     * @return Response with the status message
     */
    public static Response create405() {
        String body = "<html><head><title>405 Method Not Allowed</title></head>" +
                "<body><h2>405 Method Not Allowed</h2>" +
                "<p>The method you used for accessing this page is not allowed</p></body></html>";

        Response r = new Response("405 Method Not Allowed", body);

        return r;
    }

    /**
     * Create a 406 Not Acceptable
     *
     * @return Response with the status message
     */
    public static Response create406() {
        String body = "<html><head><title>406 Not Acceptable</title></head>" +
                "<body><h2>406 Not Acceptable</h2>" +
                "<p>The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request</p></body></html>";

        Response r = new Response("406 Not Acceptable", body);

        return r;
    }

    /**
     * Create a 418 I'm a teapot
     *
     * @return Response with the status message
     */
    public static Response create418() {
        String body = "<html><head><title>418 I'm a teapot</title></head>" +
                "<body><h2>418 I'm a teapot</h2>" +
                "<p>You tried brewing coffee, but I'm a teapot. I make tea, only tea and nothing else.</p></body></html>";

        Response r = new Response("418 I'm a teapot", body);

        return r;
    }

    /**
     * Create a 500 Internal Server Error
     *
     * @return Response with the status message
     */
    public static Response create500() {
        String body = "<html><head><title>500 Internal Server Error</title></head>" +
                "<body><h2>500 Internal Server Error</h2>" +
                "<p>There was an error processing your request.</p></body></html>";

        Response r = new Response("500 Internal Server Error", body);

        return r;
    }


}
