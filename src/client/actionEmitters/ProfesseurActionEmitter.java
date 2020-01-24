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


    public Professeur login(String usernaeme,String password) throws Exception {
        System.out.println("login professeur...");
        ArrayList<String> credentials = new ArrayList<>();
        credentials.add(usernaeme);
        credentials.add(password);
        Request request = new Request(Action.LOGIN, credentials, Role.PROFESSEUR);
        Response response = post(request);
        if(response == null){
            throw new Exception("An error has occurred");
        }
        if(response.getStatus()!=0){
            throw new Exception(response.getMessage());
        }
        return (Professeur) response.getData();
    }


    public Test getTestById(int idTest) throws Exception {
        Response response = post(new Request(Action.GET_TEST,idTest, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (Test) response.getData();
    }

    public ArrayList<Test> getProfesseursTests(String matricule) throws Exception {
        Response response = post(new Request(Action.GET_TESTS_PROFESSEUR, matricule, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Test>) response.getData();
    }

    public ArrayList<Fiche> getEtudiantFiches(int idTest) throws Exception {
        Response response = post(new Request(Action.GET_FICHES_ETUDIANT, idTest, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Fiche>) response.getData();
    }

    public ArrayList<Fiche> getTestFiches(int idTest) throws Exception {
        Response response = post(new Request(Action.GET_FICHES_TEST, idTest, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Fiche>) response.getData();
    }

    public void updateTest(int oldTestId, Test newTest) throws Exception {
        ArrayList<Object> data = new ArrayList<>();
        data.add(oldTestId);
        data.add(newTest);
        Response response = post(new Request(Action.UPDATE_TEST, data, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
    }

    public void deleteTest(int id_test) throws Exception {
        Response response = post(new Request(Action.UPDATE_TEST, id_test, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
    }

    public void updateTest(Test test,int id_groupe) throws Exception {
        ArrayList<Object> data = new ArrayList<>();
        data.add(test);
        data.add(id_groupe);
        Response response = post(new Request(Action.ADD_TEST, data, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
    }

    public ArrayList<Groupe> getGroupes() throws Exception {
        Response response = post(new Request(Action.GET_GROUPES, Role.PROFESSEUR));
        if(response.getStatus() != 0){
            throw new Exception(response.getMessage());
        }
        return (ArrayList<Groupe>) response.getData();
    }


}
