import de.deadmist.deLog.Logger;
import de.deadmist.deadSimpleHttp.handlers.provided.FileHandler;
import de.deadmist.deadSimpleHttp.server.HTTPServer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Deadmist on 24/10/15.
 */
public class testMain {

    public static void main(String args[]) throws InterruptedException, IOException {
        Logger.setAll2StdOut(true);
        Logger.setLogLevel("debug");

        HTTPServer s = new HTTPServer(9999, 20, new HashMap<>());

        s.addHandler(".*", new FileHandler("www"));

        s.start();


        while(true) {
            Thread.sleep(100000);
        }
        //s.interrupt();
    }

}
