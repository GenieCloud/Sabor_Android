// Gio Jones
// AD2 - Term C202606
// DataManager.java

package com.fullsail.android.jonesgio42;

import java.util.ArrayList;

public class DataManager {

    // Single static instance of the data manager
    private static DataManager instance;

    // Global array to hold all records
    private final ArrayList<Record> recordList;

    // Track the currently selected record index
    private int selectedRecordIndex = -1;

    // Private constructor prevents instantiation from other classes
    private DataManager() {
        recordList = new ArrayList<>();
    }

    // Public method to get the single instance of the class
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


    // Add a new record to the list
    public void addRecord(Record record) {
        recordList.add(record);
    }

    // Return all saved records
    public ArrayList<Record> getRecords() {
        return recordList;
    }

    // Remove the currently selected record
    public void deleteSelectedRecord() {
        if (selectedRecordIndex >= 0 && selectedRecordIndex < recordList.size()) {
            recordList.remove(selectedRecordIndex);
            selectedRecordIndex = -1; // Reset selection after deletion
        }
    }

    public void setSelectedRecordIndex(int index) {
        this.selectedRecordIndex = index;
    }

    public Record getSelectedRecord() {
        if (selectedRecordIndex >= 0 && selectedRecordIndex < recordList.size()) {
            return recordList.get(selectedRecordIndex);
        }
        return null;
    }
}