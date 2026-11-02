// Gio Jones
// AID - C202605
// Contact.java

package com.example.jonesgio_ce06_aid;

import java.io.Serializable;

public class Contact implements Serializable {

    // Private Member Variables
    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;

    // Contact constructor
    public Contact(String firstName, String lastName, String phoneNumber) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mPhoneNumber = phoneNumber;
    }

    // Getters & Setters
    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    // Helper method to easily display full name in list views
    @Override
    public String toString() {
        return mFirstName + " " + mLastName;
    }
}