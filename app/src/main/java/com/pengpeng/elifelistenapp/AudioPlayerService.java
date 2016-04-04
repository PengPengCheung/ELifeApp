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

/**
 * 传给activity的数据：音频状态status
 */
public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private NetworkAudioPlayer mPlayer;
    private AudioPlayerReceiver mReceiver;
    private int status = Resource.PlayerStatus.STOP;
    private boolean sourceChanged = false;
    private int progress = 0;
    private SeekBarUpdateReceiver mSeekBarReceiver;

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

    public class UpdateSeekBarThread extends Thread {
        @Override
        public void run() {
            Intent intent  = new Intent(Resource.Filter.PLAYER_ACTIVITY_SEEKBAR);
            while (mPlayer != null) {
                try {
                    Thread.sleep(30);
                    if(status == Resource.PlayerStatus.STOP){
                        progress = 0;
                    }else if(status == Resource.PlayerStatus.PLAYING){
                        progress = mPlayer.getCurrentPosition();
                    }

                    intent.putExtra(Resource.PlayerStatus.GET_PROGRESS_KEY, progress);
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch(Exception e){
                    break;
                }
            }
        }
    }


    private void init() {
        if (mPlayer != null) {
            Log.e("Service", "------------------------------------------");
            mPlayer = null;
        }
        mPlayer = NetworkAudioPlayer.getInstance(getApplicationContext());
        Log.i(Resource.Debug.TAG, "Service init");
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mReceiver = new AudioPlayerReceiver();
        IntentFilter filter = new IntentFilter(Resource.Filter.PLAYER_SERVICE);
        registerReceiver(mReceiver, filter);

        mSeekBarReceiver = new SeekBarUpdateReceiver();
        IntentFilter seekBarFilter = new IntentFilter(Resource.Filter.PLAYER_SERVICE_SEEKBAR);
        registerReceiver(mSeekBarReceiver, seekBarFilter);

        UpdateSeekBarThread thread = new UpdateSeekBarThread();
        thread.start();

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
            mPlayer.setFlag(false);
            mPlayer.release();//在release之后再调用reset方法会出错！！！暂未有解决办法
            mPlayer = null;
        }
        unregisterReceiver(mReceiver);
        unregisterReceiver(mSeekBarReceiver);
        Log.i(Resource.Debug.TAG, "Service Destroy");
        super.onDestroy();
    }

    class SeekBarUpdateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                int duration = intent.getIntExtra(Resource.ParamsKey.AUDIO_DURATION, -1);
                int seekBarMax = intent.getIntExtra(Resource.ParamsKey.SEEKBAR_MAX, -1);
                int seekBarProgress = intent.getIntExtra(Resource.ParamsKey.SEEKBAR_PROGRESS, -1);
                mPlayer.seekTo((int) (duration * (float) seekBarProgress / seekBarMax));
            }
        }
    }

    class AudioPlayerReceiver extends BroadcastReceiver {
        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int filterValue = intent.getIntExtra(Resource.Filter.FILTER_SIGNAL, -1);
                switch (filterValue) {
                    case Resource.Filter.MAIN_ACTIVITY_VALUE:
                        handleMainIntent(intent);
                        break;
                    case Resource.Filter.PLAYER_ACTIVITY_VALUE:
                        handlePlayerIntent(intent);
                        break;
                }
            }

        }

        private void handlePlayerIntent(Intent intent) {
            int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
            String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
            if(audioUrl!=null){
                changeSource(audioUrl);
            }else{
                switch(control){
                    case Resource.PlayerStatus.CONTROL_PLAY_BTN:
                        if(status == Resource.PlayerStatus.PLAYING){
                            play2Pause();
                        }else if(status == Resource.PlayerStatus.PAUSE){
                            pause2Play();
                        }else if(status == Resource.PlayerStatus.STOP){
                            stop2Play();
                        }
                        break;
                    case Resource.PlayerStatus.CONTROL_LOOP_BTN:
                        mPlayer.setLooping(true);
                        break;
                }
            }
            Intent sendToPlayer = new Intent(Resource.Filter.PLAYER_ACTIVITY);
            sendToPlayer.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
            sendBroadcast(sendToPlayer);

            Intent sendToMain = new Intent(Resource.Filter.MAIN_ACTIVITY);
            sendToMain.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
            sendBroadcast(sendToMain);
        }


        /**
         * MainActivity广播发送：control  audioUrl
         * @param intent
         */
        private void handleMainIntent(Intent intent) {
            int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
            String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
            if(audioUrl!=null){
                changeSource(audioUrl);
            }else {
                switch(control){
                    case Resource.PlayerStatus.CONTROL_PLAY_BTN:
                        if(status == Resource.PlayerStatus.PLAYING){
                            play2Pause();
                        }else if(status == Resource.PlayerStatus.PAUSE){
                            pause2Play();
                        }else if(status == Resource.PlayerStatus.STOP){
                            stop2Play();
                        }
                        break;
                    case Resource.PlayerStatus.CONTROL_STOP_BTN:
                        if(status == Resource.PlayerStatus.PLAYING){
                            play2Stop();
                        }else if(status == Resource.PlayerStatus.PAUSE){
                            pause2Stop();
                        }
                        break;
                }
            }

            Intent sendToAct = new Intent(Resource.Filter.MAIN_ACTIVITY);
            sendToAct.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
            sendBroadcast(sendToAct);

            Intent sendToPlayer = new Intent(Resource.Filter.PLAYER_ACTIVITY);
            sendToPlayer.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
            sendBroadcast(sendToPlayer);
        }

        private void changeSource(String url) {
            if(status != Resource.PlayerStatus.STOP){  //status默认状态为stop，为避免一开始没有任何播放源的时候出错，所以在这里做判断，以免出现不合法的状态
                mPlayer.stop();
            }
            if(mPlayer.getSourceUrl() == null){
                mPlayer.setSourceUrl(url);
                sourceChanged = true;
            }
            else if(!mPlayer.getSourceUrl().equals(url)) {
                mPlayer.setSourceUrl(url);
                sourceChanged = true;
            }
            stop2Play();
        }

        private void play2Stop() {
            mPlayer.stop();
            mPlayer.setPaused(false);
            status = Resource.PlayerStatus.STOP;
        }

        private void stop2Play() {
            mPlayer.setPaused(false);
            mPlayer.play();
            status = Resource.PlayerStatus.PLAYING;
        }

        private void play2Pause() {
            mPlayer.playPause();
            mPlayer.setPaused(true);
            status = Resource.PlayerStatus.PAUSE;
        }

        private void pause2Play() {
            mPlayer.setPaused(true);
            mPlayer.play();
            mPlayer.setPaused(false);
            status = Resource.PlayerStatus.PLAYING;
        }

        private void pause2Stop() {
            mPlayer.stop();
            mPlayer.setPaused(false);
            status = Resource.PlayerStatus.STOP;
        }

