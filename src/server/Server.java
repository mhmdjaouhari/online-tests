package server;


import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    public static boolean stopServer=true;
    Socket socket=null;
    ServerSocket serverSocket = null;


    @Override
    public void run() {

        try {

            serverSocket=new ServerSocket(5000);
            socket=null;

            while (true) {
                // to stop the server
                if(!stopServer){
                    System.out.println("Closing server ....");
                    Thread.currentThread().interrupt();
                    break;
                }
                socket = serverSocket.accept();
                System.out.println("Client connected"+" info : "+socket);
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                Session session = new Session(socket,inputStream,outputStream);
                session.start();
            }
        }
        catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
