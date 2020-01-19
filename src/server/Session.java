package server;

import server.dispatchers.EtudiantDispatcher;
import server.dispatchers.ProfesseurDispatcher;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Session extends Thread {
    Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    public Session(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;

    }

    public void launch() {
        Action action = Action.DEFAULT;
        Role role;
        while (true) {
            try {
                Request request = (Request) inputStream.readObject();

                role = request.getRole();

                action = request.getAction();

                if (action == Action.EXIT) {
                    System.out.println("Closing session...");
                    break;
                }
                Response response;
                switch (role) {
                    case PROFESSEUR:
                        response = ProfesseurDispatcher.handle(request);
                        outputStream.writeObject(response);
                        break;
                    case ETUDIANT:
                        response = EtudiantDispatcher.handle(request);
                        outputStream.writeObject(response);
                        break;
                    default:
                        System.out.println("Type not found");
                        new Response(1, "Type not found !!");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try{
            inputStream.close();
            outputStream.close();
        }catch (IOException e){
            System.out.println("Error in closing streams...");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        launch();
    }

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
