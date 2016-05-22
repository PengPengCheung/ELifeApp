package com.pengpeng.elifelistenapp.view;

import com.pengpeng.elifelistenapp.model.Audio;

import java.util.List;

/**
 * Created by pengpeng on 16-3-29.
 */
public interface AudioListIView {

    public void showProgress();

    public void addAudio(List<Audio> audioList);

    public void hideProgress();

    public void showLoadFailMsg();



}
