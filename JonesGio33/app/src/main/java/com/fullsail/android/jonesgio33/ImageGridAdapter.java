// Gio Jones
// AD2 - C202606
// ImageGridAdapter.java

package com.fullsail.android.jonesgio33;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class ImageGridAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<File> mImageFiles;

    public ImageGridAdapter(Context context, ArrayList<File> files) {
        this.mContext = context;
        this.mImageFiles = files;
    }

    @Override
    public int getCount() {
        return mImageFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Optimize view rendering using recycling
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.gridImageView);
        File imageFile = mImageFiles.get(position);

        if (imageFile.exists()) {
            // Load and decode the file path directly into a Bitmap image
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }
}