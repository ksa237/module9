package ru.netology;

import org.apache.hc.core5.http.NameValuePair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        final var server = new Server();

        server.addHandler("GET", "/hello", (request, responseStream) -> {

            List<NameValuePair> paramPairs = request.paramPairs();
            String name = "user";
            if (!paramPairs.isEmpty()) {
                for (NameValuePair p : paramPairs) {
                    if ("name".equalsIgnoreCase(p.getName())) {
                        name = p.getValue();
                    }
                }
            }

            String response = "Hello, " + name + "!";
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

            List<NameValuePair> paramPairs = request.paramPairs();
            String name = "user";
            if (!paramPairs.isEmpty()) {
                for (NameValuePair p : paramPairs) {
                    if ("name".equalsIgnoreCase(p.getName())) {
                        name = p.getValue();
                    }
                }
            }

            String response = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head><title>Page</title></head>\n" +
                    "<body><h1><b>Hello, "+name+"</b></h1></body>\n" +
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

            List<NameValuePair> paramPairs = request.paramPairs();
            String currentFileName = "Netologiya_%.png";
            String size = "1";
            if (!paramPairs.isEmpty()) {
                for (NameValuePair p : paramPairs) {
                    if ("size".equalsIgnoreCase(p.getName())) {
                        size = p.getValue();
                        boolean isValidSize = Arrays.asList("1", "2").contains(size);
                        if (!isValidSize) {
                            size = "1";
                        }
                    }
                }
            }
            currentFileName = currentFileName.replaceAll("%", size);

            Path filePath = Path.of(".", "public", currentFileName);
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
