package de.deadmist.deadSimpleHttp.server;

import de.deadmist.deLog.Logger;
import de.deadmist.deadSimpleHttp.handlers.IncomingHandler;
import de.deadmist.deadSimpleHttp.handlers.RequestHandler;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * A simple HTTPS server, functionality is provided through {@link de.deadmist.deadSimpleHttp.handlers.RequestHandler RequestHandlers} that
 * are matched to the requested URL.<br>
 * <p>
 * This server communicates over HTTPS, you need to supply it with a certificate and private key in a javakeystore file.
 * </p>
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
public class HTTPSServer extends Thread {
    private boolean interrupted = false;
    private HashMap<String, RequestHandler> handlers;

    private SSLServerSocket serverSocket;

    /**
     * Creates a new server instance.<br>
     * Start the server by calling start()
     *
     * @param port             Port to listen on (443 is default for HTTPS)
     * @param backlog          Size of the backlog, the bigger the more connections can be waiting
     * @param handlers         List of handlers to handle requests
     * @param keyStore         Path to the keystore file containing certificate and private key
     * @param keyStorePassword Password for the keystore
     * @throws IOException Thrown when no socket can be created
     */
    public HTTPSServer(int port, int backlog, HashMap<String, RequestHandler> handlers, String keyStore, String keyStorePassword) throws IOException {
        this.handlers = handlers;

        System.setProperty("javax.net.ssl.keyStore", keyStore);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

        SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port, backlog);
        String[] suites = serverSocket.getSupportedCipherSuites();
        serverSocket.setEnabledCipherSuites(suites);
    }

    /**
     * Creates a new server instance.<br>
     * Start the server by calling start()
     *
     * @param port             Port to listen on
     * @param backlog          Size of the backlog, the bigger the more connections can be waiting
     * @param keyStore         Path to the keystore file containing certificate and private key
     * @param keyStorePassword Password for the keystore
     * @throws IOException Thrown when no socket can be created
     */
    public HTTPSServer(int port, int backlog, String keyStore, String keyStorePassword) throws IOException {
        this.handlers = new HashMap<>();

        System.setProperty("javax.net.ssl.keyStore", keyStore);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

        SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port, backlog);
        String[] suites = serverSocket.getSupportedCipherSuites();
        serverSocket.setEnabledCipherSuites(suites);
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
        Logger.i("HTTPS", "Starting server");
        while (!interrupted) {
            try {
                Socket socket = serverSocket.accept();
                Logger.d("HTTPS", "New connection");
                IncomingHandler handler = new IncomingHandler(socket, handlers);
                handler.run();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.i("HTTPS", "Stopping server");
            }

        }
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
