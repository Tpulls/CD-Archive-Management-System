/**
 * CD Archive Management System - Selection Sort
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */
package Sorting;

import DataHandling.CDRecord;
 import java.util.ArrayList;
 import java.util.List;

public class SelectionSort {

    // Arrays:
    // Getting a value at a index: int foo = array[index]
    // Setting a value at a index: array[index] = foo;
    // the length/size: arrays.length

    // Lists
    // Getting a value:  int foo = list.get(index)
    // Setting a value: list.set(index, foo)
    // the length/size: list.size()

    public static void main(String[] args) {
            List<CDRecord> records = new ArrayList<>();

            for (int i = 0; i < 20; i++){
                int randomBarcode = (int)(Math.random() * 100);
                records.add(new CDRecord(randomBarcode));
            }

            System.out.println("Before sort: " + records.toString());
            // Call the sort function here
        System.out.println("After sort: " + records.toString());

    }
    public static void sort(List<CDRecord> records) {
        for (int index = 0; index < records.size() -1; index++) {
            int smallestIndex = index;

            for(int currentIndex = index+1; currentIndex < records.size(); currentIndex++) {
                if (records.get(currentIndex).getBarcode() <
                        records.get(smallestIndex).getBarcode()) {
                    smallestIndex = currentIndex;
                }
            }

            //swap
            CDRecord smallestRecord = records.get(smallestIndex);
            CDRecord indexRecord = records.get(index);
            records.set(index, smallestRecord);
            records.set(smallestIndex, indexRecord);
        }
    }
}
