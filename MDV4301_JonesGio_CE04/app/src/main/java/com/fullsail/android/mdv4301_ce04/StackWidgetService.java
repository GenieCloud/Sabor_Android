// Gio Jones
// MDV4301 C202607
// StackWidgetService.java

package com.fullsail.android.mdv4301_ce04;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackViewsFactory(this.getApplicationContext());
    }
}

class StackViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "StackViewsFactory";
    private final Context mContext;
    private final List<String> mWidgetItems = new ArrayList<>();

    public StackViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        loadData();
    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    private void loadData() {
        mWidgetItems.clear();

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

        try (Cursor cursor = mContext.getContentResolver().query(
                collectionUri,
                projection,
                null,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    mWidgetItems.add(contentUri.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to query MediaStore for stack widget", e);
        }
    }

    @Override
    public void onDestroy() {
        mWidgetItems.clear();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_stack_item);
        String uriString = mWidgetItems.get(position);

        try {
            Uri imageUri = Uri.parse(uriString);

            // First decode dimensions only
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try (InputStream is = mContext.getContentResolver().openInputStream(imageUri)) {
                BitmapFactory.decodeStream(is, null, options);
            }

            // Calculate sample size so bitmap fits comfortably in memory
            options.inSampleSize = calculateInSampleSize(options, 300, 300);
            options.inJustDecodeBounds = false;

            // Decode scaled bitmap
            Bitmap bitmap;
            try (InputStream is = mContext.getContentResolver().openInputStream(imageUri)) {
                bitmap = BitmapFactory.decodeStream(is, null, options);
            }

            if (bitmap != null) {
                rv.setImageViewBitmap(R.id.widgetImageView, bitmap);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to load bitmap at position: " + position, e);
            rv.setImageViewResource(R.id.widgetImageView, android.R.drawable.ic_menu_gallery);
        }

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("EXTRA_IMAGE_URI", uriString);
        rv.setOnClickFillInIntent(R.id.widgetImageView, fillInIntent);

        return rv;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}