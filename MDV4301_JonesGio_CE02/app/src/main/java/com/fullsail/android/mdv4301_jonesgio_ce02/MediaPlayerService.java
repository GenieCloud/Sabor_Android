// Gio Jones
// MDV4301-O C202607
// MediaPlayerService.java

package com.fullsail.android.mdv4301_jonesgio_ce02;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer = null;
    private static final String CHANNEL_ID = "media_playback_channel";

    private static final String ACTION_TRACK_CHANGED = "com.fullsail.android.mdv4301_jonesgio_ce02.TRACK_CHANGED";
    private static final int NOTIFICATION_ID = 1;

    private final IBinder binder = new LocalBinder();

    private final List<Integer> playlist = Arrays.asList(
            R.raw.something_elated,
            R.raw.song_one,
            R.raw.song_two
    );

    private final String[] songTitles = {"Something Elated", "Song One", "Song Two"};
    private int currentTrackIndex = 0;

    private boolean isShuffleOn = false;
    private boolean isLoopingOn = false;

    public class LocalBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            switch (action) {
                case "ACTION_PLAY":
                    playAudio();
                    break;
                case "ACTION_PAUSE":
                    pauseAudio();
                    break;
                case "ACTION_NEXT":
                    playNextTrack();
                    break;
                case "ACTION_PREVIOUS":
                    playPreviousTrack();
                    break;
                case "ACTION_TOGGLE_SHUFFLE":
                    toggleShuffle();
                    break;
                case "ACTION_TOGGLE_LOOP":
                    toggleLoop();
                    break;
                case "ACTION_SEEK_TO":
                    int position = intent.getIntExtra("EXTRA_SEEK_POSITION", 0);
                    seekToPosition(position);
                    break;
            }
        }
        return START_STICKY;
    }

    public void playAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, playlist.get(currentTrackIndex));
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setLooping(isLoopingOn);
            mediaPlayer.start();
        } else {
            mediaPlayer.start();
        }


        Intent updateIntent = new Intent(ACTION_TRACK_CHANGED);
        updateIntent.putExtra("EXTRA_TRACK_INDEX", currentTrackIndex);
        updateIntent.setPackage(getPackageName());
        sendBroadcast(updateIntent);

        startForegroundNotification();
    }

    public void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            startForegroundNotification();
        }
    }

    public void playNextTrack() {
        stopAudio();
        if (isShuffleOn) {
            Random random = new Random();
            currentTrackIndex = random.nextInt(playlist.size());
        } else {
            currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
        }
        playAudio();
    }

    public void playPreviousTrack() {
        stopAudio();
        if (currentTrackIndex - 1 < 0) {
            currentTrackIndex = playlist.size() - 1;
        } else {
            currentTrackIndex--;
        }
        playAudio();
    }

    public void toggleShuffle() {
        isShuffleOn = !isShuffleOn;
    }

    public void toggleLoop() {
        isLoopingOn = !isLoopingOn;
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(isLoopingOn);
        }
    }

    public void seekToPosition(int milliSec) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(milliSec);
        }
    }

    public int getCurrentTrackIndex() {
        return currentTrackIndex;
    }

    public int getCurrentPosition() {
        return (mediaPlayer != null) ? mediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return (mediaPlayer != null) ? mediaPlayer.getDuration() : 0;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void startForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Media Playback", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }


        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent playIntent = PendingIntent.getService(this, 0, new Intent(this, MediaPlayerService.class).setAction("ACTION_PLAY"), flags);
        PendingIntent pauseIntent = PendingIntent.getService(this, 1, new Intent(this, MediaPlayerService.class).setAction("ACTION_PAUSE"), flags);
        PendingIntent nextIntent = PendingIntent.getService(this, 2, new Intent(this, MediaPlayerService.class).setAction("ACTION_NEXT"), flags);
        PendingIntent prevIntent = PendingIntent.getService(this, 3, new Intent(this, MediaPlayerService.class).setAction("ACTION_PREVIOUS"), flags);

        boolean isPlaying = (mediaPlayer != null && mediaPlayer.isPlaying());
        NotificationCompat.Action playPauseAction = isPlaying ?
                new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pauseIntent) :
                new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", playIntent);

        String trackTitle = (currentTrackIndex >= 0 && currentTrackIndex < songTitles.length) ?
                songTitles[currentTrackIndex] : "Unknown Track";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(trackTitle)
                .setContentText("Media Player Service")
                .addAction(android.R.drawable.ic_media_previous, "Previous", prevIntent)
                .addAction(playPauseAction)
                .addAction(android.R.drawable.ic_media_next, "Next", nextIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2))
                .setPriority(NotificationCompat.PRIORITY_LOW);

        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isLoopingOn) {
            playNextTrack();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAudio();
    }
}