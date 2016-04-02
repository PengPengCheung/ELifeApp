package com.pengpeng.elifelistenapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.utils.Resource;

import org.xutils.x;

public class ELifeBaseActivity extends AppCompatActivity {


    private Audio mAudio;
//    private boolean hasAudio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Resource.Debug.TAG, "inject");
        x.view().inject(this);
        Log.i(Resource.Debug.TAG, "app");
        x.app();

        Log.i(Resource.Debug.TAG, "init");
        init();
    }

    private void init() {

    }

    public Audio getAudio() {
        return this.mAudio;
    }

    public void setAudio(Audio audio){
        this.mAudio = audio;
    }

//    public NetworkAudioPlayer getPlayer() {
//        return this.mPlayer;
//    }



//    public void setPlayingAudio(Audio audio) {
//        if (audio != null) {
//            this.mAudio = audio;
//            mPlayer.setSourceUrl(mAudio.getAudioUrl());
//            mPlayer.play();
//            hasAudio = true;
//        }
//
//    }


}
