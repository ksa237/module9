import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        final var server = HttpServer.createSimpleServer("static", 9999);
        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            @Override
            public void service(Request request, Response response) throws Exception {

                response.getWriter().write("OK");
            }
        }, "/api");

        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        Thread.currentThread().join();

    }

}
