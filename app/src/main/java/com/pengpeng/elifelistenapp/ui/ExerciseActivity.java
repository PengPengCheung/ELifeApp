package com.pengpeng.elifelistenapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pengpeng.elifelistenapp.common.AudioListManager;
import com.pengpeng.elifelistenapp.common.ELifeBaseActivity;
import com.pengpeng.elifelistenapp.model.Audio;
import com.pengpeng.elifelistenapp.presenter.AudioAnswerPresenter;
import com.pengpeng.elifelistenapp.presenter.AudioAnswerPresenterImpl;
import com.pengpeng.elifelistenapp.presenter.AudioListPresenter;
import com.pengpeng.elifelistenapp.view.AudioAnswerView;
import com.pengpeng.elifelistenapp.R;
import com.pengpeng.elifelistenapp.adapter.ViewPagerAdapter;
import com.pengpeng.elifelistenapp.utils.Resource;
import com.pengpeng.elifelistenapp.utils.Tools;
import com.pengpeng.elifelistenapp.widget.LineEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_exercise)
public class ExerciseActivity extends ELifeBaseActivity implements View.OnClickListener, AudioAnswerView {

    private static final String TAG = "ExerciseActivity";
    @ViewInject(R.id.iv_back_exe)
    private ImageView mIVBack;

    @ViewInject(R.id.iv_voice_exe)
    private ImageView mIVVoice;

    @ViewInject(R.id.tab_layout_part)
    private TabLayout mTLPart;

    @ViewInject(R.id.viewpager_exercise)
    private ViewPager mVPExercise;
    //提交
    @ViewInject(R.id.btn_submit)
    private Button mBtnSubmit;

    @ViewInject(R.id.tv_answer_head)
    private TextView mTvAnswer;

    @ViewInject(R.id.gv_answer)
    private GridView mGvAnswer;

    @ViewInject(R.id.linearLayout_gridtableLayout)
    private LinearLayout mLLAnswerWrapper;

    @ViewInject(R.id.framelayout)
    private FrameLayout frameLayout;

    public String answer[];
//    private Audio mAudio;
    private int index;
    private IntentFilter mExerciseFilter;
    private ExerciseActivityReceiver mExerciseReceive;
    private AudioListPresenter mPresenter;
    private ViewPagerAdapter adapter;
    private AudioAnswerPresenter mAudioAnswerPresenter;
    private MyAdapter mGVAdapter;

