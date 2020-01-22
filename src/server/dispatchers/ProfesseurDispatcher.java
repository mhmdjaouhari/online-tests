package server.dispatchers;

import models.Professeur;
import server.DAOs.ProfesseurDAO;
import util.Action;
import util.Request;
import util.Response;

import java.util.ArrayList;

public class ProfesseurDispatcher {

    public static Response handle(Request request){
        Action action = request.getAction();
        Professeur professeur;
        Response response;
        switch (action) {
            case LOGIN:
                professeur= (Professeur) request.getData();
                response = ProfesseurDAO.login(professeur);
                break;
            case EXIT:
                System.out.println("Closing session...");
                response = new Response(0,"Closing session...");
                break;
            case CREATE_PROF:
                professeur= (Professeur) request.getData();
                response = ProfesseurDAO.add(professeur);
                break;
            case UPDATE_PROF:
                ArrayList<Professeur> ArrayProf = ( ArrayList<Professeur>) request.getData();
                response = ProfesseurDAO.update( ArrayProf.get(0), ArrayProf.get(1));
                break;
            case DELETE_PROF:
                professeur= (Professeur) request.getData();
                response = ProfesseurDAO.delete(professeur);
                break;
            case GET_ALL_PROFS:
                response = ProfesseurDAO.getAll();
                break;
            case SEARCH_PROF:
                professeur= (Professeur) request.getData();
                response = ProfesseurDAO.search(professeur);
                break;
            default:
                System.out.println("Action not found");
                response = new Response(0,"Action not found");
        }
        return response;
    }
}
