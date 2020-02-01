package client.actionEmitters;

import models.Etudiant;
import models.Fiche;
import models.Test;
import util.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/* this package holds the Emitters to the server, it's control Request and Response, no interaction wiht DB */

public class EtudiantActionEmitter extends ActionEmitter {

    public EtudiantActionEmitter(Socket socket) throws IOException {
        super(socket);
    }

    public Etudiant login(String usernaeme,String password) throws Exception {
        System.out.println("login etudiant...");
        ArrayList<String> credentials = new ArrayList<>();
        credentials.add(usernaeme);
        credentials.add(password);
        Request request = new Request(Action.LOGIN, credentials, Role.ETUDIANT);
        Response response = post(request);
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus()!=0){
            throw new Exception(response.getMessage());
        }
        return (Etudiant)response.getData();
    }


    public ArrayList<Test> getNewEtudiantTests(String CNE) throws Exception {
        Response response = post(new Request(Action.GET_NEW_TESTS_ETUDIANT,CNE,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Test>) response.getData();
    }
    public ArrayList<Test> getOldEtudiantTests(String CNE) throws Exception {
        Response response = post(new Request(Action.GET_OLD_TESTS_ETUDIANT,CNE,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Test>) response.getData();
    }
    public ArrayList<Test> getAllEtudiantTests(String CNE) throws Exception {
        Response response = post(new Request(Action.GET_ALL_TESTS_ETUDIANT,CNE,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Test>) response.getData();
    }

    public Test getTestById(int idTest) throws Exception {
        Response response = post(new Request(Action.GET_TEST,idTest,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (Test) response.getData();
    }

    public Etudiant getEtudiant(String CNE) throws Exception {
        Response response = post(new Request(Action.GET_ETUDIANT,CNE,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (Etudiant) response.getData();
    }

    public void submitFiche(Fiche fiche) throws Exception {
        Response response = post(new Request(Action.SUBMIT_FICHE,fiche,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
    }

    public ArrayList<Fiche> getFichesEtudiant(String CNE) throws Exception {
        Response response = post(new Request(Action.GET_FICHES_ETUDIANT,CNE,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Fiche>) response.getData();
    }

    public Fiche getFicheEtudiant(String cne,int id_test) throws Exception {
        ArrayList<Object> data = new ArrayList<>();
        data.add(cne);
        data.add(id_test);
        Response response = post(new Request(Action.GET_FICHE_ETUDIANT,data,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (Fiche) response.getData();
    }

    public Test getFullTestById(int id_test) throws Exception {
        Response response = post(new Request(Action.GET_FULL_TEST,id_test,Role.ETUDIANT));
        if(response == null){
            throw new ServerOfflineException("Le serveur est hors ligne, essayez plus tard");
        }
        if(response.getStatus() != 0){

            throw new Exception(response.getMessage());
        }
        Test test = (Test) response.getData();
        return (Test) response.getData();
    }


}
