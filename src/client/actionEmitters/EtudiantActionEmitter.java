package client.actionEmitters;

import models.Etudiant;
import util.Request;
import util.Response;

import java.io.*;
import java.net.Socket;

/* this package holds the Emitters to the server, it's control Request and Response, no interaction wiht DB */

public class EtudiantActionEmitter extends ActionEmitter {

    public EtudiantActionEmitter(Socket socket) {
        super(socket);
    }

    public void login(Etudiant etudiant) {
        System.out.println("login etudiant...");
        try {
            ObjectOutputStream outputStream = getOutputStream();
            Request request = new Request("LOGIN", etudiant);
            outputStream.writeObject(request);
            ObjectInputStream inputStream = getInputStream();
            Response response = (Response) inputStream.readObject();
            if (response.getStatus() == 0) {
                etudiant = (Etudiant) response.getData();
                System.out.println("Welcome " + etudiant.getNom() + " " + etudiant.getPrenom());
            } else {
                System.out.println(response.getMessage());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
