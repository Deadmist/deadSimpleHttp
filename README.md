# deadSimpleHttp
The goal of this project is to provide a simple to setup HTTP(S) server that is easily expandable through custom handlers.
It has two central classes that handle HTTP(S) connections and parsing of requests. Requests are then forwarded to an RequestHandler and processed, sending the response is then handled by the server classes.

###RequestHandlers, how do they work?

RequestHandlers process incoming requests and create an appropriate response.
Parsing of incoming requests and sending of responses is handled by the library,
the RequestHandler is simply passed an Request object and returns a Response object.

###How Handlers are selected

Handlers are selected by matching a regex against the requested path. The regex with the longest match will be chosen and
it's associated handler executed. Should multiple regex have the same match length the longer regex will be chosen,
should multiple regex have the same length only one is chosen at random.
If no handler matches a generic handler is called and returns a 501 Not Implemented response, telling the client
that no handler was found.

###Built in utilities

To make some tasks work out of the box deadSimpleHttp provides some hopefully helpful classes.

#####FileHandler

A FileHandler class is provided that can serve files from disk, it even has support for partial downloads.
The FileHandler searches a directory for the requested file and returns it if found.

#####StandardResponses

The StandardResponses class provides methods for easily generating standard error/status messages such as "302 Found", "404 Not Found", "500 Internal Server Error" and more.

#####CatchAllHandler

This class is intended for hiding the missing handler error messages from the client.
Use it by registering a regex that matches everything but the paths you have covered with other handlers.
It will always generate a "404 Not Found" error, regardless of the request.

###HTTPS

To establish a HTTPS connection you need to supply a certificate and it's private key in form of a java key store (.jks) file.

###Example

This is an example for a setting up a simple server that listens for http/https and serves a webpage from disk

```java
public class main {
    public static void main(String[] args) throws IOException {

            //This will hold our RequestHandlers
            HashMap<String, RequestHandler> handlers = new HashMap<>();


            //Create a new request handler
            //FileHandler is a built-in handler for files
            //It takes a base directory as argument and searches for requested files in that base directory
            RequestHandler fileHandler = new FileHandler("www");

            //The built-in CatchAll handler just return 404 Not Found on every request
            RequestHandler catchAll = new CatchAllHandler();

            //Add the fileHandler and it's regex to our hashmap
            //The regex matches /path/to/file.ext
            handlers.put("^((\\/|[a-zA-Z0-9]+))+\\.[a-zA-Z0-9]+$", fileHandler);

            //Add our catchAll handler
            //The regex matches everything,
            //but since it's shorter than the fileHandler regex the fileHandler has priority
            handlers.put("^.*$", catchAll);

            //Create an instance of HTTPServer that listens on port 80 and give it our handlers
            HTTPServer n = new HTTPServer(80, 20, handlers);

            //HTTPS connection come over port 443, we also supply the path to a keystore and it's password
            HTTPSServer s = new HTTPSServer(443, 20, handlers, "keystore.jks", "password");

            //Start the server thread
            n.start();
            s.start();
    }
```

###Logging

This library uses [deLog](https://github.com/Deadmist/deLog) for logging.
You need to put the deLog.jar with this jar for it to work correctly
