package client.actionEmitters;

import models.Etudiant;
import models.Question;
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
    public ArrayList<Test> getTests(){
        // once implemented in the server uncomment this:
//        Response response = post(new Request(Action.GET_TESTS_ETUDIANT,Role.ETUDIANT));
//        ArrayList<Test> tests = (ArrayList<Test>) response.getData();
        ArrayList<Test> tests = new ArrayList<>();
        tests.add(new Test(0, "Assu qualité ISO 9001", false, 90, "a123", "Chichi"));
        tests.add(new Test(0, "Assu qualité ISO 9001", false, 90, "a123", "Chichi"));
        tests.add(new Test(0, "Assu qualité ISO 9001", false, 90, "a123", "Chichi"));
        return tests;
    }

    public Test getTest(int idTest) {
        // once implemented in the server uncomment this:
//        Response response = post(new Request(Action.GET_TEST,Role.ETUDIANT));
//        Test test = (Test) response.getData();
        Test test = new Test(0, "Assu qualité ISO 9001", false, 90, "a123", "Chichi");
        Question question1 = new Question(0, "C'est quoi la qualité ? \n 1- Qualité \n 2- qualité \n 3-quality \n 4- Kalinti", "1,2,3", 0);
        Question question2 = new Question(0, "C'est quoi la qualité ? \n 1- Qualité \n 2- qualité \n 3-quality \n 4- Kalinti", "1,2,3", 0);
        test.getQuestions().add(question1);
        test.getQuestions().add(question2);
        return test;
    }
}
