package server.DAOs;

import models.*;
import util.Response;

import java.sql.*;
import java.util.ArrayList;

public class FicheDAO {
    public static Connection conn = DataSource.getInstance().getConnection();

    public static Response getAll(int id_test)
    {
        ResultSet resultSet=null;
        ArrayList<Fiche> arr=new ArrayList<Fiche>();
        try
        {
            PreparedStatement pst =conn.prepareStatement("select * from fiches where id_test=? ;");
            pst.setInt(1,id_test);
            resultSet = pst.executeQuery();
            while (resultSet.next())
            {
                Fiche fiche = new Fiche();
                EtudiantDAO etdDao = new EtudiantDAO();
                GroupeDAO grpDao = new GroupeDAO();
                TestDAO testDao = new TestDAO();
                ReponseDAO repDao = new ReponseDAO();
                int id_grp = ((Etudiant)(etdDao.search(resultSet.getString("cne")).getData())).getIdGroupe();
                fiche.setId(resultSet.getInt("id_fiche"));
                fiche.setNomEtudiant(((Etudiant)(etdDao.search(resultSet.getString("cne")).getData())).getNom());
                fiche.setNomGroupeEtudiant(((Groupe)(grpDao.search(id_grp)).getData()).getNom());
                fiche.setNote(resultSet.getInt("note"));
                fiche.setTest((Test) (testDao.search(resultSet.getInt("id_test")).getData()));
                fiche.setReponse((ArrayList<Reponse>) (repDao.getAll(resultSet.getInt("id_fiche")).getData()));
                arr.add(fiche);
            }
            return new Response(arr);
        }
        catch (SQLException ex)
        {
            System.err.println("Request Error : try to check connexion or Query : "+ex.getMessage());
            return new Response(1,"Error SQL");
        }
    }
}
