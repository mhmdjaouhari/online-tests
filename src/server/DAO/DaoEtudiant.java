package server.DAO;

// we can add here the methods that you will need in GUI logic

import models.Etudiant;
import models.Professeur;
import util.Response;

import java.sql.*;

public class DaoEtudiant {

    private Connection con;
    private Statement st ;
    JDBCUtils connector;

    // constructor
    public DaoEtudiant(){
        con=connector.ConnectMySQL();
    }

    // getALl Students
    public Response getAll()
    {
        ResultSet res=null;
        try
        {
            st=con.createStatement();
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
            PreparedStatement pst =con.prepareStatement("insert into etudiants(cne,id_groupe,username,password,nom,prenom) values(?,?,?,?,?,?);");
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
            PreparedStatement pst =con.prepareStatement("delete from users where cne=?;");
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


    // login
    public Response login(Etudiant etd)
    {
        ResultSet res = null;
        try {
        PreparedStatement pst = con.prepareStatement("select * from etudiants where username=? and password=?;");
        pst.setString(1, etd.getUsername());
        pst.setString(2, etd.getPassword());
        ResultSet resultSet = pst.executeQuery();
        if (resultSet.next()) {
            Etudiant etudiant = new Etudiant();
            etudiant.setCNE(resultSet.getString("cne"));
            etudiant.setIdGroupe(resultSet.getInt("id_groupe"));
            etudiant.setNom(resultSet.getString("nom"));
            etudiant.setPrenom(resultSet.getString("prenom"));
            etudiant.setUsername(resultSet.getString("username"));
            etudiant.setPassword(resultSet.getString("password"));
            return new Response(etudiant);
        } else {
            return new Response(1, "Wrong information");
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        return new Response(1, "Server error");
    }
    }
}
