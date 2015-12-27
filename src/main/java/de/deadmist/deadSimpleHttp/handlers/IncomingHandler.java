package de.deadmist.deadSimpleHttp.handlers;

import de.deadmist.deLog.Logger;
import de.deadmist.deadSimpleHttp.errors.RequestException;
import de.deadmist.deadSimpleHttp.structures.Request;
import de.deadmist.deadSimpleHttp.structures.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles incoming connections and matches them to handlers that process them
 *
 * @author Jonas
 */
public class IncomingHandler extends Thread {

    private Socket socket;
    private HashMap<String, RequestHandler> handlers;

    /**
     * Creates new IncomingHandler
     *
     * @param socket   Socket of the connection
     * @param handlers HashMap with handlers, the best matching handler will be selected to handle the request
     */
    public IncomingHandler(Socket socket, HashMap<String, RequestHandler> handlers) {
        this.socket = socket;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());

            String file = request.getFile();

            Logger.d("INCOMING", "[" + socket.getRemoteSocketAddress() + "] Requested file: " + file);

            //Find longest matching handler
            int biggestMatch = 0;
            int biggestRegex = 0;
            RequestHandler biggestMatchingHandler = null;
            for (String pattern : this.handlers.keySet()) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(file);
                if (m.find()) {
                    int matchSize = m.group(0).length();
                    if (matchSize >= biggestMatch && pattern.length() > biggestRegex) {
                        biggestMatch = matchSize;
                        biggestMatchingHandler = this.handlers.get(pattern);
                        biggestRegex = pattern.length();
                    }
                }
            }

            //In case we don't find a handler use a provided one alerting us
            if (biggestMatch == 0 && biggestMatchingHandler == null) {
                biggestMatchingHandler = new HandlerMissingHandler(file);
            }

            Response response = biggestMatchingHandler.processRequest(request);

            try (OutputStream stream = socket.getOutputStream()) {
                stream.write(response.getBytes());
                stream.flush();
                socket.close();
            }

        } catch (IOException e) {
            Logger.d("INCOMING", "[" + socket.getRemoteSocketAddress() + "] IOException writing to socket", e);
            //  e.printStackTrace();
        } catch (RequestException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                Logger.w("INCOMING", "[" + socket.getRemoteSocketAddress() + "] Could not close socket");
                e.printStackTrace();
            }
        }
    }
}
