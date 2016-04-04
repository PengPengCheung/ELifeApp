package com.pengpeng.elifelistenapp;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

/**
 * Created by pengpeng on 16-3-25.
 */
public class ELifeApplication extends Application {

    public AudioListManager getManager() {
        return mManager;
    }

    public void setManager(AudioListManager mManager) {
        this.mManager = mManager;
    }

    private AudioListManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        Log.i("ELIFEAPP", "audioList");
        mManager = new AudioListManager();
        Log.i("ELIFEAPP", "new audioList");
    }

}
