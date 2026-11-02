// Gio Jones
// AD2 - C202606
// ContactAdapter.java

package com.example.jonesgio24;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    // Interface to handle row selection clicks back in the Fragment
    public interface OnContactClickListener {
        void onContactClick(ContactSummary contact);
    }

    private final ArrayList<ContactSummary> contactList;
    private final OnContactClickListener clickListener;

    public ContactAdapter(ArrayList<ContactSummary> contactList, OnContactClickListener clickListener) {
        this.contactList = contactList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactSummary contact = contactList.get(position);

        holder.textViewName.setText(contact.getDisplayName());
        holder.textViewPhone.setText(contact.getPhoneNumber());

        if (contact.getPhotoThumbnailUri() != null) {
            holder.imageViewThumbnail.setImageURI(Uri.parse(contact.getPhotoThumbnailUri()));
        } else {
            // Set a fallback generic person icon so recycled views don't display old images
            holder.imageViewThumbnail.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Sets click listener on the entire row layout container
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onContactClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }


    static class ContactViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewThumbnail;
        final TextView textViewName;
        final TextView textViewPhone;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageView_row_thumbnail);
            textViewName = itemView.findViewById(R.id.textView_row_name);
            textViewPhone = itemView.findViewById(R.id.textView_row_phone);
        }
    }
}
