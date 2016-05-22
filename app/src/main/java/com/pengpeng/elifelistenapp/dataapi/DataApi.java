package com.pengpeng.elifelistenapp.dataapi;

import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.model.UserAudioBehavior;

import java.util.List;

/**
 * Created by pengpeng on 16-3-29.
 */

public interface DataApi {

    public void getAudioListByUid(String userUUID, final int type, DataApiImpl.onLoadAudioListListener<List<Audio>> listener);

    public void getAudioById(UserAudioBehavior uaBehavior);

    public void getAudioData(String audioId,String userUUID,DataApiImpl.onLoadAudioListListener<Audio> listener);

   // public void getAudioAnswer(String audioId,int part,String userUUID,List<String> userAnswer,DataApiImpl.onLoadAudioListListener<Audio> listener);
   public void getAudioAnswer(String audioId,int part,String userUUID,DataApiImpl.onLoadAudioListListener<Audio> listener);
}
