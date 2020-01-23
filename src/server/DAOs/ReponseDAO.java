package server.DAOs;

import models.Professeur;
import models.Question;
import models.Reponse;
import models.Test;
import util.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReponseDAO {
    public static Connection conn = DataSource.getInstance().getConnection();
    public Response getAll(int id_fiche)
    {
        ResultSet resultSet=null;
        ArrayList<Reponse> arr=new ArrayList<Reponse>();
        try
        {
            PreparedStatement pst=conn.prepareStatement("select * from reponses where id_fiche = ?;");
            pst.setInt(1,id_fiche);
            resultSet=pst.executeQuery();
            System.out.println("getAll reponses done ! ");
            while (resultSet.next())
            {
                Reponse rep = new Reponse();
                ProfesseurDAO prfDao = new ProfesseurDAO();
                QuestionDAO qstDao = new QuestionDAO();
                rep.setId(resultSet.getInt("id_reponse"));
                rep.setIdFiche(resultSet.getInt("id_fiche"));
                rep.setIdQuestion(resultSet.getInt("id_question"));
                rep.setValue(resultSet.getString("value"));
                arr.add(rep);
            }
            return new Response(arr);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }
}
