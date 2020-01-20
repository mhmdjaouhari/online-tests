package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Receiving...");
                Session session = new Session(socket);
                session.start();
            }
        } catch (IOException e) {
            try {
                socket.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
