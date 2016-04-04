package com.pengpeng.elifelistenapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.pengpeng.elifelistenapp.utils.Tools;

import java.io.IOException;

/**
 * Created by pengpeng on 16-3-28.
 */
public class NetworkAudioPlayer extends MediaPlayer implements IAudioPlayer {

    private Context context;
    private boolean paused = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private boolean flag = false;  //mediaplayer没有被最终销毁

    public boolean isStopFlag() {
        return stopFlag;
    }

    private void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    private boolean stopFlag = false;

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    private boolean prepared = false;
    String urlString1= "http://sc1.111ttt.com/2015/1/12/08/105081507558.mp3";
    String urlString2 = "http://sc1.111ttt.com/2015/1/12/08/105082233058.mp3";
    String urlString3 = "http://sc1.111ttt.com/2015/1/12/09/105090933364.mp3";

    private String sourceUrl;

    private static NetworkAudioPlayer player = null;

    private NetworkAudioPlayer(Context context){
        this.context = context;
    }

    public static NetworkAudioPlayer getInstance(Context context){
        if(player == null){
            synchronized (NetworkAudioPlayer.class){
                if(player == null){
                    player = new NetworkAudioPlayer(context);
                }
            }
        }
        return player;
    }

    public void setSourceUrl(String url){
        this.sourceUrl = url;
    }



    public String getSourceUrl(){
        return this.sourceUrl;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }


    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public void playPause() {
        if(isPlaying()){
            pause();
            paused = true;
        }
    }

    @Override
    public void playPrevious() {
        if(Tools.isNetworkAvailable(context)){
            playPrepared();
        }
    }

    @Override
    public void playNext() {
        if(Tools.isNetworkAvailable(context)){
            playPrepared();
        }
    }

    @Override
    public void playPrepared() {
        if(Tools.isNetworkAvailable(context)){
            try {
                reset();
                setDataSource(getSourceUrl());
                setAudioStreamType(AudioManager.STREAM_MUSIC);
                prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void play() {
        if (isPaused()) {
            start();
            paused = false;
        } else {
            playPrepared();
        }
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        setFlag(true);
    }

}

