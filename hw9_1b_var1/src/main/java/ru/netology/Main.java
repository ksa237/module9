package ru.netology;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {


    public static void main(String[] args) {

        final var server = new Server();

        server.addHandler("GET", "/hello", (request, responseStream) -> {
            String response = "Hello, user";
            byte[] responseByte = response.getBytes();

            String headersString = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain; charset=utf-8\r\n" +
                    "Content-Length: " + responseByte.length + "\r\n" +
                    "\r\n";
            byte[] headersByte = headersString.getBytes();

            try {
                responseStream.write(headersByte);
                responseStream.write(responseByte);
                responseStream.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        });


        server.addHandler("GET", "/hello_html", (request, responseStream) -> {
            String response = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head><title>Page</title></head>\n" +
                    "<body><h1><b>Hello, user</b></h1></body>\n" +
                    "</html>";
            byte[] responseByte = response.getBytes();

            String headersString = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html; charset=utf-8\r\n" +
                    "Content-Length: " + responseByte.length + "\r\n" +
                    "\r\n";
            byte[] headersByte = headersString.getBytes();

            try {
                responseStream.write(headersByte);
                responseStream.write(responseByte);
                responseStream.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        });


        server.addHandler("GET", "/image", (request, responseStream) -> {
            Path filePath = Path.of(".", "public", "/Netologiya.png");
            long fileLength = Files.size(filePath);

            String headersString = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: image/png\r\n" +
                    "Content-Length: " + fileLength + "\r\n" +
                    "\r\n";
            byte[] headerByte = headersString.getBytes();


            try {
                responseStream.write(headerByte);
                Files.copy(filePath, responseStream);
                responseStream.flush();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        });

        server.listen(9999);

    }


}
