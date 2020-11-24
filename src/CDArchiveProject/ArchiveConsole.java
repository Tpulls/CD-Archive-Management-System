/**
 * CD Archive Management System - Archive Console
 *
 * Version Control: 1.0.2 - 25/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */



package CDArchiveProject;

import DataHandling.CDRecord;
import DataHandling.CDRecordTableModel;
import DataHandling.RecordStorage;
import Lists.DoubleLinkedList;
import Sockets.Client;
import Sockets.MessageListener;
import Sockets.MessageSender;
import Sorting.BubbleSort;
import Sorting.InsertionSort;
import Sorting.SelectionSort;
import Trees.BinaryTree;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ArchiveConsole {

    JFrame window;

    // Create a list member that stored the data in the table
    List<CDRecord> records;

    CDRecordTableModel tableData;
    JTable cdRecordTable;
    JScrollPane processLogScrollPane;


    BinaryTree tree = new BinaryTree();
    HashMap map = new HashMap();

    CDRecord focusedRecord;

    JTextField titleField;
    JTextField authorField;
    JTextField sectionField;
    JTextField xPositionField;
    JTextField yPositionField;
    JTextField barcodeField;
    JTextField descriptionField;
    JTextArea processLogTextArea;
    JCheckBox onLoan;

    Client client;

    DoubleLinkedList processLogDLL = new DoubleLinkedList();

    /**
     * Creates the Archive Console Window
     * */
    public ArchiveConsole(){

        // Setup our data
        records = RecordStorage.loadRecordList("records.data");
        // Initialize the JFrame
        window = new JFrame("Archive Management Console");
        // To ensure that the console closes on exit
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Where are the contents will reside
        window.getContentPane().setLayout(new GridBagLayout());
        // Set the Window Color
        window.getContentPane().setBackground(Color.GRAY);


        client = new Client("localhost:20000", new MessageListener() {
            @Override
            public void message(String msg, MessageSender sender) {
                processLogDLL.append(new DoubleLinkedList.Node(msg));
                processLogTextArea.setText(processLogDLL.toString());
            }
        });

        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }
            @Override
            public void windowClosing(WindowEvent e) { }
            @Override
            public void windowClosed(WindowEvent e) { }
            @Override
            public void windowIconified(WindowEvent e) { }
            @Override
            public void windowDeiconified(WindowEvent e) { }
            @Override
            public void windowActivated(WindowEvent e) { }
            @Override
            public void windowDeactivated(WindowEvent e) { }
        });

        createUI();

        // Scales and aligns the components
        window.pack();
        // Set minimum size of window
        window.setMinimumSize(new Dimension(700, 400));
        // Set the window size
        window.setSize(new Dimension(1000, 700));
        // Set the window to be visible
        window.setVisible(true);
    }

    /**
     * Creates the Archive Interface
     * */
    private void createUI() {

        //region Labels
        // Initialize a Label
        JLabel searchLabel = new JLabel("Search String: ");
        // Set Font Color to suit the dark theme
        searchLabel.setForeground(Color.WHITE);
        // Add the components
        addComponent(
                // Add to the window Panel, specify the label to add, set the stretch preference, location and size.
                window.getContentPane(), searchLabel, GridBagConstraints.BOTH, 0, 0, 1, 1, 0.0f, 0.0f,
                new Insets(5,5,5,2),  GridBagConstraints.WEST
        );

        //endregion

        //region text field
        // Initialize the text field
        JTextField searchText = new JTextField();
        // Add the components
        addComponent(
                // Add to the window Panel, specify the text field to add, set stretch preferences, location and size.
                window.getContentPane(), searchText, GridBagConstraints.BOTH, 1, 0, 1, 1, 40.0f, 0.0f,
                new Insets(5,5,2,5),  GridBagConstraints.WEST
        );

        //endregion

        //region Panels
        // Call the archiveListPanel
        JPanel archiveListPanel = createArchiveListPanel();
        // Add the component and parameters
        addComponent(
                // The panel I add the panel too, specify the panel i want too add (archiveListPanel), allow to stretch horizontally and vertically, location and sizing parameters
                window.getContentPane(), archiveListPanel, GridBagConstraints.BOTH, 0, 1, 3, 1,
                60.0f, 40.0f, new Insets(5,5,5,5),  GridBagConstraints.WEST
        );

        // Call the processLogPanel
        JPanel processLogPanel = createProcessLogPanel();
        // Add the component and parameters
        addComponent(
                // The panel I add the panel too, specify the panel i want too add (archiveListPanel), allow to stretch horizontally and vertically, location and sizing parameters
                window.getContentPane(), processLogPanel, GridBagConstraints.BOTH, 0, 2, 3, 1,
                60.0f, 40.0f, new Insets(5,5,40,5),  GridBagConstraints.WEST
        );
        // Call the recordPanel
        JPanel recordPanel = createRecordPanel();
        // Add the component and parameters
        addComponent(
                // The panel I add the panel too, specify the panel i want too add (archiveListPanel), allow to stretch horizontally and vertically, location and sizing parameters
                window.getContentPane(), recordPanel, GridBagConstraints.BOTH, 3, 1, 1, 1,
                40.0f, 40.0f, new Insets(5,5,5,5), GridBagConstraints.EAST
        );
        // Call the actionRequestPanel
        JPanel actionRequest = createActionRequestPanel();
        // Add the component and parameters
        addComponent(
                // The panel I add the panel too, specify the panel i want too add (archiveListPanel), allow to stretch horizontally and vertically, location and size
                window.getContentPane(), actionRequest, GridBagConstraints.BOTH, 3, 2, 1, 1,
                40.0f, 40.0f, new Insets(5,5,40,5),  GridBagConstraints.EAST
        );
        //endregion

        // Initialize a Button
        JButton searchButton = new JButton("Search");
        // Add the components
        addComponent(
                // Add to the window pane, specify the button to add, Stretch preference, location and size.
                window.getContentPane(), searchButton, GridBagConstraints.VERTICAL, 2, 0, 1, 1, 20.0f,
                0.0f, new Insets(5,5,2,2),  GridBagConstraints.WEST
        );

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String value = searchText.getText();

                    for (int row = 0; row <= tableData.getColumnCount() -1; row++)
                    {
                        for (int col = 0; col <= tableData.getColumnCount() - 1; col++) {
                            if (value.equals(tableData.getValueAt(row, col))) {
                                cdRecordTable.scrollRectToVisible(cdRecordTable.getCellRect(row, 0, true));

                                cdRecordTable.setRowSelectionInterval(row, row);

                                for (int i = 0; i <= tableData.getColumnCount() - 1; i++) {
                                    cdRecordTable.getColumnModel().getColumn(i).setCellRenderer(new HighlightRenderer());
                                }
                            }
                        }
                    }
            }
        });
    }

    /**
     * Creates the Archive List Panel
     * */
    private JPanel createArchiveListPanel() {
        // Create a new panel
        JPanel panel = new JPanel();
        // Setup a GridBagLayout inside the panel to organise the internal components
        panel.setLayout(new GridBagLayout());
        // Create a border
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // Set a color to the panel to differentiate
        panel.setBackground(new Color(130, 156, 208));

        //region Labels
        // Add the Label and set the text to CENTER
        JLabel titleLabel = new JLabel("Archive CDs", SwingConstants.CENTER);
        // Create the label font stlying
        titleLabel.setFont(new Font("TimesRoman", Font.BOLD, 14));

        // Add the components to the panel
        addComponent(
                // Add to the archiveListPanel, specify the label to add, set the stretch preference, location and size
                panel, titleLabel, GridBagConstraints.HORIZONTAL, 0, 0, 6, 1, 1.0f, 1.0f,
                new Insets(5,25,5,0),  GridBagConstraints.CENTER
        );

        // Instantiate the Label
        JLabel sortLabel = new JLabel("Sort By: ");
        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, sortLabel, GridBagConstraints.BOTH, 0, 2, 1, 1, 0.0f, 0.0f,
                new Insets(0,60,0,0),  GridBagConstraints.WEST
        );

        //endregion

        //region Table
        // Create a new JTable named cdRecord Table
        cdRecordTable = new JTable();

        // Create a model for the tableData and parse the testData
        tableData = new CDRecordTableModel(records);

        // Populate the JTable with the tableData
        cdRecordTable.setModel(tableData);

        // Creates empty rows in the table view if data is not present
        cdRecordTable.setFillsViewportHeight(true);

        // Create a Scroll pane and specify the JTable to add
        JScrollPane cdRecordTableScrollPane = new JScrollPane(cdRecordTable);

        //region Scroll Pane Scroll Bar

        //cdRecordTable.setPreferredScrollableViewportSize(new Dimension(450,63));
        cdRecordTable.setFillsViewportHeight(true);

        // Create a border
        cdRecordTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //region Table Column Formatting
        // Automatically size the columns
        // Reference: https://tips4java.wordpress.com/2008/11/10/table-column-adjuster/
        cdRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tableData.getColumnCount(); column++)
        {
            TableColumn tableColumn = cdRecordTable.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < cdRecordTable.getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = cdRecordTable.getCellRenderer(row, column);
                Component C = cdRecordTable.prepareRenderer(cellRenderer, row, column);
                int width = cdRecordTable.getPreferredSize().width + cdRecordTable.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // We've exceeded the maximum width, no need to check other rows
                if ( preferredWidth >= maxWidth)
                {
                    preferredWidth = maxWidth;
                    break;
                }
            }
        }
        //endregion

        // Add components to the table
        addComponent(
                // Add to the archiveListPanel, specify the scroll pane to add, set the stretch preference, location and size.
                panel, cdRecordTableScrollPane, GridBagConstraints.BOTH, 0, 1, 6, 1, 100.0f, 100.0f,
                new Insets(0,2,0,2),  GridBagConstraints.WEST
        );
        //endregion

        //region Buttons
        // Initialize the Title Button
        JButton sortByTitleButton = new JButton("By Title");

        // Add the components of the Buttons
        addComponent(
                // Add to archiveListPanel, specify the button, stretch to fill vertically, location and size
                panel, sortByTitleButton, GridBagConstraints.VERTICAL, 1, 2, 1, 1, 0.0f, 0.0f,
                new Insets(3,10,3,0),  GridBagConstraints.WEST
        );

        // Initialize the Author Button
        JButton sortByAuthorButton = new JButton("By Author");
        // Add the components of the Buttons
        addComponent(
                // Add to archiveListPanel, specify the button, stretch to fill vertically, location and size
                panel, sortByAuthorButton, GridBagConstraints.VERTICAL, 2, 2, 1, 1, 0.0f, 0.0f,
                new Insets(3,10,3,5),  GridBagConstraints.WEST
        );

        // Initialize the barcode Button
        JButton sortByBarcodeButton = new JButton("By Barcode");
        // Add the components of the Buttons
        addComponent(
                // Add to archiveListPanel, specify the button, stretch to fill vertically, location and size
                panel, sortByBarcodeButton, GridBagConstraints.VERTICAL, 3, 2, 1, 1, 0.0f, 0.0f,
                new Insets(3,100,3,0),  GridBagConstraints.WEST
        );

        //endregion

        //region Listeners
        // Create an event
        sortByTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BubbleSort.sort(records);
                tableData.fireTableDataChanged();

                for (CDRecord cds : records){
                    tree.insert(new BinaryTree.Node(cds.getBarcode(), cds));
                }

            }
        });

        sortByAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsertionSort.sort(records);
                tableData.fireTableDataChanged();

                for (CDRecord cds : records){
                    tree.insert(new BinaryTree.Node(cds.getBarcode(), cds));
                }
            }
        });

        // Add the action for the button
        sortByBarcodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This will run when the button is pressed.
                SelectionSort.sort(records);
                tableData.fireTableDataChanged();

                for (CDRecord cds : records){
                    tree.insert(new BinaryTree.Node(cds.getBarcode(), cds));
                }

            }
        });

        cdRecordTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = cdRecordTable.getSelectedRow();
                focusedRecord = records.get(selectedIndex);

                titleField.setText(focusedRecord.getTitle());
                authorField.setText(focusedRecord.getAuthor());
                sectionField.setText(focusedRecord.getSection());
                xPositionField.setText(Integer.toString(focusedRecord.getxLocation()));
                yPositionField.setText(Integer.toString(focusedRecord.getyLocation()));
                barcodeField.setText(Integer.toString(focusedRecord.getBarcode()));
                descriptionField.setText(focusedRecord.getDescription());
                onLoan.setSelected(focusedRecord.isOnLoan());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //endregion

        // return the panel;
        return panel;
    }

    /**
     * Creates the Archive Process Log panel
     * */
    private JPanel createProcessLogPanel() {
        JPanel panel = new JPanel();
        // Setup a GridBagLayout inside the panel to organise the internal components
        panel.setLayout(new GridBagLayout());
        // Set the pane background color
        panel.setBackground(new Color(130, 156, 208));
        // Create a border
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        //region Labels
        // Instantiate the Label
        JLabel processLogLabel = new JLabel("Process Log: ");
        // Create the label font stlying
        processLogLabel.setFont(new Font("TimesRoman", Font.BOLD, 14));
        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, processLogLabel, GridBagConstraints.BOTH, 0, 1, 1, 1, 0.0f, 0.0f,
                new Insets(0,5,0,2),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel binaryTreeLabel = new JLabel("Display Binary Tree: ");
        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, binaryTreeLabel, GridBagConstraints.BOTH, 0, 3, 1, 1, 0.0f, 0.0f,
                new Insets(0,40,2,2),  GridBagConstraints.EAST
        );

        // Instantiate the Label
        JLabel hashmapLabel = new JLabel("Hashmap / Set: ");
        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, hashmapLabel, GridBagConstraints.BOTH, 0, 4, 1, 1, 1.0f, 0.0f,
                new Insets(5,70,15,2),  GridBagConstraints.EAST
        );

        //endregion

        //region Buttons
        // Instantiate the button
        JButton btnProcessLog = new JButton("Process Log");
        // Add the components
        addComponent(
                panel, btnProcessLog, GridBagConstraints.NONE, 5, 1, 1, 1, 0.0f, 0.0f,
                new Insets(3,0,1,5),  GridBagConstraints.WEST
        );

        // Instantiate the button
        JButton preOrderButton = new JButton("Pre-Order");
        // Add the components
        addComponent(
                panel, preOrderButton, GridBagConstraints.BOTH, 1, 3, 1, 1, 0.0f, 0.0f,
                new Insets(5,10,5,5),  GridBagConstraints.WEST
        );

        // Instantiate the button
        JButton inOrderButton = new JButton("In-Order");
        // Add the components
        addComponent(
                panel, inOrderButton, GridBagConstraints.BOTH, 2, 3, 1, 1, 0.0f, 0.0f,
                new Insets(5,10,5,5),  GridBagConstraints.WEST
        );

        // Instantiate the button
        JButton postOrderButton = new JButton("Post-Order");
        // Add the components
        addComponent(
                panel, postOrderButton, GridBagConstraints.BOTH, 3, 3, 1, 1, 0.0f, 0.0f,
                new Insets(5,10,5,5),  GridBagConstraints.WEST
        );

        // Instantiate the button
        JButton graphicalButton = new JButton("Graphical");
        // Add the components
        addComponent(
                panel, graphicalButton, GridBagConstraints.BOTH, 4, 3, 1, 1, 0.0f, 0.0f,
                new Insets(5,10,5,5),  GridBagConstraints.WEST
        );

        // Instantiate the button
        JButton saveButton = new JButton("Save");
        // Add the components
        addComponent(
                panel, saveButton, GridBagConstraints.BOTH, 1, 4, 1, 1, 0.0f, 0.0f,
                new Insets(5,10,12,5),  GridBagConstraints.WEST
        );

        // Instantiate the button
        JButton displayButton = new JButton("Display");
        // Add the components
        addComponent(
                panel, displayButton, GridBagConstraints.BOTH, 2, 4, 1, 1, 0.0f, 0.0f,
                new Insets(5,10,12,5),  GridBagConstraints.WEST
        );
        //endregion

        //region Text Areas
        processLogTextArea = new JTextArea();
        // Create a Scroll pane and specify the JTable to add
        processLogScrollPane = new JScrollPane(processLogTextArea);
        // Create a border
        processLogScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        // Add the components of the JTextArea
        addComponent(
                panel, processLogScrollPane, GridBagConstraints.BOTH, 0, 2, 6, 1, 100.0f, 100.0f,
                new Insets(3,2,5,2),  GridBagConstraints.WEST
        );

        //endregion

        //region Action Listeners
        preOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!processLogTextArea.equals("")){
                    processLogTextArea.append("\n");
                }
                processLogTextArea.append("Pre Order: " + "\n");
                ArrayList<BinaryTree.Node> treeArray = tree.traversePreOrder();
                for (int i = 0; i < treeArray.size(); i++){
                    processLogTextArea.append(treeArray.get(i).toString() + "\n");
                }
            }
        });

        inOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!processLogTextArea.equals("")){
                    processLogTextArea.append("\n");
                }
                processLogTextArea.append("In Order: " + "\n");
                ArrayList<BinaryTree.Node> treeArray = tree.traverseInOrder();
                for (int i = 0; i < treeArray.size(); i++){
                    processLogTextArea.append(treeArray.get(i).toString() + "\n");
                }
            }
        });

        postOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!processLogTextArea.equals("")){
                    processLogTextArea.append("\n");
                }
                processLogTextArea.append("Post Order: " + "\n");
                    ArrayList<BinaryTree.Node> treeArray = tree.traversePostOrder();
                    for (int i = 0; i < treeArray.size(); i++){
                        processLogTextArea.append(treeArray.get(i).toString() + "\n");
                    }
                }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    map = new HashMap<>();
                    for (int i = 0; i < records.size(); i++ ) {
                        map.put(records.get(i).getBarcode(), records.get(i).getTitle());
                    }
                    FileWriter fw = new FileWriter("Map.txt");
                    fw.write(map.toString());
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map = new HashMap<>();
                for (int i = 0; i < records.size(); i++ ) {
                    map.put(records.get(i).getBarcode(), records.get(i).getTitle());
                }
                processLogTextArea.append(map.toString() + "\n");
            }
        });

        btnProcessLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLogTextArea.setText(processLogDLL.toString());
            }
        });
        //endregion
        return panel;
    }

    /**
     * Creates the Archive Record panel
     * */
    private JPanel createRecordPanel() {
        JPanel panel = new JPanel();
        // Setup a GridBagLayout inside the panel to organise the internal components
        panel.setLayout(new GridBagLayout());

        panel.setBackground(new Color(130, 156, 208));
        // Create a border
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        //region Labels

        // Instantiate the Label
        JLabel headerLabel = new JLabel("Record Details");
        // Create the label font stlying
        headerLabel.setFont(new Font("TimesRoman", Font.BOLD, 14));
        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, headerLabel, GridBagConstraints.HORIZONTAL, 0, 0, 3, 1, 0.0f, 0.0f,
                new Insets(5,30,20,0),  GridBagConstraints.CENTER
        );

        // Instantiate the Label
        JLabel titleLabel = new JLabel("Title: ");
        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, titleLabel, GridBagConstraints.BOTH, 0, 1, 1, 1, 0.0f, 0.0f,
                new Insets(0,40,5,30),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel authorLabel = new JLabel("Author: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, authorLabel, GridBagConstraints.BOTH, 0, 2, 1, 1, 0.0f, 0.0f,
                new Insets(5,40,5,30),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel sectionLabel = new JLabel("Section: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, sectionLabel, GridBagConstraints.BOTH, 0, 3, 1, 1, 0.0f, 0.0f,
                new Insets(5,40,5,0),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel xPositionLabel = new JLabel("X: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, xPositionLabel, GridBagConstraints.BOTH, 0, 4, 1, 1, 0.0f, 0.0f,
                new Insets(5,40,5,0),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel yPositionLabel = new JLabel("Y: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, yPositionLabel, GridBagConstraints.BOTH, 0, 5, 1, 1, 0.0f, 0.0f,
                new Insets(5,40,5,0),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel barcodeLabel = new JLabel("Barcode: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, barcodeLabel, GridBagConstraints.BOTH, 0, 6, 1, 1, 0.0f, 0.0f,
                new Insets(5,40,5,0),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel descriptionLabel = new JLabel("Description: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, descriptionLabel, GridBagConstraints.BOTH, 0, 7, 1, 1, 0.0f, 0.0f,
                new Insets(7,40,5,5),  GridBagConstraints.WEST
        );

        // Instantiate the Label
        JLabel onLoanLabel = new JLabel("On-Loan: ");

        // Add the components of the label
        addComponent(
                // Add to archiveListPanel, specify the label to add, set the stretch preference, location and size.
                panel, onLoanLabel, GridBagConstraints.BOTH, 0, 8, 1, 1, 0.0f, 0.0f,
                new Insets(7,40,5,5),  GridBagConstraints.WEST
        );

        //endregion

        //region Text Fields
        // Instantiate the text Field
        titleField = new JTextField();
        // Add the components
        addComponent(
                panel, titleField, GridBagConstraints.BOTH, 1,1,2,1,0.0f,0.0f,
                new Insets(0,0,5,60),  GridBagConstraints.WEST
        );

        // Instantiate the text Field
        authorField = new JTextField();
        // Add the components
        addComponent(
                panel, authorField, GridBagConstraints.BOTH, 1,2,2,1,0.0f,0.0f,
                new Insets(7,0,5,60),  GridBagConstraints.WEST
        );

        // Instantiate the text Field
        sectionField = new JTextField();
        // Add the components
        addComponent(
                panel, sectionField, GridBagConstraints.BOTH, 1,3,1,1,0.0f,0.0f,
                new Insets(7,0,5,150),  GridBagConstraints.WEST
        );

        // Instantiate the text Field
        xPositionField = new JTextField();
        // Add the components
        addComponent(
                panel, xPositionField, GridBagConstraints.BOTH, 1,4,1,1,0.0f,0.0f,
                new Insets(7,0,5,150),  GridBagConstraints.WEST
        );

        // Instantiate the text Field
        yPositionField = new JTextField();
        // Add the components
        addComponent(
                panel, yPositionField, GridBagConstraints.BOTH, 1,5,1,1,0.0f,0.0f,
                new Insets(7,0,5,150),  GridBagConstraints.WEST
        );

        // Instantiate the text Field
        barcodeField = new JTextField();
        // Add the components
        addComponent(
                panel, barcodeField, GridBagConstraints.BOTH, 1,6,2,1,0.0f,0.0f,
                new Insets(7,0,5,70),  GridBagConstraints.WEST
        );

        // Instantiate the text Field
        descriptionField = new JTextField();
        // Add the components
        addComponent(
                panel, descriptionField, GridBagConstraints.BOTH, 1,7,2,1,0.0f,0.0f,
                new Insets(7,0,0,40),  GridBagConstraints.WEST
        );

        //endregion

        //region Buttons
        // Instantiate the Button
        JButton btnNewItem = new JButton("New Item");
        // Add the components
        addComponent(
                panel, btnNewItem, GridBagConstraints.BOTH, 0,9,1,1,0.0f,0.0f,
                new Insets(15,40,9,0),  GridBagConstraints.WEST
        );

        // Instantiate the Button
        JButton btnSaveUpdate = new JButton("Save/Update");
        // Add the components
        addComponent(
                panel, btnSaveUpdate, GridBagConstraints.BOTH, 1,9,1,1,0.0f,0.0f,
                new Insets(15,60,9,40),  GridBagConstraints.WEST
        );

        //endregion
        onLoan = new JCheckBox();

        onLoan.setBackground(new Color(130, 156, 208));

        addComponent(panel, onLoan, GridBagConstraints.NONE, 1, 8, 1, 1, 0.0f, 0.0f,
                new Insets(0, 0, 0, 0), GridBagConstraints.WEST);


        btnSaveUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(focusedRecord != null){
                        // Get the data in the data fields
                        focusedRecord.setTitle(titleField.getText());
                        focusedRecord.setAuthor(authorField.getText());
                        focusedRecord.setSection(sectionField.getText());
                        focusedRecord.setxLocation(Integer.parseInt(xPositionField.getText()));
                        focusedRecord.setyLocation(Integer.parseInt(yPositionField.getText()));
                        focusedRecord.setBarcode(Integer.parseInt(barcodeField.getText()));
                        focusedRecord.setDescription(descriptionField.getText());
                        focusedRecord.setOnLoan(onLoan.isSelected());
                        tableData.fireTableDataChanged();
                        RecordStorage.saveRecordsList("records.data", records);
                    }

                    else {
                        CDRecord newRecord = new CDRecord(titleField.getText(), authorField.getText(), sectionField.getText(),
                                (Integer.parseInt(xPositionField.getText())), (Integer.parseInt(yPositionField.getText())),
                                (Integer.parseInt(barcodeField.getText())), descriptionField.getText(),
                                onLoan.isSelected());
                        records.add(newRecord);

                        tableData.fireTableDataChanged();
                        RecordStorage.saveRecordsList("records.data", records);
                    }
                }
                catch(Exception e1) {
                    JOptionPane.showMessageDialog(panel,
                            "Please ensure all data fields are completed correctly. Note - Fields: X, Y, Barcode only accepted integers",
                            "CD Archive Management System",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnNewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titleField.setText("");
                authorField.setText("");
                sectionField.setText("");
                xPositionField.setText("");
                yPositionField.setText("");

                descriptionField.setText("");
                onLoan.setSelected(false);
                cdRecordTable.clearSelection();
                focusedRecord = null;

                String barcodeGenerator = RandomStringUtils.randomNumeric(8);
                barcodeField.setText(barcodeGenerator);
            }
        });

        return panel;
    }

    /**
     * Creates the Archive Action Request panel
     * */
    private JPanel createActionRequestPanel(){
        JPanel panel = new JPanel();
        // Call the layout
        panel.setLayout(new GridBagLayout());
        //Set the background
        panel.setBackground(new Color(130, 156, 208));
        // Create a border
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        //region Labels
        //Instantiate the Label
        JLabel actionRequestHeaderLabel = new JLabel("Automation Action Request Panel ");
        // Create the label font stlying
        actionRequestHeaderLabel.setFont(new Font("TimesRoman", Font.BOLD, 14));
        //Add the components
        addComponent(
                panel, actionRequestHeaderLabel, GridBagConstraints.BOTH, 0,0, 3, 1,1.0f, 0.0f,
                new Insets(5,5,1,5), GridBagConstraints.CENTER
        );

        //Instantiate the Label
        JLabel actionRequestLabel = new JLabel("(For the Selected Record)");
        //Add the components
        addComponent(
                panel, actionRequestLabel, GridBagConstraints.BOTH, 0,1, 3, 1,1.0f, 0.0f,
                new Insets(1,5,5,5), GridBagConstraints.CENTER
        );

        //Instantiate the Label
        JLabel sortSectionLabel = new JLabel("Sort Section: ");
        //Add the components
        addComponent(
                panel, sortSectionLabel, GridBagConstraints.BOTH, 0,4, 1, 1,0.0f, 0.0f,
                new Insets(10,15,5,5), GridBagConstraints.CENTER
        );
        //endregion
        //region Buttons

        JButton retrieveButton = new JButton("Retrieve");
        //Add the components
        addComponent(
                panel, retrieveButton, GridBagConstraints.BOTH, 0, 2,2,1,0.0f,0.0f,
                new Insets(5,40,5,10), GridBagConstraints.WEST
        );

        JButton removeButton = new JButton("Remove");
        //Add the components
        addComponent(
                panel, removeButton, GridBagConstraints.BOTH, 2, 2,1,1,0.0f,0.0f,
                new Insets(5,10,5,10), GridBagConstraints.WEST
        );

        JButton returnButton = new JButton("Return");
        //Add the components
        addComponent(
                panel, returnButton, GridBagConstraints.BOTH, 0, 3,2,1,0.0f,0.0f,
                new Insets(5,40,5,10), GridBagConstraints.WEST
        );

        JButton addToCollectionButton = new JButton("Add to Collection");
        //Add the components
        addComponent(
                panel, addToCollectionButton, GridBagConstraints.BOTH, 2, 3,1,1,0.0f,0.0f,
                new Insets(5,10,5,10), GridBagConstraints.WEST
        );

        JButton randomCollectionSortButton = new JButton("Random Collection Sort");
        //Add the components
        addComponent(
                panel, randomCollectionSortButton, GridBagConstraints.BOTH, 1, 5,2,1,0.0f,0.0f,
                new Insets(5,10,0,10), GridBagConstraints.WEST
        );

        JButton mostlySortedButton = new JButton("Mostly Sorted Sort");
        //Add the components
        addComponent(
                panel, mostlySortedButton, GridBagConstraints.BOTH, 1, 6,2,1,0.0f,0.0f,
                new Insets(5,10,0,10), GridBagConstraints.WEST
        );

        JButton reverseOrderSortButton = new JButton("Reverse Order Sort");
        //Add the components
        addComponent(
                panel, reverseOrderSortButton, GridBagConstraints.BOTH, 1, 7,2,1,0.0f,0.0f,
                new Insets(5,10,10,10), GridBagConstraints.WEST
        );

        //endregion

        //region Text Fields
        // Instantiate the Text field
        JTextField sortField = new JTextField();
        // Add the components
        addComponent(
                panel, sortField, GridBagConstraints.BOTH, 1, 4, 2, 1, 0.0f, 0.0f,
                new Insets(10, 8, 0, 75), GridBagConstraints.WEST
        );

        //endregion

        //region Action Listeners
        //TODO: add date time stamp
        addToCollectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("Action Request(Archive Console) Data: Request, Title, Section, X Location, Y Location, barcode;" + "Add" + ";" + focusedRecord.getTitle() + "; "
                        + focusedRecord.getSection() + "; " + focusedRecord.getBarcode() + "; ");
            }
        });

        //TODO: add date time stamp
        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("Action Request(Archive Console) Data: Request, Title, Section, X Location, Y Location, barcode;" + "Retrieve" + ";" + focusedRecord.getTitle() + "; "
                        + focusedRecord.getSection() + "; " + focusedRecord.getBarcode() + "; ");
            }
        });

        //TODO: ADD
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("Action Request(Archive Console) Data: Request, Title, Section, X Location, Y Location, barcode;" + "Remove" + ";" + focusedRecord.getTitle() + "; "
                        + focusedRecord.getSection() + "; " + focusedRecord.getBarcode() + "; ");
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("Action Request(Archive Console) Data: Request, Title, Section, X Location, Y Location, barcode;" + "Return" + ";" + focusedRecord.getTitle() + "; "
                        + focusedRecord.getSection() + "; " + focusedRecord.getBarcode() + "; ");
            }
        });

        randomCollectionSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortField.getText().isBlank()){
                    client.sendMessage("-- Error -- Please provide a section field to be sorted");
                    sortField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                }
                else {
                    client.sendMessage("Action Request(Archive Console)" + "; " + "Sort" + ";" + "Random Collection" + "; " + sortField.getText().toUpperCase() + ";" + "N/A" + ";");
                    sortField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
                }
            }
        });

        mostlySortedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortField.getText().isBlank()){
                    client.sendMessage("-- Error -- Please provide a section field to be sorted");
                    sortField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                }
                else{
                    client.sendMessage("Action Request(Archive Console)" + "; " + "Sort" + ";" + " - Mostly Sorted" + "; " + sortField.getText().toUpperCase() + ";" + "N/A" + ";");
                    sortField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
                }
            }
        });

        reverseOrderSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortField.getText().isBlank()){
                    client.sendMessage("-- Error -- Please provide a section field to be sorted");
                    sortField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                }
                else
                {
                    client.sendMessage("Action Request(Archive Console)" + "; " + "Sort" + ";" + " - Reverse Order Sorted" + "; " + sortField.getText().toUpperCase() + ";" + "N/A" + ";");
                    sortField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
                }
            }
        });
        //endregion
        return panel;
    }

    /**
     * Bind the components for the Grid Bag Constraints (method overloading)
     * */
    // C exists in any object that extends Component
    private <C extends Component> void addComponent(
            Container contentPane, C component, int fill, int gridX, int gridY, int gridWidth, int gridHeight, float weightX, float weightY
    ) {
        // Instantiate the constraints
        GridBagConstraints constraints = new GridBagConstraints();

        ///region Parse the constraints
        constraints.fill = fill;
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        //endregion

        // Adding the constraints to the content pane with the constraints
        contentPane.add(component, constraints);

    }

    /**
     * Bind the components for the Grid Bag Constraints (method overloading)
     * */
    // C exists in any object that extends Component
    // add another method to handle the additional parameters of insets and anchor
    private <C extends Component> void addComponent(
            Container contentPane, C component, int fill, int gridX, int gridY, int gridWidth, int gridHeight, float weightX, float weightY,
            Insets insets, int anchor
    ) {
        // Instantiate the constraints
        GridBagConstraints constraints = new GridBagConstraints();

        ///region Parse the constraints
        constraints.fill = fill;
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        constraints.insets = insets;
        constraints.anchor = anchor;
        //endregion

        // Adding the constraints to the content pane with the constraints
        contentPane.add(component, constraints);

    }
    /**
     * Main run method for the Archive Console
     * */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ArchiveConsole();
                new AutomationConsoleUI();
            }
        });
    }

    /**
     * Class to handle highlighting a row as a result of a search action
     * */
    private class HighlightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(row == table.getSelectedRow())
            {
                setBorder(BorderFactory.createMatteBorder(2, 1, 2, 1, (new Color(255,254,233))));
            }
            return this;
        }
    }
}
