package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final int POOL_SIZE = 64;
    private static final int PORT = 9999;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(POOL_SIZE);

        Server server = new Server();
        server.setPort(PORT);
        ServerSocket serverSocket = server.start();

        try {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new Client(clientSocket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }

    }
}


