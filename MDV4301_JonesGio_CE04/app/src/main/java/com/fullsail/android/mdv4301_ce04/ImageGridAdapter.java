// Gio Jones
// MDV4301 C202607
// ImageGridAdapter.java

package com.fullsail.android.mdv4301_ce04;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<String> mImageUris;

    public ImageGridAdapter(Context context, List<String> imageUris) {
        this.mContext = context;
        this.mImageUris = imageUris;
    }

    @Override
    public int getCount() {
        return mImageUris != null ? mImageUris.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mImageUris != null ? mImageUris.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.gridImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String uriString = mImageUris.get(position);

        if (uriString != null) {
            holder.imageView.setImageURI(Uri.parse(uriString));
        } else {
            holder.imageView.setImageURI(null);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}