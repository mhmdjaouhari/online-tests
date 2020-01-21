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

    public Etudiant login(Etudiant etudiant) throws Exception {
        System.out.println("login etudiant...");
        Request request = new Request(Action.LOGIN, etudiant, Role.ETUDIANT);
        Response response = post(request);
        if(response == null){
            return null;
        }
        Etudiant result;
        if (response.getStatus() == 0) {
            result = (Etudiant) response.getData();
            System.out.println("Welcome " + result.getNom() + " " + result.getPrenom());
        } else {
            System.out.println(response.getMessage());
            throw new Exception(response.getMessage());
        }
        return result;
    }

}
