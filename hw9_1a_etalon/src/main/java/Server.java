import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    threadPool.execute(() -> handleClient(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdownNow();
        }
    }

    private void handleClient(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = socket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                sendResponse(out, 400, "Bad Request", "Empty request");
                return;
            }

            String[] parts = requestLine.split(" ");
            if (parts.length != 3) {
                sendResponse(out, 400, "Bad Request", "Malformed request line");
                return;
            }

            String method = parts[0];
            String path = parts[1];
            String version = parts[2];

            if (!"GET".equals(method)) {
                sendResponse(out, 405, "Method Not Allowed", "Only GET supported");
                return;
            }

            // Чтение заголовков
            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
                // Можно обработать заголовки если нужно
            }

            String body = "<h1>Server is working!</h1>";
            sendResponse(out, 200, "OK", body);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(OutputStream out, int statusCode, String statusText, String body) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        String response =
                "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                        "Content-Type: text/html; charset=utf-8\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "\r\n";
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }

    public static void main(String[] args) {
        new Server(9999).start();
    }
}