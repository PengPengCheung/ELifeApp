package com.pengpeng.elifelistenapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pengpeng.elifelistenapp.utils.Resource;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_network_player)
public class NetworkPlayerActivity extends ELifeBaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_exercise)
    private ImageView mIVExercise;

    @ViewInject(R.id.iv_back)
    private ImageView mIVBack;

    private PlayerActivityReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Intent intent = getIntent();
        String audioTitle = intent.getStringExtra(Resource.ParamsKey.AUDIO_TITLE);
        int status = intent.getIntExtra(Resource.ParamsKey.AUDIO_STATUS, -1);
        Intent sendIntent = new Intent(Resource.Filter.PLAYER_SERVICE);
        intent.putExtra(Resource.Filter.PLAYER_ACTIVITY, Resource.Filter.PLAYER_ACTIVITY_VALUE);
        intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_GET_PROGRESS);
        sendBroadcast(sendIntent);

    }

    private void init() {
        mIVExercise.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mReceiver = new PlayerActivityReceiver();
        IntentFilter filter = new IntentFilter(Resource.Filter.PLAYER_ACTIVITY);
        registerReceiver(mReceiver, filter);
    }


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
        }
    }

    class PlayerActivityReceiver extends BroadcastReceiver{

        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
