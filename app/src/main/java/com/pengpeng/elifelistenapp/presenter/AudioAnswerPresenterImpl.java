package com.pengpeng.elifelistenapp.presenter;

import com.pengpeng.elifelistenapp.dataapi.DataApi;
import com.pengpeng.elifelistenapp.dataapi.DataApiImpl;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.view.AudioAnswerView;

/**
 * Created by Administrator on 2016/4/30.
 */
public class AudioAnswerPresenterImpl implements AudioAnswerPresenter,DataApiImpl.onLoadAudioListListener<Audio>{
    private AudioAnswerView mAudioDataView;
    private DataApi mDataApi;
    public AudioAnswerPresenterImpl(AudioAnswerView audioDataView) {
        mAudioDataView = audioDataView;
        mDataApi = new DataApiImpl();
    }
//    @Override
//    public void loadAudioAnswer(String audioId, int part, String userUUID,List<String> userAnswer) {
//        mDataApi.getAudioAnswer(audioId,part,userUUID,userAnswer,this);
//    }
@Override
public void loadAudioAnswer(String audioId, int part, String userUUID) {
    mDataApi.getAudioAnswer(audioId,part,userUUID,this);
}

    @Override
    public void onSuccess(Audio audioAnswer) {
       // Log.i("==========",audioAnswer.getAudioStandardAnswer().getCurrentAudio(0).getCurrentAudio(0));
        mAudioDataView.addAudioData(audioAnswer);
    }

    @Override
    public void onFailure(String msg, Exception e) {

    }
}
