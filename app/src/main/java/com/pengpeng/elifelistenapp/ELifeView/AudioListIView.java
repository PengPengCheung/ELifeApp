package com.pengpeng.elifelistenapp.ELifeView;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;

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
