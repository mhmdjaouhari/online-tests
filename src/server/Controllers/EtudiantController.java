package server.Controllers;

import models.Etudiant;
import models.Professeur;
import server.DAO.DaoEtudiant;
import server.DAO.DaoProfesseur;
import util.Action;
import util.Request;
import util.Response;

public class EtudiantController {
    private static DaoEtudiant daoEtud=new DaoEtudiant();
    public static Response doGet(Request request)
    {
        Response response;
        Action action=request.getAction();
        Etudiant etud=(Etudiant)request.getData();

        switch (action)
        {
            case LOGIN:
                response = daoEtud.login(etud);
                break;
// toDo
//           case UPDATE:
//                response = daoEtud.update(etud);
//                break;
//            case DELETE:
//                response = daoEtud.delete(etud);
//                break;
//            case ADD:
//                response = daoEtud.add(etud);
//                break;

            default:
            {
                System.out.println("Action not found");
                response=new Response(1,"Action not found !! ");
            }
        }
        return response;
    }
}
