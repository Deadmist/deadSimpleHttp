package de.deadmist.deadSimpleHttp.server;

import de.deadmist.deLog.Logger;
import de.deadmist.deadSimpleHttp.handlers.IncomingHandler;
import de.deadmist.deadSimpleHttp.handlers.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * A simple HTTP server, functionality is provided through {@link de.deadmist.deadSimpleHttp.handlers.RequestHandler RequestHandlers} that
 * are matched to the requested URL.<br>
 * <p>
 * Per default no handlers are registered on the server and every request will be responded with a 501 Not Implemented error.<br>
 * If you want to add functionality you need to supply a HashMap containing a regex for matching the URL and the handler.<br>
 * When a new connection is made the regex is matched against the url and the handler with the longest match will be selected to process the request.<br>
 * If two or more regex match the same amount of characters the longer regex is chosen. If two regex have the same length only one will be chosen.
 * </p>
 * <ul>
 * <li>You can use the provided {@link de.deadmist.deadSimpleHttp.handlers.provided.FileHandler FileHandler} with
 * the regex "^((\/|[a-zA-Z0-9]+))+\.[a-zA-Z0-9]+$" for providing basic serving of files.</li>
 * <li>To hide the 501 Not Implemented - Missing Handler error messages you can use the {@link de.deadmist.deadSimpleHttp.handlers.provided.CatchAllHandler CatchAllHandler}
 * catch anything not covered by another handler and return a 404 Not Found error instead.
 * </li>
 * </ul>
 *
 * @author Deadmist
 */
public class HTTPServer extends Thread {
    private boolean interrupted = false;
    private HashMap<String, RequestHandler> handlers;

    private ServerSocket serverSocket;

    /**
     * Creates a new server instance.<br>
     * Start the server by calling start()
     *
     * @param port     Port to listen on (80 is default for HTTP)
     * @param backlog  Size of the backlog, the bigger the more connections can be waiting
     * @param handlers List of handlers to handle requests
     * @throws IOException Thrown when no socket can be created
     */
    public HTTPServer(int port, int backlog, HashMap<String, RequestHandler> handlers) throws IOException {
        this.handlers = handlers;
        serverSocket = new ServerSocket(port, backlog);
    }

    /**
     * Creates a new server instance.<br>
     * Start the server by calling start()
     *
     * @param port    Port to listen on
     * @param backlog Size of the backlog, the bigger the more connections can be waiting
     * @throws IOException Thrown when no socket can be created
     */
    public HTTPServer(int port, int backlog) throws IOException {
        this.handlers = new HashMap<>();
        serverSocket = new ServerSocket(port, backlog);
    }

    /**
     * Adds a new RequestHandler
     *
     * @param pattern Pattern to match URLs against
     * @param handler Handler to handler requests
     */
    public void addHandler(String pattern, RequestHandler handler) {
        this.handlers.put(pattern, handler);
    }

    /**
     * This method is required to be public, but should never be called directly.
     * Use start() instead
     */
    @Override
    public void run() {
        Logger.i("HTTP", "Starting server");
        while (!interrupted) {
            try {
                Socket socket = serverSocket.accept();
                Logger.d("HTTP", "New connection");
                IncomingHandler handler = new IncomingHandler(socket, handlers);
                handler.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Logger.i("HTTP", "Stopping server");
    }

    /**
     * Stops the server
     */
    @Override
    public void interrupt() {
        super.interrupt();
        this.interrupted = true;
    }
}
