// Gio Jones
// GPL - MDV3832-O
// BookAdapter.java

package com.example.jonesgio_ce05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Book> mBooks;
    private static final long ID_CONSTANT = 0x01010101L;

    public BookAdapter(Context _context, ArrayList<Book> _books) {
        mContext = _context;
        mBooks = _books;
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int _position) {
        return mBooks.get(_position);
    }

    @Override
    public long getItemId(int _position) {
        return ID_CONSTANT + _position;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        ViewHolder holder;

        // Check if an existing view can be reused
        if (_convertView == null) {
            _convertView = LayoutInflater.from(mContext).inflate(R.layout.book_item_layout, _parent, false);

            holder = new ViewHolder();
            // Match these IDs to your custom item layout XML
            holder.title = _convertView.findViewById(R.id.tv_item_title);
            holder.description = _convertView.findViewById(R.id.tv_item_description);

            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }

        // Get the data for the current position
        Book currentBook = (Book) getItem(_position);

        // Populate the views with at least 2 data points
        if (currentBook != null) {
            holder.title.setText(currentBook.getTitle());
            holder.description.setText(currentBook.getDescription());
        }

        return _convertView;
    }

    // ViewHolder pattern prevents repeated findViewById calls
    private static class ViewHolder {
        TextView title;
        TextView description;
    }
}