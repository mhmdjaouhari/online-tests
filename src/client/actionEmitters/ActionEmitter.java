package client.actionEmitters;

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
            Request request = new Request("EXIT", null);
            getOutputStream().writeObject(request);
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
