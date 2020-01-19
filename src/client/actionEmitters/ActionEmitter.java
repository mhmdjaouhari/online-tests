package client.actionEmitters;

import util.Action;
import util.Request;
import util.Role;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

abstract public class ActionEmitter {

    private Socket socket;
    protected ObjectInputStream inputStream;    // Protected is the best solution for the encapsulation so far
    protected ObjectOutputStream outputStream;

    public ActionEmitter(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }


    public void exit() {
        try {
            Request request = new Request(Action.EXIT, Role.ETUDIANT);
            outputStream.writeObject(request);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
