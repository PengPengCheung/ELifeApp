package com.pengpeng.elifelistenapp.ELifeDataApi;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.ELifeModel.UserAudioBehavior;

import java.util.List;

/**
 * Created by pengpeng on 16-3-29.
 */
public interface DataApi {

    public void getAudioListByUid(String userUUID, final int type, DataApiImpl.onLoadAudioListListener<List<Audio>> listener);

    public void getAudioById(UserAudioBehavior uaBehavior);
}
