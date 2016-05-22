package com.pengpeng.elifelistenapp.common;

/**
 * Created by pengpeng on 16-3-28.
 */
public interface IAudioPlayer {

    public boolean isPaused();

    public void playPause();

    public void playPrevious();

    public void playNext();

    public void playPrepared();

    public void play();
}