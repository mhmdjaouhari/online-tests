package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import client.actionEmitters.EtudiantActionEmitter;
import models.Etudiant;

public class Main {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            EtudiantActionEmitter etudiantEmitter = new EtudiantActionEmitter(socket,inputStream,outputStream);
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    System.out.println("closing ...");
                    etudiantEmitter.exit();
                }
            });
            String username, password;
            Scanner scanner = new Scanner(System.in);
            Etudiant etudiant = new Etudiant();
            do {
                username = scanner.nextLine();
                if (username.equals("exit")) {
                    break;
                }
                password = scanner.nextLine();
                etudiant.setUsername(username);
                etudiant.setPassword(password);
                etudiantEmitter.login(etudiant);
            } while (true);
            etudiantEmitter.exit();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}