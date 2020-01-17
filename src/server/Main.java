package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                Session session = new Session(socket);
                session.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
