package server.DAOs;

import models.Professeur;
import util.Response;

import java.sql.*;

public class ProfesseurDAO {
        public static Connection conn = DataSource.getInstance().getConnection();
        public static Statement st ;

    // Login for profs
    public static Response login(Professeur prof){
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
            ResultSet res=null;
            try
            {
                st=conn.createStatement();
                res=st.executeQuery("select * from professeurs;");
                System.out.println("getAllAProf done ! ");
                return new Response(res);
            }
            catch (SQLException ex)
            {
                System.err.println("Request Error : try to check connextion or Query : "+ex.getMessage());
                return new Response(1,"Error SQL");
            }
            // i return a resultSet of Profs that can be used in tableViews etc....
        }

        // add Prof:
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
                System.out.println("Prof Added !! ");
                // you can edit this response as uu want
                return new Response(2,"Added succesfully");
            }
            catch(SQLException ex){
                System.err.println("problem with add Query !! "+ ex.getMessage());
                return new Response(1,"Error SQL while inserting data");
            }
        }


}