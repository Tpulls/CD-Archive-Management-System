/**
 * CD Archive Management System - Double Linked List
 *
 * Version Control: 1.0.2 - 25/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Lists;

import DataHandling.CDRecord;

import javax.tools.JavaFileManager;
import java.util.List;

/**
 * Class for the Double Linked List
 * */
public class DoubleLinkedList {

    List<CDRecord> records;

    /**
     * Main class to test the Double linked list
     * */
    public static void main(String[] args) {
        DoubleLinkedList myList = new DoubleLinkedList();
        System.out.println(myList.toString());

        System.out.println("Append & Prepend Nodes");
        myList.append(new DoubleLinkedList.Node("c"));
        myList.append(new DoubleLinkedList.Node("k"));
        myList.append(new DoubleLinkedList.Node("t"));
        myList.append(new DoubleLinkedList.Node("?"));
        myList.append(new DoubleLinkedList.Node("f"));
        myList.prepend((new DoubleLinkedList.Node("b")));
        myList.prepend((new DoubleLinkedList.Node("a")));
        System.out.println(myList.toString());

        System.out.println("Replace 'k'");
        myList.find("k").setData("e");
        System.out.println(myList.toString());

        System.out.println("Insert After 'd'");
        myList.insertAfter(myList.find("c"), new DoubleLinkedList.Node("d"));
        System.out.println(myList.toString());

        System.out.println("Remove '?'");
        myList.remove(myList.find("?"));
        System.out.println(myList.toString());

        // Insert Before
        System.out.println("Insert Before '+'");
        myList.insertBefore(myList.find("e"), new DoubleLinkedList.Node("+"));
        System.out.println(myList.toString());

        // System Print Reversed text
        System.out.println("Reverse Text");
        System.out.println(myList.toStringReverse());

    }

    Node head, tail;

    /**
     * Constructor for the Node
     * */
    public static class Node {

        // Member: Pointer to next and previous
        Node next;
        Node previous;

        // Member: Field to hold data
        Object data;

        // Constructor
        public Node(Object data) {this.data = data; }

        // Method: Get data
        public Object getData(Object data) {
            return data;
        }

        // Method: Set data
        public void setData(Object data) {
            this.data = data;
        }

        // Method: get next
        public Node getNext() {
            return next;
        }

        // Method: get previous
        public Node getPrevious() {
            return previous;
        }

    }

    /**
     * Method to handle the DLL prepend
     * */
    public void prepend(Node node) {
        if (this.head == null || this.tail == null) {
            this.head = node;
            this.tail = node;
        } else {
            node.next = this.head;
            this.head.previous = node;
            this.head = node;
        }
    }

    /**
     * Method to handle the DLL append
     * */
    public void append(Node node) {
        if (this.head == null || this.tail == null) {
            this.head = node;
            this.tail = node;
        } else {
            // Create the pointer to the new tail append item
            this.tail.next = node;
            //  Create the pointer from the new tail to the previous index
            node.previous = this.tail;
            // Append the data to the new pointer
            this.tail = node;
        }
    }

    /**
     * Method to handle the DLL find
     * */
    public Node find(Object data) {
        // Start looking from the headNode
        Node focusNode = this.head;

        // While the focusNode still has value
        while (focusNode != null) {
            // if the data matches the node data
            if (focusNode.data == data) {
                // return the focusNode
                return focusNode;
            }
            // Create a statement to handle the loop (move to next)
            focusNode = focusNode.next;
        }
        return null;
    }

    /**
     * Method to handle the DLL insertBefore
     * */
    public void insertBefore(Node nodeAfter, Node nodeBefore) {
        // Unsure what this line of code achieves
        if (nodeBefore == null) {
            return;
        }
        // If the head node is after the insert
        if (this.head == nodeAfter) {
            // Set the Head to nodeBefore
            this.head = nodeBefore;
        }

        nodeAfter.previous.next = nodeBefore;

        nodeBefore.previous = nodeAfter.previous;

        nodeAfter.previous = nodeBefore;

        nodeBefore.next = nodeAfter;
    }


    /**
     * Method to handle the DLL insertAfter
     * */
    public void insertAfter(Node nodeBefore, Node nodeAfter) {
        if (nodeBefore == null) {
            return;
        }
        // Check to see if we are inserting at the tail node
        if (this.tail == nodeBefore) {
            this.tail = nodeAfter;
        }

        // Create a new pointer to the new node
        nodeAfter.next = nodeBefore.next;
        //Return the pointer
        nodeAfter.previous = nodeBefore;
        // Create a new pointer to the previous node
        nodeBefore.next = nodeAfter;

        if (nodeAfter.next != null) {
            nodeAfter.next.previous = nodeAfter;
        }
    }

    /**
     * Method to handle the DLL node remove
     * */
    public void remove(Node node) {
        // If we want to remove the head, change the new head to the next node
        if (node == this.head) {
            this.head = node.next;
        } else if (node == this.tail) {
            this.tail = null;
        } else {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }
    }

    /**
     * Method to handle of the DLL node remove
     * */
    public String toString() {
        Node focusNode = this.head;
        String str = "";
        // While there is still more nodes
        while (focusNode != null) {
            str += focusNode.data.toString();
            if (focusNode.next != null) {
                str += "\n";
            }
            focusNode = focusNode.next;
        }
        return str;
    }

    /**
     * Method to test the DLL and node modification methods
     * */
    public String toStringReverse() {
        Node focusNode = this.tail;
        String str = "DoubleLinkedList: ";
        // While there is still more nodes
        while (focusNode != null) {
            str += focusNode.data.toString();
            if (focusNode.previous != null) {
                str += " <--> ";
            }
            focusNode = focusNode.previous;
        }
        return str;
    }
}
