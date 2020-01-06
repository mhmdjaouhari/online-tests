package server.dispatchers;

import server.actionHandlers.EtudiantActionHandler;
import models.Etudiant;
import util.Request;
import util.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* this package holds all the dispatchers who listen for actions and fire a handler */

public class EtudiantDispatcher extends Dispatcher {

    public EtudiantDispatcher(Socket socket) {
        super(socket);
    }

    public void startController() {
        try (ObjectInputStream inputStream = getInputStream();
             ObjectOutputStream outputStream = getOutputStream();) {
            String action;
            do {
                Request request = (Request) inputStream.readObject();
                action = request.getAction();
                Etudiant etudiant = (Etudiant) request.getData();
                switch (action) {
                    case "LOGIN":
                        Response response = EtudiantActionHandler.login(etudiant);
                        outputStream.writeObject(response);
                        break;
                    case "EXIT":
                        System.out.println("Closing session...");
                        break;
                    default:
                        System.out.println("Action not found");
                }
            } while (!action.equals("EXIT"));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startController();
    }

}
