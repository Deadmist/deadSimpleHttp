package de.deadmist.deadSimpleHttp.errors;

/**
 * Thrown when there is an error parsing a HTTP request such as
 * <ul>
 * <li>The method requested is unknown or unsupported</li>
 * </ul>
 *
 * @author Deadmist
 */
public class RequestException extends Exception {
    public RequestException(String message) {
        super(message);
    }
}
