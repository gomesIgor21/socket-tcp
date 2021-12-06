package server;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class Worker implements Runnable{

    protected Socket client;

    public Worker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String headerLine = inFromClient.readLine();

            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();

            if(httpMethod.equals("GET")){
                if(httpQueryString.equals("/")){
                    sendResponse(200);
                } else {
                    sendResponse(400);
                }
            }

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public void sendResponse(int statusCode) throws IOException {
        File index = new File("./src/server/index.html");

        PrintWriter out = new PrintWriter(client.getOutputStream());

        BufferedReader reader = new BufferedReader(new FileReader(index));

        if(statusCode == 200){
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: text/html");
            out.println("Content-Length: " + index.length());
            out.println("\r\n");
            String line = reader.readLine();
            while (line != null) {
                out.println(line);
                line = reader.readLine();
            }
            reader.close();
            out.flush();
            out.close();
            client.close();
        } else {
            out.println("HTTP/1.1 404 NOT_FOUND");
            out.println("Content-type: text/html");
            out.println("\r\n");
            out.println("<h1>Not Found</h1>");
            out.flush();
            out.close();
            client.close();
        }
    }
}
