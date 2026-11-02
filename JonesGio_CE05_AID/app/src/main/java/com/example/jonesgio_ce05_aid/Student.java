// Gio Jones
// AID - C202605
// Student.java

package com.example.jonesgio_ce05_aid;

import androidx.annotation.NonNull;

public class Student extends Person {
    // Unique data member for Student class
    private final String grade;

    public Student(String name, String university, String idNumber, String grade) {
        super(name, university, idNumber); // Passes core data to the Person constructor
        this.grade = grade;
    }

    public String getGrade() { return grade; }

    @NonNull
    @Override
    public String toString() {
        // Formats the item row for the ListFragment
        return getName() + " | " + getGrade();
    }
}