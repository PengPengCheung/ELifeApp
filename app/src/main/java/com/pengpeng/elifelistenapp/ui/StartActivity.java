package com.pengpeng.elifelistenapp.ui;

import android.content.Intent;
import android.os.Bundle;

import com.pengpeng.elifelistenapp.R;
import com.pengpeng.elifelistenapp.common.ELifeBaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/4/9.
 */

public class StartActivity extends ELifeBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                StartActivity.this.finish();
            }
        }, 1000);
    }
}
