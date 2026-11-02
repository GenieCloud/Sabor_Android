// Gio Jones
// MDV3832-O-C202604
// Person.java

package com.example.jonesgio_ce04;

import androidx.annotation.NonNull;

public class Person {
    private final String mFirstName;
    private final String mLastName;
    private final String mBirthday;
    private final int mPictureResId;

    public Person(String firstName, String lastName, String birthday, int pictureResId) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mBirthday = birthday;
        this.mPictureResId = pictureResId;
    }

    // Getters
    public String getFirstName() { return mFirstName; }
    public String getLastName() { return mLastName; }
    public String getBirthday() { return mBirthday; }
    public int getPictureResId() { return mPictureResId; }

    // Required for ArrayAdapter to show only names
    @NonNull
    @Override
    public String toString() {
        return mFirstName + " " + mLastName;
    }
}
