package com.pengpeng.elifelistenapp.dataapi;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.model.UserAudioBehavior;
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

    /**
     * 用于ExerciseActivity获取音频文本
     * @param
     */
    @Override
    public void getAudioData(String audioId,String userUUID,final onLoadAudioListListener<Audio> listener) {
        OkHttpUtils.ResultCallback<String> loadAudioDataCallback = new OkHttpUtils.ResultCallback<String>(){
            @Override
            public void onSuccess(String response) {
                Type classTypeData = new TypeToken<DataApiResponse<Audio>>() {
                }.getType();
                //解析数据
                DataApiResponse<Audio> dataApiAudioDataResponse = JsonUtils.deserialize(response, classTypeData);
                listener.onSuccess(dataApiAudioDataResponse.getData());
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure(Resource.Event.DATA_WRONG_EVENT_MSG, e);
            }
        };

        Map<String, Object> paramsAudio = new HashMap<>();
        paramsAudio.put(Resource.ParamsKey.AUDIO_ID, audioId);
        paramsAudio.put(Resource.ParamsKey.USER_ID, userUUID);
        OkHttpUtils.post(Resource.Path.getAudioPath, loadAudioDataCallback, paramsAudio);
    }

    /**
     * 用于ExerciseActivity获取音频答案
     * @param
     */
//    @Override
//   public void getAudioAnswer(String audioId,int part, String userUUID, List<String> userAnswer, final onLoadAudioListListener<Audio> listener) {
//        OkHttpUtils.ResultCallback<String> loadAudioAnswerCallback = new OkHttpUtils.ResultCallback<String>(){
//            @Override
//            public void onSuccess(String response) {
//                Type classTypeData = new TypeToken<DataApiResponse<Audio>>() {
//                }.getType();
//                //解析数据
//                DataApiResponse<Audio> dataApiAudioAnswerResponse = JsonUtils.deserialize(response, classTypeData);
//                listener.onSuccess(dataApiAudioAnswerResponse.getData());
//            }
//            @Override
//            public void onFailure(Exception e) {
//                listener.onFailure(Resource.Event.DATA_WRONG_EVENT_MSG, e);
//            }
//        };
//        Map<String, Object> paramsAnswer = new HashMap<>();
//        paramsAnswer.put(Resource.ParamsKey.AUDIO_ID, audioId);
//        paramsAnswer.put(Resource.ParamsKey.AUDIO_PART,part);
//        paramsAnswer.put(Resource.ParamsKey.USER_ID, userUUID);
//        paramsAnswer.put(Resource.ParamsKey.USERANSWER,userAnswer);
//        OkHttpUtils.post(Resource.Path.audioStandardAnswer, loadAudioAnswerCallback, paramsAnswer);
//    }

    @Override
    public void getAudioAnswer(String audioId,int part, String userUUID,final onLoadAudioListListener<Audio> listener) {
        OkHttpUtils.ResultCallback<String> loadAudioAnswerCallback = new OkHttpUtils.ResultCallback<String>(){
            @Override
            public void onSuccess(String response) {
                Log.i("-----------response",response);
                Type classTypeData = new TypeToken<DataApiResponse<Audio>>() {
                }.getType();
                //解析数据
                Log.i("-----------data",classTypeData.toString());
                DataApiResponse<Audio> dataApiAudioAnswerResponse = JsonUtils.deserialize(response, classTypeData);
                Log.i("-----------dataApiAudio",dataApiAudioAnswerResponse.getData().getAudioStandardAnswer().toString());
                listener.onSuccess(dataApiAudioAnswerResponse.getData());
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure(Resource.Event.DATA_WRONG_EVENT_MSG, e);
            }
        };
        Map<String, Object> paramsAnswer = new HashMap<>();
        paramsAnswer.put(Resource.ParamsKey.AUDIO_ID, audioId);
        paramsAnswer.put(Resource.ParamsKey.AUDIO_PART,part);
        paramsAnswer.put(Resource.ParamsKey.USER_ID, userUUID);
        //paramsAnswer.put(Resource.ParamsKey.USERANSWER,userAnswer);
        OkHttpUtils.post(Resource.Path.audioStandardAnswer, loadAudioAnswerCallback, paramsAnswer);
    }


    public interface onLoadAudioListListener<T> {
        void onSuccess(T audioList);
        void onFailure(String msg, Exception e);
    }
}
