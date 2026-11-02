// Gio Jones
// AID - C202605
// Person.java

package com.example.jonesgio_ce05_aid;

public class Person {
    // Common properties shared by all classes
    private final String name;
    private final String university;
    private final String idNumber;

    public Person(String name, String university, String idNumber) {
        this.name = name;
        this.university = university;
        this.idNumber = idNumber;
    }

    // Getters
    public String getName() { return name; }
    public String getUniversity() { return university; }
    public String getIdNumber() { return idNumber; }
}