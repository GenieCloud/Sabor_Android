// Gio Jones
// MDV4301-O - C202607
// MapMarkerItem.java

package com.fullsail.android.mdv4301_jonesgio_ce01;

import com.google.android.gms.maps.model.LatLng;

public class MapMarkerItem {
    private final String imageUriString;
    private final String notes;
    private final LatLng coordinates;

    public MapMarkerItem(String imageUriString, String notes, LatLng coordinates) {
        this.imageUriString = imageUriString;
        this.notes = notes;
        this.coordinates = coordinates;
    }

    public String getImageUriString() {
        return imageUriString;
    }

    public String getNotes() {
        return notes;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}