package com.pengpeng.elifelistenapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pengpeng.elifelistenapp.utils.Resource;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_exercise)
public class ExerciseActivity extends AppCompatActivity {
    
    private static final String TAG = "ExerciseActivity";

    @ViewInject(R.id.tab_layout_part)
    private TabLayout mTLPart;
    
    @ViewInject(R.id.viewpager_exercise)
    private ViewPager mVPExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Log.i(TAG, "app");
        x.app();

        mVPExercise.setOffscreenPageLimit(2);
        setupViewPager(mVPExercise);
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_one));
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_two));
        mTLPart.addTab(mTLPart.newTab().setText(R.string.part_three));
        mTLPart.setupWithViewPager(mVPExercise);

    }

    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ExerciseFragment.newInstance(Resource.Type.PART_ONE), getString(R.string.part_one));
        adapter.addFragment(ExerciseFragment.newInstance(Resource.Type.PART_TWO), getString(R.string.part_two));
        adapter.addFragment(ExerciseFragment.newInstance(Resource.Type.PART_THREE), getString(R.string.part_three));
        mViewPager.setAdapter(adapter);
    }
}
