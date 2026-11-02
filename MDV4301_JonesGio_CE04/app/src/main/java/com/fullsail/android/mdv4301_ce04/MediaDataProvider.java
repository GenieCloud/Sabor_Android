// Gio Jones
// MDV4301 C202607
// MediaDataProvider.java

package com.fullsail.android.mdv4301_ce04;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MediaDataProvider {

    private static final String TAG = "MediaDataProvider";

    public interface MediaQueryCallback {
        void onQueryComplete(List<String> imageUriStrings);
    }

    public static List<String> loadDeviceImagesSync(Context context) {
        List<String> imageUris = new ArrayList<>();

        Uri collectionUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collectionUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collectionUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Images.Media._ID
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = context.getContentResolver().query(
                collectionUri,
                projection,
                null,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                Log.d(TAG, "Cursor row count: " + cursor.getCount());
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    // Append ID directly to collectionUri so volume authority matches!
                    Uri contentUri = ContentUris.withAppendedId(collectionUri, id);
                    imageUris.add(contentUri.toString());
                }
            } else {
                Log.e(TAG, "Cursor returned NULL!");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying MediaStore", e);
        }

        return imageUris;
    }

    public static void loadDeviceImages(Context context, MediaQueryCallback callback) {
        new Thread(() -> {
            List<String> uris = loadDeviceImagesSync(context);
            if (callback != null) {
                callback.onQueryComplete(uris);
            }
        }).start();
    }
}