/**
 * CD Archive Management System - Message Listener
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Sockets;

public interface MessageListener {
    // Function to take the message and the details of who sent it
    void message(String msg, MessageSender sender);

}
