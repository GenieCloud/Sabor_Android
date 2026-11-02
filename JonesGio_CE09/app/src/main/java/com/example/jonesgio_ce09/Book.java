// Gio Jones
// GPL - Term C202604
// Book.java

package com.example.jonesgio_ce09;
public class Book {
    // Member variables
    private final String titleString;
    private final String thumbnailUrl;

    // Constructor
    public Book(String title, String url) {
        this.titleString = title;
        this.thumbnailUrl = url;
    }

    // Getters for getting the title and thumbnail URL
    public String getTitle() {
        return titleString;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}