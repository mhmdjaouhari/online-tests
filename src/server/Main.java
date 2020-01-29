package server;

import javafx.collections.ObservableList;
import models.Etudiant;
import server.DAOs.EtudiantDAO;

//just for console tests
public class Main {

    public static void main(String[] args) {
//        Server server= new Server();
//        server.run();
        ObservableList<Etudiant> arr=(ObservableList<Etudiant>)EtudiantDAO.getAll(3).getData();
        System.out.println(arr);
    }
}
