// Gio Jones
// AD2 - Term C202606
// RedditPost.java

package com.example.jonesgio12;

import java.io.Serializable;

public class RedditPost implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final String author;
    private final int upvotes;

    public RedditPost(String title, String author, int upvotes) {
        this.title = title;
        this.author = author;
        this.upvotes = upvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getUpvotes() {
        return upvotes;
    }
}