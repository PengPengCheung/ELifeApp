package com.pengpeng.elifelistenapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.utils.Resource;
import com.pengpeng.elifelistenapp.utils.Tools;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_network_player)
public class PlayerActivity extends ELifeBaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_exercise)
    private ImageView mIVExercise;

    @ViewInject(R.id.iv_back)
    private ImageView mIVBack;

    @ViewInject(R.id.iv_play_btn)
    private ImageView mIVPlayBtn;

    @ViewInject(R.id.iv_next_btn)
    private ImageView mIVNextBtn;

    @ViewInject(R.id.iv_pre_btn)
    private ImageView mIVPreviousBtn;

    @ViewInject(R.id.iv_loop)
    private ImageView mIVLoopBtn;

    @ViewInject(R.id.tv_start)
    private TextView mTVStart;

    @ViewInject(R.id.tv_end)
    private TextView mTVEnd;

    @ViewInject(R.id.seekBar)
    private SeekBar mSeekbar;

    @ViewInject(R.id.tv_audio_title)
    private TextView mTVAudioTitle;

    private NetworkAudioPlayer mPlayer;
    private Audio mAudio;
    private AudioListManager mManager;
    private int index;
    private PlayerActivityReceiver mReceiver;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateProgress();
                    break;
            }
        }
    };

//    private static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    updateProgress();
//                    break;
//            }
//        }
//    };

    private void updateProgress() {
        if (mAudio != null) {
            Log.e(TAG, "audio not null");
//            if(mPlayer == null || mPlayer.isStopFlag()){
//                mTVStart.setText("0:00");
//                mSeekbar.setProgress(0);
//                return;
//            }
            if (mPlayer.isFlag()) {
                int totalTime = mAudio.getAudioPartEndTime().get(2);
                int currentTime = mPlayer.getCurrentPosition();
                int seekBarMax = mSeekbar.getMax();
                Log.e("Progress: ", totalTime + " " + currentTime + " " + seekBarMax);
//                if (currentTime < endTime || currentTime >= startTime) {
                if (totalTime > 0 && currentTime > 0 && seekBarMax > 0) {
                    Log.i("Progress: ", String.valueOf(mSeekbar.getProgress()));
                    mTVStart.setText(Tools.getTimeText(currentTime));
                    mSeekbar.setProgress((int) (seekBarMax * (float) currentTime / totalTime));
                }
            }
        }
    }

    private String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        index = intent.getIntExtra(Resource.ParamsKey.AUDIO_INDEX, -1);
        init();
        String title = intent.getStringExtra(Resource.ParamsKey.AUDIO_TITLE);
        if (title != null) {
            mTVAudioTitle.setText(title);
        }
        int status = intent.getIntExtra(Resource.ParamsKey.AUDIO_STATUS, -1);
        switch (status) {
            case Resource.PlayerStatus.PLAYING:
                mIVPlayBtn.setImageResource(R.drawable.pause);
                break;
            case Resource.PlayerStatus.PAUSE:
                mIVPlayBtn.setImageResource(R.drawable.play);
                break;
            case Resource.PlayerStatus.STOP:
                mIVPlayBtn.setImageResource(R.drawable.play);
                mTVStart.setText("0:00");
                mSeekbar.setProgress(0);
                break;
        }
        Log.i(TAG, "status " + status);
        if(mPlayer!=null){
            Log.i(TAG, "" + mPlayer.isPaused());
        }
