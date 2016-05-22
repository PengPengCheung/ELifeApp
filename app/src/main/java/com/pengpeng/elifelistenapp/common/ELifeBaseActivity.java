package com.pengpeng.elifelistenapp.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.utils.Resource;

import org.xutils.x;

public class ELifeBaseActivity extends AppCompatActivity {


    private Audio mAudio;


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



}
