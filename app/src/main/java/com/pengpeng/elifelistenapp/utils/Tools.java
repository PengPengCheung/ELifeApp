package com.pengpeng.elifelistenapp.utils;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.widget.SeekBar;

import java.util.List;
import java.util.UUID;

/**
 * Created by pengpeng on 16-3-28.
 */
public class Tools {

    /**
     * 检查网络是否可用，需要在AndroidManifest.xml文件中加上相应权限
     *
     * @param context 检查网络时候的上下文
     * @return 有网络（wifi或者移动数据网络）返回true，没网络返false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            /*没有网络*/
            return false;
        } else {
            /*有网络*/
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo network : info) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int[] listToIntArray(List<Integer> list){
        if(list != null){
            int[] intArray = new int[list.size()];
            for(int i = 0; i<list.size(); i++){
                intArray[i] = list.get(i);
            }
            return intArray;
        }
        return null;
    }

    public static String getType(int pageIndex){
        String type = null;
        switch(pageIndex){
            case Resource.Type.TYPE_MOVIE:
                type = Resource.Type.STR_TYPE_MOVIE;
                break;
            case Resource.Type.TYPE_SPEECH:
                type = Resource.Type.STR_TYPE_SPEECH;
                break;
            case Resource.Type.TYPE_NEWS:
                type = Resource.Type.STR_TYPE_NEWS;
                break;
        }
        return type;
    }

    /**
     * 获得手机的UUID标识号
     *
     * @param activity 传入的activity组件参数
     * @return 返回UUID标识号
     */

    public static String getUUID(Activity activity) {
        if (activity != null) {
            TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice, tmSerial, androidId;
            tmDevice = tm.getDeviceId();
            tmSerial = tm.getSimSerialNumber();
            androidId = android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String uniqueId = deviceUuid.toString();
            Log.i("uniqueId", uniqueId);
            return uniqueId;
        } else {
            return null;
        }
    }

    /**
     * 传入的time参数单位为milliseconds，即毫秒.这个方法可以将毫秒单位的时间转换为0：00形式的时间
     *
     * @param time 传入的时间长度，单位为milliseconds，即毫秒
     * @return 返回00：00形式的时间字符串
     */
    public static String getTimeText(int time) {
        if (time < 0) {
            return "time wrong";
        }

        int totalSeconds = time / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String showTime;
        if (seconds > 9 && seconds < 60) {
            showTime = minutes + ":" + seconds;
        } else {
            showTime = minutes + ":0" + seconds;
        }
        return showTime;
    }

    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static int progressToPosition(SeekBar seekBar, MediaPlayer player) {
        if (seekBar != null && player != null) {
            int pos = (int) (player.getDuration() * (float) seekBar.getProgress() / seekBar.getMax());
            return pos;
        }
        return 0;
    }

    public static int positionToProgress(MediaPlayer player, SeekBar seekBar) {
        if (seekBar != null && player != null) {
            return (int) (seekBar.getMax() * (float) player.getCurrentPosition() / player.getDuration());
        }
        return 0;
    }

//    public static void main(String[] args){
//        System.out.println("hello I am .");
//
//        new JSONObject(new HashMap());
//    }
}