//        mPlayer.playPause();

    }

    private void init() {
        mIVExercise.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mIVPlayBtn.setOnClickListener(this);
        mIVNextBtn.setOnClickListener(this);
        mIVPreviousBtn.setOnClickListener(this);
        mIVLoopBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(new ProgressBarListener());
        mPlayer = NetworkAudioPlayer.getInstance(getApplicationContext());
        mManager = ((ELifeApplication)getApplication()).getManager();
        mAudio = mManager.get(index);
        mReceiver = new PlayerActivityReceiver();
        IntentFilter filter = new IntentFilter(Resource.Filter.PLAYER_ACTIVITY);
        registerReceiver(mReceiver, filter);

        mTVEnd.setText(Tools.getTimeText(mAudio.getAudioPartEndTime().get(2)));
        UpdateSeekBarThread thread = new UpdateSeekBarThread();
        thread.start();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_exercise:
                Intent intent = new Intent(this, ExerciseActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                Intent backIntent = new Intent(this, MainActivity.class);
                mManager.setCurrent(index);
                backIntent.putExtra(Resource.ParamsKey.AUDIO_INDEX, index);
                startActivity(backIntent);
                PlayerActivity.this.finish();
                break;
            case R.id.iv_play_btn:
                sendIntent(Resource.PlayerStatus.CONTROL_PLAY_BTN);
                Log.i("PlayerActivity", "play");
                break;
            case R.id.iv_next_btn:
                ++index;
                if(index>mManager.getAudioList().size()-1){
                    Toast.makeText(this, "no more audio", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAudio = mManager.get(index);
                sendIntent(Resource.PlayerStatus.CONTROL_NEXT_BTN, mAudio.getAudioUrl());
                mTVAudioTitle.setText(mAudio.getAudioTitle());
                Log.i("PlayerActivity", "next");

                break;
            case R.id.iv_pre_btn:
                --index;
                if(index<0){
                    Toast.makeText(this, "no more audio", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAudio = mManager.getAudioList().get(index);
                sendIntent(Resource.PlayerStatus.CONTROL_PREVIOUS_BTN, mAudio.getAudioUrl());
                mTVAudioTitle.setText(mAudio.getAudioTitle());
                Log.i("PlayerActivity", "pre");
                break;
            case R.id.iv_loop:
                sendIntent(Resource.PlayerStatus.CONTROL_LOOP_BTN);
                Log.i("PlayerActivity", "loop");
                break;
        }
    }

    private void sendIntent(int controlKey, String audioUrl){
        if(audioUrl == null){
            sendIntent(controlKey);
            return;
        }
        Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
        intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.PLAYER_ACTIVITY_VALUE);
        intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, controlKey);
        intent.putExtra(Resource.ParamsKey.AUDIO_URL, audioUrl);
        sendBroadcast(intent);
    }

    private void sendIntent(int controlKey) {
        Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
        intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.PLAYER_ACTIVITY_VALUE);
        intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, controlKey);
        sendBroadcast(intent);
        Log.i(TAG, "in method sendIntent");
    }

    private class ProgressBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mPlayer.seekTo((int) (mAudio.getAudioPartEndTime().get(2) * (float) seekBar.getProgress() / seekBar.getMax()));
                seekBar.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public class UpdateSeekBarThread extends Thread {
        @Override
        public void run() {

//            Looper.prepare();

            Log.i("thread","run");
            mPlayer.setFlag(true);

            while (mPlayer.isFlag()) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }

//            Looper.loop();

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    class PlayerActivityReceiver extends BroadcastReceiver {

        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
//                int progress = intent.getIntExtra(Resource.PlayerStatus.GET_PROGRESS_KEY, -1);
//                if(progress!=-1){
//                    Log.i("Progress", ""+progress);
//                }

                int status = intent.getIntExtra(Resource.PlayerStatus.UPDATE_KEY, -1);
                switch (status){
                    case Resource.PlayerStatus.PLAYING:
                        mIVPlayBtn.setImageResource(R.drawable.pause);
                        //设置进度条进度、开始时间和结束时间，如果换了音频材料要改变音频标题
                        break;
                    case Resource.PlayerStatus.PAUSE:
                        mIVPlayBtn.setImageResource(R.drawable.play);
                        break;
                    case Resource.PlayerStatus.STOP:
                        mIVPlayBtn.setImageResource(R.drawable.play);
                        break;
                }
                Log.i(TAG, "onReceive");
            }

        }
    }
}
