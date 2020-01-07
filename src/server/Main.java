package server;

import server.dispatchers.EtudiantDispatcher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                EtudiantDispatcher etudiantDispatcher = new EtudiantDispatcher(socket);
                etudiantDispatcher.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
