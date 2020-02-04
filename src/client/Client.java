package client;

import client.actionEmitters.ActionEmitter;
import client.actionEmitters.EtudiantActionEmitter;
import client.actionEmitters.ProfesseurActionEmitter;
import javafx.event.ActionEvent;
import util.Action;
import util.Constants;
import util.Role;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class Client {

    ActionEmitter emitter;
    Role role;

    private static String HOST;
    private static int PORT;

    public Client(Role role){
                try {
            File configFile = new File("config.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            HOST = props.getProperty("HOST");
            PORT = Integer.parseInt(props.getProperty("PORT"));
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Config file not found");
        }

      this.role = role;
    }

    public boolean connect(){
        try{
            Socket socket = new Socket(Client.getHOST(),Client.getPORT());
            emitter = instantiateEmitter(socket);
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    System.out.println("closing ...");
                    if(emitter.getClientOnline()){
                        emitter.exit(role);
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

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        Client.HOST = HOST;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        Client.PORT = PORT;
    }
}
