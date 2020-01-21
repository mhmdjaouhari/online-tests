package server.dispatchers;

import models.Etudiant;
import models.Professeur;
import server.DAOs.EtudiantDAO;
import util.Action;
import util.Request;
import util.Response;

import java.util.ArrayList;

public class EtudiantDispatcher {

    public static Response handle(Request request){
        Action action = request.getAction();
        Etudiant etudiant ;
        Response response;
        switch (action) {
            case LOGIN:
                etudiant=(Etudiant) request.getData();
                response = EtudiantDAO.login(etudiant);
                break;
            case EXIT:
                System.out.println("Closing session...");
                response = new Response(0,"Closing session...");
                break;
            case ADD:
                etudiant=(Etudiant) request.getData();
                response = EtudiantDAO.add(etudiant);
                break;
            case UPDATE:
                ArrayList<Etudiant> ArrayEtud = ( ArrayList<Etudiant>) request.getData();
                response = EtudiantDAO.update(ArrayEtud.get(0),ArrayEtud.get(1));
                break;
            case DELETE:
                etudiant=(Etudiant) request.getData();
                response = EtudiantDAO.delete(etudiant);
                break;
            case GETALL:
                response = EtudiantDAO.getAll();
                break;
            case SEARCH:
                etudiant=(Etudiant) request.getData();
                response = EtudiantDAO.search(etudiant);
                break;
            default:
                System.out.println("Action not found");
                response = new Response(0,"Action not found");
        }
        return response;
    }
}
