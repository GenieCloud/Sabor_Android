// Gio Jones
// AD2 - C202606
// ContactSummary.java

package com.example.jonesgio24;

public class ContactSummary {
    private final String id;
    private final String displayName;
    private final String phoneNumber;
    private final String photoThumbnailUri;

    public ContactSummary(String id, String displayName, String phoneNumber, String photoThumbnailUri) {
        this.id = id;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.photoThumbnailUri = photoThumbnailUri;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPhotoThumbnailUri() { return photoThumbnailUri; }
}