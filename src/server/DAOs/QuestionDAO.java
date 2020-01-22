package server.DAOs;

import models.Professeur;
import models.Question;
import models.Test;
import util.Response;

import java.sql.*;
import java.util.ArrayList;

public class QuestionDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

    //find by id
    public Response search(Question qst)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("select * from questions where id_question=? ;");
            pst.setInt(1,qst.getId());
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getInt("id_question"));
                question.setIdTest(resultSet.getInt("id_test"));
                question.setTexte(resultSet.getString("texte"));
                question.setValue(resultSet.getString("value"));

                return new Response(question);
            } else {
                return new Response(1, "Question doesn't exist ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SQL ERROR");
        }
    }

    //add question
    public Response add(Question qst)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("insert into questions(id_question,id_test,texte,value) values(?,?,?,?);");
            pst.setInt(1, qst.getId());
            pst.setInt(2, qst.getIdTest());
            pst.setString(3, qst.getTexte());
            pst.setString(4, qst.getValue());
            pst.executeUpdate();

            return new Response(0,"Qst Added "+qst.getId());
        }
        catch(SQLException ex){
            System.err.println("problem with add Query !! "+ ex.getMessage());
            return new Response(1,"SERVER DB ERROR while inserting data");
        }
    }

    // Update QST
    public Response update(Question oldQst,Question newQst)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("update questions set texte=?,value=?,id_test=? where id_question=?;");
            pst.setString(1, newQst.getTexte());
            pst.setString(2, newQst.getValue());
            pst.setInt(3, newQst.getIdTest());
            pst.setInt(4, oldQst.getId());
            if(pst.executeUpdate()!=0) {
                System.out.println("Question updated : " + oldQst.getId());
                return new Response(0, "You are Updated :" + oldQst.getId());
            }
            else
            {
                return new Response(1,"Question doesn't exist");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SERVER DB ERROR");
        }
    }


    // delete qst by id_question
    public Response delete(Question qst)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("delete from questions where id_question=?;");
            pst.setInt(1,qst.getId());
            if(pst.executeUpdate()!=0) {
                System.out.println("Question deleted : " + qst.getId());
                return new Response(0, "You are delete :" + qst.getId());
            }
            else
            {
                return new Response(1,"Question doesn't exist");
            }
        }
        catch(SQLException ex)
        {
            System.err.println("problem with delete Query  : "+ ex.getMessage());
            return new Response(1,"Server delete Error");
        }
    }

    // getALl Questions
    public Response getAll()
    {
        ResultSet resultSet=null;
        ArrayList<Question> arr=new ArrayList<Question>();
        try
        {
            Statement st=conn.createStatement();
            resultSet=st.executeQuery("select * from questions;");
            System.out.println("getAll questions done ! ");
            while (resultSet.next())
            {
                Question question = new Question();
                question.setId(resultSet.getInt("id_question"));
                question.setIdTest(resultSet.getInt("id_test"));
                question.setTexte(resultSet.getString("texte"));
                question.setValue(resultSet.getString("value"));
                arr.add(question);
            }
            return new Response(arr);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }


    // getALl Questions in a test
    public Response getAll(int id_test)
    {
        ResultSet resultSet=null;
        ArrayList<Question> arr=new ArrayList<Question>();
        try
        {
            PreparedStatement pst=conn.prepareStatement("select * from questions where id_test = ?;");
            pst.setInt(1,id_test);
            resultSet=pst.executeQuery();
            System.out.println("getAll questions done ! ");
            while (resultSet.next())
            {
                Question question = new Question();
                question.setId(resultSet.getInt("id_question"));
                question.setIdTest(resultSet.getInt("id_test"));
                question.setTexte(resultSet.getString("texte"));
                question.setValue(resultSet.getString("value"));
                arr.add(question);
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
        Question q = new Question();
        int id=0;
        ResultSet resultSet = null;
        try
        {
            Statement st=conn.createStatement();
            resultSet=st.executeQuery("select * from questions Order by id_question Desc;");
            if (resultSet.next())
            {
                id = resultSet.getInt("id_question");
            }
            return new Response(id);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }
}
