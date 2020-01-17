package server.DAOs;

import server.DAOs.DataSource;
import models.Etudiant;
import util.Response;

import java.sql.*;

/* This class handles the actions coming from the client,
    and all classes who interact with database are in this ActionHandlers Package */

public class EtudiantDAO {

    public static Response login(Etudiant etudiant) {
        Connection conn = DataSource.getInstance().getConnection();
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
}
