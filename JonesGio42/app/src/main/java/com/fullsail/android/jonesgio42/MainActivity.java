// Gio Jones
// AD2 - Term C202606
// MainActivity.java

package com.fullsail.android.jonesgio42;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements WebAppInterface.WebAppInterfaceListener {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webView);

        // Configure WebView settings
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Required for JS Interface and DOM manipulation
        webSettings.setAllowFileAccess(true);    // Needed to load from local assets folder

        // Attach the JavaScript Interface Bridge
        mWebView.addJavascriptInterface(new WebAppInterface(this, this), "Android");

        // Set the custom WebViewClient to intercept custom schemas
        mWebView.setWebViewClient(new CustomWebViewClient());

        // Launch into the initial start-up screen
        loadListScreen();
    }

    // --- Navigation Helper Methods ---

    private void loadListScreen() {
        mWebView.loadUrl("file:///android_asset/list.html");

        // Wait for the page to finish loading, then inject the dynamic data list
        mWebView.setWebViewClient(new CustomWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                updateListUI();
                // Reset to standard client after page load completes
                mWebView.setWebViewClient(new CustomWebViewClient());
            }
        });
    }

    private void updateListUI() {
        // Build the dynamic HTML list string out of the DataManager array
        StringBuilder htmlBuilder = new StringBuilder();
        int index = 0;
        for (Record record : DataManager.getInstance().getRecords()) {
            // Embed parameters into custom schema link for each list item
            htmlBuilder.append("<li><a href=\"my-app-schema://details-screen?index=")
                    .append(index)
                    .append("\">")
                    .append(record.getTitle())
                    .append("</a></li>");
            index++;
        }

        // Safely evaluate JavaScript on the UI thread to update the DOM
        final String jsCode = "javascript:displayRecords('" + htmlBuilder.toString() + "')";
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(jsCode);
            }
        });
    }

    private void loadDetailsScreen() {
        mWebView.loadUrl("file:///android_asset/details.html");

        mWebView.setWebViewClient(new CustomWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Record current = DataManager.getInstance().getSelectedRecord();
                if (current != null) {
                    // Inject selected record fields straight into JavaScript populateDetails function
                    String jsCode = String.format("javascript:populateDetails('%s', '%s', %d)",
                            current.getTitle().replace("'", "\\'"),
                            current.getDescription().replace("'", "\\'"),
                            current.getQuantity());
                    mWebView.loadUrl(jsCode);
                }
                mWebView.setWebViewClient(new CustomWebViewClient());
            }
        });
    }

    // --- WebAppInterfaceListener Implementations ---

    @Override
    public void onRecordSaved() {
        // JS Interface runs on background thread; bounce back to main thread to navigation
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadListScreen();
            }
        });
    }

    @Override
    public void onRecordDeleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadListScreen();
            }
        });
    }

    // --- Custom WebViewClient to handle Custom Schema Parameters ---

    private class CustomWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return handleCustomSchema(url);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            return handleCustomSchema(url);
        }

        private boolean handleCustomSchema(String url) {
            if (url.startsWith("my-app-schema://")) {
                Uri uri = Uri.parse(url);
                String host = uri.getHost();

                if ("form-screen".equals(host)) {
                    mWebView.loadUrl("file:///android_asset/form.html");
                    return true;
                } else if ("details-screen".equals(host)) {
                    // Extract data reference index parameter
                    String indexParam = uri.getQueryParameter("index");
                    if (indexParam != null) {
                        int index = Integer.parseInt(indexParam);
                        DataManager.getInstance().setSelectedRecordIndex(index);
                        loadDetailsScreen();
                    }
                    return true;
                } else if ("list-screen".equals(host)) {
                    loadListScreen();
                    return true;
                }
            }
            return false; // Lets WebView handle standard links normally
        }
    }
}