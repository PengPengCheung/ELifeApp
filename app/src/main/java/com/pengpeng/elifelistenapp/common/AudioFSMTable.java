package com.pengpeng.elifelistenapp.common;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by baidu on 16/5/23.
 */
public class AudioFSMTable {
    public static class FSMState{
        public static final String AUDIO_STOP = "stop";
        public static final String AUDIO_PAUSE = "pause";
        public static final String AUDIO_PLAY = "play";
    }

    public static class FSMEvent{

        //MainActivity 的按钮事件
        public static final String BTN_MAIN_PLAY = "MainActivity_play_button";
        public static final String BTN_MAIN_STOP = "MainActivity_stop_button";
        public static final String BTN_MAIN_PAUSE = "MainActivity_pause_button";

        //PlayerActivity 的按钮事件
        public static final String BTN_PLAYER_PLAY = "PlayerActivity_play_button";
        public static final String BTN_PLAYER_PAUSE = "PlayerActivity_pause_button";
        public static final String BTN_PLAYER_NEXT = "PlayerActivity_next_audio";
        public static final String BTN_PLAYER_PRE = "PlayerActivity_previous_audio";
        public static final String BTN_PLAYER_LOOP = "PlayerActivity_loop";

        //ExerciseActivity 的按钮事件
        public static final String BTN_EXERCISE_PLAY = "ExerciseActivity_play_button";
        public static final String BTN_EXERCISE_PAUSE = "ExerciseActivity_pause_button";

        //ExerciseActivity 的切换事件
        public static final String SWITCH_EXERCISE = "ExerciseActivity_switch";
    }

    public static HashMap<String, HashMap<String, String>> dictFSM;
    public static HashMap<String, String> dictStateStop;
    public static HashMap<String, String> dictStatePlay;
    public static HashMap<String, String> dictStatePause;

    public static void initFSM(){
        initStateStop();
        initStatePlay();
        initStatePause();
        initDictFSM();
    }

    private static void initDictFSM() {
        dictFSM = new HashMap<String, HashMap<String, String>>();
        dictFSM.clear();
        dictFSM.put(FSMState.AUDIO_PLAY, dictStatePlay);
        dictFSM.put(FSMState.AUDIO_PAUSE, dictStatePause);
        dictFSM.put(FSMState.AUDIO_STOP, dictStateStop);

    }

    private static void initStateStop() {
        dictStateStop = new HashMap<>();
        dictStateStop.clear();
        dictStateStop.put(FSMEvent.BTN_MAIN_PLAY, FSMState.AUDIO_PLAY);
        dictStateStop.put(FSMEvent.BTN_MAIN_STOP, FSMState.AUDIO_STOP);
        dictStateStop.put(FSMEvent.BTN_PLAYER_PLAY, FSMState.AUDIO_PLAY);
        dictStateStop.put(FSMEvent.BTN_PLAYER_NEXT, FSMState.AUDIO_PLAY);
        dictStateStop.put(FSMEvent.BTN_PLAYER_PRE, FSMState.AUDIO_PLAY);
        dictStateStop.put(FSMEvent.BTN_PLAYER_LOOP, FSMState.AUDIO_STOP);
        dictStateStop.put(FSMEvent.BTN_EXERCISE_PLAY, FSMState.AUDIO_PLAY);
        dictStateStop.put(FSMEvent.SWITCH_EXERCISE, FSMState.AUDIO_STOP);
    }

    private static void initStatePlay(){
        dictStatePlay = new HashMap<>();
        dictStatePlay.clear();
        dictStatePlay.put(FSMEvent.BTN_MAIN_STOP, FSMState.AUDIO_STOP);
        dictStatePlay.put(FSMEvent.BTN_MAIN_PAUSE, FSMState.AUDIO_PAUSE);
        dictStatePlay.put(FSMEvent.BTN_PLAYER_PAUSE, FSMState.AUDIO_PAUSE);
        dictStatePlay.put(FSMEvent.BTN_PLAYER_NEXT, FSMState.AUDIO_PLAY);
        dictStatePlay.put(FSMEvent.BTN_PLAYER_PRE, FSMState.AUDIO_PLAY);
        dictStatePlay.put(FSMEvent.BTN_PLAYER_LOOP, FSMState.AUDIO_PLAY);
        dictStatePlay.put(FSMEvent.BTN_EXERCISE_PAUSE, FSMState.AUDIO_PAUSE);
        dictStatePlay.put(FSMEvent.SWITCH_EXERCISE, FSMState.AUDIO_PLAY);
    }

    private static void initStatePause(){
        dictStatePause = new HashMap<>();
        dictStatePause.clear();
        dictStatePause.put(FSMEvent.BTN_MAIN_PLAY, FSMState.AUDIO_PLAY);
        dictStatePause.put(FSMEvent.BTN_MAIN_STOP, FSMState.AUDIO_STOP);
        dictStatePause.put(FSMEvent.BTN_PLAYER_PLAY, FSMState.AUDIO_PLAY);
        dictStatePause.put(FSMEvent.BTN_PLAYER_NEXT, FSMState.AUDIO_PLAY);
        dictStatePause.put(FSMEvent.BTN_PLAYER_PRE, FSMState.AUDIO_PLAY);
        dictStatePause.put(FSMEvent.BTN_PLAYER_LOOP, FSMState.AUDIO_PLAY);
        dictStatePause.put(FSMEvent.BTN_EXERCISE_PLAY, FSMState.AUDIO_PLAY);
        dictStatePause.put(FSMEvent.SWITCH_EXERCISE, FSMState.AUDIO_PAUSE);
    }

    private final static byte[] lock = new byte[0];

    public static String queryDestState(String sourceState, String event){
        String destState;
        synchronized (lock) {
            HashMap<String, String> dictEventState = new HashMap<String, String>();
            dictEventState = dictFSM.get(sourceState);
            if (dictEventState == null) {
                Log.e("AudioFSM", "不存在该状态对应的状态机，请完善逻辑!");
                return null;
            }
            destState = dictEventState.get(event);
            if (destState == null) {
                Log.e("AudioFSM", "处于状态 (" + sourceState + ")时， " +
                        "不存在执行event = " + event + " 的跳转，请考虑是否完善逻辑!");
                return null;
            }
        }
        return destState;
    }

    public static void destroy(){
        if(dictStateStop!=null){
            dictStateStop.clear();
        }
        if(dictStatePlay!=null){
            dictStatePlay.clear();
        }
        if(dictStatePause!=null){
            dictStatePause.clear();
        }
        if(dictFSM!=null){
            dictFSM.clear();
        }
    }

}
