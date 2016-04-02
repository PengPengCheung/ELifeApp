package com.pengpeng.elifelistenapp.utils;

/**
 * Created by pengpeng on 16-3-29.
 */
public class Resource {
    public static final class Type{
        public static final int TYPE_MOVIE = 0;
        public static final int TYPE_SPEECH = 1;
        public static final int TYPE_NEWS = 2;

        public static final int PART_ONE = 1;
        public static final int PART_TWO = 2;
        public static final int PART_THREE = 3;
    }

    public static final class Filter{
        public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
        public static final int MAIN_ACTIVITY_VALUE = 1;
        public static final String PLAYER_SERVICE = "PLAYER_SERVICE";
        public static final String PLAYER_ACTIVITY = "PLAYER_ACTIVITY";
        public static final int PLAYER_ACTIVITY_VALUE = 2;
    }

    public static final class Path{
        public static final String serverPath = "http://192.168.204.215:8000/";
        //        public static final String getAudioPath = "http://192.168.235.33:8000/getAudio";//丽璇的data的数据类型是list而不是对象！！！！
        public static final String getAudioPath = serverPath+"getAudio";
        public static final String audioListPath = serverPath+"showLists/";
    }

    public static final class Event{
        public final static String TIME_OUT_EVENT = "CONNECT_TIME_OUT";
        public final static String TIME_OUT_EVENT_MSG = "连接服务器失败";
        public final static String DATA_WRONG_EVENT = "FETCHED_DATA_WRONG";
        public final static String DATA_WRONG_EVENT_MSG = "数据获取错误";
    }

    public static final class Debug{
        public final static String TAG = "ELifeTAG";
        public final static String MAIN = "MainActivity";
    }

    public static final class ParamsKey{
        public static final String USER_ID = "userUUID";
        public static final String AUDIO_TITLE = "audioTitle";
        public static final String AUDIO_URL = "audioUrl";
        public static final String AUDIO_STATUS = "audioStatus";
    }

    public static final class PlayerStatus{
        public static final String CONTROL_KEY = "control";
        public static final String UPDATE_KEY = "update";
        public static final int CONTROL_PLAY_BTN = 1;
        public static final int CONTROL_STOP_BTN = 2;
        public static final int CONTROL_GET_PROGRESS = 6;


        public static final int PLAYING = 3;
        public static final int PAUSE = 4;
        public static final int STOP = 5;

    }
}
