package server.dispatchers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

abstract public class Dispatcher extends Thread {
    Socket socket;

    public Dispatcher(Socket socket){
        this.socket = socket;
    }

    abstract public void launch();

    protected ObjectOutputStream getOutputStream() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

    protected ObjectInputStream getInputStream() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
