/**
 * CD Archive Management System - CD Record Table Model
 *
 * Version Control: 1.0.2 - 25/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package DataHandling;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CDRecordTableModel extends AbstractTableModel {

    /**
     * Used to set the table column headers
     * */
    private static String[] columnNames = new String[] {
            "Title", "Author", "Section", "X", "Y", "Barcode", "Description", "On Loan"
    };

    // Instantiate a new records List
    List<CDRecord> records;
    /**
     * Create a constructor method to allow a list of data
     * */
    public CDRecordTableModel(List<CDRecord> records) {
        // Parse the records to the variable
        this.records = records;
    }

    /**
     * Create a method to override the column names
     * */
    @Override
    public String getColumnName(int col) {
        // return the column names according to the list
        return columnNames[col];
    }
    /**
     * Create a method to get the row count
     * */
    @Override
    public int getRowCount() {
        // Return the number of records in the list
        return records.size();
    }
    /**
     * Create a method to get the column count
     * */
    @Override
    public int getColumnCount() {
        // return the number of fields in the column name
        return columnNames.length;
    }

    /**
     * Create a method to Map the data to each of the columns
     * */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // retrieve the data of the row according to the selected index
        CDRecord record = this.records.get(rowIndex);

        // Populate the columns fields in the assigned row
        switch(columnIndex) {
            case 0: return record.getTitle();
            case 1: return record.getAuthor();
            case 2: return record.getSection();
            case 3: return record.getxLocation();
            case 4: return record.getyLocation();
            case 5: return record.getBarcode();
            case 6: return record.getDescription();
            // using 'is' to meet boolean semantics
            case 7: return record.isOnLoan();
        }
        return null;
    }
}