//        private void handlePlayerIntent2(Intent intent) {
//            int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
//            String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
//            if (audioUrl != null) {
//                mPlayer.setSourceUrl(audioUrl);
//                sourceChanged = true;
//            }
//            switch (control) {
//                case Resource.PlayerStatus.CONTROL_PLAY_BTN:
//                    if (status == Resource.PlayerStatus.PLAYING) {
//                        if (sourceChanged) {
//                            mPlayer.play();
//                            status = Resource.PlayerStatus.PLAYING;
//                            sourceChanged = false;
//                        } else {
//                            mPlayer.playPause();
//                            status = Resource.PlayerStatus.PAUSE;
//                        }
//                    } else {
//                        if (sourceChanged) {
//                            mPlayer.setPaused(false);//因为换音频材料，所以paused状态为false
//                        }
//                        mPlayer.play();
//                        status = Resource.PlayerStatus.PLAYING;
//                        sourceChanged = false;
//                    }
//                    break;
//                case Resource.PlayerStatus.CONTROL_NEXT_BTN:
//                    handleSourceChanged();
//                    break;
//                case Resource.PlayerStatus.CONTROL_PREVIOUS_BTN:
//                    handleSourceChanged();
//                    break;
//                case Resource.PlayerStatus.CONTROL_LOOP_BTN:
//                    mPlayer.setLooping(true);
//                    break;
//            }
//
//            Log.i("AudioPlayerService", "handlePlayerIntent");
//
//            Intent sendToPlayer = new Intent(Resource.Filter.PLAYER_ACTIVITY);
//            sendToPlayer.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
////            sendToPlayer.putExtra(Resource.PlayerStatus.GET_PROGRESS_KEY, mPlayer.getCurrentPosition());
//            sendBroadcast(sendToPlayer);
//
//            Intent sendToMain = new Intent(Resource.Filter.MAIN_ACTIVITY);
//            sendToMain.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
//            sendBroadcast(sendToMain);
//
//        }

