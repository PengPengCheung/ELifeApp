package com.pengpeng.elifelistenapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.pengpeng.elifelistenapp.R;
import com.pengpeng.elifelistenapp.common.ELifeBaseActivity;
import com.pengpeng.elifelistenapp.utils.Tools;

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

        if (Tools.isNetworkAvailable(this)) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    StartActivity.this.finish();
                }
            }, 1000);
        } else {
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
        }

    }
}
