package client;

import client.actionEmitters.ActionEmitter;
import client.actionEmitters.EtudiantActionEmitter;
import client.actionEmitters.ProfesseurActionEmitter;
import javafx.event.ActionEvent;
import util.Action;
import util.Constants;
import util.Role;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    ActionEmitter emitter;
    Role role;

    public Client(Role role){
      this.role = role;
    }

    public boolean connect(){
        try{
            Socket socket = new Socket(Constants.HOST, 5000);
            emitter = instantiateEmitter(socket);
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    System.out.println("closing ...");
                    if(emitter.getClientOnline()){
                        emitter.exit();
                    }
                }
            });
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private ActionEmitter instantiateEmitter(Socket socket) throws IOException{
        switch (role){
            case ETUDIANT:
                return new EtudiantActionEmitter(socket);
            case PROFESSEUR:
                return new ProfesseurActionEmitter(socket);
            default: return null;
        }
    }

    public ActionEmitter getEmitter() {
        return emitter;
    }

    public void setEmitter(ActionEmitter emitter) {
        this.emitter = emitter;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
