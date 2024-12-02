package org.oop.assignment;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DictClient {
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

    public String callTranslation(String word, String dictionary){
        String result = "";
        try (Socket socket = new Socket(serverHost, serverPort)) {
            socket.setSoTimeout(TIME_OUT);
            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer = new BufferedWriter(writer);

            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8)
            );

            result += translate(word, dictionary, writer, reader);

            writer.write("quit\r\n");
            writer.flush();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.err.println(ioException.getMessage());
        }
        return result;
    }

    static String translate(String word, String dictionary,
                          Writer writer, BufferedReader reader) throws IOException {
        writer.write("DEFINE " + dictionary + " " + word + "\r\n");
        writer.flush();
        StringBuilder response = new StringBuilder();

        // TODO: handle exception

        for (String line = reader.readLine(); line != null; line = reader.readLine()){
            if(line.startsWith("250 ")) {
                 return response.toString();
            } else if (line.startsWith("552 ")) {
//                System.err.println("No definition found for " + word);
//                return response.toString();
                response.append("No definition found for ");
                response.append(word);
                return response.toString();
            }
            else if (line.matches("\\d\\d\\d .*")) continue;
            else if (line.trim().equals(".")) continue;
            else response.append(line).append("\r\n");
        }
        return response.toString();
    }
}
