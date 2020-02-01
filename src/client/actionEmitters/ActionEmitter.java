package client.actionEmitters;

import util.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;

abstract public class ActionEmitter {

    private Socket socket;
    protected ObjectInputStream inputStream;    // Protected is the best solution for the encapsulation so far
    protected ObjectOutputStream outputStream;
    private Boolean isClientOnline;
    private Boolean connectedToServer;

    public ActionEmitter(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        isClientOnline = true;
        connectedToServer = true;
    }


    public boolean exit() {

        try {
            if(!isServerOnline()){
                Request request = new Request(Action.EXIT, Role.ETUDIANT);
                outputStream.writeObject(request);
                outputStream.close();
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Boolean getClientOnline() {
        return isClientOnline;
    }

    public void setClientOnline(Boolean clientOnline) {
        isClientOnline = clientOnline;
    }

    public Boolean getConnectedToServer() {
        return connectedToServer;
    }

    public void setConnectedToServer(Boolean connectedToServer) {
        this.connectedToServer = connectedToServer;
    }

    public  boolean isServerOnline(){
        boolean isAlive=false;
        SocketAddress socketAddress = new InetSocketAddress(Constants.HOST,Constants.PORT);
        Socket TestSocket = new Socket();
        try{
            TestSocket.connect(socketAddress);
            TestSocket.close();
            isAlive = true;
        }catch (IOException e){
            connectedToServer = false;
            System.out.println("Server unreachable");
        }
        return isAlive;
    }

    public void reConnect() {
        try {
            System.out.println("okk");
            socket = new Socket(Constants.HOST,Constants.PORT);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            connectedToServer = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Response post(Request request) {
        Response response = null;
        try{
            if(!isServerOnline()){
                System.out.println("Server is offline, please try later...");
                outputStream.reset();
                return null;
            }
            if(!connectedToServer){
                System.out.println("switched...");
                reConnect();
            }
            outputStream.writeObject(request);
            response = (Response) inputStream.readObject();
            outputStream.reset();
        }catch (IOException | ClassNotFoundException e){
            if(e instanceof SocketException){
                reConnect();
            }
            e.printStackTrace();
        }
        return response;
    }
}
