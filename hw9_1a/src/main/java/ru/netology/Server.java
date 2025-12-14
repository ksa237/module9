package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private int port;
    private ServerSocket serverSocket = null;

    public Server() {

    }

    public ServerSocket start() {
        try {

            this.serverSocket = new ServerSocket(port);
            //System.out.println("Запущен сервер, порт:" + port);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return this.serverSocket;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

