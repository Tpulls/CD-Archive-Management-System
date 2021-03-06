/**
 * CD Archive Management System - Client
 *
 * Version Control: 1.0.2 - 25/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Sockets;

import java.awt.print.PrinterAbortException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements MessageSender{

    Socket connection;
    PrintWriter outputStream;
    BufferedReader inputStream;
    MessageListener messageListener;
    Thread messageListenerThread;

    /**
     * Method to handle the setup of the client
     * */
    public Client(String address, MessageListener messageListener) {
        this.messageListener = messageListener;
        MessageSender sender = this;

        String host = "localhost";
        // numbers above 1000 as below 1000 are operating systems
        int port =  20000;

        if(address.contains(":")){
            String[] hostAndPort = address.split(":");
            host = hostAndPort[0];
            port = Integer.parseInt(hostAndPort[1]);
        } else {
            host = address;
        }

        System.out.println("Creating client with remote host " + host + " and port " + port);

        try {
            // Open the connection
            connection = new Socket(host, port);
            // Create input and output streams
            outputStream = new PrintWriter(connection.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // Create listening thread
            messageListenerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (connection.isConnected()){
                        try {
                            String message = inputStream.readLine();
                            messageListener.message(message, sender);
                            System.out.println(message);
                        } catch (Exception e) {
                            System.out.println("Failed to receive message: " + e);
                            break;
                        }
                    }
                }
            });
            messageListenerThread.start();
        } catch(Exception e) {
          System.err.println("Failed to connect: " + e);
        }
    }

    /**
     * Method to handle the connection of the client
     * */
    private boolean isConnected() {
        // Checking if the connection stream is open to send data
        return connection != null && outputStream != null && inputStream != null;
    }

    /**
     * Method to handle the disconnection of the client
     * */
    public void disconnect() {
        if (isConnected()){
            try {
                inputStream = null;
                outputStream = null;
                connection.close();

            } catch (Exception e) {
                System.err.println("Failed to close connection: " + e);
            }
        }
    }
    /**
     * Method to handle the message sending
     * */
    @Override
    public void sendMessage(String msg) {
        if (isConnected()) {
            outputStream.println(msg);
        }
    }
}
