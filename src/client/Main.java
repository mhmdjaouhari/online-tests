package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import client.actionEmitters.EtudiantActionEmitter;
import models.Etudiant;
import util.Role;

public class Main {

    public static void main(String[] args) {
        Client client = new Client(Role.ETUDIANT);
        client.connect();
        String username, password;
        Scanner scanner = new Scanner(System.in);
        Etudiant etudiant = new Etudiant();
        do {
            username = scanner.nextLine();
            if (username.equals("exit")) {
                if(client.getEmitter().exit()){
                    client.getEmitter().setClientOnline(false);
                    break;
                }
            }
            password = scanner.nextLine();
            etudiant.setUsername(username);
            etudiant.setPassword(password);
            try {
                ((EtudiantActionEmitter)client.getEmitter()).login(etudiant);
            } catch(Exception e){
                System.err.print(e.getMessage());
            }
        } while (true);
    }
}
