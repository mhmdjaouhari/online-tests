package server.dispatchers;

import models.*;
import server.DAOs.EtudiantDAO;
import server.DAOs.TestDAO;
import util.Action;
import util.Request;
import util.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class EtudiantDispatcher {
    public static Response handle(Request request){
        Action action = request.getAction();
        Response response;
        try {
            switch (action) {
                case LOGIN:{
                    ArrayList<String> credentials = (ArrayList<String>) request.getData();
                    Etudiant etudiant = EtudiantDAO.login(credentials.get(0),credentials.get(1));
                    response = new Response(0,"User logged successfully",etudiant);
                    break;
                }
                case EXIT: {
                    System.out.println("Closing session...");
                    response = new Response(0, "Closing session...");
                    break;
                }
                case CREATE_ETUDIANT: {
                    Etudiant etudiant = (Etudiant) request.getData();
                    EtudiantDAO.add(etudiant);
                    response = new Response(0,"Etudiant created successfully");
                    break;
                }
                case UPDATE_ETUDIANT: {
                    ArrayList<Object> data = (ArrayList<Object>) request.getData();
                    String oldEtudiantCne = "";
                    Etudiant newEtudiant = new Etudiant();
                    for(Object elm:data){
                        if(elm instanceof String){
                            oldEtudiantCne = (String) elm;
                        }
                        else if(elm instanceof Etudiant){
                            newEtudiant = (Etudiant) elm;
                        }
                        else{
                            throw new IOException("Invalid request data");
                        }
                    }
                    EtudiantDAO.update(oldEtudiantCne, newEtudiant);
                    response = new Response(0,"Etudiant updated successfully");
                    break;
                }
                case DELETE_ETUDIANT: {
                    String cne = (String) request.getData();
                    EtudiantDAO.delete(cne);
                    response = new Response(0,"Etudiant deleted successfully");
                    break;
                }
                case GET_ALL_ETUDIANTS: {
                    ArrayList<Etudiant> etudiants = EtudiantDAO.getAllEtudiants();
                    response = new Response(0,"Etudiants loaded successfully",etudiants);
                    break;
                }
                case GET_ETUDIANT: {
                    String cne = (String) request.getData();
                    Etudiant etudiant = EtudiantDAO.getEtudiantById(cne);
                    response = new Response(0,"Etudiant loadded successfully",etudiant);
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
                case SUBMIT_FICHE: {
                    Fiche fiche = (Fiche) request.getData();
                    int id_fiche = TestDAO.submitFiche(fiche);
                    response = new Response(0, "Fiche submitted successfully", id_fiche);
                    break;
                }
                case GET_FICHES_ETUDIANT: {
                    String cne = (String) request.getData();
                    ArrayList<Fiche> fichesEtudiants = TestDAO.getEtudiantFiches(cne);
                    String message = fichesEtudiants.size() + " Tests loaded successfully";
                    response = new Response(0,message,fichesEtudiants);
                    break;
                }
                case GET_FULL_TEST:{
                    int id_test = (int) request.getData();
                    Test test = TestDAO.getFullTestById(id_test);
                    response = new Response(0,"Test loaded successfully",test);
                    break;
                }
                default: {
                    System.out.println("Action not found");
                    response = new Response(0, "Action not found");
                }
            }
        }catch(SQLException|IOException e){
            response = new Response(1,e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
