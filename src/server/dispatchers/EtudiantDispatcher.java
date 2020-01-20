package server.dispatchers;

import models.Etudiant;
import server.DAOs.EtudiantDAO;
import util.Action;
import util.Request;
import util.Response;

public class EtudiantDispatcher {

    public static Response handle(Request request){
        Action action = request.getAction();
        Etudiant etudiant = (Etudiant) request.getData();
//        System.out.println("recieved : " + etudiant.toString());
        Response response;
        switch (action) {
            case LOGIN:
                response = EtudiantDAO.login(etudiant);
                break;
            case EXIT:
                System.out.println("Closing session...");
                response = new Response(0,"Closing session...");
                break;
            default:
                System.out.println("Action not found");
                response = new Response(0,"Action not found");
        }
        return response;
    }
}
