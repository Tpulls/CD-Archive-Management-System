/**
 * CD Archive Management System - Insertion Sort
 *
 * Version Control: 1.0.2 - 25/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package Sorting;

import DataHandling.CDRecord;

import java.util.ArrayList;
import java.util.List;

public class InsertionSort {

    public static void main(String[] args) {
        List<CDRecord> records = new ArrayList<>();

        // Generate random records
        for (int i = 0; i < 20; i++) {
            int randomBarcode = (int) (Math.random() * 100);
            records.add(new CDRecord(randomBarcode));
        }
        System.out.println("Before sort: " + records.toString());
        // Call sort function here!
        Sorting.InsertionSort.sort(records);
        System.out.println("After sort: " + records.toString());
    }
    public static void sort(List<CDRecord> records) {
        // Set the index to 1 as to allow for the Index(1) to compare with index(0)
        for (int index = 1; index < records.size(); index++) {
            // To hold the current index value
            CDRecord indexRecord = records.get(index);
            // calculate the index of the time that we review next
            int previousIndex = index - 1;
            // Loop to swap the index
            // Start the sort at index<1>. When the previous record is compared to the index record and is greater, commit the statement conditions below:
            while (previousIndex >= 0 && records.get(previousIndex).getAuthor().compareToIgnoreCase(indexRecord.getAuthor()) > 0) {
                //
                CDRecord previousRecord = records.get(previousIndex);
                records.set(previousIndex + 1, previousRecord);
                previousIndex--;
            }

        records.set(previousIndex+1, indexRecord);
        }
    }
}

