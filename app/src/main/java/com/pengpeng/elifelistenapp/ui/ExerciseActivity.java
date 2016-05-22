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
import java.util.List;

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
    private Button submit_btn;

    @ViewInject(R.id.tv_answer_head)
    private TextView mTvAnswer;

    @ViewInject(R.id.gv_answer)
    private GridView mGvAnswer;

    @ViewInject(R.id.linearLayout_gridtableLayout)
    private LinearLayout ll_gridetableLayout;

    @ViewInject(R.id.framelayout)
    private FrameLayout frameLayout;

    public String answer[];
    private Audio mAudio;
//    private AudioListManager mManager;
    private int index;
    private IntentFilter mExerciseFilter;
    private ExerciseActivityReceiver mExerciseReceive;
    private AudioListPresenter mPresenter;
    private ViewPagerAdapter adapter;
    private List<LineEditText> editTextData = new ArrayList<LineEditText>();
    private LineEditText lineEditText;
    private AudioAnswerPresenter mAudioAnswerPresenter;
    private List<String> userAnswer = new ArrayList<String>();
    private List<String> standardAnswer0 = new ArrayList<String>();
    private List<String> standardAnswer1 = new ArrayList<String>();
    private List<String> standardAnswer2 = new ArrayList<String>();

    private boolean answerflag0 = false, answerflag1 = false, answerflag2 = false;
    private LayoutInflater inflater;
    //    private String grid_answer_id[]={"(1)","(2)","(3)","(4)","(5)","(6)","(7)","(8)","(9)","(10)","(11)","(12)"};
//   private String grid_answer_detail[]={"aaa","bbb","ccc","ddd","eee","fff","aaa","bbb","ccc","ddd","eee","fff"};
    private String grid_answer_id0[] = new String[50];
    private String grid_answer_detail0[] = new String[50];
    private String grid_answer_id1[] = new String[50];
    private String grid_answer_detail1[] = new String[50];
    private String grid_answer_id2[] = new String[50];
    private String grid_answer_detail2[] = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        index = intent.getIntExtra(Resource.ParamsKey.AUDIO_TEXT, -1);


        mVPExercise.setOffscreenPageLimit(2);
        setupViewPager(mVPExercise);
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_one));
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_two));
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_three));

        mTLPart.setupWithViewPager(mVPExercise);
        inflater = LayoutInflater.from(this);
        mGvAnswer.setAdapter(new MyAdapter());
    }

    private void init() {
        submit_btn.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mIVVoice.setOnClickListener(this);

//        mManager = ((ELifeApplication) getApplication()).getManager();
        mAudio = AudioListManager.getInstance().getCurrentAudio();
        ViewPager mViewPager = new ViewPager(this);


        mAudioAnswerPresenter = new AudioAnswerPresenterImpl(ExerciseActivity.this);
//        mAudioAnswerPresenter.loadAudioAnswer(mAudio.getAudioId(),mViewPager.getCurrentItem(),"123",userAnswer);
        mAudioAnswerPresenter.loadAudioAnswer(mAudio.getAudioId(), mViewPager.getCurrentItem(), "123");

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
                if (mViewPager.getCurrentItem() == 0) {
                    hideAnswer();
                    answerflag0 = true;
                    mGvAnswer.setAdapter(new MyAdapter());
                } else if (mViewPager.getCurrentItem() == 1) {
                    hideAnswer();
                    answerflag1 = true;
                    mGvAnswer.setAdapter(new MyAdapter());
                } else {
                    hideAnswer();
                    answerflag2 = true;
                    mGvAnswer.setAdapter(new MyAdapter());
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
        submit_btn.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        ll_gridetableLayout.setVisibility(View.VISIBLE);
        ll_gridetableLayout.setLayoutParams(new FrameLayout.LayoutParams(//动态设置宽度
                LinearLayout.LayoutParams.MATCH_PARENT,
                150));
        mGvAnswer.setVisibility(View.VISIBLE);
    }

    private void hideAnswer() {
        mTvAnswer.setText("完成练习后提交检测正确率");
        mTvAnswer.setTextColor(Color.GRAY);
        submit_btn.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        ll_gridetableLayout.setVisibility(View.GONE);
        mGvAnswer.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                answerflag0 = true;
                if (answerflag0 == true) {
                    showAnswer();
                    mGvAnswer.setAdapter(new MyAdapter());
                }
                if (answerflag1 == true) {
                    showAnswer();
                    mGvAnswer.setAdapter(new MyAdapter());
                }
                if (answerflag2 == true) {
                    showAnswer();
                    mGvAnswer.setAdapter(new MyAdapter());
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
                intent.putExtra(Resource.ParamsKey.AUDIO_PARTENDTIME, Tools.listToIntArray(mAudio.getAudioPartEndTime()));
                if(mAudio.getAudioPartEndTime() !=null ){

                    Log.e("pengpeng ExerActivity", mAudio.getAudioPartEndTime().toString());
                }else{
                    Log.e("pengpeng ExerActivity", "null");
                }
                intent.putExtra(Resource.PlayerStatus.CONTROL_KEY, Resource.PlayerStatus.CONTROL_PLAY_BTN);
                sendBroadcast(intent);
                break;

        }


        // Collections.addAll(userAnswer,lineEditText.getText().toString());

    }

    @Override
    public void addAudioData(Audio audioAnswer) {

        standardAnswer0 = audioAnswer.getAudioStandardAnswer().get(0);
        standardAnswer1 = audioAnswer.getAudioStandardAnswer().get(1);
        standardAnswer2 = audioAnswer.getAudioStandardAnswer().get(2);


        Log.i("--------0", audioAnswer.getAudioStandardAnswer().get(0).toString());
        Log.i("--------2", audioAnswer.getAudioStandardAnswer().get(2).toString());


            for (int i = 0; i < standardAnswer0.size(); i++) {
                grid_answer_id0[i] = "(" + (i + 1) + ")";
                grid_answer_detail0[i] = standardAnswer0.get(i);
                Log.i("--------standanswer0", grid_answer_detail0[i]);
            }


            for (int i = 0; i < standardAnswer1.size(); i++) {
                grid_answer_id1[i] = "(" + (i + 1) + ")";
                grid_answer_detail1[i] = standardAnswer1.get(i);
                Log.i("--------standanswer1", grid_answer_detail1[i]);
            }


            for (int i = 0; i < standardAnswer2.size(); i++) {
                grid_answer_id2[i] = "(" + (i + 1) + ")";
                grid_answer_detail2[i] = standardAnswer2.get(i);
                Log.i("--------standanswer2", grid_answer_detail2[i]);
            }

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


        @Override
        public int getCount() {
                return grid_answer_detail0.length;

        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View view1 = inflater.inflate(R.layout.item_gridview, parent, false);
            TextView tv_answer_id = (TextView) view1.findViewById(R.id.answer_id);
            TextView tv_answer_detail = (TextView) view1.findViewById(R.id.answer_detail);
             answerflag0 = true;
            if (answerflag0 == true) {
                tv_answer_id.setText(grid_answer_id0[position]);
                tv_answer_detail.setText(grid_answer_detail0[position]);
            }
            if (answerflag1 == true) {
                tv_answer_id.setText(grid_answer_id1[position]);
                tv_answer_detail.setText(grid_answer_detail1[position]);
            }
            if (answerflag2 == true) {
                tv_answer_id.setText(grid_answer_id2[position]);
                tv_answer_detail.setText(grid_answer_detail2[position]);
            }
            return view1;
        }


    }
}
