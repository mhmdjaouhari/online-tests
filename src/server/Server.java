package server;


import GUI.admin.ConsolleController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class  Server implements Runnable{
    public static boolean stopServer=true;

    @Override
    public void run() {
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                if(!stopServer){
                    System.out.println("Closing server ....");
                    serverSocket.close();
                    Thread.currentThread().interrupt();
                    break;
                }
                socket = serverSocket.accept();
                System.out.println("Client connected"+" info : "+socket);
                //ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                //ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                Session session = new Session(socket);
                session.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean isStopServer() {
        return stopServer;
    }

    public static void setStopServer(boolean stopServer) {
        Server.stopServer = stopServer;
    }
}