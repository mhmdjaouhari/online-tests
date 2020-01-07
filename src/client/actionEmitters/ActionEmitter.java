package client.actionEmitters;

import util.Action;
import util.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

abstract public class ActionEmitter {

    private Socket socket;

    public ActionEmitter(Socket socket) {
        this.socket = socket;
    }

    protected ObjectOutputStream getOutputStream() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

    protected ObjectInputStream getInputStream() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public void exit() {
        try {
            Request request = new Request(Action.EXIT);
            getOutputStream().writeObject(request);
            System.out.println("Exiting in 5 seconds...");
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
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
