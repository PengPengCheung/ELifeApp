package com.pengpeng.elifelistenapp.ELifeDataApi;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.ELifeModel.UserAudioBehavior;
import com.pengpeng.elifelistenapp.utils.JsonUtils;
import com.pengpeng.elifelistenapp.utils.OkHttpUtils;
import com.pengpeng.elifelistenapp.utils.Resource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengpeng on 16-3-29.
 */
public class DataApiImpl implements DataApi{

    private static final String TAG = Resource.Debug.TAG;

    /**
     * 用于MainActivity获取音频列表
     * @param userUUID 用户id
     */
    @Override
    public void getAudioListByUid(String userUUID,  int audioType, final onLoadAudioListListener<List<Audio>> listener) {
        OkHttpUtils.ResultCallback<String> loadAudioListCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Type classType = new TypeToken<DataApiResponse<List<Audio>>>() {
                }.getType();
                DataApiResponse<List<Audio>> dataApiResponse = JsonUtils.deserialize(response, classType);
                listener.onSuccess(dataApiResponse.getData());
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(Resource.Event.DATA_WRONG_EVENT_MSG, e);
                Log.e(TAG, e.getMessage());
            }
        };

            Map<String, Object> params = new HashMap<>();
            params.put(Resource.ParamsKey.USER_ID, userUUID);
        switch (audioType){
            case Resource.Type.TYPE_NEWS:
                params.put(Resource.ParamsKey.AUDIO_TYPE, "news");
                break;
            case Resource.Type.TYPE_MOVIE:
                params.put(Resource.ParamsKey.AUDIO_TYPE, "movie");
                break;
            case Resource.Type.TYPE_SPEECH:
                params.put(Resource.ParamsKey.AUDIO_TYPE, "speech");
                break;
        }
            OkHttpUtils.post(Resource.Path.audioListPath, loadAudioListCallback, params);
    }

    /**
     * 用于NetworkPlayerActivity获取音频
     * @param uaBehavior 传入用户id
     */
    @Override
    public void getAudioById(UserAudioBehavior uaBehavior) {

    }

    public interface onLoadAudioListListener<T> {
        void onSuccess(T audioList);
        void onFailure(String msg, Exception e);
    }
}
