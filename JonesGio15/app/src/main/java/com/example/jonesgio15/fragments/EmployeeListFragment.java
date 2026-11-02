// Gio Jones
// AD2 - C202606
// EmployeeListFragment.java

package com.example.jonesgio15.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import com.example.jonesgio15.FormActivity;
import com.example.jonesgio15.R;
import com.example.jonesgio15.SettingsActivity;
import com.example.jonesgio15.database.EmployeeDatabaseHelper;

public class EmployeeListFragment extends Fragment {

    private Spinner spinnerSortColumn;
    private Spinner spinnerSortDirection;
    private ListView listViewEmployees;
    private SimpleCursorAdapter listAdapter;

    public EmployeeListFragment() {

    }

    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);

        // Binds UI layout controls
        spinnerSortColumn = view.findViewById(R.id.spinner_sort_column);
        spinnerSortDirection = view.findViewById(R.id.spinner_sort_direction);
        listViewEmployees = view.findViewById(R.id.listview_employees);

        setupSpinners();
        setupListView();

        // Listener array to trigger dynamic sorting queries instantly on dropdown adjustments
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshEmployeeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerSortColumn.setOnItemSelectedListener(spinnerListener);
        spinnerSortDirection.setOnItemSelectedListener(spinnerListener);

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> columnAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_columns_array,
                android.R.layout.simple_spinner_item
        );
        columnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortColumn.setAdapter(columnAdapter);

        ArrayAdapter<CharSequence> directionAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_direction_array,
                android.R.layout.simple_spinner_item
        );
        directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortDirection.setAdapter(directionAdapter);
    }

    private void setupListView() {
        // Passes the clicked item's exact SQLite row database ID to FormActivity
        listViewEmployees.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), FormActivity.class);
            intent.putExtra(FormActivity.EXTRA_EMPLOYEE_ID, id);
            startActivity(intent);
        });
    }

    /**
     * Reads database records dynamically adhering to active dropdown sort parameters
     */
    private void refreshEmployeeList() {
        EmployeeDatabaseHelper dbHelper = new EmployeeDatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Parses spinners to determine target table column and direction modifier
        String selectedColumn = spinnerSortColumn.getSelectedItemPosition() == 0 ?
                EmployeeDatabaseHelper.COLUMN_EMP_NUMBER : EmployeeDatabaseHelper.COLUMN_STATUS;

        String selectedDirection = spinnerSortDirection.getSelectedItemPosition() == 0 ? "ASC" : "DESC";
        String orderByClause = selectedColumn + " " + selectedDirection;

        // Runs query operation against SQLite
        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_EMPLOYEES,
                null, null, null, null, null, orderByClause
        );

        // Maps column data safely into native simple list layout row lines
        String[] fromColumns = {
                EmployeeDatabaseHelper.COLUMN_FIRST_NAME,
                EmployeeDatabaseHelper.COLUMN_EMP_NUMBER
        };
        int[] toViews = { android.R.id.text1, android.R.id.text2 };

        if (listAdapter == null) {
            listAdapter = new SimpleCursorAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_2,
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );
            listViewEmployees.setAdapter(listAdapter);
        } else {
            listAdapter.swapCursor(cursor); // Hot-swaps the cursor background gracefully
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Developer Tip implementation: Ensures data refreshes clean whenever returning to this view screen
        refreshEmployeeList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(getActivity(), FormActivity.class);
            intent.putExtra(FormActivity.EXTRA_EMPLOYEE_ID, -1L);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}