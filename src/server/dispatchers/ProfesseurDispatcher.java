package server.dispatchers;

import models.*;
import server.DAOs.ProfesseurDAO;
import server.DAOs.TestDAO;
import util.Action;
import util.Request;
import util.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProfesseurDispatcher {

    public static Response handle(Request request){
        Action action = request.getAction();
        Response response;
        TestDAO testDao;
        try{
            switch (action) {
                case LOGIN:{
                    ArrayList<String> credentials = (ArrayList<String>) request.getData();
                    Professeur professeur = ProfesseurDAO.login(credentials.get(0),credentials.get(1));
                    response = new Response(0,"Proffesseur logged successfully",professeur);
                    break;
                }
                case EXIT: {
                    System.out.println("Closing session...");
                    response = new Response(0, "Closing session...");
                    break;
                }
                case CREATE_PROF: {
                    Professeur professeur = (Professeur) request.getData();
                    ProfesseurDAO.addProfesseur(professeur);
                    response = new Response(0,"Professeur created successfully");
                    break;
                }
                case UPDATE_PROF: {
                    ArrayList<Object> data = (ArrayList<Object>) request.getData();
                    String oldProfesseurMatricule = "";
                    Professeur newProfesseur = new Professeur();
                    for(Object elm:data){
                        if(elm instanceof String){
                            oldProfesseurMatricule = (String) elm;
                        }
                        else if(elm instanceof Professeur){
                            newProfesseur = (Professeur) elm;
                        }
                        else{
                            throw new IOException("Invalid request data");
                        }
                    }
                    ProfesseurDAO.update(oldProfesseurMatricule, newProfesseur);
                    response = new Response(0,"Proffesseur updated successfully");
                    break;
                }
                case DELETE_PROF: {
                    String matricule = (String) request.getData();
                    ProfesseurDAO.delete(matricule);
                    response = new Response(0,"Proffesseur deleted successfully");
                    break;
                }
                case GET_ALL_PROFS:{
                    ArrayList<Professeur> professeurs = ProfesseurDAO.getAllProfesseurs();
                    response = new Response(0,"Professeurs loaded successfully",professeurs);
                    break;
                }
                case GET_PROF: {
                    String matricule = (String) request.getData();
                    Professeur professeur = ProfesseurDAO.getProfesseurById(matricule);
                    response = new Response(0,"professeur loadded successfully",professeur);
                    break;
                }
                case GET_TESTS_PROFESSEUR: {
                    String matricule = (String) request.getData();
                    ArrayList<Test> tests = TestDAO.getProfesseurTests(matricule);
                    response = new Response(0,"Tests loaded successfully",tests);
                    break;
                }
                case GET_TEST: {
                    int id_test = (int) request.getData();
                    Test test = TestDAO.getTestById(id_test);
                    response = new Response(0, "Test loaded successfully", test);
                    break;
                }
                case GET_TESTS_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Test> etudiantTests = TestDAO.getEtudiantTests(cne);
                    String message = etudiantTests.size() + " Tests loaded successfully";
                    response = new Response(0, message, etudiantTests);
                    break;
                }
                case GET_FICHES_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Fiche> fichesEtudiants = TestDAO.getEtudiantFiches(cne);
                    String message = fichesEtudiants.size() + " Fiches loaded successfully";
                    response = new Response(0,message,fichesEtudiants);
                    break;
                }
                case GET_FICHES_TEST: {
                    int idTest = (int) request.getData();
                    ArrayList<Fiche> fichesTests = TestDAO.getTestFiches(idTest);
                    String message = fichesTests.size() + " Fiche loaded successfully";
                    response = new Response(0,message,fichesTests);
                    break;
                }
                case ADD_TEST: {
                    ArrayList<Object> data = (ArrayList<Object>) request.getData();
                    Integer idGroupe = 0;
                    Test test = new Test();
                    for(Object elm:data){
                        if(elm instanceof Integer){
                            idGroupe = (Integer) elm;
                        }
                        else if(elm instanceof Test){
                            test = (Test) elm;
                        }
                        else{
                            throw new IOException("Invalid request data");
                        }
                    }
                    TestDAO.addTest(test,idGroupe);
                    response = new Response(0,"Test added successfully");
                }
                case DELETE_TEST:{
                    int idTest = (int)request.getData();
                    TestDAO.deleteTest(idTest);
                    response = new Response(0,"Test deleted successfully");
                }
                case UPDATE_TEST:{
                    ArrayList<Object> data = (ArrayList<Object>) request.getData();
                    Integer oldTestId = 0;
                    Test newTest = new Test();
                    for(Object elm:data){
                        if(elm instanceof Integer){
                            oldTestId = (Integer) elm;
                        }
                        else if(elm instanceof Test){
                            newTest = (Test) elm;
                        }
                        else{
                            throw new IOException("Invalid request data");
                        }
                    }
                    TestDAO.updateTest(oldTestId, newTest);
                    response = new Response(0,"Proffesseur updated successfully");
                    break;
                }
                case GET_GROUPES: {
                    ArrayList<Groupe> groupes = TestDAO.getAllGroupes();
                    response = new Response(0,"Groupes loadded successfully",groupes);
                    break;
                }
                default: {
                    System.out.println("Action not found");
                    response = new Response(0, "Action not found");
                }
            }
        }catch (SQLException | IOException e){
            response = new Response(1,e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
