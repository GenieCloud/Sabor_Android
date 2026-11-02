// Gio Jones
// GPL - MDV3832-O
// Book.java

package com.example.jonesgio_ce05;

import java.io.Serializable;
import java.util.Date;

// NOTE: Serializable allows for easy data passing
public class Book implements Serializable {
    private final String mTitle;
    private final String mDescription;
    private final int mIssueCount;
    private final Date mPublishDate;

    public Book(String _title, String _description, int _issueCount, Date _publishDate) {
        mTitle = _title;
        mDescription = _description;
        mIssueCount = _issueCount;
        mPublishDate = _publishDate;
    }

    // Getter methods
    public String getTitle() { return mTitle; }
    public String getDescription() { return mDescription; }
    public int getIssueCount() { return mIssueCount; }
    public Date getPublishDate() { return mPublishDate; }

    @Override
    public String toString() {
        return mTitle;
    }
}