    //    private String grid_answer_id[]={"(1)","(2)","(3)","(4)","(5)","(6)","(7)","(8)","(9)","(10)","(11)","(12)"};
//   private String grid_answer_detail[]={"aaa","bbb","ccc","ddd","eee","fff","aaa","bbb","ccc","ddd","eee","fff"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Intent intent = getIntent();
        index = intent.getIntExtra(Resource.ParamsKey.AUDIO_TEXT, -1);


//        mVPExercise.setOffscreenPageLimit(2);
        setupViewPager(mVPExercise);
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_one));
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_two));
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_three));

        mTLPart.setupWithViewPager(mVPExercise);


    }

    private void init() {
        mBtnSubmit.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mIVVoice.setOnClickListener(this);
//        AudioListManager.getInstance().getCurrentAudio() = AudioListManager.getInstance().getCurrentAudio();
        ViewPager mViewPager = new ViewPager(this);


        mAudioAnswerPresenter = new AudioAnswerPresenterImpl(ExerciseActivity.this);
//        mAudioAnswerPresenter.loadAudioAnswer(mAudio.getAudioId(),mViewPager.getCurrentItem(),"123",userAnswer);
        mAudioAnswerPresenter.loadAudioAnswer(AudioListManager.getInstance().getCurrentAudio().getAudioId(), mViewPager.getCurrentItem(), "123");

        //处理和文本part相关的操作---SERVICE
        mExerciseReceive = new ExerciseActivityReceiver();
        mExerciseFilter = new IntentFilter(Resource.Filter.EXERCISE_ACTIVITY);
        registerReceiver(mExerciseReceive, mExerciseFilter);


    }

    private void setupViewPager(final ViewPager mViewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ExerciseFragment.newInstance(Resource.Type.PART_ONE), getString(R.string.part_one));
        adapter.addFragment(ExerciseFragment.newInstance(Resource.Type.PART_TWO), getString(R.string.part_two));
        adapter.addFragment(ExerciseFragment.newInstance(Resource.Type.PART_THREE), getString(R.string.part_three));
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((ExerciseFragment) adapter.getItem(position)).fillExercise();
                hideAnswer();
                if (mGVAdapter != null) {
                    mGVAdapter.setPart(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void showAnswer() {
        mTvAnswer.setText("ANSWER:");
        mTvAnswer.setTextColor(Color.WHITE);
        mBtnSubmit.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        mLLAnswerWrapper.setVisibility(View.VISIBLE);
        mLLAnswerWrapper.setLayoutParams(new FrameLayout.LayoutParams(//动态设置宽度
                LinearLayout.LayoutParams.MATCH_PARENT,
                150));
        mGvAnswer.setVisibility(View.VISIBLE);
    }

    private void hideAnswer() {
        mTvAnswer.setText("完成练习后提交检测正确率");
        mTvAnswer.setTextColor(Color.GRAY);
        mBtnSubmit.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        mLLAnswerWrapper.setVisibility(View.GONE);
        mGvAnswer.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                showAnswer();
                if (mGVAdapter != null) {
                    mGVAdapter.setPart(mVPExercise.getCurrentItem());
                }
                break;
            case R.id.iv_back_exe:
                Intent backIntent = new Intent(this, PlayerActivity.class);
                startActivity(backIntent);
                ExerciseActivity.this.finish();
                break;
            case R.id.iv_voice_exe:
                Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
                intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.EXERCISE_ACTIVITY_VALUE);
                intent.putExtra(Resource.Type.PART, mVPExercise.getCurrentItem());
                intent.putExtra(Resource.ParamsKey.AUDIO_PARTENDTIME, Tools.listToIntArray(AudioListManager.getInstance().getCurrentAudio().getAudioPartEndTime()));
                if (AudioListManager.getInstance().getCurrentAudio().getAudioPartEndTime() != null) {

                    Log.e("pengpeng ExerActivity", AudioListManager.getInstance().getCurrentAudio().getAudioPartEndTime().toString());
                } else {
                    Log.e("pengpeng ExerActivity", "null");
                }
                intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_PLAY_BTN);
                sendBroadcast(intent);
                break;

        }


        // Collections.addAll(userAnswer,lineEditText.getText().toString());

    }

    class AnswerHolder {
        public String answerId;
        public String answer;
    }

    private Map<Integer, List<AnswerHolder>> answerMap = new HashMap<>();

    @Override
    public void addAudioData(Audio audio) {

        List<List<String>> mTotalAnswer = audio.getAudioStandardAnswer();
        if (mTotalAnswer != null) {
            for (int i = 0; i < mTotalAnswer.size(); i++) {
                List<AnswerHolder> answerHolderList = convertToAnswerHolder(mTotalAnswer.get(i));
                answerMap.put(i, answerHolderList);
            }
        }

        mGVAdapter = new MyAdapter(this);
        mGvAnswer.setAdapter(mGVAdapter);

    }

    private List<AnswerHolder> convertToAnswerHolder(List<String> partAnswer) {
        List<AnswerHolder> list = new ArrayList<>();
        for (int i = 0; i < partAnswer.size(); i++) {
            AnswerHolder holder = new AnswerHolder();
            holder.answerId = "(" + (i + 1) + ")";
            holder.answer = partAnswer.get(i);
            list.add(holder);
        }
        return list;
    }


    //接受从service传来的数据
    class ExerciseActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int status = intent.getIntExtra(Resource.PlayerStatus.UPDATE_KEY, -1);

            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(Resource.Filter.PLAYER_SERVICE);
        intent.putExtra(Resource.Filter.FILTER_SIGNAL, Resource.Filter.EXERCISE_ACTIVITY_VALUE);
        intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_STOP_BTN);
        sendBroadcast(intent);
    }

    protected void onDestroy() {
        unregisterReceiver(mExerciseReceive);
        super.onDestroy();
    }

    class MyAdapter extends BaseAdapter {

        int part = 0;
        private LayoutInflater inflater;

        MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);

        }

        public void setPart(int part) {
            this.part = part;
        }


        @Override
        public int getCount() {
            return answerMap.get(part).size();

        }

        @Override
        public Object getItem(int position) {
            return answerMap.get(part).get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View view1 = inflater.inflate(R.layout.item_gridview, parent, false);
            TextView mTVAnswerId = (TextView) view1.findViewById(R.id.answer_id);
            TextView mTVAnswer = (TextView) view1.findViewById(R.id.answer_detail);
            mTVAnswerId.setText(((AnswerHolder) getItem(position)).answerId);
            mTVAnswer.setText(((AnswerHolder) getItem(position)).answer);

            return view1;
        }


    }
}
