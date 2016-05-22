package com.pengpeng.elifelistenapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pengpeng.elifelistenapp.common.AudioListManager;
import com.pengpeng.elifelistenapp.common.AudioPlayerService;
import com.pengpeng.elifelistenapp.common.ELifeBaseActivity;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.R;
import com.pengpeng.elifelistenapp.adapter.ViewPagerAdapter;
import com.pengpeng.elifelistenapp.utils.ImageLoaderUtils;
import com.pengpeng.elifelistenapp.utils.Resource;
import com.pengpeng.elifelistenapp.utils.Tools;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
//加载当前ACTIVITY布局
@ContentView(R.layout.activity_main)
public class MainActivity extends ELifeBaseActivity implements View.OnClickListener {
    private MainActivityReceiver mReceiver;
    private boolean hasAudio = false;
    private int playerStatus = Resource.PlayerStatus.STOP;
    //给View进行初始化
    @ViewInject(R.id.tab_layout_type)
    private TabLayout mTablayout;

    @ViewInject(R.id.viewpager_content)
    private ViewPager mViewPager;

    @ViewInject(R.id.rl_player)
    private RelativeLayout mRLPlayer;

    @ViewInject(R.id.ib_main_play)
    private ImageView mIbPlayer;

    @ViewInject(R.id.ib_stop)
    private ImageView mIvStop;

    @ViewInject(R.id.iv_audio_img_icon)
    private ImageView mIvAudio;

    @ViewInject(R.id.tv_player_audio_title)
    private TextView mTvAudioTitle;

    public void setPlayingAudio(Audio audio, int position) {

        if (audio != null && audio.getAudioUrl() != null) {
            setAudio(audio);
            AudioListManager.getInstance().setCurrentType(Tools.getType(mViewPager.getCurrentItem()));
            AudioListManager.getInstance().setCurrentIndex(position);
            Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
            intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.MAIN_ACTIVITY_VALUE);
            intent.putExtra(Resource.ParamsKey.AUDIO_URL, audio.getAudioUrl());
            intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_PLAY_BTN);
            sendBroadcast(intent);
            hasAudio = true;
            Log.i(Resource.Debug.TAG, "MainActivity SetAudio "+hasAudio);
        }
    }


    public boolean isHasAudio() {
        return this.hasAudio;
    }

    private void setAudioView(Audio audio) {
        if (audio != null) {
            mTvAudioTitle.setText(audio.getAudioTitle());//加载音频标题
            ImageLoaderUtils.display(this, mIvAudio, audio.getAudioImageUrl());//加载音频图片
        }
    }

    private void setPlayingView(Audio audio) {
        setAudioView(audio);
        if (audio != null) {
            mIbPlayer.setImageResource(R.drawable.pause);
        }
    }

    private void setPauseView(Audio audio) {
        setAudioView(audio);
        if (audio != null) {
            mIbPlayer.setImageResource(R.drawable.play);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Resource.Debug.MAIN, "onCreate");

        init();

        //设置限制的页数之后，每次网络请求会把这些页的数据都请求回来，就是说，有3页fragment的话，限制是2,那么第一次请求的时候，会发出三次请求。
        mViewPager.setOffscreenPageLimit(2);
        setupViewPager(mViewPager);
        mTablayout.addTab(mTablayout.newTab().setText(R.string.movie));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.speech));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.news));
        mTablayout.setupWithViewPager(mViewPager);

    }



    private void init() {
        mRLPlayer.setOnClickListener(this);
        mIbPlayer.setOnClickListener(this);
        mIvStop.setOnClickListener(this);
        //动态注册了广播
        mReceiver = new MainActivityReceiver();
        IntentFilter mainFilter = new IntentFilter(Resource.Filter.MAIN_ACTIVITY);
        registerReceiver(mReceiver, mainFilter);

        Intent intent = new Intent(this, AudioPlayerService.class);
        startService(intent);
    }

    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ItemFragment.newInstance(Resource.Type.TYPE_MOVIE), getString(R.string.movie));
        adapter.addFragment(ItemFragment.newInstance(Resource.Type.TYPE_SPEECH), getString(R.string.speech));
        adapter.addFragment(ItemFragment.newInstance(Resource.Type.TYPE_NEWS), getString(R.string.news));
        mViewPager.setAdapter(adapter);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_player:
                clickToPlayerActivity();
                break;
            case R.id.ib_main_play:
                playOrPause();
                break;
            case R.id.ib_stop:
                playerStop();
                break;
        }
    }

    private void clickToPlayerActivity() {
        if (isHasAudio()) {

            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            intent.putExtra(Resource.ParamsKey.AUDIO_TITLE, getAudio().getAudioTitle());
            intent.putExtra(Resource.ParamsKey.AUDIO_STATUS, playerStatus);
            intent.putExtra(Resource.ParamsKey.AUDIO_INDEX, AudioListManager.getInstance().getCurrentIndex());
            startActivity(intent);
        }
    }

    private void playerStop() {

        if(isHasAudio()){
            Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
            intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_STOP_BTN);
            intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.MAIN_ACTIVITY_VALUE);
            //发送广播：可以将一个指定地址和参数信息的Intent对象以广播的形式发送出去。
            sendBroadcast(intent);
        }
    }


    private void playOrPause() {
        if(isHasAudio()){
            Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
            intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_PLAY_BTN);
            intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.MAIN_ACTIVITY_VALUE);
            sendBroadcast(intent);
        }
    }


    //从音频页回到首页需要恢复原来的播放现场
    @Override
    protected void onRestart() {
        super.onRestart();
        handleBackIntent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Resource.Debug.MAIN, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(Resource.Debug.MAIN, "onResume");

        if (getAudio() != null) {
            mTvAudioTitle.setText(getAudio().getAudioTitle());
            ImageLoaderUtils.display(this, mIvAudio, getAudio().getAudioImageUrl());
        }

    }

    @Override
    protected void onDestroy() {

        Intent intent = new Intent(MainActivity.this, AudioPlayerService.class);
        stopService(intent);
        //销毁广播
        unregisterReceiver(mReceiver);

        Log.i(Resource.Debug.MAIN, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Resource.Debug.MAIN, "onStart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleBackIntent();
    }

    private void handleBackIntent() {
            setAudio(AudioListManager.getInstance().getCurrentAudio());
            Intent changeAudioIntent = new Intent(Resource.Filter.PLAYER_SERVICE);
            changeAudioIntent.putExtra(Resource.ParamsKey.AUDIO_URL, getAudio().getAudioUrl());
            sendBroadcast(changeAudioIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Resource.Debug.MAIN, "onPause");
    }
//广播接收者
    public class MainActivityReceiver extends BroadcastReceiver {

        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                Log.i(Resource.Debug.TAG, "Activity receive");
               // 获得广播
                int status = intent.getIntExtra(Resource.PlayerStatus.UPDATE_KEY, -1);
                switch (status){
                    case Resource.PlayerStatus.PLAYING:
                        setPlayingView(getAudio());
                        playerStatus = Resource.PlayerStatus.PLAYING;
                        break;
                    case Resource.PlayerStatus.PAUSE:
                        setPauseView(getAudio());
                        playerStatus = Resource.PlayerStatus.PAUSE;
                        break;
                    case Resource.PlayerStatus.STOP:
                        setPauseView(getAudio());
                        playerStatus = Resource.PlayerStatus.STOP;
                        break;
                }
            }
        }
    }
}