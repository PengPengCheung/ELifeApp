package com.pengpeng.elifelistenapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.pengpeng.elifelistenapp.utils.Resource;

import java.io.IOException;

/**
 * 传给activity的数据：音频状态status
 */
public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private NetworkAudioPlayer mPlayer;
    private AudioPlayerReceiver mReceiver;
    private int status = Resource.PlayerStatus.STOP;
    private boolean sourceChanged = false;
    private int progress = 0;

    public AudioPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        mPlayer = NetworkAudioPlayer.getInstance(getApplicationContext());
        Log.i(Resource.Debug.TAG, "Service init");
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mReceiver = new AudioPlayerReceiver();
        IntentFilter filter = new IntentFilter(Resource.Filter.PLAYER_SERVICE);
        registerReceiver(mReceiver, filter);
    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        mPlayer.setPaused(false);
        status = Resource.PlayerStatus.PLAYING;
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayer.play();
        status = Resource.PlayerStatus.STOP;
        Intent intent = new Intent(Resource.Filter.MAIN_ACTIVITY);
        intent.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
        sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        if (mPlayer != null) {
            Log.i(Resource.Debug.TAG, "before release");
            mPlayer.stop();
            mPlayer.release();//在release之后再调用reset方法会出错！！！暂未有解决办法
            mPlayer = null;
        }
        Log.i(Resource.Debug.TAG, "Service Destroy");
        super.onDestroy();
    }

    class AudioPlayerReceiver extends BroadcastReceiver {
        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
                int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
                if (audioUrl != null){
                    Log.i(Resource.Debug.TAG, audioUrl);
                    mPlayer.setSourceUrl(audioUrl);
                    sourceChanged = true;
                }
                try {
                    if(mPlayer.getSourceUrl()!=null){
                        Log.i("PlayerURL", mPlayer.getSourceUrl());
                        switch (control){
                            case Resource.PlayerStatus.CONTROL_PLAY_BTN:

                                if(status == Resource.PlayerStatus.PLAYING){
                                    Log.i("Player", "1");
                                    if(sourceChanged){
                                        Log.i("Player", "2");
                                        mPlayer.play();
                                        Log.i("Player", "3");
                                        status = Resource.PlayerStatus.PLAYING;
                                        sourceChanged = false;
                                    }else{
                                        Log.i("Player", "4");
                                        mPlayer.playPause();
                                        Log.i("Player", "5");
                                        status = Resource.PlayerStatus.PAUSE;
                                    }
                                }else{
                                    Log.i("Player", "6");
                                    Log.i("Player", ""+mPlayer.isPaused());
                                    if(sourceChanged){
                                        mPlayer.setPaused(false);//因为换音频材料，所以paused状态为false
                                    }
                                    Log.i("Player", ""+mPlayer.isPaused()+" "+sourceChanged);
                                    mPlayer.play();
                                    Log.i("Player", "7");
                                    status = Resource.PlayerStatus.PLAYING;
                                    sourceChanged = false;
                                }
                                Log.i(Resource.Debug.TAG, "CONTROL_PLAY_BTN "+ status);
                                break;
                            case Resource.PlayerStatus.CONTROL_STOP_BTN:
                                if(status == Resource.PlayerStatus.PLAYING || status == Resource.PlayerStatus.PAUSE){
                                    mPlayer.stop();
                                    status = Resource.PlayerStatus.STOP;
                                }
                                Log.i(Resource.Debug.TAG, "CONTROL_STOP_BTN "+ status);
                                break;
                            case Resource.PlayerStatus.CONTROL_GET_PROGRESS:
                                if(status == Resource.PlayerStatus.PLAYING){
                                    int currentTime = mPlayer.getCurrentPosition();
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i(Resource.Debug.TAG, "Service send "+status);
                Intent sendToAct = new Intent(Resource.Filter.MAIN_ACTIVITY);
                sendToAct.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
                sendBroadcast(sendToAct);


            }
        }
    }
}
