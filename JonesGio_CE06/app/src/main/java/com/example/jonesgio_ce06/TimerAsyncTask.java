// Gio Jones
// GPL - C202604
// TimerAsyncTask.java

package com.example.jonesgio_ce06;

import android.os.AsyncTask;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.lang.ref.WeakReference;

public class TimerAsyncTask extends AsyncTask<Long, Long, Void> {

    private final WeakReference<MainActivity> activityRef;
    private final long totalTimeMillis;
    private long startTime;
    private long timeElapsed;

    public TimerAsyncTask(MainActivity activity, long totalTimeMillis) {
        this.activityRef = new WeakReference<>(activity);
        this.totalTimeMillis = totalTimeMillis;
    }

    @Override
    protected void onPreExecute() {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            // Toast saying the timer has started
            Toast.makeText(activity, R.string.timer_started, Toast.LENGTH_SHORT).show();
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    protected Void doInBackground(Long... params) {
        // System.currentTimeMillis() for accuracy
        while (System.currentTimeMillis() - startTime < totalTimeMillis) {
            if (isCancelled()) break;

            timeElapsed = System.currentTimeMillis() - startTime;
            long remainingTime = totalTimeMillis - timeElapsed;

            // Update UI with current amount of time remaining
            publishProgress(remainingTime);

            try {

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            activity.updateUI(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            //Update UI
            activity.updateUI(0);
            // AlertDialog saying timer expired
            new AlertDialog.Builder(activity)
                    .setTitle("Timer")
                    .setMessage(R.string.timer_expired)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    protected void onCancelled() {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            // AlertDialog showing elapsed time
            long secondsElapsed = timeElapsed / 1000;
            String msg = activity.getString(R.string.timer_stopped_prefix) + " " + secondsElapsed + "s";

            new AlertDialog.Builder(activity)
                    .setTitle("Timer Stopped")
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}