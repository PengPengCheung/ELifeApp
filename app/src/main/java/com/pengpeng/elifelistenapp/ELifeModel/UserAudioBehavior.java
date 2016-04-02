package com.pengpeng.elifelistenapp.ELifeModel;

/**
 * Created by pengpeng on 16-1-12.
 */
public class UserAudioBehavior {

    private String audioId;
    private String userId;
    private Boolean isCollected;             //是否点赞，True表示点赞，False表示不点赞
    private String userAnswer;          //用户的练习答案
    private float audioCorrectRate;

    public Boolean getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Boolean isCollected) {
        this.isCollected = isCollected;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getAudioCorrectRate() {
        return audioCorrectRate;
    }

    public void setAudioCorrectRate(float audioCorrectRate) {
        this.audioCorrectRate = audioCorrectRate;
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }


}
