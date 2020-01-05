package server;

public class ServerClientHandler implements Runnable {
    @Override
    public void run() {
        System.out.println("koko");
    }

    public static void main(String[] args) {
        Runnable rn=new ServerClientHandler();
        Thread th=new Thread(rn);
        th.start();
    }
}
