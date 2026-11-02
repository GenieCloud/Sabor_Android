// Gio Jones
// MDV4301-O - C202607
// MapMarkerModel.java

package com.fullsail.android.mdv4301_jonesgio_ce01;

public class MapMarkerModel {
    private final String title;
    private final String description;
    private final double latitude;
    private final double longitude;
    private final String imageUriString; // Storing the URI as a string

    public MapMarkerModel(String title, String description, double latitude, double longitude, String imageUriString) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUriString = imageUriString;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImageUriString() { return imageUriString; }
}