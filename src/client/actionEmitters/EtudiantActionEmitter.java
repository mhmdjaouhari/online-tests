package client.actionEmitters;

import models.Etudiant;
import models.Test;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/* this package holds the Emitters to the server, it's control Request and Response, no interaction wiht DB */

public class EtudiantActionEmitter extends ActionEmitter {

    public EtudiantActionEmitter(Socket socket) throws IOException {
        super(socket);
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

    // should be implemented on the sever-side
    public ArrayList<Test> getNewTests(){
        // once implemented in the server uncomment this:
//        Response response = post(new Request(Action.GET_NEW_TESTS,Role.ETUDIANT));
//        ArrayList<Test> tests = (ArrayList<Test>) response.getData();
        ArrayList<Test> tests = new ArrayList<>();
        tests.add(new Test(1, "Assu qualité ISO 9001", false, 90, "a123", "Chichi"));
        tests.add(new Test(1, "Assu qualité ISO 9001", false, 90, "a123", "Chichi"));
        tests.add(new Test(1, "Assu qualité ISO 9001", false, 90, "a123", "Chichi"));
        return tests;
    }



}
