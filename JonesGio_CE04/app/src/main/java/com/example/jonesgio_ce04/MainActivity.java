// Gio Jones
// MDV3832-O-C202604
// MainActivity.java

package com.example.jonesgio_ce04;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Member variables for data and UI
    private ArrayList<Person> mPeople;
    private ListView mListView;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the collection of 10 people
        setupData();

        // Initialize UI components
        mListView = findViewById(R.id.listView);
        mGridView = findViewById(R.id.gridView);
        Spinner viewSpinner = findViewById(R.id.spinner_view_type);
        Spinner adapterSpinner = findViewById(R.id.spinner_adapter_type);

        // Listener for View Type Spinner (List vs Grid)
        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toggle visibility based on selection
                if (position == 0) {
                    // List View
                    mListView.setVisibility(View.VISIBLE);
                    mGridView.setVisibility(View.GONE);
                } else {
                    // Grid View
                    mListView.setVisibility(View.GONE);
                    mGridView.setVisibility(View.VISIBLE);
                }
                // Refresh the adapter for the newly shown view
                updateAdapter(adapterSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Listener for Adapter Type Spinner
        adapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAdapter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Item Click Listener for AlertDialog
        AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
            Person selected = mPeople.get(position);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(selected.getFirstName() + " " + selected.getLastName())
                    .setMessage("Birthday: " + selected.getBirthday())
                    .setIcon(selected.getPictureResId())
                    .setPositiveButton("OK", null)
                    .show();
        };

        mListView.setOnItemClickListener(itemClickListener);
        mGridView.setOnItemClickListener(itemClickListener);
    }

    private void updateAdapter(int adapterType) {
        if (adapterType == 0) {
            // Array Adapter: Show only full names
            ArrayAdapter<Person> arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, mPeople);
            mListView.setAdapter(arrayAdapter);
            mGridView.setAdapter(arrayAdapter);

        } else if (adapterType == 1) {
            // Simple Adapter: Name & Birthday
            ArrayList<Map<String, String>> data = new ArrayList<>();
            for (Person p : mPeople) {
                Map<String, String> map = new HashMap<>();
                map.put("name", p.getFirstName() + " " + p.getLastName());
                map.put("date", p.getBirthday());
                data.add(map);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
                    R.layout.simple_adapter_item,
                    new String[]{"name", "date"},
                    new int[]{R.id.text_name, R.id.text_birthday});

            mListView.setAdapter(simpleAdapter);
            mGridView.setAdapter(simpleAdapter);

        } else if (adapterType == 2) {
            // Base Adapter: Custom implementation with image and ViewHolder
            PersonAdapter personAdapter = new PersonAdapter(this, mPeople);
            mListView.setAdapter(personAdapter);
            mGridView.setAdapter(personAdapter);
        }
    }

    private void setupData() {
        mPeople = new ArrayList<>();
        mPeople.add(new Person("Sabiha", "Franco", "05/12/1992", R.drawable.person1));
        mPeople.add(new Person("John", "Carlos", "09/06/1954", R.drawable.person2));
        mPeople.add(new Person("Michael", "Jones", "07/04/1988", R.drawable.person3));
        mPeople.add(new Person("Carl", "Johnson", "04/22/1984", R.drawable.person4));
        mPeople.add(new Person("Catalina", "Vazquez", "10/31/1967", R.drawable.person5));
        mPeople.add(new Person("Gio", "Vittorio", "04/20/1990", R.drawable.person6));
        mPeople.add(new Person("Kadeem", "Hawkins", "03/14/1945", R.drawable.person7));
        mPeople.add(new Person("Philip", "Thomas", "12/10/1992", R.drawable.person8));
        mPeople.add(new Person("Alice", "Maddox", "01/19/1991", R.drawable.person9));
        mPeople.add(new Person("Jose", "Gonzalez", "10/28/1994", R.drawable.person10));
    }
}