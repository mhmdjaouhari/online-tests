package server.DAOs;

import server.DAOs.DataSource;
import models.Etudiant;
import util.Response;

import java.sql.*;

/* This class handles the actions coming from the client,
    and all classes who interact with database are in this ActionHandlers Package */

public class EtudiantDAO {
    public static Connection conn = DataSource.getInstance().getConnection();
    public static Statement st ;

    public static Response login(Etudiant etudiant) {
        try {
            PreparedStatement statement = conn.prepareStatement("select * from etudiants where username=? and password=?");
            statement.setString(1, etudiant.getUsername());
            statement.setString(2, etudiant.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Etudiant fullEtudiant = new Etudiant();
                fullEtudiant.setCNE(resultSet.getString("CNE"));
                fullEtudiant.setIdGroupe(resultSet.getInt("id_groupe"));
                fullEtudiant.setNom(resultSet.getString("nom"));
                fullEtudiant.setPrenom(resultSet.getString("prenom"));
                fullEtudiant.setUsername(resultSet.getString("username"));
                fullEtudiant.setPassword(resultSet.getString("password"));
                return new Response(fullEtudiant);
            } else {
                 return new Response(1, "Wrong information");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "Server error");
        }
    }



    // getALl Students
    public static Response getAll()
    {
        ResultSet res=null;
        try
        {
            st=conn.createStatement();
            res=st.executeQuery("select * from etudiants;");
            System.out.println("getAll Students done ! ");
            return new Response(res);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connection or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
        // i return a resultSet of Profs that can be used in tableViews etc....
    }

    // add Prof:
    public Response add(Etudiant etd)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("insert into etudiants(cne,id_groupe,username,password,nom,prenom) values(?,?,?,?,?,?);");
            pst.setString(1, etd.getCNE());
            pst.setInt(2, etd.getIdGroupe());
            pst.setString(3, etd.getUsername());
            pst.setString(4, etd.getPassword());
            pst.setString(5, etd.getNom());
            pst.setString(6, etd.getPrenom());
            pst.executeUpdate();

            System.out.println("Student Added !! ");
            // you can edit this response as uu want
            return new Response(2,"Added succesfully");
        }
        catch(SQLException ex){
            System.err.println("problem with add Query !! "+ ex.getMessage());
            return new Response(1,"Error SQL while inserting data");
        }
    }


    // delete Student by cne
    public Response delete(Etudiant etd)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("delete from users where cne=?;");
            pst.setString(1,etd.getCNE());
            pst.executeUpdate();
            // for test
            System.out.println("User deleted : "+etd.getPrenom());

            return new Response(2,"Student deleted !");
        }
        catch(SQLException ex)
        {
            System.err.println("problem with delete Query  : "+ ex.getMessage());
            return new Response(1,"Server delete Error");
        }
    }

}
