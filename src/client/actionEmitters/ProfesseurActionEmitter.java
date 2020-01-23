package client.actionEmitters;

import models.*;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ProfesseurActionEmitter extends ActionEmitter {

    public ProfesseurActionEmitter(Socket socket) throws IOException {
        super(socket);
    }


    public Professeur login(Professeur prof) throws Exception {
        System.out.println("login professeur...");
        Request request = new Request(Action.LOGIN, prof, Role.PROFESSEUR);
        Response response = post(request);
        if(response == null){
            return null;
        }
        Professeur result;
        if (response.getStatus() == 0) {
            result = (Professeur) response.getData();
            System.out.println("Welcome " + result.getNom() + " " + result.getPrenom());
        } else {
            System.out.println(response.getMessage());
            throw new Exception(response.getMessage());
        }
        return result;
    }


    public Test getTest(int idTest) {
        Response response = post(new Request(Action.GET_TEST,idTest, Role.PROFESSEUR));
        Test test = (Test) response.getData();
        return test;
    }

    public ArrayList<Test> getTests(Professeur prof) {
        Response response = post(new Request(Action.GET_TESTS, prof, Role.PROFESSEUR));
        ArrayList<Test> test = (ArrayList<Test>) response.getData();
        return test;
    }

    public ArrayList<Fiche> getFiches(int idTest) {
        Response response = post(new Request(Action.GET_FICHES, idTest, Role.PROFESSEUR));
        ArrayList<Fiche> fiches = (ArrayList<Fiche>) response.getData();
        return fiches;
    }

    public String submitTest(Test test) {
        Response response = post(new Request(Action.SUBMIT_TEST, test, Role.PROFESSEUR));
        String tt = (String) response.getData();
        return tt;
    }

    public ArrayList<Groupe> getGroupes() {
        Response response = post(new Request(Action.GET_GROUPES, Role.PROFESSEUR));
        ArrayList<Groupe> groupes = (ArrayList<Groupe>) response.getData();
        return groupes;
    }


}
