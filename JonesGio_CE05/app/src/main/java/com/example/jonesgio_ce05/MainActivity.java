// Gio Jones
// GPL - MDV3832-O
// MainActivity.java

package com.example.jonesgio_ce05;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> mBookList;
    private TextView tvTitle, tvDesc, tvCount, tvDate;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializes data
        createData();

        // Handle detail views
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDesc = findViewById(R.id.tv_detail_description);
        tvCount = findViewById(R.id.tv_detail_count);
        tvDate = findViewById(R.id.tv_detail_date);

        // Handles device orientation
        setupSelectionView();
    }

    private void createData() {
        mBookList = new ArrayList<>();
        // Add books objects here, uses loop to ensure 10 unique books objects instead of create 10 manual entries
        for(int i = 1; i <= 10; i++) {
            mBookList.add(new Book("Book " + i, "Description for book " + i, i * 10, new Date()));
        }
    }

    private void setupSelectionView() {
        // Adds views to the spinner
        Spinner spinner = findViewById(R.id.spinner_selection);
        ListView listView = findViewById(R.id.list_selection);

        if (spinner != null) {
            // Portrait Mode logic
            ArrayAdapter<Book> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, mBookList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> _parent, View _view, int _pos, long _id) {
                    updateDetails(mBookList.get(_pos));
                }
                @Override
                public void onNothingSelected(AdapterView<?> _parent) {}
            });
        }

        if (listView != null) {
            // Landscape Mode logic
            BookAdapter adapter = new BookAdapter(this, mBookList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((_parent, _view, _pos, _id) ->
                    updateDetails(mBookList.get(_pos)));
        }
    }

    // Single helper method to update the book details
    private void updateDetails(Book _book) {
        if (_book != null) {
            tvTitle.setText(getString(R.string.label_title, _book.getTitle()));
            tvDesc.setText(getString(R.string.label_description, _book.getDescription()));
            tvCount.setText(getString(R.string.label_issues, _book.getIssueCount()));

            // Format the Date to a readable String (e.g., 04/15/2026)
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String dateString = dateFormat.format(_book.getPublishDate());

            tvDate.setText(getString(R.string.label_date, dateString));
        }
    }
}