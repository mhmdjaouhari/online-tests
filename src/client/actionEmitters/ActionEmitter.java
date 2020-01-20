package client.actionEmitters;

import util.Action;
import util.Request;
import util.Role;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.UnknownHostException;
import java.util.Scanner;

abstract public class ActionEmitter {

    private Socket socket;
    protected ObjectInputStream inputStream;    // Protected is the best solution for the encapsulation so far
    protected ObjectOutputStream outputStream;
    private Boolean isClientOnline;
    private Boolean connectedToServer;

    public ActionEmitter(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        isClientOnline = true;
        connectedToServer = true;
    }


    public boolean exit() {

        try {
            if(!isServerOnline()){
                System.out.println("Server is offline, changes aren't comitted, exit anyway ?(Y/N) :");
                Scanner scanner = new Scanner(System.in);
                String answer = scanner.next();
                if(answer.equalsIgnoreCase("y")){
                    outputStream.close();
                    inputStream.close();
                    return true;
                }else{
                    return false;
                }
            }
            Request request = new Request(Action.EXIT, Role.ETUDIANT);
            outputStream.writeObject(request);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // this function contains the case when the server is
    // offline and the user exit, so changes aren't comitted to the server


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

    public boolean isServerOnline(){
        boolean isAlive=false;
        SocketAddress socketAddress = new InetSocketAddress("localhost",5000);
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

    public void reConenct() throws IOException {
        socket = new Socket("localhost",5000);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

    }
}
