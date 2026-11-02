// Gio Jones
// GPL - C202604
// BookAdapter.java
package com.example.jonesgio_ce07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    // Member Variables
    private final Context mContext;
    private final ArrayList<Book> mBooks;

    // Constructor
    public BookAdapter(Context _context, ArrayList<Book> _books) {
        mContext = _context;
        mBooks = _books;
    }

    // BaseAdapter Methods
    @Override
    public int getCount() {
        return mBooks != null ? mBooks.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mBooks != null ? mBooks.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        }

        // Gets the current book object
        Book currentBook = (Book) getItem(position);

        // Find title & images within the book_item layout
        TextView tvTitle = convertView.findViewById(R.id.textView_title);
        ImageView ivCover = convertView.findViewById(R.id.imageView_cover);

        // Set the title text
        if (currentBook != null) {
            tvTitle.setText(currentBook.getTitle());

            // Uses Picasso to load the image URL into the ImageView
            if (!currentBook.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(currentBook.getImageUrl())
                        .placeholder(android.R.drawable.ic_menu_report_image) // Default image if an image does not exist or load from the API
                        .into(ivCover);
            }
        }

        return convertView;
    }
}