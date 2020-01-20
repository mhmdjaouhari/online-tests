package client.actionEmitters;

import models.Etudiant;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.io.*;
import java.net.Socket;

/* this package holds the Emitters to the server, it's control Request and Response, no interaction wiht DB */

public class EtudiantActionEmitter extends ActionEmitter {

    public EtudiantActionEmitter(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream) {
        super(socket,inputStream,outputStream);
    }

    public void login(Etudiant etudiant) {
        System.out.println("login etudiant...");
        Request request = new Request(Action.LOGIN, etudiant, Role.ETUDIANT);
        Response response = post(request);
        if(response == null){
            return;
        }
        if (response.getStatus() == 0) {
            etudiant = (Etudiant) response.getData();
            System.out.println("Welcome " + etudiant.getNom() + " " + etudiant.getPrenom());
        } else {
            System.out.println(response.getMessage());
        }

    }

}
