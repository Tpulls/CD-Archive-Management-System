/**
 * CD Archive Management System - CD Record
 *
 * Version Control: 1.0.2 - 25/11/2020
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

    /**
     * Constructor for the barcode
     * */
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

    /**
     * Constructor for the CDRecord components
     * */
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

    /**
     * Constructor to get the Barcode
     * */
    public int getBarcode(){
        return barcode;
    }

    /**
     * Test Data method - Not required past testing
     * */
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
    /**
     * Constructor to get the Title
     * */
    public String getTitle() {
        return title;
    }
    /**
     * Constructor to set the Title
     * */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Constructor to get the Author
     * */
    public String getAuthor() {
        return author;
    }
    /**
     * Constructor to set the Author
     * */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * Constructor to get the Section
     * */
    public String getSection() {
        return section;
    }
    /**
     * Constructor to set the Section
     * */
    public void setSection(String section) {
        this.section = section;
    }
    /**
     * Constructor to get the X Location
     * */
    public int getxLocation() {
        return xLocation;
    }
    /**
     * Constructor to set the X Location
     * */
    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }
    /**
     * Constructor to get the Y Location
     * */
    public int getyLocation() {
        return yLocation;
    }
    /**
     * Constructor to set the Y Location
     * */
    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }
    /**
     * Constructor to set the Barcode
     * */
    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }
    /**
     * Constructor to get the Description
     * */
    public String getDescription() {
        return description;
    }
    /**
     * Constructor to set the Description
     * */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Constructor to get the on loan status
     * */
    public boolean isOnLoan() {
        return onLoan;
    }
    /**
     * Constructor to set the on loan status
     * */
    public void setOnLoan(boolean onLoan) {
        this.onLoan = onLoan;
    }
}
