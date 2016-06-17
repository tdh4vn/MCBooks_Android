package vn.mcbooks.mcbooks.utils;

import com.google.android.exoplayer.ExoPlayer;

import android.media.MediaPlayer;
import android.widget.MediaController.MediaPlayerControl;
/**
 * Created by hungtran on 6/15/16.
 */
public class PlayerControl implements MediaPlayerControl {
    private final MediaPlayer mediaPlayer;

    public PlayerControl(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        int seekPosition = mediaPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : Math.min(Math.max(0, pos), getDuration());
        mediaPlayer.seekTo(seekPosition);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return (mediaPlayer.getCurrentPosition()*100)/mediaPlayer.getDuration();
    }


    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        throw new UnsupportedOperationException();
    }
}
