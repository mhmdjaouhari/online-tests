package server;

import models.Etudiant;
import models.Professeur;
import server.dispatchers.EtudiantDispatcher;
import util.Action;
import util.Request;
import util.Role;

import java.util.ArrayList;

//just for console tests
public class Main {
    public static void main(String[] args) {
        Server server= new Server();
        server.run();
    }
}
