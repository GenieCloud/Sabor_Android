// Gio Jones
// AD2 - C202606
// ImageAdapter.java

package com.example.jonesgio22_a;

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

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<File> mImageFiles;

    // Constructor
    public ImageAdapter(Context context, ArrayList<File> imageFiles) {
        this.mContext = context;
        this.mImageFiles = imageFiles;
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_image_view);

        // Get the specific image file for this grid position
        File imageFile = mImageFiles.get(position);

        if (imageFile != null && imageFile.exists()) {
            // Decode the file into a Bitmap and attach it to the view
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }

        return convertView;
    }
}