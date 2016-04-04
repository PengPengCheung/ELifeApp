package com.pengpeng.elifelistenapp;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 16-4-4.
 */
public class AudioListManager {
    private List<Audio> mAudioList;
    private int mCurrent = 0;

    public AudioListManager() {
        mAudioList = new ArrayList<>();
    }

    public List<Audio> getAudioList() {
        return mAudioList;
    }

    public void setAudioList(List<Audio> mAudioList) {
        this.mAudioList = mAudioList;
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void setCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
    }

    public Audio get(int position) {
        if (mAudioList != null) {
            if (position >= 0 && position < mAudioList.size()) {
                mCurrent = position;
                return mAudioList.get(mCurrent);
            } else if (position < 0) {
                mCurrent = 0;
                return mAudioList.get(0);
            } else if (position >= mAudioList.size()) {
                mCurrent = mAudioList.size() - 1;
                return mAudioList.get(mAudioList.size() - 1);
            }
        }
        return null;
    }

    public void addAll(List<Audio> list) {
        mAudioList.addAll(list);
    }

    public void add(Audio audio) {
        mAudioList.add(audio);
    }

    public void setAudio(Audio audio, int position) {
        if (position < 0 || position > mAudioList.size() - 1) {
            throw new IndexOutOfBoundsException("position out of IndexOfAudioList!");
        }
        mAudioList.set(position, audio);

    }

    public void remove(int position) {
        mAudioList.remove(position);
    }
}