//        private void handleSourceChanged() {
//            if (status == Resource.PlayerStatus.PLAYING) {
//                if (sourceChanged) {
//                    mPlayer.stop();
//                    mPlayer.play();
//                    status = Resource.PlayerStatus.PLAYING;
//                    sourceChanged = false;
//                }
//            } else {
//                if (sourceChanged) {
//                    mPlayer.setPaused(false);//因为换音频材料，所以paused状态为false
//                }
//                mPlayer.stop();
//                mPlayer.play();
//                status = Resource.PlayerStatus.PLAYING;
//                sourceChanged = false;
//            }
//        }
//
//        private void handleMainIntent2(Intent intent) {
//            String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
//            int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
//            if (audioUrl != null) {
//                Log.i(Resource.Debug.TAG, audioUrl);
//                mPlayer.setSourceUrl(audioUrl);
//                sourceChanged = true;
//            }
//            if (mPlayer.getSourceUrl() != null) {
//                Log.i("PlayerURL", mPlayer.getSourceUrl());
//                switch (control) {
//                    case Resource.PlayerStatus.CONTROL_PLAY_BTN:
//
//                        if (status == Resource.PlayerStatus.PLAYING) {
//                            Log.i("Player", "1");
//                            if (sourceChanged) {
//                                Log.i("Player", "2");
//                                mPlayer.play();
//                                Log.i("Player", "3");
//                                status = Resource.PlayerStatus.PLAYING;
//                                sourceChanged = false;
//                            } else {
//                                Log.i("Player", "4");
//                                mPlayer.playPause();
//                                Log.i("Player", "5");
//                                status = Resource.PlayerStatus.PAUSE;
//                            }
//                        } else {
//                            Log.i("Player", "6");
//                            Log.i("Player", "" + mPlayer.isPaused());
//                            if (sourceChanged) {
//                                mPlayer.setPaused(false);//因为换音频材料，所以paused状态为false
//                            }
//                            Log.i("Player", "" + mPlayer.isPaused() + " " + sourceChanged);
//                            mPlayer.play();
//                            Log.i("Player", "7");
//                            status = Resource.PlayerStatus.PLAYING;
//                            sourceChanged = false;
//                        }
//                        Log.i(Resource.Debug.TAG, "CONTROL_PLAY_BTN " + status);
//                        break;
//                    case Resource.PlayerStatus.CONTROL_STOP_BTN:
//                        if (status == Resource.PlayerStatus.PLAYING || status == Resource.PlayerStatus.PAUSE) {
//                            mPlayer.stop();
//                            status = Resource.PlayerStatus.STOP;
//                        }
//                        Log.i(Resource.Debug.TAG, "CONTROL_STOP_BTN " + status);
//                        break;
//                }
//                sourceChanged = false;
//            }
//
//            Log.i(Resource.Debug.TAG, "Service send " + status);
//            Intent sendToAct = new Intent(Resource.Filter.MAIN_ACTIVITY);
//            sendToAct.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
//            sendBroadcast(sendToAct);
//        }

    }
}
