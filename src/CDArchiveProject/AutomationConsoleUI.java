/**
 * CD Archive Management System - Automation Console
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package CDArchiveProject;

import DataHandling.CDRecord;
import DataHandling.RecordStorage;
import DataHandling.CDRecordTableModel;
import Sockets.Client;
import Sockets.MessageListener;
import Sockets.MessageSender;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class AutomationConsoleUI {

    JFrame window;
    Client client;
    ArrayList processHistoryData;
    CDRecord focusedRecord;

    JTable archiveConsoleTable;
    CDRecordTableModel tableData;
    List<CDRecord> records;

    JLabel currentRequestedActionLabel;
    JLabel selectedItemBarcodeLabel;
    JLabel sectionLabel;
    JLabel selectedIndexLabel;

    JTextField actionField;
    JTextField selectedItemBarcodeField;
    JTextField sectionField;

    JButton processButton;
    JButton addItemButton;
    JButton exitButton;

    JComboBox actionRequestCombo;

    String requestedActions[] = {"Add to Collection", "Retrieve", "Return", "Remove", "Sort"};


    public AutomationConsoleUI() {
        records = RecordStorage.loadRecordList("records.data");
        window = new JFrame("Automation Console");
        window.getContentPane().setLayout(new GridBagLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.GRAY);

        Container content = window.getContentPane();

        content.setLayout(new GridBagLayout());
        createUI();
        window.pack();
        window.setMinimumSize(new Dimension(650, 300));
        window.setSize(new Dimension(725, 400));
        processHistoryData = new ArrayList();
        window.setVisible(true);

        client = new Client("localhost:20000", new MessageListener() {
            @Override
            public void message(String msg, MessageSender sender) {
                processHistoryData.add(msg);
            }
        });

    }

    private void createUI() {

                //region Labels
        currentRequestedActionLabel = new JLabel("Current Requested Action: ");
        addComponent(
                window.getContentPane(), currentRequestedActionLabel, GridBagConstraints.BOTH, 1, 0, 1, 1, 0.0f, 0.0f,
                new Insets(10, 60, 5, 5), GridBagConstraints.EAST
        );

        selectedItemBarcodeLabel = new JLabel("Bar Code of Selected Item: ");
        addComponent(
                window.getContentPane(), selectedItemBarcodeLabel, GridBagConstraints.BOTH, 1, 1, 1, 1, 0.0f, 0.0f,
                new Insets(10, 60, 5, 5), GridBagConstraints.EAST
        );

        sectionLabel = new JLabel("Section: ");
        addComponent(
                window.getContentPane(), sectionLabel, GridBagConstraints.BOTH, 3, 1, 1, 1, 0.0f, 0.0f,
                new Insets(10, 10, 5, 5), GridBagConstraints.EAST
        );

        selectedIndexLabel = new JLabel("");
        addComponent(
                window.getContentPane(), selectedIndexLabel, GridBagConstraints.BOTH, 6, 0, 1, 1, 0.0f, 0.0f,
                new Insets(10, 10, 5, 5), GridBagConstraints.WEST
        );
        //endregion

        //region Buttons
        processButton = new JButton("Process");
        addComponent(
                window.getContentPane(), processButton, GridBagConstraints.BOTH, 5, 0, 1, 1, 0.0f, 0.0f,
                new Insets(10, 10, 5, 5), GridBagConstraints.EAST
        );


        addItemButton = new JButton("Add Item");
        addComponent(
                window.getContentPane(), addItemButton, GridBagConstraints.BOTH, 5, 1, 1, 1, 0.0f, 0.0f,
                new Insets(10, 10, 5, 5), GridBagConstraints.EAST
        );

        exitButton = new JButton("Exit");
        addComponent(
                window.getContentPane(), exitButton, GridBagConstraints.BOTH, 7, 3, 1, 1, 0.0f, 0.0f,
                new Insets(10, 10, 5, 5), GridBagConstraints.WEST
        );

        //endregion

        //region TextFields

        actionField = new JTextField();
        addComponent(
                window.getContentPane(), actionField, GridBagConstraints.BOTH, 5, 0, 1, 1, 0.0f, 0.0f,
                new Insets(10, 10, 5, 5), GridBagConstraints.EAST
        );

        selectedItemBarcodeField = new JTextField();
        addComponent(
                window.getContentPane(), selectedItemBarcodeField, GridBagConstraints.BOTH, 2, 1, 1, 1, 70.0f, 0.0f,
                new Insets(10, 5, 5, 0), GridBagConstraints.EAST
        );

        sectionField = new JTextField();
        addComponent(
                window.getContentPane(), sectionField, GridBagConstraints.BOTH, 4, 1, 1, 1, 30.0f, 0.0f,
                new Insets(10, 0, 5, 0), GridBagConstraints.EAST
        );
        //endregion

        //region ComboBox
        actionRequestCombo = new JComboBox(requestedActions);
        actionRequestCombo.getSelectedIndex();
        addComponent(
                window.getContentPane(), actionRequestCombo, GridBagConstraints.BOTH, 2, 0, 3, 1, 1.0f, 0.0f,
                new Insets(10, 5, 5, 0), GridBagConstraints.EAST
        );

        //endregion

        JPanel createArchiveListPanel =  createArchiveListPanel();
        // Add the component and parameters
        addComponent(
                window.getContentPane(), createArchiveListPanel, GridBagConstraints.BOTH, 0, 2, 8, 1, 100.0f, 100.0f,
                new Insets(2, 40, 15, 40), GridBagConstraints.SOUTH
        );

        //region ActionListeners

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("Record Added.");
            }
        });

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send reply to archive console
                String outMessage = "";
                switch (actionRequestCombo.getSelectedIndex()) {
                    case 0: outMessage += "Action Request(Automation Console) - Add to Collection: " ; break;

                    case 1: outMessage += "Action Request(Automation Console) - Retrieve "; break;

                    case 2: outMessage += "Action Request(Automation Console) - Return: "; break;

                    case 3: outMessage += "Action Request(Automation Console) - Remove: "; break;

                    case 4: outMessage += "Action Request(Automation Console) - Sort: "; break;
                }

                outMessage += ("\n" + "Title: " + focusedRecord.getTitle() + "; " + "Section: " + focusedRecord.getSection()
                        + "; " + "X Location: " + focusedRecord.getxLocation() + "; " + "Y Location: " + focusedRecord.getyLocation() + ";");

                if(client !=  null) {
                    client.sendMessage(outMessage);
                }
            }
        });

        //endregion
    }

    // Create ArchiveListPanel
    private JPanel createArchiveListPanel() {
        //region panel
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setLayout(new GridBagLayout());

        archiveConsoleTable = new JTable();

        tableData = new CDRecordTableModel(records);

        archiveConsoleTable.setModel(tableData);

        archiveConsoleTable.setFillsViewportHeight(true);

        JScrollPane cdRecordTableScrollPane = new JScrollPane(archiveConsoleTable);
        //cdRecordTable.setPreferredScrollableViewportSize(new Dimension(450,63));
        archiveConsoleTable.setFillsViewportHeight(true);

        archiveConsoleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tableData.getColumnCount(); column++)
        {
            TableColumn tableColumn = archiveConsoleTable.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < archiveConsoleTable.getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = archiveConsoleTable.getCellRenderer(row, column);
                Component C = archiveConsoleTable.prepareRenderer(cellRenderer, row, column);
                int width = archiveConsoleTable.getPreferredSize().width + archiveConsoleTable.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // We've exceeded the maximum width, no need to check other rows
                if ( preferredWidth >= maxWidth)
                {
                    preferredWidth = maxWidth;
                    break;
                }
            }
        }

        addComponent(
                // Add to the archiveListPanel, specify the scroll pane to add, set the stretch preference, location and size.
                panel, cdRecordTableScrollPane, GridBagConstraints.BOTH, 0, 0, 8, 1, 100.0f, 100.0f,
                new Insets(0,2,0,2),  GridBagConstraints.WEST
        );

        //endregion

        //region Listeners
        archiveConsoleTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = archiveConsoleTable.getSelectedRow();
                focusedRecord = records.get(selectedIndex);

                selectedItemBarcodeField.setText(Integer.toString(focusedRecord.getBarcode()));
                sectionField.setText(focusedRecord.getSection());
                selectedIndexLabel.setText(Integer.toString(selectedIndex));
            }

            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        return panel;
        //endregion
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AutomationConsoleUI client = new AutomationConsoleUI();
            }
        });
    }


    //region addComponent

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

    //endregion
}
