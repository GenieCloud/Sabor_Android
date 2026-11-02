// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce06_aid;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        ListFragment.OnContactSelectedListener,
        FormFragment.OnContactSavedListener {

    // Database collection & global UI elements
    private ArrayList<Contact> mContactDatabase;
    private FloatingActionButton mFab;
    private static final String TAG_LIST = "LIST_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the data collection
        mContactDatabase = new ArrayList<>();
        mFab = findViewById(R.id.fab_add_contact);

        // Loads the initial ListFragment on first startup
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ListFragment.newInstance(), TAG_LIST)
                    .commit();
        }

        // Sets click listener to launch the Form Fragment
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        // Connected custom slide animations for entering and popping back
                        .setCustomAnimations(
                                R.anim.slide_in_right,  // enter animation for FormFragment
                                R.anim.slide_out_left,  // exit animation for ListFragment
                                R.anim.slide_in_left,   // popEnter animation when returning to list
                                R.anim.slide_out_right  // popExit animation when FormFragment closes
                        )
                        .replace(R.id.fragment_container, FormFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Hides FAB if a fragment other than the List Fragment is visible
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    mFab.hide();
                } else {
                    mFab.show();

                    refreshListFragment();
                }
            }
        });
    }

    /**
     * Interface callback executes when FormFragment successfully submits a valid contact.
     */
    @Override
    public void onContactSaved(Contact newContact) {
        if (newContact != null) {
            mContactDatabase.add(newContact);
        }
    }

    /**
     * Interface callback executes when an item in the ListFragment is clicked.
     */
    @Override
    public void onContactSelected(Contact contact) {
        if (contact == null) {
            return;
        }

        // Transitions over to the Details screen with matching slide animations
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,  // enter animation for DetailsFragment
                        R.anim.slide_out_left,  // exit animation for ListFragment
                        R.anim.slide_in_left,   // popEnter animation when returning to list
                        R.anim.slide_out_right  // popExit animation when DetailsFragment closes
                )
                .replace(R.id.fragment_container, DetailsFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    /**
     * Helper method to find the active ListFragment & push the updated data array to it.
     */
    private void refreshListFragment() {
        ListFragment listFrag = (ListFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIST);
        if (listFrag != null) {
            listFrag.updateListData(mContactDatabase);
        }
    }
}