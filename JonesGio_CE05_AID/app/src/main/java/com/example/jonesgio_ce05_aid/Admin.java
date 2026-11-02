// Gio Jones
// AID - C202605
// Admin.java

package com.example.jonesgio_ce05_aid;

import androidx.annotation.NonNull;

public class Admin extends Person {
    // Unique data member for Administrator
    private final String program;

    public Admin(String name, String university, String idNumber, String program) {
        super(name, university, idNumber);
        this.program = program;
    }

    public String getProgram() { return program; }

    @NonNull
    @Override
    public String toString() {
        // Formats the item row for the ListFragment
        return getName() + " | " + getProgram();
    }
}