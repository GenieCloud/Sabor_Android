// Gio Jones
// GPL - C202604
// Book.java
package com.example.jonesgio_ce07;

import java.io.Serializable;

public class Book implements Serializable {

    private final String mTitle;
    private final String mImageUrl;

    // Book Constructor
    public Book(String _title, String _imageUrl) {
        mTitle = _title;
        mImageUrl = _imageUrl;
    }

    // Getter for book titles
    public String getTitle() {
        return mTitle;
    }
    // Getter for book images
    public String getImageUrl() {
        return mImageUrl;
    }
}