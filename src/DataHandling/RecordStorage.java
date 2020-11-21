/**
 * CD Archive Management System - Reocord Storage
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package DataHandling;

import DataHandling.CDRecordTableModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class RecordStorage {

    // This is for testing
    public static void main(String[] args) {
        // Set the file path for the record and set to a list variable
        List<CDRecord> records = loadRecordList("records.data");
        // Print the list
        System.out.println(records);
    }

    // Provide file name and get a list of records (load)
    public static List<CDRecord> loadRecordList(String filepath) {
        // Create a place to store the records
        List<CDRecord> records = new ArrayList<>();

        try {
            // Implement the File reader to read the file
            FileReader fr = new FileReader(filepath);
            // Implement the bufferedReader to reader a line at a time
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] dataColumns = line.split(";");
                CDRecord record = new CDRecord(
                        // Set the split data to a string dataColumn index value
                        dataColumns[0],
                        dataColumns[1],
                        dataColumns[2],
                        Integer.parseInt(dataColumns[3]),
                        Integer.parseInt(dataColumns[4]),
                        Integer.parseInt(dataColumns[5]),
                        dataColumns[6],
                        // Create a boolean calculation storing true if correct or false if not
                        dataColumns[7].equalsIgnoreCase("yes")
                );
                records.add(record);
            }

        } catch (Exception e) {
            System.err.println("Failed to load records: " + e.toString());
        }
        return records;
    }


    // save the records
    public static void saveRecordsList(String filepath, List<CDRecord> records) {
        // Open file for writing
        try {
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);

            for (CDRecord record : records) {
                bw.write(
                        record.getTitle() + ';'
                                + record.getAuthor() + ';'
                                + record.getSection() + ';'
                                + record.getxLocation() + ';'
                                + record.getyLocation() + ';'
                                + record.getBarcode() + ';'
                                + record.getDescription() + ';'
                                + record.onLoan + ';');
                bw.newLine();
            }
            bw.close();
        }
        catch(Exception e){
            System.err.println("Failed to load records: " + e.toString());
    }

        // For each record
        // Create string with member fields separated by:
        // write line to file
        // save and close file

    }


}
