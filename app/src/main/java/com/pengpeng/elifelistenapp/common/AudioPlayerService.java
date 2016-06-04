package com.pengpeng.elifelistenapp.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.utils.LogUtil;
import com.pengpeng.elifelistenapp.utils.Resource;

import java.util.ArrayList;


/**
 * 传给activity的数据：音频状态status
 */
public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static String TAG = "PlayerService";
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
            Intent intent = new Intent(Resource.Filter.PLAYER_ACTIVITY_SEEKBAR);
            while (mPlayer != null) {
                try {
                    Thread.sleep(30);
                    if (status == Resource.PlayerStatus.STOP) {
                        progress = 0;
                    } else if (status == Resource.PlayerStatus.PLAYING) {
                        progress = mPlayer.getCurrentPosition();
                    }
                    //progress通过广播从service传给PlayerActivity的onReceive()
                    intent.putExtra(Resource.PlayerStatus.GET_PROGRESS_KEY, progress);
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    private void getSavedAudio() {
        SharedPreferences sp = getSharedPreferences(Resource.ParamsKey.AUDIO_SP_KEY, MODE_PRIVATE);
        if (sp != null) {
            String title = sp.getString(Resource.ParamsKey.AUDIO_TITLE, "");
            String imageUrl = sp.getString(Resource.ParamsKey.AUDIO_IMAGE_URL, "");
            String audioUrl = sp.getString(Resource.ParamsKey.AUDIO_URL, "");
            String audioType = sp.getString(Resource.ParamsKey.AUDIO_TYPE, "");
            int audioTime = sp.getInt(Resource.ParamsKey.AUDIO_PARTENDTIME, -1);
            String audioId = sp.getString(Resource.ParamsKey.AUDIO_ID, "");
            LogUtil.e(TAG, title + ", " + imageUrl + ", " + audioUrl + ", " + audioType);
            if (!audioUrl.isEmpty() && !title.isEmpty() && !audioType.isEmpty()) {
                Audio audio = new Audio();
                audio.setAudioImageUrl(imageUrl);
                audio.setAudioUrl(audioUrl);
                audio.setAudioTitle(title);
                audio.setAudioType(audioType);
                audio.setAudioPartEndTime(new ArrayList<Integer>(3));
                audio.getAudioPartEndTime().add(0, 0);
                audio.getAudioPartEndTime().add(1, 0);
                audio.getAudioPartEndTime().add(2, audioTime);
                audio.setAudioId(audioId);
                AudioListManager.getInstance().setCurrentAudio(audio);
                mPlayer.setSourceUrl(audioUrl);
            }
        }
    }

    private void init() {
        if (mPlayer != null) {
            mPlayer = null;
        }
        mPlayer = NetworkAudioPlayer.getInstance(getApplicationContext());
        getSavedAudio();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mReceiver = new AudioPlayerReceiver();
        IntentFilter filter = new IntentFilter(Resource.Filter.PLAYER_SERVICE);
        registerReceiver(mReceiver, filter);//register第一个参数就相当于标签

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

        mPlayer.setPaused(false);

        if (filterValue == Resource.Filter.EXERCISE_ACTIVITY_VALUE) {
            exerciseSeekTo(mPart, splitEndTime);
        }
        mPlayer.start();
        status = Resource.PlayerStatus.PLAYING;
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        // 下面这段逻辑会导致切换音频时偶发性地出现播放状态按钮设为播放按钮的情况，刷新音频列表之后出现概率高
//        mPlayer.play();
//        status = Resource.PlayerStatus.STOP;
//        Intent intent = new Intent(Resource.Filter.MAIN_ACTIVITY);
//        intent.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
//        LogUtil.e(TAG, "ppp onCompletion, status is " + status);
//        sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        if (mPlayer != null) {
            Log.i(Resource.Debug.TAG, "before release");
            mPlayer.stop();
            mPlayer.unInit();
        }
        unregisterReceiver(mReceiver);
        unregisterReceiver(mSeekBarReceiver);
        Log.i(Resource.Debug.TAG, "Service Destroy");
        super.onDestroy();
    }

    class SeekBarUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int duration = intent.getIntExtra(Resource.ParamsKey.AUDIO_DURATION, -1);
                int seekBarMax = intent.getIntExtra(Resource.ParamsKey.SEEKBAR_MAX, -1);
                int seekBarProgress = intent.getIntExtra(Resource.ParamsKey.SEEKBAR_PROGRESS, -1);
                mPlayer.seekTo((int) (duration * (float) seekBarProgress / seekBarMax));
            }
        }
    }

    private int filterValue = -1;

    class AudioPlayerReceiver extends BroadcastReceiver {
        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                filterValue = intent.getIntExtra(Resource.Filter.FILTER_SIGNAL, -1);
                switch (filterValue) {
                    case Resource.Filter.MAIN_ACTIVITY_VALUE:
                        handleMainIntent(intent);
                        break;
                    case Resource.Filter.PLAYER_ACTIVITY_VALUE:
                        handlePlayerIntent(intent);
                        break;
                    case Resource.Filter.EXERCISE_ACTIVITY_VALUE:
                        handleExerciseIntent(intent);
                        break;
                }
            }

        }
    }

    class ExerciseThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (splitTime > 0) {
                if (status == Resource.PlayerStatus.PLAYING) {
                    if (mPlayer.getCurrentPosition() >= splitTime) {
                        play2Pause();
                        break;
                    }
                }
            }
        }
    }

    private int splitTime = 0;
    private int mPart = -1;
    int[] splitEndTime;

    private void handleExerciseIntent(Intent intent) {

        int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
        if (control != -1) {
            int part = intent.getIntExtra(Resource.Type.PART, -1);
            Thread thread = new ExerciseThread();
            switch (control) {
                case Resource.PlayerStatus.CONTROL_PLAY_BTN:
                    if (status == Resource.PlayerStatus.PLAYING) {
                        if (part != mPart) {
                            LogUtil.e(TAG, "peng part is " + part);
                            LogUtil.e(TAG, "peng mPart is " + mPart);
                            mPart = part;
                            handleSeekTo(intent, part);
                        } else {
                            play2Pause();
                        }
                    } else if (status == Resource.PlayerStatus.PAUSE) {
                        if (part != mPart) {
                            mPart = part;
                            handleSeekTo(intent, part);
                        } else {
                            pause2Play();
                            thread.start();
                        }
                    } else if (status == Resource.PlayerStatus.STOP) {
                        mPart = part;
                        handleSeekTo(intent, part);
                        thread.start();
                    }
                    break;
                case Resource.PlayerStatus.CONTROL_STOP_BTN:
                    mPlayer.stop();
                    mPlayer.setPaused(false);
                    status = Resource.PlayerStatus.STOP;
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

    private void handleSeekTo(Intent intent, int part) {

        splitEndTime = intent.getIntArrayExtra(Resource.ParamsKey.AUDIO_PARTENDTIME);
        switchPart(part, splitEndTime);
//            if(splitEndTime!=null){
//                Log.e("pengpeng partEndTime", splitEndTime.toString());
//            }else{
//                Log.e("pengpeng partEndTime", "null");
//            }
//            LogUtil.e(TAG, "peng handleSeekTo mPart is " + mPart);
//            LogUtil.e(TAG, "peng splitEndTime is " + String.valueOf(splitEndTime.toString()));
//            if( mPart != -1 && splitEndTime != null){
//
//                splitTime = transferPartToEndTime(mPart, splitEndTime);
//
//                switch(status){
//                    case Resource.PlayerStatus.PLAYING:
//                        LogUtil.e(TAG, "peng handleSeekTo Playing mPart is " + mPart);
//                        exerciseSeekTo(mPart, splitEndTime);
//                        break;
//                    case Resource.PlayerStatus.PAUSE:
//                        LogUtil.e(TAG, "peng handleSeekTo Pause mPart is " + mPart);
//                        exerciseSeekTo(mPart, splitEndTime);
//                        break;
//                    case Resource.PlayerStatus.STOP:
//                        stop2Play(); // 耗时操作，需等待调用onPreparedListener的接口
//                        break;
//                }
//
//            }
    }

    public int transferPartToStartTime(int part, int[] time) {
        int startTime = -1;
        switch (part) {
            case Resource.Type.PART_ONE:
                LogUtil.e(TAG, "pp Part One, part is " + part + ", mPart is " + mPart);
                startTime = 0;
                break;
            case Resource.Type.PART_TWO:
                LogUtil.e(TAG, "pp Part Two, part is " + part + ", mPart is " + mPart);
                startTime = time[0];
                break;
            case Resource.Type.PART_THREE:
                LogUtil.e(TAG, "pp Part Three, part is " + part + ", mPart is " + mPart);
                startTime = time[1];
                break;
        }
        return startTime;
    }

    private int transferPartToEndTime(int part, int[] time) {
        int endTime = -1;
        switch (part) {
            case Resource.Type.PART_ONE:
                endTime = time[0];
                break;
            case Resource.Type.PART_TWO:
                endTime = time[1];
                break;
            case Resource.Type.PART_THREE:
                endTime = time[2];
                break;
        }
        return endTime;
    }

    private void exerciseSeekTo(int part, int[] splitEndTime) {
//            if(part == 0){
//                mPlayer.seekTo(0);
//            } else if (part < splitEndTime.length){
//                mPlayer.seekTo(splitEndTime[part-1]);
//            }
        mPlayer.seekTo(transferPartToStartTime(part, splitEndTime));
//            mPlayer.play();
    }

    private void handlePlayerIntent(Intent intent) {
        int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
        String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
        if (audioUrl != null) {
            changeSource(audioUrl);
        } else {
            switch (control) {
                case Resource.PlayerStatus.CONTROL_PLAY_BTN:
                    if (status == Resource.PlayerStatus.PLAYING) {
                        play2Pause();
                    } else if (status == Resource.PlayerStatus.PAUSE) {
                        pause2Play();
                    } else if (status == Resource.PlayerStatus.STOP) {
                        stop2Play();
                    }
                    break;
                case Resource.PlayerStatus.CONTROL_LOOP_BTN:
                    mPlayer.setLooping(true);
                    break;
                case Resource.PlayerStatus.CONTROL_STOP_BTN:
                    if (status == Resource.PlayerStatus.PLAYING) {
                        play2Pause(); // 从听力页跳转到练习页，音频 状态设为暂停
                    }
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
     *
     * @param intent
     */
    private void handleMainIntent(Intent intent) {
        int control = intent.getIntExtra(Resource.PlayerStatus.CONTROL_KEY, -1);
        String audioUrl = intent.getStringExtra(Resource.ParamsKey.AUDIO_URL);
        if (audioUrl != null) {
            changeSource(audioUrl);
        } else {
            switch (control) {
                case Resource.PlayerStatus.CONTROL_PLAY_BTN:
                    if (status == Resource.PlayerStatus.PLAYING) {
                        play2Pause();
                    } else if (status == Resource.PlayerStatus.PAUSE) {
                        pause2Play();
                    } else if (status == Resource.PlayerStatus.STOP) {
                        stop2Play();
                    }
                    break;
                case Resource.PlayerStatus.CONTROL_STOP_BTN:
                    if (status == Resource.PlayerStatus.PLAYING) {
                        play2Stop();
                    } else if (status == Resource.PlayerStatus.PAUSE) {
                        pause2Stop();
                    }
                    break;
            }
        }

        LogUtil.e(TAG, "ppp handleMainIntent, status is " + status);

        Intent sendToAct = new Intent(Resource.Filter.MAIN_ACTIVITY);
        sendToAct.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
        sendBroadcast(sendToAct);

        Intent sendToPlayer = new Intent(Resource.Filter.PLAYER_ACTIVITY);
        sendToPlayer.putExtra(Resource.PlayerStatus.UPDATE_KEY, status);
        sendBroadcast(sendToPlayer);
    }

    private void changeSource(String url) {
        if (status != Resource.PlayerStatus.STOP) {  //status默认状态为stop，为避免一开始没有任何播放源的时候出错，所以在这里做判断，以免出现不合法的状态
            mPlayer.stop();
        }
        if (mPlayer.getSourceUrl() == null) {
            mPlayer.setSourceUrl(url);
            sourceChanged = true;
        } else if (!mPlayer.getSourceUrl().equals(url)) {
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
        LogUtil.e(TAG, "ppp stop2play, status before is " + status);
        if (status != Resource.PlayerStatus.STOP) {
            mPlayer.stop();
        }
        mPlayer.setPaused(false);
        mPlayer.play();
        LogUtil.e(TAG, "ppp stop2play, status after is " + status);
        status = Resource.PlayerStatus.PLAYING;  //耗时操作，等到转到播放状态之后才会令status = PLAYING
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

    private void switchPart(int part, int[] times) {
        int startTime = transferPartToStartTime(part, times);
        switch (status) {
            case Resource.PlayerStatus.PLAYING:
                mPlayer.seekTo(startTime);
                break;
            case Resource.PlayerStatus.PAUSE:
                mPlayer.seekTo(startTime);
                pause2Play();
                break;
            case Resource.PlayerStatus.STOP:
                stop2Play();
                //这里应该seekTo但是由于是异步操作，不能直接在这里seekTo，需等到onPrepared回调后才可以seekTo
                break;
        }
    }
}
