package server.dispatchers;

import server.Controllers.EtudiantController;
import server.Controllers.ProfController;
import util.Action;
import util.Request;
import util.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* this package holds all the dispatchers who listen for actions and fire a handler */

public class ClientDispatcher extends Dispatcher {

    public ClientDispatcher(Socket socket) {
        super(socket);
    }

    public void launch() {
        Action action = Action.DEFAULT;
        String type;
        while (true) {
            try {
                ObjectInputStream inputStream = getInputStream();
                ObjectOutputStream outputStream = getOutputStream();


                Request request = (Request) inputStream.readObject();
                type = request.getType();
                action = request.getAction();

                if (action == Action.EXIT) {
                    System.out.println("Closing session...");
                    break;
                }

                switch (type)
                {
                    case "prof":
                        Response response = ProfController.doGet(request);
                        outputStream.writeObject(response);
                        break;
                    case "etudiant":
                        Response rs = EtudiantController.doGet(request);
                        outputStream.writeObject(rs);
                        break;
                    default:
                        System.out.println("Type not found");
                        new Response(1,"Type not found !!");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {
        launch();
    }

}
