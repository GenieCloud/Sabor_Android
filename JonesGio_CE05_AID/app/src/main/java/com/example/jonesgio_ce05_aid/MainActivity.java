// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce05_aid;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // A single, mutable array for storing all custom subclass objects to a list
    private final ArrayList<Person> mDirectory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner categorySpinner = findViewById(R.id.spinner_categories);
        Button refreshButton = findViewById(R.id.btn_refresh);

        if (categorySpinner != null) {
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    displayFormFragment(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        if (refreshButton != null) {
            refreshButton.setOnClickListener(v -> {
                if (categorySpinner != null) {
                    // Resets the form back to its default view
                    displayFormFragment(categorySpinner.getSelectedItemPosition());

                    // Updates the list UI to reload its items explicitly on demand
                    updateListUI();
                }
            });
        }
    }

    private void displayFormFragment(int position) {
        Fragment selectedFormFragment;
        switch (position) {
            case 0:
                selectedFormFragment = new StudentFormFragment();
                break;
            case 1:
                selectedFormFragment = new TeacherFormFragment();
                break;
            case 2:
                selectedFormFragment = new AdminFormFragment();
                break;
            default:
                return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_form, selectedFormFragment)
                .commit();
    }

    /**
     * Calls to form fragments when the submit button is pressed.
     */
    public void addPersonToDirectory(Person person) {
        if (person != null) {
            mDirectory.add(person);
        }
    }

    /**
     * Finds the active ListFragment and explicitly pushes the new directory data
     */
    private void updateListUI() {
        // Finds the attached list fragment by its layout ID container path
        Fragment listFrag = getSupportFragmentManager().findFragmentById(R.id.container_list);

        // Verifies that the found fragment matches our custom class BEFORE casting
        if (listFrag instanceof MyListFragment) {
            ((MyListFragment) listFrag).refreshAdapterData();
        }
    }

    /**
     * Getter method allowing the ListFragment to read from the core data source.
     */
    public ArrayList<Person> getDirectory() {
        return mDirectory;
    }
}