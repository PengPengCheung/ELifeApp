package com.pengpeng.elifelistenapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.pengpeng.elifelistenapp.utils.Tools;

import java.io.IOException;

/**
 * Created by pengpeng on 16-3-28.
 */
public class NetworkAudioPlayer extends MediaPlayer implements IAudioPlayer {

    private Context context;
    private boolean paused = false;

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    private boolean prepared = false;
    String urlString1= "http://sc1.111ttt.com/2015/1/12/08/105081507558.mp3";
    String urlString2 = "http://sc1.111ttt.com/2015/1/12/08/105082233058.mp3";
    //    String urlString3 = "http://music.baidutt.com/up/kwcackwp/kuuwa.mp3";
    String urlString3 = "http://sc1.111ttt.com/2015/1/12/09/105090933364.mp3";
    String url = "http://192.168.235.8:8000/test";

    private String sourceUrl;

    private static NetworkAudioPlayer player = null;

    private NetworkAudioPlayer(Context context){
        this.context = context;
//        action = new CoreActionImpl();
//        uaUserAudioBehavior = new UserAudioBehavior();
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



    public String getSourceUrl() throws IOException {
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
                Log.e("playPrepared", "1");
                if(player!=null){
                    Log.e("playPrepared","not null");
                }
                reset();
                Log.e("playPrepared", "2");
                setDataSource(getSourceUrl());
                Log.e("playPrepared", "3");
                setAudioStreamType(AudioManager.STREAM_MUSIC);
                Log.e("playPrepared", "4");
                prepareAsync();
                Log.e("playPrepared", "5");
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
}

