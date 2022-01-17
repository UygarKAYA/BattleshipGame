package GameNetwork;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    private PrintWriter writer;
    private String newField;

    private InputStreamReader streamReader;
    private OutputStreamWriter streamWriter;

    public static void main(String[] args) {
        Client client = new Client();
        Thread thread = new Thread(client);
        thread.start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", 34000);
            streamReader = new InputStreamReader(socket.getInputStream());
            streamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedWriter = new BufferedWriter(streamWriter);
            bufferedReader = new BufferedReader(streamReader);
            bufferedWriter.write("Connected Successfully");
            writer = new PrintWriter(streamWriter);
            writer.println("Connected Successfully");
            writer.flush();
        } catch (Exception exception) {
            System.out.println("Exception " + exception);
        }

        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                String connectionMessage;
                while ((connectionMessage = bufferedReader.readLine()) != null) {
                    newField = connectionMessage;
                    System.out.println(newField);
                }
            } catch (Exception exception) {
                System.out.println("There is no new area");
            }
        }
    }

    public PrintWriter getWriter() { return writer; }
    public String getMessage() { return newField; }
}