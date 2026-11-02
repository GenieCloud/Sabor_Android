// Gio Jones
// GPL - C20260490
// MainActivity.java

package com.example.jonesgio_ce06;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // View member references
    private EditText editMin;
    private EditText editSec;
    private TimerAsyncTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initializes views by ID
        editMin = findViewById(R.id.edit_minutes);
        editSec = findViewById(R.id.edit_seconds);
        Button btnStart = findViewById(R.id.button_start);
        Button btnStop = findViewById(R.id.button_stop);

        // Set onClick listeners
        btnStart.setOnClickListener(v -> startTimer());
        btnStop.setOnClickListener(v -> stopTimer());
    }

    private void startTimer() {
        String minStr = editMin.getText().toString();
        String secStr = editSec.getText().toString();

        // Check if empty or timer is at 00:00
        if ((minStr.isEmpty() && secStr.isEmpty())) {
            Toast.makeText(this, R.string.error_invalid_time, Toast.LENGTH_SHORT).show();
            return;
        }

        long minutes = minStr.isEmpty() ? 0 : Long.parseLong(minStr);
        long seconds = secStr.isEmpty() ? 0 : Long.parseLong(secStr);
        long totalMillis = (minutes * 60 + seconds) * 1000;

        if (totalMillis <= 0) {
            Toast.makeText(this, R.string.error_invalid_time, Toast.LENGTH_SHORT).show();
        } else {
            // Start AsyncTask with total time passed as a parameter
            timerTask = new TimerAsyncTask(this, totalMillis);
            timerTask.execute(totalMillis);
        }
    }

    private void stopTimer() {
        // If AsyncTask is running, stop the timer
        if (timerTask != null && timerTask.getStatus() == AsyncTask.Status.RUNNING) {
            timerTask.cancel(true);
        }

        // Set text fields to "00"
        editMin.setText(R.string.hint_00);
        editSec.setText(R.string.hint_00);
    }

    /**
     * Update the UI to show remaining time.
     * This is called from the AsyncTask's onProgressUpdate.
     */
    public void updateUI(long remainingMillis) {
        long minutes = (remainingMillis / 1000) / 60;
        long seconds = (remainingMillis / 1000) % 60;

        editMin.setText(String.format("%02d", minutes));
        editSec.setText(String.format("%02d", seconds));
    }
}