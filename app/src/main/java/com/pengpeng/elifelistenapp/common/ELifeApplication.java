package com.pengpeng.elifelistenapp.common;

import android.app.Application;
import android.util.Log;

import com.pengpeng.elifelistenapp.BuildConfig;

import org.xutils.x;

/**
 * Created by pengpeng on 16-3-25.
 */
public class ELifeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }

}
