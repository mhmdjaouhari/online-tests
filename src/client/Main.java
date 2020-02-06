package client;

import models.Etudiant;
import util.Role;

public class Main {

    public static void main(String[] args) {
        Client client = new Client(Role.ETUDIANT);
        client.connect();
    }
}
