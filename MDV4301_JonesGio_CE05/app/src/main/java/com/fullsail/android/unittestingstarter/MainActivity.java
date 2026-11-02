package com.fullsail.android.unittestingstarter;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fullsail.android.unittestingstarter.fragment.PersonListFragment;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_FORM = 0x01001;
    private static final int REQUEST_PREF = 0x01002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            PersonListFragment fragment = PersonListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, PersonListFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, FormActivity.class);
            startActivityForResult(intent, REQUEST_FORM);
        } else if(item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_PREF);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        PersonListFragment fragment = (PersonListFragment)
                getSupportFragmentManager().findFragmentByTag(PersonListFragment.TAG);

        if(fragment != null) {
            fragment.refresh();
        }
    }
}
