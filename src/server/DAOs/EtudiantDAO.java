package server.DAOs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Etudiant;
import util.Response;

import java.sql.*;
import java.util.HashMap;

/* This class handles the actions coming from the client,
    and all classes who interact with database are in this ActionHandlers Package */

public class EtudiantDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

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

    // add Etudiant:
    public static Response add(Etudiant etud)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("insert into etudiants(cne,id_groupe,username,password,nom,prenom) values(?,?,?,?,?,?);");
            pst.setString(1, etud.getCNE());
            pst.setInt(2, etud.getIdGroupe());
            pst.setString(3, etud.getUsername());
            pst.setString(4, etud.getPassword());
            pst.setString(5, etud.getNom());
            pst.setString(6, etud.getPassword());
            pst.executeUpdate();
            System.out.println("Etudiant Added !! ");
            return new Response(0,"Added succesfully");
        }
        catch(SQLException ex){
            System.err.println("problem with add Query !! "+ ex.getMessage());
            return new Response(1,"étudiant déja existe dans la BD :\n"+ex.getMessage());
        }
    }

    // delete Student by cne
    public static Response delete(Etudiant etd)
    {
        try
        {
            PreparedStatement pst =conn.prepareStatement("delete from etudiants where cne=?;");
            pst.setString(1,etd.getCNE());
            if(pst.executeUpdate()!=0)
            {
                System.out.println("Etudiant deleted : "+etd.getCNE());
                return new Response(0,"You are delete :"+etd.getCNE());
            }
            else{
                System.out.println("Etudiant doesn't exist");
                return new Response(1,"Etudiant doesn't exist");
            }

        }
        catch(SQLException ex)
        {
            System.err.println("problem with delete Query  : "+ ex.getMessage());
            return new Response(1,"Server delete Error");
        }
    }

    // getAll Students
    public static Response getAll(int id)
    {
        ResultSet resultSet=null;
        ObservableList<Etudiant> ArrayEtud= FXCollections.observableArrayList();
        try
        {
            Statement st=conn.createStatement();
            String query;
            if(id==-1)
                query="select * from etudiants;";
            else {
                query="select * from etudiants where id_groupe="+id+";";
            }
            resultSet=st.executeQuery(query);
            System.out.println("getAllAProf done ! ");
            while (resultSet.next())
            {
                Etudiant fullEtudiant = new Etudiant();
                fullEtudiant.setCNE(resultSet.getString("CNE"));
                fullEtudiant.setIdGroupe(resultSet.getInt("id_groupe"));
                fullEtudiant.setNom(resultSet.getString("nom"));
                fullEtudiant.setPrenom(resultSet.getString("prenom"));
                fullEtudiant.setUsername(resultSet.getString("username"));
                fullEtudiant.setPassword(resultSet.getString("password"));
                ArrayEtud.add(fullEtudiant);
            }
            return new Response(ArrayEtud);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }

    // Search student by cne
    public static Response search(Etudiant etud)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("select * from etudiants where cne=? ;");
            pst.setString(1,etud.getCNE());
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                Etudiant fullEtudiant = new Etudiant();
                fullEtudiant.setCNE(resultSet.getString("CNE"));
                fullEtudiant.setIdGroupe(resultSet.getInt("id_groupe"));
                fullEtudiant.setNom(resultSet.getString("nom"));
                fullEtudiant.setPrenom(resultSet.getString("prenom"));
                fullEtudiant.setUsername(resultSet.getString("username"));
                fullEtudiant.setPassword(resultSet.getString("password"));
                System.out.println("Etudiant exist: "+fullEtudiant);
                return new Response(fullEtudiant);
            } else {
                return new Response(1, "Etudiant doesn't exist ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SQL ERROR"+e.getMessage());
        }
    }

    // update Student
    public static Response update(Etudiant oldEtud,Etudiant newEtud)
    {
        try{
            PreparedStatement pst =conn.prepareStatement("update etudiants set cne=?,id_groupe=?,username=?,password=?,nom=?,prenom=? where cne=?;");
            pst.setString(1, newEtud.getCNE());
            pst.setInt(2, newEtud.getIdGroupe());
            pst.setString(3, newEtud.getUsername());
            pst.setString(4, newEtud.getPassword());
            pst.setString(5, newEtud.getNom());
            pst.setString(6, newEtud.getPassword());
            pst.setString(7, oldEtud.getCNE());
            if(pst.executeUpdate()!=0) {
                System.out.println("Prof updated : " + newEtud.getPrenom());
                return new Response(0, "You are Updated :" + newEtud.getPrenom());
            }
            else
            {
                return new Response(1,"Prof doesn't exist");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SERVER DB ERROR :"+e.getMessage());
        }
    }

    public static Response getAllGroups(){
        ResultSet resultSet=null;
        HashMap<Integer,String> groups = new  HashMap<Integer,String>();
        try
        {
            Statement st=conn.createStatement();
            resultSet=st.executeQuery("select * from groupes");
            while (resultSet.next())
            {
                groups.put((Integer) resultSet.getInt("id_groupe"),(String) resultSet.getString("nom"));
            }
            return new Response(groups);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }

    // get id group by name
    public static Response getIdgroup(String nom) {
        try {
            PreparedStatement pst = conn.prepareStatement("select id_groupe from groupes where nom=? ;");
            pst.setString(1, nom);
            ResultSet resultSet = pst.executeQuery();
            String str = null;
            if (resultSet.next()) {
                return new Response(resultSet.getInt("id_groupe"));
            } else {
                return new Response(1, "Invalid group!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SQL ERROR" + e.getMessage());
        }
    }
}
