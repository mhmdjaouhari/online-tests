package server.DAOs;

import models.Professeur;

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
    public static ArrayList<Professeur> getAllProfesseurs() throws SQLException {
        ResultSet resultSet=null;
        ArrayList<Professeur> professeurs=new ArrayList<Professeur>();
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
                professeurs.add(professeur);
            }
            return professeurs;


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


    // delete professeur by Matricule
    public static void delete(String matricule) throws SQLException {
        PreparedStatement statement =conn.prepareStatement("delete from professeurs where matricule=?;");
        statement.setString(1,matricule);
        if(statement.executeUpdate()==0) {
            throw new SQLException("Professeur doesn't exist");
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
    public static void update(String oldProfesseurMatricule,Professeur newProfesseur) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "update professeurs set username=?,password=?,nom=?,prenom=? where matricule=?;"
        );
        Professeur oldProfesseur = getProfesseurById(oldProfesseurMatricule);


        if (newProfesseur.getUsername() != null) {
            statement.setString(1, newProfesseur.getUsername());
        } else {
            statement.setString(1, oldProfesseur.getUsername());
        }

        if (newProfesseur.getPassword() != null) {
            statement.setString(2, newProfesseur.getPassword());
        } else {
            statement.setString(2, oldProfesseur.getPassword());
        }

        if (newProfesseur.getNom() != null) {
            statement.setString(3, newProfesseur.getNom());
        } else {
            statement.setString(3, oldProfesseur.getNom());
        }

        if (newProfesseur.getPrenom() != null) {
            statement.setString(4, newProfesseur.getPrenom());
        } else {
            statement.setString(4, oldProfesseur.getPrenom());
        }
        statement.setString(5, oldProfesseurMatricule);
        if (statement.executeUpdate() == 0) {
            throw new SQLException("Professeur doesn't exist");
        }
    }
}