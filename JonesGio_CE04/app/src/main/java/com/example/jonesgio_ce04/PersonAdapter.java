// Gio Jones
// MDV3832-O-C202604
// PersonAdapter.java

package com.example.jonesgio_ce04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class PersonAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Person> mPeople;

    // Pass collection by reference through the constructor
    public PersonAdapter(Context context, ArrayList<Person> people) {
        mContext = context;
        mPeople = people;
    }

    @Override
    public int getCount() {
        if (mPeople != null) {
            return mPeople.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mPeople != null && position >= 0 && position < mPeople.size()) {
            return mPeople.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // ViewHolder pattern for max efficiency
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.base_adapter_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person = (Person) getItem(position);

        if (person != null) {
            holder.mNameView.setText(person.toString());
            holder.mBirthdayView.setText(person.getBirthday());
            holder.mImageView.setImageResource(person.getPictureResId());
        }

        return convertView;
    }

    // ViewHolder class to save view references
    private static class ViewHolder {
        final TextView mNameView;
        final TextView mBirthdayView;
        final ImageView mImageView;

        ViewHolder(View layout) {
            mNameView = layout.findViewById(R.id.text_base_name);
            mBirthdayView = layout.findViewById(R.id.text_base_birthday);
            mImageView = layout.findViewById(R.id.image_person);
        }
    }
}