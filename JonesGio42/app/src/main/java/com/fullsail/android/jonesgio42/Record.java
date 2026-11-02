// Gio Jones
// AD2 - Term C202606
// Record.java

package com.fullsail.android.jonesgio42;

public class Record {

    // Member variables
    private String title;
    private String description;
    private int quantity;

    // Constructor to initialize a new record
    public Record(String title, String description, int quantity) {
        this.title = title;
        this.description = description;
        this.quantity = quantity;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
