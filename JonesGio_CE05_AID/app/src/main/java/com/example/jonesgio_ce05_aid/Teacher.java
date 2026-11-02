// Gio Jones
// AID - C202605
// Teacher.java

package com.example.jonesgio_ce05_aid;

import androidx.annotation.NonNull;

public class Teacher extends Person {
    // Unique data member for Teacher class
    private final String course;

    public Teacher(String name, String university, String idNumber, String course) {
        super(name, university, idNumber);
        this.course = course;
    }

    public String getCourse() { return course; }

    @NonNull
    @Override
    public String toString() {
        // Formats the item row for the ListFragment
        return getName() + " | " + getCourse();
    }
}