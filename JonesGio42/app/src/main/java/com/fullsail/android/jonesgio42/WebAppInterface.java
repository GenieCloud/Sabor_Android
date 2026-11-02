// Gio Jones
// AD2 - Term C202606
// WebAppInterface.java

package com.fullsail.android.jonesgio42;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {

    private final Context mContext;
    private final WebAppInterfaceListener mListener;

    // Custom interface to communicate events back to the MainActivity safely
    public interface WebAppInterfaceListener {
        void onRecordSaved();
        void onRecordDeleted();
    }

    // Constructor passing context and our custom listener
    public WebAppInterface(Context context, WebAppInterfaceListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    /**
     * Called by form.html when the user submits a new record.
     *
     * The @JavascriptInterface annotation is REQUIRED for JavaScript to see it.
     */
    @JavascriptInterface
    public void saveRecord(String title, String description, int quantity) {
        // Create the new record object
        Record newRecord = new Record(title, description, quantity);

        // Save it to our central DataManager list
        DataManager.getInstance().addRecord(newRecord);

        // Notify the Activity on the main thread to update screens
        if (mListener != null) {
            mListener.onRecordSaved();
        }
    }

    /**
     * Called by details.html when the user clicks the Delete button.
     */
    @JavascriptInterface
    public void deleteRecord() {
        // Remove the selected item from the manager
        DataManager.getInstance().deleteSelectedRecord();

        // Notify the Activity to jump back to the list screen
        if (mListener != null) {
            mListener.onRecordDeleted();
        }
    }
}