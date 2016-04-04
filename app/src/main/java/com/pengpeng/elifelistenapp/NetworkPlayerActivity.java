package com.pengpeng.elifelistenapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pengpeng.elifelistenapp.utils.Resource;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_network_player)
public class NetworkPlayerActivity extends ELifeBaseActivity implements View.OnClickListener {

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

    private int progress;

    private static final String TAG = "ActivityNetworkPlayer";

    private PlayerActivityReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        Intent intent = getIntent();
        String audioTitle = intent.getStringExtra(Resource.ParamsKey.AUDIO_TITLE);
        int status = intent.getIntExtra(Resource.ParamsKey.AUDIO_STATUS, -1);


        Intent sendIntent = new Intent(Resource.Filter.PLAYER_SERVICE);
        sendIntent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.PLAYER_ACTIVITY_VALUE);
        sendIntent.putExtra(Resource.PlayerStatus.CONTROL_KEY, status);
        sendBroadcast(sendIntent);
        Log.e(TAG, "sendIntent");

    }

    private void init() {
        mIVExercise.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mIVPlayBtn.setOnClickListener(this);
        mIVNextBtn.setOnClickListener(this);
        mIVPreviousBtn.setOnClickListener(this);
        mIVLoopBtn.setOnClickListener(this);
        mReceiver = new PlayerActivityReceiver();
        IntentFilter filter = new IntentFilter(Resource.Filter.PLAYER_ACTIVITY);
        registerReceiver(mReceiver, filter);
        Intent intent = new Intent(NetworkPlayerActivity.this, AudioPlayerService.class);
        startService(intent);
    }

//    public class UpdateSeekBarThread extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                handler.sendEmptyMessage(0);
//                Intent intent = new Intent(Resource.Filter.PLAYER_ACTIVITY);
//                intent.putExtra(Resource.PlayerStatus.GET_PROGRESS_KEY, Resource.PlayerStatus.GET_PROGRESS_VALUE);
//                intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.PLAYER_ACTIVITY_VALUE);
//                sendBroadcast(intent);
//            }
//
//        }
//    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_exercise:
                Intent intent = new Intent(NetworkPlayerActivity.this, ExerciseActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                Intent backIntent = new Intent(NetworkPlayerActivity.this, MainActivity.class);
                startActivity(backIntent);
                break;
            case R.id.iv_play_btn:
                sendIntent(Resource.PlayerStatus.CONTROL_PLAY_BTN);
                Log.i("PlayerActivity", "play");
                break;
            case R.id.iv_next_btn:
                sendIntent(Resource.PlayerStatus.CONTROL_NEXT_BTN);
                Log.i("PlayerActivity", "next");
                break;
            case R.id.iv_pre_btn:
                sendIntent(Resource.PlayerStatus.CONTROL_PREVIOUS_BTN);
                Log.i("PlayerActivity", "pre");
                break;
            case R.id.iv_loop:
                sendIntent(Resource.PlayerStatus.CONTROL_LOOP_BTN);
                Log.i("PlayerActivity", "loop");
                break;

        }
    }

    private void sendIntent(int controlKey) {
        Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
        intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.PLAYER_ACTIVITY_VALUE);
        intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, controlKey);
        sendBroadcast(intent);
        Log.i(TAG, "in method sendIntent");
    }

    class PlayerActivityReceiver extends BroadcastReceiver{

        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                int progress = intent.getIntExtra(Resource.PlayerStatus.GET_PROGRESS_KEY, -1);
                if(progress!=-1){
                    Log.i("Progress", ""+progress);
                }

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
