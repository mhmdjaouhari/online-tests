package server;

import GUI.admin.ConsolleController;
import GUI.admin.StatisticsController;
import server.dispatchers.EtudiantDispatcher;
import server.dispatchers.ProfesseurDispatcher;
import util.Action;
import util.Request;
import util.Response;
import util.Role;

import java.io.*;
import java.net.Socket;


public class Session extends Thread {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Session(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public Session(Socket socket){
        this.socket = socket;
    }

    public void launch() {
        //getting streams after running the thread,
        // to not crash the entire server in case of error
         try{
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            this.outputStream = new ObjectOutputStream(out);
            this.inputStream = new ObjectInputStream(in);

        }catch (IOException e){
            System.out.println("Ping/Test request");
            return;
        }
        Action action = Action.DEFAULT;
        Role role;
        while (true){
            try {
                Request request = (Request) inputStream.readObject();

                role = request.getRole();
                action = request.getAction();

                if (action == Action.EXIT) {
                    if(role==Role.ETUDIANT) {
                        ConsolleController.log.appendText("Un client de type 'Etudiant' est déconnecté : " + socket + "\n");
                    }
                    else if (role==Role.PROFESSEUR) {
                        ConsolleController.log.appendText("Un client de type 'Prof' est déconnecté : " + socket + "\n");
                    }
                    break;
                }
                Response response;
                switch (role) {
                    case PROFESSEUR:
                        response = ProfesseurDispatcher.handle(request);
                        outputStream.writeObject(response);
                        break;
                    case ETUDIANT:
                        response = EtudiantDispatcher.handle(request);
                        outputStream.writeObject(response);
                        break;
                    default:
                        System.out.println("Type not found");
                        new Response(1, "Type not found !!");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Client break the connection ! "+Thread.currentThread());
                ConsolleController.log.appendText("Client break the connection : "+socket+"\n");
                Thread.currentThread().interrupt();
                break;
            }
        }
        try{
            inputStream.close();
            outputStream.close();
        }catch (IOException e){
            System.out.println("Error in closing streams...");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        launch();
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
