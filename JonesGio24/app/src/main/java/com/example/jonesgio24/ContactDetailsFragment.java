// Gio Jones
// AD2 - C202606
// ContactDetailsFragment.java

package com.example.jonesgio24;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactDetailsFragment extends Fragment {

    private static final String ARG_CONTACT_ID = "contact_id";

    private ImageView imageViewLarge;
    private TextView textViewName;
    private TextView textViewPhones;

    public ContactDetailsFragment() {

    }

    public static ContactDetailsFragment newInstance(String contactId) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewLarge = view.findViewById(R.id.imageView_details_large);
        textViewName = view.findViewById(R.id.textView_details_name);
        textViewPhones = view.findViewById(R.id.textView_details_phones);


        if (getArguments() != null && getArguments().containsKey(ARG_CONTACT_ID)) {
            String contactId = getArguments().getString(ARG_CONTACT_ID);
            displayContactDetails(contactId);
        }
    }

    public void displayContactDetails(String contactId) {
        if (getContext() == null || getView() == null) return;

        String displayName = "Unknown";
        String photoUriString = null;
        StringBuilder phoneNumbers = new StringBuilder();


        Cursor contactCursor = getContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI},
                ContactsContract.Contacts._ID + " = ?",
                new String[]{contactId},
                null
        );

        if (contactCursor != null) {
            if (contactCursor.moveToFirst()) {
                int nameIdx = contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int photoIdx = contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);

                if (nameIdx >= 0) displayName = contactCursor.getString(nameIdx);
                if (photoIdx >= 0) photoUriString = contactCursor.getString(photoIdx);
            }
            contactCursor.close();
        }

        Cursor phoneCursor = getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                int numIdx = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int typeIdx = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

                if (numIdx >= 0) {
                    String number = phoneCursor.getString(numIdx);
                    int type = typeIdx >= 0 ? phoneCursor.getInt(typeIdx) : ContactsContract.CommonDataKinds.Phone.TYPE_OTHER;


                    CharSequence label = ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(), type, "");
                    phoneNumbers.append(label).append(": ").append(number).append("\n");
                }
            }
            phoneCursor.close();
        }


        textViewName.setText(displayName);

        if (phoneNumbers.length() > 0) {
            textViewPhones.setText(phoneNumbers.toString().trim());
        } else {
            textViewPhones.setText("No phone numbers found.");
        }


        if (photoUriString != null) {
            imageViewLarge.setImageURI(Uri.parse(photoUriString));
        } else {
            imageViewLarge.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }
}