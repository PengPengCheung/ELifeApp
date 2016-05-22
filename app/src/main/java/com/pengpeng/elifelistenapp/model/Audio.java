package com.pengpeng.elifelistenapp.model;

import java.util.List;

/**
 * Created by pengpeng on 16-1-12.
 */
public class Audio {
    private String audioId;         //音频id
    private String audioTitle;      //音频标题
    private String audioType;       //音频类别，分为film, news, lecture, speech
    private String audioLrcUrl;     //音频lrc文件url，来自服务器而不是网络
    private List<List<Integer>> audioTextBlankIndex;  //对音频文本的挖空位置下标列表
    private String audioImageUrl;   //音频图片资源url，来自服务器而不是网络
    private String audioDate;       //音频发布日期
    private String audioUrl;        //音频资源url，来自服务器而不是网络
    private List<String> audioText;       //音频文本
    private List<Integer> audioPartEndTime;        //音频各部分的开始时间，其列表的下表即音频的分段编号，列表长度即切割成的段数，暂时的段数设为3段
    private float avgCorrectRate;   //该段音频的平均准确率
    private List<List<String>> audioTextBlankWord;

    public List<List<Integer>> getAudioTextBlankIndex() {
        return audioTextBlankIndex;
    }

    public void setAudioTextBlankIndex(List<List<Integer>> audioTextBlankIndex) {
        this.audioTextBlankIndex = audioTextBlankIndex;
    }

    public List<List<String>> getAudioStandardAnswer() {
        return audioTextBlankWord;
    }

    public void setAudioStandardAnswer(List<List<String>> audioTextBlankWord) {
        this.audioTextBlankWord = audioTextBlankWord;
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioImageUrl() {
        return audioImageUrl;
    }

    public void setAudioImageUrl(String audioImageUrl) {
        this.audioImageUrl = audioImageUrl;
    }

    public String getAudioDate() {
        return audioDate;
    }

    public void setAudioDate(String audioDate) {
        this.audioDate = audioDate;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public List<String> getAudioText() {
        return audioText;
    }

    public void setAudioText(List<String> audioText) {
        this.audioText = audioText;
    }


    public List<Integer> getAudioPartEndTime() {
        return audioPartEndTime;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }



    public String getAudioLrcUrl() {
        return audioLrcUrl;
    }

    public void setAudioLrcUrl(String audioLrcUrl) {
        this.audioLrcUrl = audioLrcUrl;
    }

    public float getAvgCorrectRate() {
        return avgCorrectRate;
    }

    public void setAvgCorrectRate(float avgCorrectRate) {
        this.avgCorrectRate = avgCorrectRate;
    }

    public void setAudioPartEndTime(List<Integer> audioPartEndTime) {
        this.audioPartEndTime = audioPartEndTime;
    }

}
