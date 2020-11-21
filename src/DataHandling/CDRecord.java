/**
 * CD Archive Management System - CD Record
 *
 * Version Control: 19/11/2020
 *      refer to: https://github.com/Tpulls/CD-Archive-Management-System
 *
 * AUTHOR: Thomas Pullar
 */

package DataHandling;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CDRecord {

    String title;
    String author;
    String section;
    int xLocation;
    int yLocation;
    int barcode;
    String description;
    boolean onLoan;

    // Create a constructor specifically for the barcode - N/a
    public CDRecord(int barcode){
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "CDRecord{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", section='" + section + '\'' +
                ", xLocation=" + xLocation +
                ", yLocation=" + yLocation +
                ", barcode=" + barcode +
                ", description='" + description + '\'' +
                ", onLoan=" + onLoan +
                '}';
    }

    // Create a constructor for the CDRecord components
    public CDRecord(String title, String author, String section, int xLocation, int yLocation, int barcode, String description, boolean onLoan){
        // Parse the data to the variable
        this.title = title;
        this.author = author;
        this.section = section;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.barcode = barcode;
        this.description = description;
        this.onLoan = onLoan;
    }

    // Method that returns the barcode
    public int getBarcode(){
        return barcode;
    }


    public static List<CDRecord> getTestData() {
    CDRecord[] records = new CDRecord[]{
            new CDRecord("Foo", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Bar", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Foo", "Bin", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Saalu", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Fds", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Foo", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Reet", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Quat", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Geet", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Feeb", "Bar", "A", 0, 0, 1001, "Album", false),
            new CDRecord("Deep", "Bar", "A", 0, 0, 1001, "Album", false)

    };
    // return the array as a list
    return Arrays.asList(records);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getxLocation() {
        return xLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOnLoan() {
        return onLoan;
    }

    public void setOnLoan(boolean onLoan) {
        this.onLoan = onLoan;
    }
}
