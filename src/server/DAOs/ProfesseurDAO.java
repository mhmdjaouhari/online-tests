package server.DAOs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Professeur;
import util.Response;

import java.sql.*;
import java.util.ArrayList;

public class ProfesseurDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

    // Login for profs
    public static Professeur login(String username,String password) throws SQLException {
        ResultSet res=null;
            PreparedStatement pst =conn.prepareStatement("select * from professeurs where username=? and password=?;");
            pst.setString(1,username);
            pst.setString(2,password);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                Professeur professeur = new Professeur();
                professeur.setMatricule(resultSet.getString("matricule"));
                professeur.setNom(resultSet.getString("nom"));
                professeur.setPrenom(resultSet.getString("prenom"));
                professeur.setUsername(resultSet.getString("username"));
                professeur.setPassword(resultSet.getString("password"));
                return professeur;
            } else {
                throw new SQLException("Invalid crendentials");
            }

    }

    // getALl profs
    public static ObservableList<Professeur> getAllProfesseurs() throws SQLException {
        ResultSet resultSet=null;
        ObservableList<Professeur> ArrayProf= FXCollections.observableArrayList();
            Statement st=conn.createStatement();
            resultSet=st.executeQuery("select * from professeurs;");
            while (resultSet.next())
            {
                Professeur professeur = new Professeur();
                professeur.setMatricule(resultSet.getString("matricule"));
                professeur.setNom(resultSet.getString("nom"));
                professeur.setPrenom(resultSet.getString("prenom"));
                professeur.setUsername(resultSet.getString("username"));
                professeur.setPassword(resultSet.getString("password"));
                ArrayProf.add(professeur);
            }
            return ArrayProf;
    }

    // Add prof
    public static void addProfesseur(Professeur professeur) throws SQLException {
        PreparedStatement statement =conn.prepareStatement(
                "insert into professeurs(matricule,username,password,nom,prenom) values(?,?,?,?,?);"
        );
        statement.setString(1, professeur.getMatricule());
        statement.setString(2, professeur.getUsername());
        statement.setString(3, professeur.getPassword());
        statement.setString(4, professeur.getNom());
        statement.setString(5, professeur.getPrenom());
        statement.executeUpdate();
        System.out.println("Prof Added :"+professeur.getNom());
    }

    // delete professeur
    public static void delete(Professeur prof) throws SQLException {
        PreparedStatement pst =conn.prepareStatement("delete from professeurs where matricule=?;");
        pst.setString(1,prof.getMatricule());
        if(pst.executeUpdate()!=0)
        {
            System.out.println("Prof deleted : "+prof.getMatricule());
        }
        else {
            throw new SQLException("Prof doesn't exist");
        }
    }

    // get professeur by Matricule
    public static Professeur getProfesseurById(String matricule) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select * from professeurs where matricule=? ;");
        statement.setString(1, matricule);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Professeur professeur = new Professeur();
            professeur.setMatricule(resultSet.getString("matricule"));
            professeur.setNom(resultSet.getString("nom"));
            professeur.setPrenom(resultSet.getString("prenom"));
            professeur.setUsername(resultSet.getString("username"));
            professeur.setPassword(resultSet.getString("password"));
            return professeur;
        } else {
            throw new SQLException("Professeur doesn't exist");
        }
    }

    // Update prof
    public static void update(Professeur oldProf,Professeur newProf) throws SQLException {
        PreparedStatement pst =conn.prepareStatement("update professeurs set matricule=?,username=?,password=?,nom=?,prenom=? where matricule=?;");
        pst.setString(1, newProf.getMatricule());
        pst.setString(2, newProf.getUsername());
        pst.setString(3, newProf.getPassword());
        pst.setString(4, newProf.getNom());
        pst.setString(5, newProf.getPrenom());
        pst.setString(6, oldProf.getMatricule());
        if(pst.executeUpdate()!=0) {
            System.out.println("Prof updated : " + newProf.getPrenom());
        }
        else
            throw new SQLException("Professeur doesn't exist");
    }
}