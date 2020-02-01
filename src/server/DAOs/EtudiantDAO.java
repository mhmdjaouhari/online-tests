package server.DAOs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Etudiant;
import util.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/* This class handles the actions coming from the client,
    and all classes who interact with database are in this ActionHandlers Package */

public class EtudiantDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

    public static Etudiant login(String username, String password) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select * from etudiants where username=? and password=?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Etudiant fullEtudiant = new Etudiant();
            fullEtudiant.setCNE(resultSet.getString("CNE"));
            fullEtudiant.setIdGroupe(resultSet.getInt("id_groupe"));
            fullEtudiant.setNom(resultSet.getString("nom"));
            fullEtudiant.setPrenom(resultSet.getString("prenom"));
            fullEtudiant.setUsername(resultSet.getString("username"));
            fullEtudiant.setPassword(resultSet.getString("password"));
            return fullEtudiant;
        } else {
             throw new SQLException("Wrong information");
        }

    }

    // add Etudiant:
    public static void add(Etudiant etudiant) throws SQLException {
        PreparedStatement statement =conn.prepareStatement(
                "insert into etudiants(cne,id_groupe,username,password,nom,prenom) values(?,?,?,?,?,?);"
        );
        statement.setString(1, etudiant.getCNE());
        statement.setInt(2, etudiant.getIdGroupe());
        statement.setString(3, etudiant.getUsername());
        statement.setString(4, etudiant.getPassword());
        statement.setString(5, etudiant.getNom());
        statement.setString(6, etudiant.getPrenom());
        if(statement.executeUpdate()==0){
            throw new SQLException("Problem in adding etudian");
        }
        System.out.println("Etudiant Added !! ");
    }

    // delete Student
    public static void delete(Etudiant etd) throws SQLException {
        PreparedStatement pst =conn.prepareStatement("delete from etudiants where cne=?;");
        pst.setString(1,etd.getCNE());
        if(pst.executeUpdate()!=0)
        {
            System.out.println("Etudiant deleted : "+etd.getCNE());
        }
        else{
            throw new SQLException("Etudiant doesn't exist");
        }
    }

    // get all Students (id=-1) or all std in a given grp, "as <ObservableList>" for tabViews
    public static ObservableList<Etudiant> getAllEtudiants( int id_grp) throws SQLException {
        ResultSet resultSet=null;
        ObservableList<Etudiant> ArrayEtud= FXCollections.observableArrayList();
        Statement st=conn.createStatement();
        String query;
        if(id_grp==-1)
            query="select * from etudiants;";
        else {
            query="select * from etudiants where id_groupe="+id_grp+";";
        }
        resultSet=st.executeQuery(query);
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
            return ArrayEtud;
        }

    // getAll Student in a given groupe
    public static ArrayList<Etudiant> getAllEtudiantsInGroupe(int id_grp) throws SQLException {
        ResultSet resultSet=null;
        ArrayList<Etudiant> etudiants=new ArrayList<Etudiant>();
            PreparedStatement statement =conn.prepareStatement("select * from etudiants where id_groupe=? ;");
            statement.setInt(1,id_grp);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setCNE(resultSet.getString("CNE"));
                etudiant.setIdGroupe(resultSet.getInt("id_groupe"));
                etudiant.setNom(resultSet.getString("nom"));
                etudiant.setPrenom(resultSet.getString("prenom"));
                etudiant.setUsername(resultSet.getString("username"));
                etudiant.setPassword(resultSet.getString("password"));
                etudiants.add(etudiant);
            }
            return etudiants;
    }

    // Search student by cne
    public static Etudiant getEtudiantById(String cne) throws SQLException {
        PreparedStatement statement =conn.prepareStatement("select * from etudiants where cne=? ;");
        statement.setString(1,cne);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Etudiant etudiant = new Etudiant();
            etudiant.setCNE(resultSet.getString("CNE"));
            etudiant.setIdGroupe(resultSet.getInt("id_groupe"));
            etudiant.setNom(resultSet.getString("nom"));
            etudiant.setPrenom(resultSet.getString("prenom"));
            etudiant.setUsername(resultSet.getString("username"));
            etudiant.setPassword(resultSet.getString("password"));
            return etudiant;
        } else {
            throw new SQLException("Etudiant not found");
        }
    }

    // update Student
    public static void update(Etudiant oldEtud,Etudiant newEtud) throws SQLException {
        PreparedStatement pst =conn.prepareStatement("update etudiants set cne=?,id_groupe=?,username=?,password=?,nom=?,prenom=? where cne=?;");
        pst.setString(1, newEtud.getCNE());
        pst.setInt(2, newEtud.getIdGroupe());
        pst.setString(3, newEtud.getUsername());
        pst.setString(4, newEtud.getPassword());
        pst.setString(5, newEtud.getNom());
        pst.setString(6, newEtud.getPrenom());
        pst.setString(7, oldEtud.getCNE());
        if(pst.executeUpdate()!=0) {
            System.out.println("Prof updated : " + newEtud.getPrenom());
        }
        else
            throw new SQLException("Etudiant not found");
    }
}
