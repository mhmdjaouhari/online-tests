package server;

import server.dispatchers.ClientDispatcher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static boolean isActive=true;
    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (isActive) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientDispatcher clientDispatcher = new ClientDispatcher(socket);
                clientDispatcher.start();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }


    // to Stop or lunch Server
    public static void stopServer(boolean state)
    {
        isActive=state;
    }
}
