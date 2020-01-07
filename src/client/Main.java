package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import client.actionEmitters.EtudiantActionEmitter;
import models.Etudiant;

public class Main {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            EtudiantActionEmitter etudiantEmitter = new EtudiantActionEmitter(socket);
            String login, password;
            Scanner scanner = new Scanner(System.in);
            Etudiant etudiant = new Etudiant();
            do {
                login = scanner.nextLine();
                if (login.equals("exit")) {
                    break;
                }
                password = scanner.nextLine();
                etudiant.setLogin(login);
                etudiant.setPassword(password);
                etudiantEmitter.login(etudiant);
            } while (true);
            etudiantEmitter.exit();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
