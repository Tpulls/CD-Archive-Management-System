/**
 * CD Archive Management System - Insertion Sort
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Trees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryTree {

    public static void main(String[] args) {

        Node[] nodes = new Node[]{
                new Node(40, "World"),
                new Node(50, "!"),
                new Node(30, "Hello"),
        };

        BinaryTree tree = new BinaryTree(Arrays.asList(nodes));

        System.out.println("Pre-order" + tree.traversePreOrder());
        System.out.println("In-order" + tree.traverseInOrder());
        System.out.println("Post-order" + tree.traverseInOrder());

        System.out.println(tree.traverseInOrder());

    }

    /**
     * Create a class for node with the data types
     */
    public static class Node {
        int key;
        Node left, right;
        Object data;


        /**
         * Create a constructor for the Node
         *
         * @param key
         * @param data
         */
        public Node(int key, Object data) {
            this.key = key;
            this.data = data;
        }

        public int getKey() {
            return key;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        /**
         * Create a toString constructor
         */
        public String toString() {
            return Integer.toString(this.key) + " = " + data.toString();
        }
    }

    Node root;

    public BinaryTree() {}

        public BinaryTree(List<Node> nodes) {
            for (Node node : nodes){
                this.insert(node);
            }
        }

    public Node find(int key) {
        return find(this.root, key);
    }

    private Node find(Node focusNode, int key) {
        if (focusNode.key == key) {
            return focusNode;
        } else if (key < focusNode.key) {
            return find(focusNode.left, key);
        } else if (key > focusNode.key) {
            return find(focusNode.right, key);
        } else {
            return null;
        }
    }

    // Method Overloading - Two of the same method names with different signature (parameter fields)
    public void insert(Node insertNode) {
        if (this.root == null) {
            this.root = insertNode;
        } else {
            // Call the insert method (focusNode, insertNode)
            insert(this.root, insertNode);
        }
    }

    private void insert(Node focusNode, Node insertNode) {
        if (insertNode.key < focusNode.key) {
            if (focusNode.left == null) {
                focusNode.left = insertNode;
            } else {
                insert(focusNode.left, insertNode);
            }
        } else if (insertNode.key > focusNode.key) {
            if (focusNode.right == null) {
                focusNode.right = insertNode;
            } else {
                insert(focusNode.right, insertNode);
            }
        }
    }

    public ArrayList<Node> traversePreOrder() {
        return traversePreOrder(this.root);
    }

    private ArrayList<Node> traversePreOrder(Node focusNode) {
        // Create an arrayList to capture the nodes visited
        ArrayList<Node> nodes = new ArrayList<>();
        // Check if the focusNode exists
        if (focusNode != null) {
            nodes.add(focusNode);
            nodes.addAll(traversePreOrder(focusNode.left));
            nodes.addAll(traversePreOrder(focusNode.right));
        }
        return nodes;
    }

    public ArrayList<Node> traverseInOrder() {
        // Designate the root at the starting point
        return traverseInOrder(this.root);
    }

    private ArrayList<Node> traverseInOrder(Node focusNode) {
        ArrayList<Node> nodes = new ArrayList<>();

        if (focusNode != null) {
            // traverse the left side of the tree and add the nodes to a list
            nodes.addAll(traverseInOrder(focusNode.left));
            /* print the data
            System.out.println(focusNode);*/
            // Add the node to the list
            nodes.add(focusNode);
            // traverse the right side of the tree and add the nodes to a list
            nodes.addAll(traverseInOrder(focusNode.right));
        }
        return nodes;
    }

    public ArrayList<Node> traversePostOrder() {
        // Designate the root as the start point
        return traversePostOrder(this.root);
    }

    private ArrayList<Node> traversePostOrder(Node focusNode) {
        // Create an ArrayList
        ArrayList<Node> nodes = new ArrayList<>();
        // Create an if statement to check if the focusNode is null
        if (focusNode != null) {
            nodes.addAll(traversePostOrder(focusNode.left));
            nodes.addAll(traversePostOrder(focusNode.right));
            nodes.add(focusNode);
        }
        return nodes;
    }
}
