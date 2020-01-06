package server.actionHandlers;

import server.dataSource.DataSource;
import models.Etudiant;
import util.Response;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;

/* This class handles the actions coming from the client,
    and all classes who interact with database are in this ActionHandlers Package */

public class EtudiantActionHandler {

    public static Response login(Etudiant etudiant) {
        Connection conn = DataSource.getInstance().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select * from etudiant where login=? and password=?");
            statement.setString(1, etudiant.getLogin());
            statement.setString(2, etudiant.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Etudiant fullEtudiant = new Etudiant();
                fullEtudiant.setCNE(resultSet.getString("CNE"));
                fullEtudiant.setIdGroupe(resultSet.getInt("id_groupe"));
                fullEtudiant.setNom(resultSet.getString("nom"));
                fullEtudiant.setPrenom(resultSet.getString("prenom"));
                fullEtudiant.setLogin(resultSet.getString("login"));
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
