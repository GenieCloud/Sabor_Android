// Gio Jones
// MDV4301-O C202607
// MainActivity.java

package com.fullsail.android.mdv4301_jonesgio_ce02;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivAlbumArt;
    private TextView tvTrackTitle;
    private SeekBar seekbarPlayback;
    private ImageButton btnPrevious, btnPlay, btnPause, btnNext;
    private ToggleButton toggleShuffle, toggleLoop;

    private TrackChangeReceiver receiver;

    private MediaPlayerService mediaService;
    private boolean isBound = false;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable;

    public static final String ACTION_TRACK_CHANGED = "com.fullsail.android.mdv4301_jonesgio_ce02.TRACK_CHANGED";

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaService = binder.getService();
            isBound = true;

            // Syncs UI text instantly with the track index the service is currently on upon binding
            updateUIForTrack(mediaService.getCurrentTrackIndex());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAlbumArt = findViewById(R.id.iv_album_art);
        tvTrackTitle = findViewById(R.id.tv_track_title);
        seekbarPlayback = findViewById(R.id.seekbar_playback);

        btnPrevious = findViewById(R.id.btn_previous);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnNext = findViewById(R.id.btn_next);

        toggleShuffle = findViewById(R.id.toggle_shuffle);
        toggleLoop = findViewById(R.id.toggle_loop);

        btnPrevious.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        toggleShuffle.setOnClickListener(this);
        toggleLoop.setOnClickListener(this);

        seekbarPlayback.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isBound) {
                    mediaService.seekToPosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressHandler.removeCallbacks(progressRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressHandler.post(progressRunnable);
            }
        });

        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (isBound && mediaService.isPlaying()) {
                    int currentPosition = mediaService.getCurrentPosition();
                    int duration = mediaService.getDuration();

                    seekbarPlayback.setMax(duration);
                    seekbarPlayback.setProgress(currentPosition);
                }
                progressHandler.postDelayed(this, 1000);
            }
        };
    }

    private void updateUIForTrack(int trackIndex) {
        if (trackIndex == 0) {
            tvTrackTitle.setText("Something Elated");
            ivAlbumArt.setImageResource(android.R.drawable.ic_media_play);
        } else if (trackIndex == 1) {
            tvTrackTitle.setText("Song One");
            ivAlbumArt.setImageResource(android.R.drawable.ic_dialog_map);
        } else if (trackIndex == 2) {
            tvTrackTitle.setText("Song Two");
            ivAlbumArt.setImageResource(android.R.drawable.ic_dialog_info);
        } else {
            tvTrackTitle.setText("Unknown Track");
            ivAlbumArt.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new TrackChangeReceiver();

        IntentFilter filter = new IntentFilter(ACTION_TRACK_CHANGED);

        ContextCompat.registerReceiver(this, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);

        Intent intent = new Intent(this, MediaPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        progressHandler.post(progressRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        progressHandler.removeCallbacks(progressRunnable);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MediaPlayerService.class);

        if (v.getId() == R.id.btn_play) {
            intent.setAction("ACTION_PLAY");
        } else if (v.getId() == R.id.btn_pause) {
            intent.setAction("ACTION_PAUSE");
        } else if (v.getId() == R.id.btn_next) {
            intent.setAction("ACTION_NEXT");
        } else if (v.getId() == R.id.btn_previous) {
            intent.setAction("ACTION_PREVIOUS");
        } else if (v.getId() == R.id.toggle_shuffle) {
            intent.setAction("ACTION_TOGGLE_SHUFFLE");
        } else if (v.getId() == R.id.toggle_loop) {
            intent.setAction("ACTION_TOGGLE_LOOP");
        }

        if (intent.getAction() != null) {
            startService(intent);
        }
    }

    private class TrackChangeReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) return;

            if (ACTION_TRACK_CHANGED.equals(intent.getAction())) {
                int trackIndex = intent.getIntExtra("EXTRA_TRACK_INDEX", -1);
                if (trackIndex != -1) {
                    updateUIForTrack(trackIndex);
                }
            }
        }
    }
}