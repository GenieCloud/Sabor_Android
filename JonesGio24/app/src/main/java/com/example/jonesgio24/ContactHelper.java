// Gio Jones
// AD2 - C202606
// ContactHelper.java

package com.example.jonesgio24;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;

public class ContactHelper {


    public static ArrayList<ContactSummary> getAllContacts(Context context) {
        ArrayList<ContactSummary> contactList = new ArrayList<>();


        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, // Pulling all columns for debugging safety
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC" // Alphabetical order
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {

                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int hasPhoneIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                int photoIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);

                String id = idIndex >= 0 ? cursor.getString(idIndex) : "";
                String name = nameIndex >= 0 ? cursor.getString(nameIndex) : "Unknown";
                int hasPhone = hasPhoneIndex >= 0 ? cursor.getInt(hasPhoneIndex) : 0;
                String photoUri = photoIndex >= 0 ? cursor.getString(photoIndex) : null;

                String primaryPhone = "No Phone";


                if (hasPhone > 0) {
                    primaryPhone = getPrimaryPhoneNumber(context, id);
                }


                contactList.add(new ContactSummary(id, name, primaryPhone, photoUri));
            }
            cursor.close();
        }

        return contactList;
    }

    // Helper method to retrieve the primary phone number using the contact's ID
    private static String getPrimaryPhoneNumber(Context context, String contactId) {
        String phone = "No Phone";

        Cursor phoneCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        if (phoneCursor != null) {
            if (phoneCursor.moveToFirst()) {
                int phoneNumIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if (phoneNumIndex >= 0) {
                    phone = phoneCursor.getString(phoneNumIndex);
                }
            }
            phoneCursor.close();
        }
        return phone;
    }
}