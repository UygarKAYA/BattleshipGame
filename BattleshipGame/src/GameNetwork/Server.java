package GameNetwork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<String> players;
    private ArrayList<PrintWriter> output;
    private String responseMessage;

    public static void main(String[] args) {
        Thread server = new Thread(new Server());
        server.start();
    }

    public Server() {
        initilizeServer();
    }

    private void initilizeServer() {
        try { this.serverSocket = new ServerSocket(34000); }
        catch (Exception exception) { exception.printStackTrace(); }
    }

    @Override
    public void run() {
        players = new ArrayList<>();
        output = new ArrayList<>();
        int cnt = 0;
        try {
            while (true) {
                socket = serverSocket.accept();

                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                output.add(writer);

                Thread clientThread = new Thread(new Operator(socket, cnt));
                clientThread.start();
                cnt++;
                System.out.println("Connection Established");
            }
        } catch (Exception exception) { exception.printStackTrace(); }
        finally {
            try { serverSocket.accept().close(); }
            catch (Exception exception) { exception.printStackTrace(); }
        }
    }

    private class Operator implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private int cnt;

        private Operator(Socket clientSocket, int cnt) {
            this.clientSocket = clientSocket;
            this.cnt = cnt;
            try {
                InputStreamReader isReader = new InputStreamReader(this.clientSocket.getInputStream());
                reader = new BufferedReader(isReader);
                players.add("" + cnt);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while ((responseMessage = reader.readLine()) != null) {
                    System.out.println("Admitted message is " + responseMessage);
                    for (int i = 0; i < output.size(); i++) {
                        PrintWriter writer = output.get(i);
                        try {
                            if (!players.get(i).equals("" + cnt)) {
                                writer.println(responseMessage);
                                System.out.println("Response message is " + responseMessage);
                                writer.flush();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            } catch (Exception exception) {
                System.out.println("There is no response massage");
            }
        }
    }
}