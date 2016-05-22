package com.pengpeng.elifelistenapp.common;

import com.pengpeng.elifelistenapp.model.Audio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengpeng on 16-4-4.
 */
public class AudioListManager {
    private Map<String, List<Audio>> mManager;
    private static AudioListManager instance;
    private int mCurrent = -1; // 始终表示某个类别对应的音频下标
    private String mCurrentType; // 标识当前播放音频的类别

    private AudioListManager() {
        mManager = new HashMap<>();
    }

    public static AudioListManager getInstance(){
        if(instance == null){
            synchronized (AudioListManager.class){
                if(instance == null){
                    instance = new AudioListManager();
                }
            }
        }
        return instance;
    }

    public List<Audio> getAudioListByType(String type) {
        if(isValidate(type)){
            return mManager.get(type);
        }
        return null;
    }

    public int getCurrentIndex() {
        return mCurrent;
    }

    public void setCurrentIndex(int position) {
        int sum = mManager.get(mCurrentType).size();
        if (position >= 0 && position < sum) {
            mCurrent = position;
        } else if (position < 0) {
            mCurrent = 0;
        } else if (position >= sum) {
            mCurrent = sum - 1;
        }
    }

    public void setCurrentType(String type){
        mCurrentType = type;
    }

    public int getCurrentListSize(){
        if(mCurrentType != null){
            return mManager.get(mCurrentType).size();
        }
        return 0;
    }

    public Audio getCurrentAudio() {
        if(mCurrent >= 0 && mCurrentType != null && mCurrent < mManager.get(mCurrentType).size()){
            return mManager.get(mCurrentType).get(mCurrent);
        }
//            int sum = mManager.get(type).size();
//            if (position >= 0 && position < sum) {
//                mCurrent = position;
//                return mManager.get(type).get(mCurrent);
//            } else if (position < 0) {
//                mCurrent = 0;
//                return mManager.get(type).get(0);
//            } else if (position >= sum) {
//                mCurrent = sum - 1;
//                return mManager.get(type).get(sum - 1);
//            }
//        }
        return null;
    }

    public void addAll(List<Audio> list, String type) {

        if(type != null && !mManager.containsKey(type)){
            mManager.put(type, list);
            return;
        }

        if(type != null && mManager.containsKey(type)){
            mManager.get(type).addAll(list);
        }
    }

    public void add(Audio audio, String type) {
        if(isValidate(type)){
            mManager.get(type).add(audio);
        }
    }

    private boolean isValidate(String type){
        if( type != null && mManager.containsKey(type) ){
            return true;
        }
        return false;
    }

    public void setAudio(Audio audio, int position, String type) {
        if(type!=null && mManager.containsKey(type)){
            int sum = mManager.get(type).size();
            if (position < 0 || position > sum - 1) {
                throw new IndexOutOfBoundsException("position out of IndexOfAudioList!");
            }
            mManager.get(type).set(position, audio);
        }
    }

    public void remove(int position, String type) {
        if(isValidate(type)){
            mManager.get(type).remove(position);
        }
    }
}
