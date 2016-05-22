package com.pengpeng.elifelistenapp.presenter;

import com.pengpeng.elifelistenapp.dataapi.DataApi;
import com.pengpeng.elifelistenapp.dataapi.DataApiImpl;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.view.AudioListIView;

import java.util.List;

/**
 * Created by pengpeng on 16-3-29.
 */
public class AudioListPresenterImpl implements AudioListPresenter, DataApiImpl.onLoadAudioListListener<List<Audio>>{


    private DataApi mDataApi;
    private AudioListIView mAudioListIView;


    public AudioListPresenterImpl(AudioListIView audioListIView) {
        mAudioListIView = audioListIView;
        mDataApi = new DataApiImpl();
    }



    @Override
    public void loadAudioList(String uuid, int type) {
        mAudioListIView.showProgress();
        mDataApi.getAudioListByUid(uuid, type, this);

    }

    @Override
    public void onSuccess(List<Audio> audioList) {
        mAudioListIView.hideProgress();
        mAudioListIView.addAudio(audioList);

    }

    @Override
    public void onFailure(String msg, Exception e) {
        mAudioListIView.hideProgress();
        mAudioListIView.showLoadFailMsg();
    }



}
