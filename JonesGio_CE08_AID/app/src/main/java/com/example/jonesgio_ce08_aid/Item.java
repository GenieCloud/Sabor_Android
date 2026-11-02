// Gio Jones
// AID - C202605
// Item.java
package com.example.jonesgio_ce08_aid;

import java.io.Serializable;

public class Item implements Serializable {
    // Safely transfers Serializable objects in Intents
    private static final long serialVersionUID = 1L;

    // Data variables
    private String name;
    private int quantity;
    private String category;

    // Constructor
    public Item(String name, int quantity, String category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    // Getters
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; }

    // Makes displaying the object in a standard ListView simple
    @Override
    public String toString() {
        return name + " (Qty: " + quantity + ") - " + category;
    }
}