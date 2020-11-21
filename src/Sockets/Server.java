/**
 * CD Archive Management System - Server
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements MessageSender {

    public static void main(String[] args) {
        Server server = new Server(20000, new MessageListener() {
            @Override
            public void message(String msg, MessageSender sender) {
                sender.sendMessage(msg);
            }
        });
    }

    ServerSocket connectionListener;
    MessageListener messageListener;
    ArrayList<ServerClient> clients;

    public Server(int port, MessageListener messageListener) {
        // Take the parameter and use as the listener
        this.messageListener = messageListener;
        // Define what the sender is
        MessageSender sender = this;
        // Initialize the list of client
        clients = new ArrayList<>();


        try {
            // Listening for incoming connections
            connectionListener = new ServerSocket(port);
            System.out.println("Started listening...");
            // variable to determine if the Server is running
            boolean running = true;
            while (running) {
                ServerClient client = new ServerClient(connectionListener.accept());
                clients.add(client);
                System.out.println("Client connected: " + client.connection.getInetAddress().getHostAddress());

                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Starting client message listener...");
                        while(client.connection.isConnected()) {
                            try {
                                messageListener.message(client.inputStream.readLine(), sender);
                            } catch(Exception e) {
                                System.err.println("Failed to read message: " + e);
                                break;
                            }
                        }
                    }
                })).start();
            }
        } catch(Exception e) {
            System.err.println("Failed to listen for connections: " + e);
        }
    }

    @Override
    public void sendMessage(String msg) {
        for(ServerClient client : clients) {
            if (client.connection.isClosed()) {
                clients.remove(client);
            } else {
                client.outputStream.println(msg);
            }
        }
    }

    class ServerClient {
        Socket connection;
        PrintWriter outputStream;
        BufferedReader inputStream;

        ServerClient(Socket connection) {
            this.connection = connection;
            try
            {
                outputStream = new PrintWriter(connection.getOutputStream(), true);
                inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (Exception e)
            {
                System.err.println("Failed to create client: " + e);
            }
        }
    }
}
