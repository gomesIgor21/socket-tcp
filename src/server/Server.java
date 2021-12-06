package server;

import java.net.*;

public class Server implements Runnable{

    protected boolean isStopped = false;
    protected static ServerSocket server = null;

    public static void main(String[] args) {

        try {

            server = new ServerSocket(9000);
            System.out.println("Server is ON and listening for incoming requests...");
            Thread t1 = new Thread(new Server());
            t1.start();

        } catch(Exception e) {
            System.out.println("Could not open port 9000.\n" + e);
        }

    }

    @Override
    public void run() {

        while(!isStopped) {

            try {
                Socket client = server.accept();
                System.out.println("Client has connected.");
                Thread t2 = new Thread(new Server());
                t2.start();
                new Thread(new Worker(client)).start();
            } catch(Exception e) {
                System.out.println(e);
            }

        }

    }
}