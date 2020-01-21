package server.DAOs;

import models.Etudiant;
import models.Professeur;
import util.Response;

import java.sql.*;
import java.util.ArrayList;

public class ProfesseurDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

    // Login for profs
        public static Response login(Professeur prof)
        {
        ResultSet res=null;
        try
        {
            PreparedStatement pst =conn.prepareStatement("select * from professeurs where username=? and password=?;");
            pst.setString(1,prof.getUsername());
            pst.setString(2,prof.getPassword());
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                Professeur professeur = new Professeur();
                professeur.setMatricule(resultSet.getString("matricule"));
                professeur.setNom(resultSet.getString("nom"));
                professeur.setPrenom(resultSet.getString("prenom"));
                professeur.setUsername(resultSet.getString("username"));
                professeur.setPassword(resultSet.getString("password"));
                return new Response(professeur);
            } else {
                return new Response(1, "Wrong information");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "Server error");
        }
    }

    // getALl profs
        public static Response getAll()
        {
            ResultSet resultSet=null;
            ArrayList<Professeur> arr=new ArrayList<Professeur>();
            try
            {
                Statement st=conn.createStatement();
                resultSet=st.executeQuery("select * from professeurs;");
                System.out.println("getAllAProf done ! ");
                while (resultSet.next()) {
                    Professeur professeur = new Professeur();
                    professeur.setMatricule(resultSet.getString("matricule"));
                    professeur.setNom(resultSet.getString("nom"));
                    professeur.setPrenom(resultSet.getString("prenom"));
                    professeur.setUsername(resultSet.getString("username"));
                    professeur.setPassword(resultSet.getString("password"));
                    arr.add(professeur);
                }
                return new Response(arr);
            }
            catch (SQLException ex)
            {
                System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
                return new Response(1,"Error SQL");
            }
        }

    // Add prof
        public static Response add(Professeur prof)
        {
            try
            {
                PreparedStatement pst =conn.prepareStatement("insert into professeurs(matricule,username,password,nom,prenom) values(?,?,?,?,?);");
                pst.setString(1, prof.getMatricule());
                pst.setString(2, prof.getUsername());
                pst.setString(3, prof.getPassword());
                pst.setString(4, prof.getNom());
                pst.setString(5, prof.getPrenom());
                pst.executeUpdate();
                System.out.println("Prof Added :"+prof.getNom());
                return new Response(0,"Prof Added "+prof.getNom());
            }
            catch(SQLException ex){
                System.err.println("problem with add Query !! "+ ex.getMessage());
                return new Response(1,"SERVER DB ERROR while inserting data");
            }
        }

    // delete prof by Matricule
        public static Response delete(Professeur prof)
        {
        try
        {
            PreparedStatement pst =conn.prepareStatement("delete from professeurs where matricule=?;");
            pst.setString(1,prof.getMatricule());
            if(pst.executeUpdate()!=0) {
                System.out.println("Prof deleted : " + prof.getPrenom());
                return new Response(0, "You are delete :" + prof.getPrenom());
            }
            else
            {
                return new Response(1,"Prof doesn't exist");
            }
        }
        catch(SQLException ex)
        {
            System.err.println("problem with delete Query  : "+ ex.getMessage());
            return new Response(1,"Server delete Error");
        }
        }

    // Search prof by Matricule
        public static Response search(Professeur prof)
        {
            try{
                PreparedStatement pst =conn.prepareStatement("select * from professeurs where matricule=? ;");
                pst.setString(1,prof.getMatricule());
                ResultSet resultSet = pst.executeQuery();
                if (resultSet.next()) {
                    Professeur professeur = new Professeur();
                    professeur.setMatricule(resultSet.getString("matricule"));
                    professeur.setNom(resultSet.getString("nom"));
                    professeur.setPrenom(resultSet.getString("prenom"));
                    professeur.setUsername(resultSet.getString("username"));
                    professeur.setPassword(resultSet.getString("password"));
                    return new Response(professeur);
                } else {
                    return new Response(1, "Prof doesn't exist ");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return new Response(1, "SQL ERROR");
            }
        }

    // Update prof
        public static Response update(Professeur oldProf,Professeur newProf)
        {
        try{
            PreparedStatement pst =conn.prepareStatement("update professeurs set matricule=?,username=?,password=?,nom=?,prenom=? where matricule=?;");
            pst.setString(1, newProf.getMatricule());
            pst.setString(2, newProf.getUsername());
            pst.setString(3, newProf.getPassword());
            pst.setString(4, newProf.getNom());
            pst.setString(5, newProf.getPrenom());
            pst.setString(6, oldProf.getMatricule());
            if(pst.executeUpdate()!=0) {
                System.out.println("Prof updated : " + newProf.getPrenom());
                return new Response(0, "You are Updated :" + newProf.getPrenom());
            }
            else
            {
                return new Response(1,"Prof doesn't exist");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response(1, "SERVER DB ERROR");
        }
        }

}
