package com.pengpeng.elifelistenapp;

import android.app.Application;

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
