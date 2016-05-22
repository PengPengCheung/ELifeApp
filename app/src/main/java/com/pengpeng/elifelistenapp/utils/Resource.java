package com.pengpeng.elifelistenapp.utils;

/**
 * Created by pengpeng on 16-3-29.
 */
public class Resource {
    public static final class Type{
        public static final int TYPE_MOVIE = 0;
        public static final int TYPE_SPEECH = 1;
        public static final int TYPE_NEWS = 2;

        public static final String STR_TYPE_MOVIE = "movie";
        public static final String STR_TYPE_NEWS = "news";
        public static final String STR_TYPE_SPEECH = "speech";

        public static final String PART = "part";
        public static final int PART_ONE = 0;
        public static final int PART_TWO = 1;
        public static final int PART_THREE = 2;
    }

    public static final class Filter{
        public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
        public static final int MAIN_ACTIVITY_VALUE = 1;
        public static final String PLAYER_SERVICE = "PLAYER_SERVICE";
        public static final String PLAYER_ACTIVITY = "PLAYER_ACTIVITY";
        public static final int PLAYER_ACTIVITY_VALUE = 2;
        public static final String FILTER_SIGNAL = "FILTER_SIGNAL";  //用于区分各个activity传给service的广播
        public static final String PLAYER_ACTIVITY_SEEKBAR = "ACTIVITY_SEEKBAR";
        public static final String PLAYER_SERVICE_SEEKBAR = "SERVICE_SEEKBAR";

        public static final String EXERCISE_ACTIVITY="EXERCISE_ACTIVITY";
        public static final int EXERCISE_ACTIVITY_VALUE=3;
    }

    public static final class Path{
        public static final String serverPath = "http://139.129.33.178:8000/";
      //          public static final String getAudioPath = "http://192.168.235.33:8000/getAudio";//丽璇的data的数据类型是list而不是对象！！！！
       public static final String getAudioPath = serverPath+"getAudio/";
       public static final String audioListPath = serverPath+"showLists/";
        public static final String audioStandardAnswer = serverPath+"returnStandardAnswer/";
       //public static final String audioListPath ="http://192.168.235.25:8000/showList/";
        //public static final String getAudioPath = "http://192.168.235.25:8000/data_test";
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
        public static final String AUDIO_ID = "audioId";
        public static final String USER_ID = "userId";
        public static final String AUDIO_TITLE = "audioTitle";
        public static final String AUDIO_URL = "audioUrl";
        public static final String AUDIO_STATUS = "audioStatus"; //前台activity之间用于更新player状态，不作为发送给服务的键，其对应的键值为player的状态，注意与UPDATE_KEY的区别
        public static final String AUDIO_TYPE = "audioType";
        public static final String AUDIO_PART = "audioPart";
        public static final String AUDIO_INDEX = "audioIndex";
        public static final String AUDIO_DURATION = "audioDuration";
        public static final String SEEKBAR_PROGRESS = "SeekBarProgress";
        public static final String SEEKBAR_MAX = "SeekBarMax";
        public static final String AUDIO_TEXT="audioText";
        public static final String AUDIO_PARTENDTIME="audioPartEndTime";
        public static final String USERANSWER = "userAnswer";

    }

    public static final class PlayerStatus{
        public static final String CONTROL_KEY = "control"; //activity中控制音频的各个键，其对应的键值为控制键
        public static final String UPDATE_KEY = "update"; //后台服务用于通知前台activity更新，其对应的键值为player的状态
        public static final String GET_PROGRESS_KEY = "progress"; //获取音频的进度,每次由服务响应返回给player activity的intent中都要包含音频进度信息
        public static final String GET_PROGRESS_VALUE = "progress_value";

        public static final int CONTROL_PLAY_BTN = 1;
        public static final int CONTROL_STOP_BTN = 2;

        public static final int CONTROL_NEXT_BTN = 7;
        public static final int CONTROL_PREVIOUS_BTN = 8;
        public static final int CONTROL_LOOP_BTN = 9;
        public static final int CONTROL_SHOW_TEXT = 10;

        public static final int PLAYING = 3;
        public static final int PAUSE = 4;
        public static final int STOP = 5;

    }
}
