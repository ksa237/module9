import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final Map<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();

    public void addHandler(String method, String path, Handler handler) {
        handlers.computeIfAbsent(method, k -> new ConcurrentHashMap<>()).put(path, handler);
    }

    public void listen(int port) {
        ExecutorService threadPool = Executors.newFixedThreadPool(64);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private void handleConnection(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            Request request = parseRequest(in);
            if (request == null) {
                sendResponse(out, "400 Bad Request", "text/plain", "Bad Request".getBytes(StandardCharsets.UTF_8));
                return;
            }

            Handler handler = handlers
                    .getOrDefault(request.method(), new ConcurrentHashMap<>())
                    .get(request.path());

            if (handler != null) {
                handler.handle(request, out);
            } else {
                sendResponse(out, "404 Not Found", "text/plain", "Not Found".getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignore) {}
        }
    }

    private Request parseRequest(BufferedReader in) throws IOException {
        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        String[] parts = requestLine.split(" ");
        if (parts.length < 2) return null;

        String method = parts[0];
        String path = parts[1];

        Map<String, String> headers = new ConcurrentHashMap<>();
        String headerLine;
        int contentLength = 0;

        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if (headerParts.length == 2) {
                String headerName = headerParts[0].trim();
                String headerValue = headerParts[1].trim();
                headers.put(headerName, headerValue);
                if ("Content-Length".equalsIgnoreCase(headerName)) {
                    try {
                        contentLength = Integer.parseInt(headerValue);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        String body = null;
        if (contentLength > 0) {
            char[] bodyChars = new char[contentLength];
            int read = 0;
            while (read < contentLength) {
                int r = in.read(bodyChars, read, contentLength - read);
                if (r == -1) break;
                read += r;
            }
            body = new String(bodyChars, 0, read);
        }

        return new Request(method, path, headers, body);
    }

    private void sendResponse(BufferedOutputStream out, String status, String contentType, byte[] body) throws IOException {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), false);
        pw.printf("HTTP/1.1 %s\r\n", status);
        pw.printf("Content-Type: %s\r\n", contentType);
        pw.printf("Content-Length: %d\r\n", body.length);
        pw.print("\r\n");
        pw.flush();
        out.write(body);
        out.flush();
    }

    @FunctionalInterface
    public interface Handler {
        void handle(Request request, BufferedOutputStream responseStream) throws IOException;
    }

    public static class Request {
        private final String method;
        private final String path;
        private final Map<String, String> headers;
        private final String body;

        public Request(String method, String path, Map<String, String> headers, String body) {
            this.method = method;
            this.path = path;
            this.headers = headers;
            this.body = body;
        }

        public String method() {
            return method;
        }

        public String path() {
            return path;
        }

        public Map<String, String> headers() {
            return headers;
        }

        public String body() {
            return body;
        }
    }

    public static void main(String[] args){
        final var server = new Server();
        // код инициализации сервера (из вашего предыдущего ДЗ)

        // добавление хендлеров (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                String response = "the GET method is selected";
                byte[] responseByte = response.getBytes();

                String headersString = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain; charset=utf-8\r\n" +
                        "Content-Length: " + response.length() + "\r\n" +
                        "\r\n";
                byte[] headersByte = headersString.getBytes();



                try {


                    responseStream.write(headersByte);
                    responseStream.write(responseByte);
                    responseStream.flush();



                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
//        server.addHandler("POST", "/messages", new Handler() {
//            public void handle(Request request, BufferedOutputStream responseStream) {
//                // TODO: handlers code
//            }
//        });

        server.listen(9999);
    }

}
