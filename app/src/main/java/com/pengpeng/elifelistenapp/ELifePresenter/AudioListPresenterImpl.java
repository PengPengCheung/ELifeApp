package com.pengpeng.elifelistenapp.ELifePresenter;

import android.util.Log;

import com.pengpeng.elifelistenapp.ELifeDataApi.DataApi;
import com.pengpeng.elifelistenapp.ELifeDataApi.DataApiImpl;
import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.ELifeView.AudioListIView;
import com.pengpeng.elifelistenapp.utils.Resource;

import java.util.List;

/**
 * Created by pengpeng on 16-3-29.
 */
public class AudioListPresenterImpl implements AudioListPresenter, DataApiImpl.onLoadAudioListListener<List<Audio>> {



    private DataApi mDataApi;
    private AudioListIView mView;

    public AudioListPresenterImpl(AudioListIView view){
        mView = view;
        mDataApi = new DataApiImpl();
    }

    @Override
    public void loadAudioList(String uuid, int type) {
        mView.showProgress();
        mDataApi.getAudioListByUid(uuid, type, this);

    }

    @Override
    public void onSuccess(List<Audio> audioList) {
        Log.i(Resource.Debug.TAG, "enterOnSuccess");
        mView.hideProgress();
        Log.i(Resource.Debug.TAG, "hideProgress_success");
        mView.addAudio(audioList);
        Log.i(Resource.Debug.TAG, "addAudio");

    }

    @Override
    public void onFailure(String msg, Exception e) {
        Log.i(Resource.Debug.TAG, "enterOnFailure");
        mView.hideProgress();
        Log.i(Resource.Debug.TAG, "hideProgress_fail");
        mView.showLoadFailMsg();
        Log.i(Resource.Debug.TAG, "showLoadFailMsg");
    }
}
