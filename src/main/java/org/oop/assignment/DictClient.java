package org.oop.assignment;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DictClient {
    // TODO: define an Enum to map the service connection information
    public static final String SERVER_HOST = "dict.org";
    public static final Integer SERVER_PORT = 2628;
    public static final Integer TIME_OUT = 15000;

    public String serverHost;
    public Integer serverPort;

    public DictClient() {
        this(SERVER_HOST, SERVER_PORT);
    }

    public DictClient(String serverHost, Integer serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void callTranslation(String word, String dictionary){
        try (Socket socket = new Socket(serverHost, serverPort)) {
            socket.setSoTimeout(TIME_OUT);
            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer = new BufferedWriter(writer);

            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8)
            );

            translate(word, dictionary, writer, reader);

            writer.write("quit\r\n");
            writer.flush();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.err.println(ioException.getMessage());
        }
    }

    static void translate(String word, String dictionary,
                          Writer writer, BufferedReader reader) throws IOException, UnsupportedEncodingException {

        writer.write("DEFINE " + dictionary + " " + word + "\r\n");
        writer.flush();

        for (String line = reader.readLine(); line != null; line = reader.readLine()){
            if(line.startsWith("250 ")) {
                return;
            } else if (line.startsWith("552 ")) {
                System.err.println("No definition found for " + word);
                return;
            }
            else if (line.matches("\\d\\d\\d .*")) continue;
            else if (line.trim().equals(".")) continue;
            else System.out.println(line);
        }
    }
}
