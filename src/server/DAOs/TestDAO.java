package server.DAOs;

import models.Groupe;
import models.Professeur;
import models.Question;
import models.Test;
import util.Response;

import java.sql.*;
import java.util.ArrayList;

public class TestDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

    //find by id
    public Response search(Test t)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("select * from tests where id_test=? ;");
            pst.setInt(1,t.getId());
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                Test test = new Test();
                QuestionDAO qstDao = new QuestionDAO();
                ProfesseurDAO prfDao = new ProfesseurDAO();
                test.setId(resultSet.getInt("id_test"));
                test.setTitre(resultSet.getString("titre"));
                test.setDuration(resultSet.getInt("duration"));
                test.setLocked(resultSet.getBoolean("locked"));
                test.setMatriculeProf(resultSet.getString("matricule"));
                test.setNomProf(((Professeur)(prfDao.search(resultSet.getString("matricule")).getData())).getNom());
                test.setQuestions((ArrayList<Question>)qstDao.getAll(resultSet.getInt("id_test")).getData());

                return new Response(test);
            } else {
                return new Response(1, "Question doesn't exist ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SQL ERROR");
        }
    }
    public Response search(int id_test)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("select * from tests where id_test=? ;");
            pst.setInt(1,id_test);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                Test test = new Test();
                QuestionDAO qstDao = new QuestionDAO();
                ProfesseurDAO prfDao = new ProfesseurDAO();
                test.setId(resultSet.getInt("id_test"));
                test.setTitre(resultSet.getString("titre"));
                test.setDuration(resultSet.getInt("duration"));
                test.setLocked(resultSet.getBoolean("locked"));
                test.setMatriculeProf(resultSet.getString("matricule"));
                test.setNomProf(((Professeur)(prfDao.search(resultSet.getString("matricule")).getData())).getNom());
                test.setQuestions((ArrayList<Question>)qstDao.getAll(resultSet.getInt("id_test")).getData());

                return new Response(test);
            } else {
                return new Response(1, "Question doesn't exist ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SQL ERROR");
        }
    }

    //add test
    public static Response add(Test t)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("insert into Test(id_test,matricule,titre,duration,locked) values(?,?,?,?);");
            pst.setInt(1, t.getId());
            pst.setString(2, t.getMatriculeProf());
            pst.setString(3, t.getTitre());
            pst.setBoolean(4, t.isLocked());
            pst.executeUpdate();
            if(t.getQuestions()!=null)
            {
                QuestionDAO qstDao = new QuestionDAO();
                for (Question q : t.getQuestions()) {
                    qstDao.add(q);
                }
            }
            if (t.getGroupes() != null) {

                for (Groupe q : t.getGroupes()) {
                    PreparedStatement pst1 =conn.prepareStatement("insert into affectations(id_test,id_groupe) values(?,?);");
                    pst1.setInt(1, t.getId());
                    pst1.setInt(2, q.getId());
                    pst1.executeUpdate();

                }
            }
            return new Response(0,"test Added "+t.getId());
        }
        catch(SQLException ex){
            System.err.println("problem with add Query !! "+ ex.getMessage());
            return new Response(1,"SERVER DB ERROR while inserting data");
        }
    }

    // Update Test
    public static Response update(Test oldTest,Test newTest)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("update tests set titre=?,duration=?,locked=? where id_test=?;");
            pst.setString(1, newTest.getTitre());
            pst.setInt(2, newTest.getDuration());
            pst.setBoolean(3, newTest.isLocked());
            pst.setInt(4, oldTest.getId());
            if(pst.executeUpdate()!=0) {
                System.out.println("Test updated : " + oldTest.getId());
                return new Response(0, "You are Updated :" + oldTest.getId());
            }
            else
            {
                return new Response(1,"Test doesn't exist");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SERVER DB ERROR");
        }
    }


    // delete test by id_test
    public static Response delete(Test test)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("delete from Tests where id_test=?;");
            pst.setInt(1,test.getId());
            if(pst.executeUpdate()!=0) {
                System.out.println("Test deleted : " + test.getId());
                return new Response(0, "You are delete :" + test.getId());
            }
            else
            {
                return new Response(1,"Test doesn't exist");
            }
        }
        catch(SQLException ex)
        {
            System.err.println("problem with delete Query  : "+ ex.getMessage());
            return new Response(1,"Server delete Error");
        }
    }

    // getALl Tests
    public static Response getAll()
    {
        ResultSet resultSet=null;
        ArrayList<Test> arr=new ArrayList<Test>();
        try
        {
            Statement st=conn.createStatement();
            resultSet=st.executeQuery("select * from tests;");
            System.out.println("getAll tests done ! ");
            while (resultSet.next())
            {
                Test test = new Test();
                ProfesseurDAO prfDao = new ProfesseurDAO();
                QuestionDAO qstDao = new QuestionDAO();
                test.setId(resultSet.getInt("id_test"));
                test.setTitre(resultSet.getString("titre"));
                test.setDuration(resultSet.getInt("duration"));
                test.setLocked(resultSet.getBoolean("locked"));
                test.setMatriculeProf(resultSet.getString("matricule"));
                test.setNomProf(((Professeur)(prfDao.search(resultSet.getString("matricule")).getData())).getNom());
                test.setQuestions((ArrayList<Question>)qstDao.getAll(resultSet.getInt("id_test")).getData());
                arr.add(test);
            }
            return new Response(arr);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connexion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }


    // getALl test of a teacher
    public static Response getAll(String matricule)
    {
        ResultSet resultSet=null;
        ArrayList<Test> arr=new ArrayList<Test>();
        try
        {
            PreparedStatement pst=conn.prepareStatement("select * from tests where matricule = ?;");
            pst.setString(1,matricule);
            resultSet=pst.executeQuery();
            System.out.println("getAll Tests done ! ");
            while (resultSet.next())
            {
                Test test = new Test();
                ProfesseurDAO prfDao = new ProfesseurDAO();
                QuestionDAO qstDao = new QuestionDAO();
                test.setId(resultSet.getInt("id_test"));
                test.setTitre(resultSet.getString("titre"));
                test.setDuration(resultSet.getInt("duration"));
                test.setLocked(resultSet.getBoolean("locked"));
                test.setMatriculeProf(resultSet.getString("matricule"));
                test.setNomProf(((Professeur)(prfDao.search(resultSet.getString("matricule")).getData())).getNom());
                test.setQuestions((ArrayList<Question>)qstDao.getAll(resultSet.getInt("id_test")).getData());
                arr.add(test);
            }
            return new Response(arr);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }

    //get Maxid
    public Response maxId()
    {
        Test q = new Test();
        int id=0;
        ResultSet resultSet = null;
        try
        {
            Statement st=conn.createStatement();
            resultSet=st.executeQuery("select * from tests Order by id_test Desc;");
            if (resultSet.next())
            {
                id = resultSet.getInt("id_test");
            }
            return new Response(id);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connexion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }
}
