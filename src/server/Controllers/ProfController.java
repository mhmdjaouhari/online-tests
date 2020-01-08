package server.Controllers;

import models.Professeur;
import server.DAO.DaoProfesseur;
import util.Action;
import util.Request;
import util.Response;

public class ProfController {
    private static DaoProfesseur daoProf=new DaoProfesseur();
    public static Response doGet(Request request)
    {
        Response response;
        Action action=request.getAction();
        Professeur prof=(Professeur)request.getData();

        switch (action)
        {
            case LOGIN:
                response = daoProf.login(prof);
                break;
// toDo
//           case UPDATE:
//                response = daoProf.update(prof);
//                break;
//            case DELETE:
//                response = daoProf.delete(prof);
//                break;
//            case ADD:
//                response = daoProf.add(prof);
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
