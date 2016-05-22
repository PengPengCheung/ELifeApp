package com.pengpeng.elifelistenapp.presenter;

import android.util.Log;

import com.pengpeng.elifelistenapp.dataapi.DataApi;
import com.pengpeng.elifelistenapp.dataapi.DataApiImpl;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.view.AudioAnswerView;

/**
 * Created by Administrator on 2016/4/26.
 */
public class AudioDataPresenterImpl implements AudioDataPresenter,DataApiImpl.onLoadAudioListListener<Audio> {
     private AudioAnswerView mAudioDataView;
    private DataApi mDataApi;
    public AudioDataPresenterImpl(AudioAnswerView audioDataView) {
        mAudioDataView = audioDataView;
        mDataApi = new DataApiImpl();
    }
    @Override
    public void loadAudioData(String audioId,String uuid) {
        mDataApi.getAudioData(audioId,uuid,this);
    }

    @Override
    public void onSuccess(Audio audioData) {
        Log.i("------------scsdata",audioData.toString());
        mAudioDataView.addAudioData(audioData);

    }

    @Override
    public void onFailure(String msg, Exception e) {

    }
}
