/**
 * CD Archive Management System - Message Sender
 *
 * Version Control: 1.0.2 - 25/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Sockets;

public interface MessageSender {
    /**
     * Constructor to send a message
     * */
    void sendMessage(String msg);

}
