/**
 * CD Archive Management System - Bubble Sort
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

public class BubbleSort {

    public static void main(String[] args) {
         //   Object[] items = null;
        List<CDRecord> records = new ArrayList<>();

        for (int i = 0; i < 20; i++){
            int randomBarcode = (int)(Math.random() * 100);
            records.add(new CDRecord(randomBarcode));
        }

        System.out.println("Before Sort: " + records.toString());
        BubbleSort.sort(records);
        System.out.println("After Sort: " + records.toString());
    }

    public static void sort(List<CDRecord> records){
        // Assume that the list needs to be sorted
        boolean swapped = true;
        // The list is sorted once it can be traversed without swapping
        while (swapped) {
            swapped = false;
            for (int i = 1; i < records.size(); i++){
                CDRecord left = records.get(i-1);
                CDRecord right = records.get(i);
                // if(left.getBarcode() > right.getBarcode()){
                // Compare the titles, return a value of 1 for larger, value of -1 for less and 0 for equal
                if (left.getTitle().compareTo(right.getTitle()) > 0){
                    // Swap the barcodes
                    records.set(i, left);
                    records.set(i-1, right);
                    swapped = true;
                }

            }
        }


    }

}
