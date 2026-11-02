// Gio Jones
// GPL - Term C202604
// BookAdapter.java

package com.example.jonesgio_ce09;

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
    private final Context context;
    private final ArrayList<Book> bookList;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.bookList = books;
    }

    @Override
    public int getCount() { return bookList.size(); }

    @Override
    public Object getItem(int i) { return bookList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        }

        Book currentBook = bookList.get(position);

        // Title and Image converted by ID
        TextView title = convertView.findViewById(R.id.text_book_title);
        ImageView cover = convertView.findViewById(R.id.image_book_cover);

        title.setText(currentBook.getTitle());

        // Picasso to load the thumbnail URL
        Picasso.get()
                .load(currentBook.getThumbnailUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(cover);

        return convertView;
    }
}